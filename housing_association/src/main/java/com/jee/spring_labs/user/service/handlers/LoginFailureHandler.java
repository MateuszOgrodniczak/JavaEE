package com.jee.spring_labs.user.service.handlers;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    /**
     * Redirect error codes
     */
    private static final String ERROR_CODE_BAD_CREDENTIALS = "0";
    private static final String ERROR_CODE_ACCOUNT_DISABLED = "1";
    private static final String ERROR_CODE_UNKNOWN = "2";

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String redirectUrl = "/home/login?error=";
        if (e instanceof BadCredentialsException) {
            httpServletResponse.sendRedirect(redirectUrl + ERROR_CODE_BAD_CREDENTIALS);
        } else if (e instanceof DisabledException) {
            String username = httpServletRequest.getParameter("username");
            httpServletResponse.sendRedirect(redirectUrl + ERROR_CODE_ACCOUNT_DISABLED + "&username=" + username);
        } else {
            httpServletResponse.sendRedirect(redirectUrl + ERROR_CODE_UNKNOWN);
        }
    }
}
