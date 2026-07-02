package com.cinestream.movie_service.consumer;

import com.cinestream.common.proto.VideoEvent;
import com.cinestream.movie_service.domain.Movie;
import com.cinestream.movie_service.repository.MovieRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Consumes VideoEvent messages published by video-service once a video upload
 * finishes, and updates the corresponding movie (marks it PUBLISHED and stores
 * the resulting storage key). Closes the video-upload saga loop.
 */
@Component
@RequiredArgsConstructor
public class VideoEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(VideoEventConsumer.class);

    private final MovieRepository movieRepository;

    @KafkaListener(topics = "video-events", groupId = "movie-service-group")
    @Transactional
    public void consumeVideoEvent(byte[] message) throws InvalidProtocolBufferException {

        VideoEvent event = VideoEvent.parseFrom(message);
        Long movieId = event.getMovieId();

        Optional<Movie> optional = movieRepository.findById(movieId);
        if (optional.isEmpty()) {
            log.warn("VideoEvent received for unknown movieId={}", movieId);
            return;
        }

        Movie movie = optional.get();

        switch (event.getEventType()) {
            case VIDEO_READY -> {
                if (!event.getVideoKey().isEmpty()) {
                    movie.setVideoUrl(event.getVideoKey());
                }
                movie.setStatus("PUBLISHED");
                movieRepository.save(movie);
                log.info("Movie {} marked PUBLISHED after upload (key={})",
                        movieId, event.getVideoKey());
            }
            case VIDEO_FAILED -> log.warn(
                    "Video upload FAILED for movieId={}, movie stays {}",
                    movieId, movie.getStatus());
            default -> log.warn("Unhandled VideoEventType for movieId={}", movieId);
        }
    }
}
