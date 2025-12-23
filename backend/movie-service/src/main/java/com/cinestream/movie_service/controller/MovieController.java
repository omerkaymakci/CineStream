package com.cinestream.movie_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    @GetMapping("/movies")
    public List<String> getMovies() {
        return List.of(
                "Inception",
                "Interstellar",
                "The Dark Knight"
        );
    }
}

