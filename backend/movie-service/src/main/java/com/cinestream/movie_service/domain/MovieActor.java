package com.cinestream.movie_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie_actors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieActor {

    @EmbeddedId
    private MovieActorId id;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @MapsId("actorId")
    @JoinColumn(name = "actor_id")
    private Actor actor;

    private String role; // karakter adı
}

