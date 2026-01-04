package com.cinestream.movie_service.controller;

import com.cinestream.common.exception.ResourceNotFoundException;
import com.cinestream.movie_service.domain.Genre;
import com.cinestream.movie_service.domain.Movie;
import com.cinestream.movie_service.dto.request.MovieRequest;
import com.cinestream.movie_service.dto.response.MovieResponse;
import com.cinestream.movie_service.repository.GenreRepository;
import com.cinestream.movie_service.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService service;
    private final GenreRepository genreRepository;

    public MovieController(MovieService service,
                           GenreRepository genreRepository) {
        this.service = service;
        this.genreRepository = genreRepository;
    }

    @PostMapping
    public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest request) {
        Set<Genre> genres = new HashSet<>();
        if (request.getGenreIds() != null) {
            genres = request.getGenreIds().stream()
                    .map(id -> genreRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + id)))
                    .collect(Collectors.toSet());
        }

        Movie created = service.create(request.toEntity(genres));

        // Location header ekliyoruz
        URI location = URI.create("/api/movies/" + created.getId());
        return ResponseEntity.created(location)
                .body(MovieResponse.fromEntity(created));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAll() {
        List<Movie> movies = service.getAll();
        List<MovieResponse> responses = movies.stream()
                .map(MovieResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getById(@PathVariable Long id) {
        Movie movie = service.getById(id); // Service exception fırlatırsa GlobalExceptionHandler yakalar
        return ResponseEntity.ok(MovieResponse.fromEntity(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody MovieRequest request) {
        Set<Genre> genres = new HashSet<>();
        if (request.getGenreIds() != null) {
            genres = request.getGenreIds().stream()
                    .map(idTemp -> genreRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + id)))
                    .collect(Collectors.toSet());
        }

        Movie updated = service.update(id, request.toEntity(genres));
        return ResponseEntity.ok(MovieResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}


