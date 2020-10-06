package com.jee.spring_labs.user.service.handlers;

import com.jee.spring_labs.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private PasswordEncoder encoder;

    @Value("${microservices.housing.url}")
    private String housingAssociationUrl;

    @Value("${microservices.authorization.url}")
    private String authorizationServerUrl;

    @Value("${microservices.authorization.client}")
    private String authorizationServerClientId;

    @Value("${microservices.authorization.secret}")
    private String authorizationServerSecret;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        HttpSession session = httpServletRequest.getSession();
        String passwordUnencoded = (String) session.getAttribute("passwordUnencoded");
        session.setAttribute("passwordUnencoded", null);

        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setUsername(user.getUsername());
        details.setPassword(passwordUnencoded);
        details.setClientId(authorizationServerClientId);
        details.setClientSecret(authorizationServerSecret);
        details.setAccessTokenUri(authorizationServerUrl);

        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details);
        try {
            OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
            httpServletRequest.getSession().setAttribute("accessToken", accessToken);
            System.out.println(accessToken.getTokenType() + ", " + accessToken.getValue() + ", " + accessToken.getScope() + ", " + accessToken.getAdditionalInformation());
        } catch (Exception e) {
            log.error(e.getMessage());
            httpServletResponse.sendRedirect(housingAssociationUrl + "/home/login?error=0");
            return;
        }
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());
        httpServletResponse.sendRedirect(housingAssociationUrl + "/" + user.getRole().toString().toLowerCase());
    }
}
