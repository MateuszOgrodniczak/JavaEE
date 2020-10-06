package com.bpmn.project.validator;

import com.bpmn.project.model.RegistrationModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.apache.commons.validator.routines.EmailValidator;

@Component
public class RegistrationFormValidator implements Validator {
    public boolean supports(Class<?> clazz) {
        return clazz.equals(RegistrationModel.class);
    }

    private EmailValidator emailValidator = EmailValidator.getInstance();
    private static final String PHONE_NUMBER_REGEX = "\\d{3}-\\d{3}-\\d{3}";
    private static final String PHONE_NUMBER_REGEX_ONLY_DIGITS = "\\d{9}";

    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "surname", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "phoneNumber", "error.field.required");

        ValidationUtils.rejectIfEmpty(errors, "login", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "password", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "confirmedPassword", "error.field.required");

        RegistrationModel form = (RegistrationModel) o;
        String email = form.getEmail();
        String password = form.getPassword();
        String confirmedPassword = form.getConfirmedPassword();
        String phoneNumber = form.getPhoneNumber();

        if(errors.getErrorCount() == 0) {
            if(StringUtils.hasText(email) && !emailValidator.isValid(email)) {
                errors.rejectValue("email", "error.email.invalid");
            }
            if(!phoneNumber.matches(PHONE_NUMBER_REGEX) && !phoneNumber.matches(PHONE_NUMBER_REGEX_ONLY_DIGITS)) {
                errors.rejectValue("phoneNumber", "error.phoneNumber.invalid");
            }
            if(!password.equals(confirmedPassword)) {
                errors.rejectValue("confirmedPassword", "error.password.noMatch");
            }
        }
    }
}
