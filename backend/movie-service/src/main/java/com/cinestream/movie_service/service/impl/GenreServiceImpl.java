package com.cinestream.movie_service.service.impl;

import com.cinestream.movie_service.domain.Genre;
import com.cinestream.movie_service.dto.request.GenreRequest;
import com.cinestream.movie_service.dto.response.GenreResponse;
import com.cinestream.movie_service.repository.GenreRepository;
import com.cinestream.movie_service.service.GenreService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreResponse create(GenreRequest request) {

        if (genreRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Genre already exists: " + request.getName());
        }

        Genre genre = Genre.builder()
                .name(request.getName())
                .build();

        return GenreResponse.fromEntity(genreRepository.save(genre));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponse> getAll() {

        return genreRepository.findAll()
                .stream()
                .map(GenreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponse getById(Long id) {

        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found: " + id));

        return GenreResponse.fromEntity(genre);
    }

    @Override
    public GenreResponse update(Long id, GenreRequest request) {

        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found: " + id));

        genre.setName(request.getName());
        return GenreResponse.fromEntity(genreRepository.save(genre));
    }

    @Override
    public void delete(Long id) {

        if (!genreRepository.existsById(id)) {
            throw new RuntimeException("Genre not found: " + id);
        }

        genreRepository.deleteById(id);
    }

}
