package com.nuvem.system_places_manages.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePlaceNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePlaceName {
    String message() default "deve ser único";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
