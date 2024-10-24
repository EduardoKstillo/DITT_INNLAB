package com.bezkoder.spring.security.postgresql.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FCMInterruptedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FCMInterruptedException(String message) {
        super(message);
    }

    public FCMInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}