package com.cinestream.movie_service.dto.response;

import com.cinestream.movie_service.domain.Genre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreResponse {

    private Long id;
    private String name;

    public static GenreResponse fromEntity(Genre genre) {
        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}

