package com.cinestream.movie_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenreRequest {

    @NotBlank(message = "Genre name cannot be empty")
    private String name;
}
