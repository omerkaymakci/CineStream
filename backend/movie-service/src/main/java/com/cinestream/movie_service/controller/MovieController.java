package com.cinestream.movie_service.controller;

import com.cinestream.movie_service.domain.Movie;
import com.cinestream.movie_service.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @PostMapping
    public Movie create(@RequestBody Movie movie) {
        return service.create(movie);
    }

    @GetMapping("/{id}")
    public Movie get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<Movie> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @RequestBody Movie movie) {
        return service.update(id, movie);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }
}


