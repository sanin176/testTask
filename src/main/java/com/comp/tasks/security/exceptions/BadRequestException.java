package com.comp.tasks.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 5941673567888932923L;

    public BadRequestException() {
        super();
    }
    public BadRequestException(Exception parent){
        super(parent);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
