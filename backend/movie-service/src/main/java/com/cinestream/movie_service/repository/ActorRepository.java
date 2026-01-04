package com.cinestream.movie_service.repository;

import com.cinestream.movie_service.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
}
