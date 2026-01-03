package com.cinestream.common.exception;

public class AlreadyExistsException extends BaseException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public static AlreadyExistsException of(String resource) {
        return new AlreadyExistsException(resource + " already exists");
    }
}
