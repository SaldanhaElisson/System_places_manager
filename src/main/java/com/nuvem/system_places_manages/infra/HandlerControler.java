package com.nuvem.system_places_manages.infra;

import com.nuvem.system_places_manages.domain.execptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestControllerAdvice
public class HandlerControler {

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

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleResponseStatusException(ResponseStatusException ex) {
        var status = ex.getStatusCode();
        return ResponseEntity.status(status).body(new ErrorMessage(status.value(), "Entidade não encontrada"));
    }

}
