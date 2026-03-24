package com.cinestream.video_service.service.impl;

import com.cinestream.video_service.domain.VideoUploadRecord;
import com.cinestream.video_service.repository.VideoUploadRecordRepository;
import com.cinestream.video_service.service.VideoUploadRecordService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Component
public class VideoUploadRecordServiceImpl implements VideoUploadRecordService {
    private final VideoUploadRecordRepository repository;

    public VideoUploadRecordServiceImpl(VideoUploadRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByMovieId(Long movieId) {
        return repository.existsByMovieId(movieId);
    }

    @Override
    public void saveVideoUploadRecord(Long movieId, String status, String videoUrl) {
        VideoUploadRecord record = new VideoUploadRecord();
        record.setMovieId(movieId);
        record.setStatus(status);
        record.setCreatedAt(Instant.now());
        record.setVideoUrl(videoUrl);
        repository.save(record);
    }
}
