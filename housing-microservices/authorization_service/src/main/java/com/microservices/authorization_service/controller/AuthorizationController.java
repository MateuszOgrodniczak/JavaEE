/*
package com.microservices.authorization_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/authorization")
@Slf4j
public class AuthorizationController {

    @Value("${token.invalidate.password}")
    private String allowedSecurityPassword;

    @Resource(name="defaultTokenServices")
    private ConsumerTokenServices tokenServices;

    @GetMapping("/invalidateToken/{securityPassword}/{tokenId:.+}")
    @ResponseBody
    public ResponseEntity<Void> invalidateToken(@PathVariable String securityPassword, @PathVariable String tokenId, HttpServletRequest request) {
        if(!allowedSecurityPassword.equals(securityPassword)) {
            log.error("Illegal token invalidation request from: " + request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tokenServices.revokeToken(tokenId);
        return ResponseEntity.ok().build();
    }
}
*/
