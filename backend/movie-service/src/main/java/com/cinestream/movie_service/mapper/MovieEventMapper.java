package com.cinestream.movie_service.mapper;

import com.cinestream.common.proto.MovieEvent;
import com.cinestream.common.proto.MovieEventType;
import com.cinestream.movie_service.domain.Movie;
import com.google.protobuf.Timestamp;

import java.time.Instant;

public final class MovieEventMapper {

    private MovieEventMapper() {
        // utility class
    }

    public static MovieEvent toCreatedEvent(Movie movie) {
        return baseBuilder(movie)
                .setEventType(MovieEventType.CREATED)
                .build();
    }

    public static MovieEvent toUpdatedEvent(Movie movie) {
        return baseBuilder(movie)
                .setEventType(MovieEventType.UPDATED)
                .build();
    }

    public static MovieEvent toDeletedEvent(Movie movie) {
        return MovieEvent.newBuilder()
                .setEventType(MovieEventType.DELETED)
                .setMovieId(movie.getId())
                .setOccurredAt(now())
                .build();
    }

    /* ---------- Payload ---------- */

    public static byte[] toPayload(MovieEvent event) {
        return event.toByteArray();
    }

    public static byte[] toPayload(Movie movie) {
        return toPayload(toCreatedEvent(movie));
    }

    /* ---------- Common builder ---------- */

    private static MovieEvent.Builder baseBuilder(Movie movie) {
        return MovieEvent.newBuilder()
                .setMovieId(movie.getId())
                .setTitle(movie.getTitle())
                .setStatus(movie.getStatus())
                .setVideoUrl(movie.getVideoUrl())
                .setOccurredAt(now());
    }

    private static Timestamp now() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }
}
