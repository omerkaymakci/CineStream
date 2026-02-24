package com.cinestream.movie_service.domain.outbox;

public enum OutboxStatus {
    NEW,
    SENT,
    FAILED
}