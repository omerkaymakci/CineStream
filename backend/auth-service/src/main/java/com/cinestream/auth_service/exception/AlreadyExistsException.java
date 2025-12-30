package com.cinestream.auth_service.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String username) {
        super("User already exists: " + username);
    }
}

