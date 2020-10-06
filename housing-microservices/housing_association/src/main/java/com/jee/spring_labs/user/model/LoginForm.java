package com.jee.spring_labs.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginForm {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
