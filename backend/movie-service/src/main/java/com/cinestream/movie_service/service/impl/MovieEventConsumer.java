package com.cinestream.movie_service.service.impl;

import com.cinestream.common.proto.MovieEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MovieEventConsumer {

    @KafkaListener(
            topics = "movie-events",
            groupId = "movie-service-group"
    )
    public void listen(byte[] message) {
        try {
            MovieEvent event = MovieEvent.parseFrom(message);

            System.out.println("🎬 Movie Event Received");
            System.out.println("ID: " + event.getMovieId());
            System.out.println("Title: " + event.getTitle());
            System.out.println("Status: " + event.getStatus());

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse MovieEvent", e);
        }
    }
}

