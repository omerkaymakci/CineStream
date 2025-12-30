package com.cinestream.auth_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ApiErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // getters
}

