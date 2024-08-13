package com.nuvem.system_places_manages.infra;

import com.nuvem.system_places_manages.domain.exceptions.CustomValidationException;
import com.nuvem.system_places_manages.domain.exceptions.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.ArrayList;

@RestControllerAdvice
public class HandlerControler {

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorMessage> handleCustomValidationException(CustomValidationException ex) {
        var status = HttpStatus.BAD_REQUEST;
        var errorMessages = new ArrayList<String>();
        var fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            var msg = "O campo " + fieldError.getField() + " " + fieldError.getDefaultMessage();
            errorMessages.add(msg);
        });

        return ResponseEntity.status(status).body(new ErrorMessage(status.value(), errorMessages));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex) {
        var status = HttpStatus.BAD_REQUEST;
        var errorMessages = new ArrayList<String>();
        var fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            var msg = "O campo " + fieldError.getField() + " " + fieldError.getDefaultMessage();
            errorMessages.add(msg);
        });

        return ResponseEntity.status(status).body(new ErrorMessage(status.value(), errorMessages));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException ex) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ErrorMessage(status.value(), ex.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro na solicitação: " + ex.getMessage());
    }

}
