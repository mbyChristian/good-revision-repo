package org.study.subjectresource.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.study.subjectresource.dto.UsersDto;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UsersDto> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // Initialisation si nécessaire
    }

    @Override
    public boolean isValid(UsersDto usersDto, ConstraintValidatorContext context) {
        if (usersDto.password() == null || usersDto.confirmPassword() == null) {
            return false;
        }
        return usersDto.password().equals(usersDto.confirmPassword());
    }
}