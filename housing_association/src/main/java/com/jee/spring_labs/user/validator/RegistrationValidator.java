package com.jee.spring_labs.user.validator;

import com.jee.spring_labs.user.model.RegistrationForm;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class RegistrationValidator implements Validator {

    private EmailValidator emailValidator = EmailValidator.getInstance();
    private static final String POSTAL_CODE_REGEX = "\\d{2}-\\d{3}";
    private static final String PHONE_NUMBER_REGEX = "\\d{3}-\\d{3}-\\d{3}";

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "surname", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "telephone", "error.field.required");


        ValidationUtils.rejectIfEmpty(errors, "username", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "password", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "confirmedPassword", "error.field.required");

        RegistrationForm form = (RegistrationForm) target;
        String email = form.getEmail();
        String password = form.getPassword();
        String confirmedPassword = form.getConfirmedPassword();
        String telephone = form.getTelephone();
        String postalCode = form.getPostalCode();
        boolean verified = form.isVerified();

        if (errors.getErrorCount() == 0) {
            if (!verified) {
                errors.rejectValue("verified", "error.recaptcha.failed");
            }
            if (StringUtils.hasText(email) && !emailValidator.isValid(email)) {
                errors.rejectValue("email", "error.email.invalid");
            }
            if (!telephone.matches(PHONE_NUMBER_REGEX)) {
                errors.rejectValue("telephone", "error.telephone.invalid");
            }
            if (!password.equals(confirmedPassword)) {
                errors.rejectValue("confirmedPassword", "error.password.no_match");
            }
            if (StringUtils.hasText(postalCode) && !postalCode.matches(POSTAL_CODE_REGEX)) {
                errors.rejectValue("postalCode", "error.postal_code.invalid");
            }
        }
    }
}
