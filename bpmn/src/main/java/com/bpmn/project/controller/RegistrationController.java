package com.bpmn.project.controller;

import com.bpmn.project.model.ActivationToken;
import com.bpmn.project.model.RegistrationModel;
import com.bpmn.project.validator.RegistrationFormValidator;

import net.bytebuddy.utility.RandomString;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.json.JSONObject;

@Controller
@RequestMapping("/home")
public class RegistrationController implements JavaDelegate {

	private static final String CAPTCHA_GOOGLE_URL = "https://www.google.com/recaptcha/api/siteverify";
	private static final String CAPTCHA_SECRET_KEY = "XXX";
	private static final String ACTIVATION_TOKEN_URL = "http://localhost:8080/home/activateAccount?token=";

	/**
	 * Static fields instead of dao layer
	 */
	private static String instanceId;
	private static RegistrationModel form;
	private static boolean recaptchaVerified = false;
	private static Map<String, ActivationToken> activationToken = new HashMap<>();

	@Autowired
	private ProcessEngine camunda;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RegistrationFormValidator validator;

	@Autowired
	private JavaMailSender mailSender;

	@GetMapping
	public String getHomePage() {
		return "home";
	}

	@GetMapping("/register")
	public ModelAndView getRegistrationPage() {
		runtimeService.startProcessInstanceByKey("register_process");
		return new ModelAndView("register", "form", new RegistrationModel());
	}

	@GetMapping("/cancelRegistration")
	public String cancelRegistration() {
		camunda.getRuntimeService().signalEventReceived("cancelSignal");
		return "home";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("form") RegistrationModel form_, Model model, BindingResult result) {

		form = form_;

		TaskQuery taskQuery = camunda.getTaskService().createTaskQuery().processInstanceId(instanceId);
		Task task = taskQuery.singleResult();

		camunda.getFormService().submitTaskForm(task.getId(),
				Variables.putValue("login", form.getLogin()).putValue("password", form.getPassword())
						.putValue("confirmPassword", form.getConfirmedPassword()).putValue("name", form.getName())
						.putValue("surname", form.getSurname()).putValue("phoneNumber", form.getPhoneNumber())
						.putValue("email", form.getEmail()));

		validator.validate(form, result);

		if (result.hasErrors()) {
			return "register";
		}

		if (!validateRecaptcha()) {
			model.addAttribute("recaptchaError", "Błąd walidacji recaptcha");
			return "register";
		}

		sendMail(form_.getEmail());

		model.addAttribute("activationLink",
				"Wysłano link aktywujący na podany adres email. Sprawdz skrzynkę pocztową.");
		return "home";
	}

	@GetMapping("/verifyRecaptcha")
	public @ResponseBody String verifyRecaptcha(@RequestParam("token") String token, HttpServletRequest request) {
		String params = "secret=" + CAPTCHA_SECRET_KEY + "&response=" + token;
		StringBuilder output = new StringBuilder();
		JSONObject json;
		try {
			URLConnection connection = new URL(CAPTCHA_GOOGLE_URL + "?" + params).openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String readLine;
			while ((readLine = reader.readLine()) != null) {
				output.append(readLine);
			}
			json = new JSONObject(output.toString());
			if ((Boolean) json.get("success")) {
				RegistrationController.recaptchaVerified = true;
			}
			else RegistrationController.recaptchaVerified = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}

	@GetMapping("/activateAccount")
	public String activateAccount(@RequestParam("token") String token, @RequestParam("email") String email) {
		ActivationToken sentToken = activationToken.get(email);
		if(sentToken == null) {
			return "home";
		}
		else if (sentToken.getValue().equals(token) && sentToken.getExpirationDate().isAfter(LocalDateTime.now())) {
			activationToken.remove(email);
			camunda.getRuntimeService().correlateMessage("GetActivationResponse");
			return "redirect:/home/welcome";
		}
		return "home";
	}
	
	@GetMapping("/welcome")
	public String welcomePage() {
		return "welcome";
	}

	private boolean validateRecaptcha() {
		return recaptchaVerified;
	}

	private void sendMail(String addressee) {
		ActivationToken token = new ActivationToken();
		token.setCreationDate(LocalDateTime.now());
		token.setExpirationDate(token.getCreationDate().plusHours(24));

		String generatedString = RandomString.make(10);
		token.setValue(generatedString);
		if (activationToken.containsKey(addressee)) {
			activationToken.replace(addressee, token);
		} else {
			activationToken.put(addressee, token);
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			String URL = ACTIVATION_TOKEN_URL + generatedString + "&email=" + addressee;
			String subject = "BPMN - link aktywacyjny";
			String text = "Kliknij w link aktywujący aby zakończyć proces rejestracji: " + URL;
			message.setTo(addressee);
			message.setSubject(subject);
			message.setText(text);
			mailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("[CAMUNDA] - User data verification");
		BindingResult result = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, result);
		execution.setVariable("verified", recaptchaVerified);
		if (result.hasErrors()) {
			String errors = result.getAllErrors().toString();
			throw new BpmnError("0", errors);
		}
	}
}
