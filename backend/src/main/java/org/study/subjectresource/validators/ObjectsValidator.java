package org.study.subjectresource.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.ObjectDeletedException;
import org.springframework.stereotype.Component;
import org.study.subjectresource.exceptions.ObjectValidationException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectsValidator<T> {
    private final ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public void validate(T objectTovalidate){
        Set<ConstraintViolation<T>> violations = validator.validate(objectTovalidate);
        if(!violations.isEmpty()){
            Set<String> errorsMessages= violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
            throw new ObjectValidationException(errorsMessages,objectTovalidate.getClass().getSimpleName());
        }
    }
}
