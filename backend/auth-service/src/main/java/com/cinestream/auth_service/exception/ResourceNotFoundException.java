package com.cinestream.auth_service.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String roleName) {
        super("Role not found: " + roleName);
    }
}

