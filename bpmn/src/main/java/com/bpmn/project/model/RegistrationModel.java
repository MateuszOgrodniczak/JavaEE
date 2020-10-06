package com.bpmn.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationModel {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
	private String login;
    private String password;
    private String confirmedPassword;
}
