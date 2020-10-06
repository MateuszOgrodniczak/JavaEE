package com.jee.spring_labs.user.controller;

import com.jee.spring_labs.model.User;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.model.Application;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/userApplications")
@Slf4j
public class ApplicationController {

    private static final String SEND_APPLICATION = "/views/sendApplication";

    @Value("${microservices.application.url}")
    private String applicationUrl;

    @Value("${microservices.housing.url}")
    private String housingUrl;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/export/{applicationId}")
    @ResponseBody
    public ResponseEntity<byte[]> exportApplication(@PathVariable String applicationId, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication == null || !authentication.isAuthenticated()) {
            response.sendRedirect(housingUrl + "/home");
            return null;
        }

        HttpSession session = request.getSession();
        OAuth2AccessToken auth2AccessToken = (OAuth2AccessToken) session.getAttribute("accessToken");
        String authorizationToken = auth2AccessToken.getTokenType() + " " + auth2AccessToken.getValue();

        byte[] contents = restTemplate.execute(
                applicationUrl + "/" + applicationId + "/export",
                HttpMethod.GET,
                (ClientHttpRequest requestCallback) -> requestCallback.getHeaders().add("Authorization", authorizationToken),
                responseExtractor -> IOUtils.toByteArray(responseExtractor.getBody()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "app_form_" + applicationId + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping("/{role}/applications")
    public ModelAndView getUserApplications(@PathVariable("role") String role, Principal principal, HttpSession session) {
        if (role == null || (!role.equals("admin") && !role.equals("owner") && !role.equals("tenant"))) {
            return new ModelAndView("redirect:/home");
        }
        String viewName = role + "/" + role + "Page";
        String userName = principal.getName();

        ModelAndView mv = new ModelAndView("/views/" + viewName);

        mv.addObject("applicationsQueried", true);

        OAuth2AccessToken auth2AccessToken = (OAuth2AccessToken) session.getAttribute("accessToken");
        String authorizationToken = auth2AccessToken.getTokenType() + " " + auth2AccessToken.getValue();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<List> sentApplications = restTemplate.exchange(applicationUrl + "/sent/" + userName, HttpMethod.GET, entity, List.class);
        ResponseEntity<List> receivedApplications = restTemplate.exchange(applicationUrl + "/received/" + userName, HttpMethod.GET, entity, List.class);

        mv.addObject("applicationsSent", sentApplications.getBody());
        mv.addObject("applicationsReceived", receivedApplications.getBody());
        return mv;
    }

    @GetMapping("/{role}/saveApplication")
    public ModelAndView getSaveApplicationView(@PathVariable("role") String role, Authentication authentication, HttpServletRequest request) {
        if (role == null || (!role.equals("admin") && !role.equals("owner") && !role.equals("tenant"))) {
            return new ModelAndView("redirect:/home");
        }
        ModelAndView mv = new ModelAndView(SEND_APPLICATION);
        HttpSession session = request.getSession();
        if (session.getAttribute("usernames") == null) {
            session.setAttribute("usernames", userDao.getUsernames(authentication.getName()));
        }

        mv.addObject("application", new Application());
        mv.addObject("usernames", session.getAttribute("usernames"));
        mv.addObject("userRole", role);
        mv.addObject("applicationServiceUrl", applicationUrl);
        return mv;
    }

    @PostMapping("/{role}/saveApplication")
    public String saveApplication(@PathVariable("role") String role, @ModelAttribute("application") Application application, Model model, BindingResult result,
                                  Authentication authentication, HttpServletRequest httpServletRequest) {
        if (role == null || (!role.equals("admin") && !role.equals("owner") && !role.equals("tenant"))) {
            return "redirect:/home";
        }
        String viewName = "views/" + role + "/" + role + "Page";
        if (result.hasErrors()) {
            model.addAttribute("sendApplicationError", true);
            return SEND_APPLICATION;
        }
        User sender = (User) authentication.getPrincipal();
        String[] recipientNames = httpServletRequest.getParameterValues("recipientNames[]");

        application.setIssuerLogin(sender.getUsername());
        application.setIssuerNameAndSurname(sender.getName() + " " + sender.getSurname());
        application.setRecipients(Arrays.asList(recipientNames));
        application.setCreationDate(LocalDate.now());

        OAuth2AccessToken auth2AccessToken = (OAuth2AccessToken) httpServletRequest.getSession().getAttribute("accessToken");
        String authorizationToken = auth2AccessToken.getTokenType() + " " + auth2AccessToken.getValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationToken);

        HttpEntity<Application> entity = new HttpEntity<>(application, headers);
        try {
            ResponseEntity<Application> response = restTemplate.postForEntity(applicationUrl + "/add", entity, Application.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("sendApplicationSuccess", true);
            } else {
                model.addAttribute("sendApplicationError", true);
            }
        } catch (HttpClientErrorException e) {
            log.error(e.getStatusCode().toString());
            model.addAttribute("sendApplicationError", true);
        }

        model.addAttribute("returnUrl", viewName);
        return "redirect:" + housingUrl + "/userApplications/" + role + "/saveApplication";
    }
}
