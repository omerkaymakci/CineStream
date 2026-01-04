package com.cinestream.movie_service.dto.request;

import com.cinestream.movie_service.domain.Genre;
import com.cinestream.movie_service.domain.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    private String title;
    private String description;
    private Integer durationMinutes;
    private String status;   // DRAFT, PUBLISHED
    private Boolean active;  // opsiyonel, default true
    private LocalDate releaseDate; // opsiyonel, default today
    private String videoUrl;
    private Set<Long> genreIds; // sadece genre ID'lerini gönderiyoruz

    public Movie toEntity(Set<Genre> genres) {
        return Movie.builder()
                .title(this.title)
                .description(this.description)
                .durationMinutes(this.durationMinutes)
                .status(this.status)
                .active(this.active != null ? this.active : true)
                .releaseDate(this.releaseDate != null ? this.releaseDate : LocalDate.now())
                .videoUrl(this.videoUrl)
                .genres(genres != null ? genres : new HashSet<>())
                .build();
    }
}
