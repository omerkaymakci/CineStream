package com.cinestream.movie_service.service.Impl;

import com.cinestream.common.exception.ResourceNotFoundException;
import com.cinestream.movie_service.domain.Movie;
import com.cinestream.movie_service.repository.MovieRepository;
import com.cinestream.movie_service.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository repository;

    public MovieServiceImpl(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Movie create(Movie movie) {
        return repository.save(movie);
    }

    @Override
    public Movie getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Movie not found: " + id));
    }

    @Override
    public List<Movie> getAll() {
        return repository.findAll();
    }

    @Override
    public Movie update(Long id, Movie updated) {
        Movie existing = getById(id);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setReleaseDate(updated.getReleaseDate());
        existing.setDurationMinutes(updated.getDurationMinutes());

        return repository.save(existing);
    }

    @Override
    public void deactivate(Long id) {
        Movie movie = getById(id);
        movie.setActive(false);
        repository.save(movie);
    }
}

