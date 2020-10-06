package com.jee.spring_labs.user.service.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Slf4j
public class OauthLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${token.invalidate.password}")
    private String tokenInvalidatePassword;

    @Value("${microservices.housing.url}")
    private String housingAssociationUrl;

    @Autowired
    private RestTemplate template;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        /*try {
            OAuth2AccessToken accessToken = (OAuth2AccessToken) session.getAttribute("accessToken");
            ResponseEntity responseEntity = template.exchange("http://localhost:8000/authorization/invalidateToken/" + tokenInvalidatePassword
                            + "/" + accessToken.getValue(),
                    HttpMethod.GET, null, ResponseEntity.class);
            if (responseEntity.getStatusCode().is4xxClientError()) {
                log.error("Logout failed: authorization server could not invalidate access token");
            }
        } catch (ClassCastException e) {
            log.error("Logout failed: could not obtain oauth access token from current session");
        }*/
        session.invalidate();
        response.sendRedirect(housingAssociationUrl + "/home");
    }
}
