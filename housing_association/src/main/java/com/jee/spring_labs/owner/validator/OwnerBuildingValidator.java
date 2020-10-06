package com.jee.spring_labs.owner.validator;

import com.jee.spring_labs.model.Building;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class OwnerBuildingValidator implements Validator {

    private static final int FIELD_MAX_SIZE = 30;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Building.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.street", "error.field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.city", "error.field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.postalCode", "error.field.required");

        Building building = (Building) target;
        String name = building.getName();
        String street = building.getAddress().getStreet();
        String city = building.getAddress().getCity();
        String postalCode = building.getAddress().getPostalCode();

        if (name.length() > FIELD_MAX_SIZE) {
            errors.rejectValue("name", "error.field.too.long");
        }
        if (street.length() > FIELD_MAX_SIZE) {
            errors.rejectValue("street", "error.field.too.long");
        }
        if (city.length() > FIELD_MAX_SIZE) {
            errors.rejectValue("city", "error.field.too.long");
        }
        if (postalCode.length() > FIELD_MAX_SIZE) {
            errors.rejectValue("postalCode", "error.field.too.long");
        }
    }
}
