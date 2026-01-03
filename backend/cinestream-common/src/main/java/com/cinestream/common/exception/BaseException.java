package com.cinestream.common.exception;

public abstract class BaseException extends RuntimeException {

    protected BaseException(String message) {
        super(message);
    }
}

