package com.nuvem.system_places_manages.application.validations;

import com.nuvem.system_places_manages.domain.repository.BaseRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    private final ApplicationContext applicationContext;
    private BaseRepository<?, ?> repository;

    public UniqueNameValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(UniqueName constraintAnnotation) {
        this.repository = applicationContext.getBean(constraintAnnotation.repository());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return !repository.existsByName(value);
    }
}
