package com.comp.tasks.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 594783408976409923L;

    public NotFoundException() {
        super();
    }
    public NotFoundException(Exception parent){
        super(parent);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
