package com.comp.tasks.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 5941442522888932923L;

    public AuthenticationException() {
        super();
    }
    public AuthenticationException(Exception parent){
        super(parent);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
