package com.jee.spring_labs.admin.validator;

import com.jee.spring_labs.model.Building;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AdminBuildingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Building.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "owner.username", "error.field.required");
    }
}
