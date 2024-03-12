package com.scalablescripts.auth.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCredentialsError extends ResponseStatusException {
    public InvalidCredentialsError() {
        super(HttpStatus.BAD_REQUEST, "invalid credentials");
    }
}
