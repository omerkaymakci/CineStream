package com.cinestream.movie_service.repository;

import com.cinestream.movie_service.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
