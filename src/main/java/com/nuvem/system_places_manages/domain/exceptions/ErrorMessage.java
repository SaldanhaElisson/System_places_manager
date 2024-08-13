package com.nuvem.system_places_manages.domain.exceptions;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorMessage {
    private Integer status;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private List<String> errorMessages;

    public ErrorMessage(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorMessage(Integer status, List<String> errorMessages) {
        this.status = status;
        this.errorMessages = errorMessages;
    }

}
