package com.cinestream.video_service.service;

public interface VideoUploadRecordService {
    boolean existsByMovieId(Long movieId);
    void saveVideoUploadRecord(Long movieId, String status, String videoUrl);
}
