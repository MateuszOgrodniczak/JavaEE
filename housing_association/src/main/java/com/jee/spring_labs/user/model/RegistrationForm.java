package com.jee.spring_labs.user.model;

import com.jee.spring_labs.model.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {
    //Basic data
    private String name;
    private String surname;
    private String email;
    private String telephone;

    //Address
    private String street;
    private String city;
    private String postalCode;

    //Credentials
    private String username;
    private UserRole role;
    private String password;
    private String confirmedPassword;

    //Recaptcha
    private boolean verified;
}
