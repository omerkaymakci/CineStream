package com.cinestream.movie_service.service;

import com.cinestream.movie_service.dto.request.GenreRequest;
import com.cinestream.movie_service.dto.response.GenreResponse;

import java.util.List;

public interface GenreService {

    GenreResponse create(GenreRequest request);

    List<GenreResponse> getAll();

    GenreResponse getById(Long id);

    GenreResponse update(Long id, GenreRequest request);

    void delete(Long id);

}
