package com.comp.tasks.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 5941673567888912923L;

    public ForbiddenException() {
        super();
    }
    public ForbiddenException(Exception parent){
        super(parent);
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
