package com.nuvem.system_places_manages.domain.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class CustomValidationException extends RuntimeException {
    private final BindingResult bindingResult;

    public CustomValidationException(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
    }

}