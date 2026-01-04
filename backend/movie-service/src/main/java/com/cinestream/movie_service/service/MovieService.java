package com.cinestream.movie_service.service;

import com.cinestream.common.exception.AlreadyExistsException;
import com.cinestream.common.exception.ResourceNotFoundException;
import com.cinestream.movie_service.domain.Movie;

import java.util.List;

public interface MovieService {

    Movie create(Movie movie);

    Movie getById(Long id);

    List<Movie> getAll();

    Movie update(Long id, Movie movie);

    void delete(Long id);
}

