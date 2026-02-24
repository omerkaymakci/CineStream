package com.cinestream.movie_service.service.impl;

import com.cinestream.common.exception.AlreadyExistsException;
import com.cinestream.common.exception.ResourceNotFoundException;
import com.cinestream.common.proto.MovieEvent;
import com.cinestream.movie_service.domain.Movie;
import com.cinestream.movie_service.domain.outbox.AggregateType;
import com.cinestream.movie_service.domain.outbox.OutboxEvent;
import com.cinestream.movie_service.outbox.OutboxEventRepository;
import com.cinestream.movie_service.mapper.MovieEventMapper;
import com.cinestream.movie_service.repository.MovieRepository;
import com.cinestream.movie_service.service.MovieService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final OutboxEventRepository outboxEventRepository;

    private static final String MOVIE_EVENT_TOPIC = "movie-events";

    public MovieServiceImpl(MovieRepository movieRepository,
                            KafkaTemplate<String, byte[]> kafkaTemplate,
                            OutboxEventRepository outboxEventRepository) {
        this.movieRepository = movieRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.outboxEventRepository = outboxEventRepository;
    }

    @Override
    @Transactional
    public Movie create(Movie movie) {

        movieRepository.findByTitle(movie.getTitle())
                .ifPresent(m -> {
                    throw AlreadyExistsException.of("Movie");
                });

        Movie saved = movieRepository.save(movie);

        OutboxEvent outboxEvent = OutboxEvent.create(
                AggregateType.MOVIE,
                saved.getId().toString(),
                "CREATED",
                MovieEventMapper.toPayload(saved) // protobuf → byte[]
        );

        outboxEventRepository.save(outboxEvent);

        return saved;
    }


    @Override
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Movie"));
    }

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie update(Long id, Movie movie) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Movie"));

        existing.setTitle(movie.getTitle());
        existing.setDescription(movie.getDescription());
        existing.setVideoUrl(movie.getVideoUrl());

        Movie updated = movieRepository.save(existing);

        sendEvent(updated, "UPDATED");

        return updated;
    }

    @Override
    public void delete(Long id) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Movie"));

        movieRepository.delete(existing);

        sendEvent(existing, "DELETED");
    }

    private void sendEvent(Movie movie, String eventType) {
        MovieEvent event = MovieEvent.newBuilder()
                .setMovieId(movie.getId())
                .setTitle(movie.getTitle())
                .setStatus(movie.getStatus() != null ? movie.getStatus() : "")
                .setVideoUrl(movie.getVideoUrl() != null ? movie.getVideoUrl() : "")
                .build();

        kafkaTemplate.send(MOVIE_EVENT_TOPIC, event.toByteArray());
    }
}
