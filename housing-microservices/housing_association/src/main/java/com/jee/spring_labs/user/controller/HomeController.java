package com.jee.spring_labs.user.controller;

import com.jee.spring_labs.model.User;
import com.jee.spring_labs.model.UserRole;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.model.ActivationToken;
import com.jee.spring_labs.user.model.EmailRequest;
import com.jee.spring_labs.user.model.LoginForm;
import com.jee.spring_labs.user.model.RegistrationForm;
import com.jee.spring_labs.user.service.UserService;
import com.jee.spring_labs.user.validator.RegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
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
import javax.validation.constraints.Email;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Locale;

@Controller
@RequestMapping("/home")
@Slf4j
public class HomeController {
    private static final String LOGIN_ERROR_BAD_CREDENTIALS = "0";
    private static final String LOGIN_ERROR_ACCOUNT_DISABLED = "1";
    private static final String LOGIN_ERROR_UNKNOWN = "2";

    private static final String CAPTCHA_GOOGLE_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String CAPTCHA_SECRET_KEY = "6LeHkqAUAAAAAEZpnYWPzPp3iyEl8LMg7dTohDFd";
    private static final String ACTIVATION_TOKEN_URL = "http://localhost:8080/home/activateAccount?token=";

    private static final String REGISTER_PAGE = "views/register";

    @Value("${microservices.housing.url}")
    private String housingServiceUrl;

    @Value("${microservices.notification.url}")
    private String notificationServiceUrl;

    @Value("${microservices.housing.activation.url}")
    private String activationTokenUrl;

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    private final RegistrationValidator registrationValidator = new RegistrationValidator();

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RestTemplate template;

    @GetMapping
    public String getHomePage() {
        return "views/home";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(HttpServletRequest request, Locale locale) {
        ModelAndView mv = new ModelAndView("views/login");
        String errorCode = request.getParameter("error");
        if (errorCode != null) {
            String errorMessage = null;
            switch (errorCode) {
                case LOGIN_ERROR_BAD_CREDENTIALS:
                    errorMessage = messageSource.getMessage("error.login.bad.credentials", null, locale);
                    break;
                case LOGIN_ERROR_ACCOUNT_DISABLED:
                    String email = service.getEmailByUsername(request.getParameter("username"));
                    String URL = "/housing/home/sendActivationLink?email=" + email;
                    mv.addObject("resendLinkURL", URL);
                    errorMessage = messageSource.getMessage("error.login.account.disabled", null, locale);
                    break;
                case LOGIN_ERROR_UNKNOWN:
                    errorMessage = messageSource.getMessage("error.login.unknown", null, locale);
                    break;
            }
            mv.addObject("loginError", errorMessage);
        }
        mv.addObject("loginForm", new LoginForm());
        return mv;
    }

 /*   @PostMapping("/login")
    public void doLogin(@ModelAttribute LoginForm loginForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setUsername(loginForm.getUsername());
        details.setPassword(loginForm.getPassword());
        details.setClientId("politechnika");
        details.setClientSecret("politechnika1");
        details.setAccessTokenUri("http://localhost:8000/oauth/token");

        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details);
        oAuth2RestTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());
        String accessTokenHeader = null;
        try {
            OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
            httpServletRequest.getSession().setAttribute("accessToken", accessToken);
            accessTokenHeader = accessToken.getTokenType() + " " + accessToken.getValue();
            System.out.println(accessToken.getTokenType() + ", " + accessToken.getValue() + ", " + accessToken.getScope() + ", " + accessToken.getAdditionalInformation());
        } catch (Exception e) {
            log.error(e.getMessage());
            httpServletResponse.sendRedirect("http://localhost:8080/housing/home/login?error=0");
            return;
        }

        User loggedUser = userRepository.findUserByUsername(loginForm.getUsername());
        httpServletRequest.getSession().setAttribute("userId", loggedUser.getId());
        httpServletRequest.getSession().setAttribute("userRole", loggedUser.getRole());

        httpServletResponse.setHeader("Authorization", accessTokenHeader);
        httpServletResponse.sendRedirect("/housing/" + loggedUser.getRole().toString().toLowerCase());
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        try {
            OAuth2AccessToken accessToken = (OAuth2AccessToken) session.getAttribute("accessToken");
            ResponseEntity responseEntity = template.exchange("http://localhost:8000/authorization/invalidateToken/" + tokenInvalidatePassword
                            + "/" + accessToken.getValue(),
                    HttpMethod.GET, null, ResponseEntity.class);
            if (responseEntity.getStatusCode().is4xxClientError()) {
                log.error("Logout failed: authorization server could not invalidate access token");
            }
        } catch (ClassCastException e) {
            log.error("Logout failed: could not obtain oauth access token from current session");
        }
        session.invalidate();
        return getHomePage();
    }*/


    @GetMapping("/register")
    public ModelAndView getRegisterPage(HttpServletRequest request) {
        request.getSession().setAttribute("recaptchaVerified", false);
        ModelAndView mv = new ModelAndView("views/register");
        mv.addObject("registerForm", new RegistrationForm());
        mv.addObject("roles", UserRole.values());
        return mv;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerForm") RegistrationForm registerForm, BindingResult result, Model model, HttpServletRequest request) {

        boolean recaptchaVerified = (boolean) request.getSession().getAttribute("recaptchaVerified");
        registerForm.setVerified(recaptchaVerified);
        registrationValidator.validate(registerForm, result);

        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return REGISTER_PAGE;
        }

        if (service.checkIfUsernameTaken(registerForm.getUsername())) {
            model.addAttribute("roles", UserRole.values());
            result.rejectValue("username", "error.username.taken");
            return REGISTER_PAGE;
        }

        if (service.checkIfEmailTaken(registerForm.getEmail())) {
            model.addAttribute("roles", UserRole.values());
            result.rejectValue("email", "error.email.taken");
            return REGISTER_PAGE;
        }
        User user = new User();
        user.setName(registerForm.getName());
        user.setSurname(registerForm.getSurname());
        user.setEmail(registerForm.getEmail());
        user.setTelephone(registerForm.getTelephone());
        user.setUsername(registerForm.getUsername());
        user.setPassword(encoder.encode(registerForm.getPassword()));
        user.setRole(registerForm.getRole());

        service.saveUser(user);
        return "redirect:" + housingServiceUrl + "/home/sendActivationLink?email=" + registerForm.getEmail() + "&login=" + registerForm.getUsername();
    }

    @GetMapping(value = "/verifyRecaptcha")
    public @ResponseBody
    String verifyRecaptcha(@RequestParam("token") String token, HttpServletRequest request) {
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
                request.getSession().setAttribute("recaptchaVerified", true);
            }
        } catch (IOException e) {
            log.error("Error while verifying recaptcha", e);
        }

        return output.toString();
    }

    @GetMapping("/sendActivationLink")
    public ModelAndView sendActivationLink(HttpServletRequest request, Locale locale) {
        ModelAndView mv = new ModelAndView("views/accountActivation");
        String addressee = request.getParameter("email");
        String username = request.getParameter("login");

        ActivationToken token = new ActivationToken();
        token.setCreationDate(LocalDateTime.now());
        token.setExpirationDate(token.getCreationDate().plusHours(24));

        String generatedString = RandomString.make(10);
        token.setValue(generatedString);

        if (!service.setUserToken(token, addressee)) {
            mv.addObject("sendingError", messageSource.getMessage("activation_link.error.user", null, locale));
            return mv;
        }

        String URL = activationTokenUrl + generatedString + "&email=" + addressee;
        String subject = messageSource.getMessage("activation_link.subject", null, locale);
        String text = messageSource.getMessage("activation_link.text", new String[]{URL}, locale);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmailRequest emailRequest = new EmailRequest(addressee, subject, text, username);

        HttpEntity<EmailRequest> entity = new HttpEntity<>(emailRequest, headers);

        try {
            ResponseEntity<String> response = template.postForEntity(notificationServiceUrl + "/sendActivationLink", entity, String.class);
            log.info("Activation link set successfully: " + response.getBody());
            mv.addObject("linkSent", messageSource.getMessage("activation_link.sent", null, locale));
        } catch (HttpClientErrorException e) {
            log.error("Error while sending activation link: " + e.getResponseBodyAsString());
            mv.addObject("sendingError", messageSource.getMessage("activation_link.error", null, locale));
        }

        return mv;
    }

    @GetMapping("/activateAccount")
    public ModelAndView activateAccount(@RequestParam("token") String token, @RequestParam("email") String email, Locale locale) {
        ModelAndView mv = new ModelAndView("views/accountActivation");
        if (service.isTokenCorrect(token, email)) {
            mv.addObject("accountActivated", messageSource.getMessage("activation_link.activated", null, locale));
            return mv;
        }
        mv.addObject("invalidToken", messageSource.getMessage("activation_link.error.token", null, locale));
        return mv;
    }

    @GetMapping("/account/{username}")
    public String getAccountView(@PathVariable("username") String username, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null && user.getUsername().equals(username)) {
            model.addAttribute("user", user);
            return "views/account";
        }
        return "redirect:/home";
    }
}
