package com.example.authjwtdemo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnAuthenticatedError extends ResponseStatusException {
    public UnAuthenticatedError() {
        super(HttpStatus.UNAUTHORIZED,"Authentication Error.");
    }
}
