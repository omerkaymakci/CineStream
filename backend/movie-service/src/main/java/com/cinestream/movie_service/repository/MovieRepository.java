package com.cinestream.movie_service.repository;

import com.cinestream.movie_service.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    boolean existsByTitle(String title);
}
