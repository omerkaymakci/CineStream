package com.cinestream.common.exception;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String resource) {
        return new ResourceNotFoundException(resource + " not found");
    }
}

