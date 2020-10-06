package com.jee.spring_labs.user.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthorizationForm {
    private String grant_type;
    private String username;
    private String password;
}
