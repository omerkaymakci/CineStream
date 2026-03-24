package com.cinestream.video_service.repository;

import com.cinestream.video_service.domain.VideoUploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoUploadRecordRepository extends JpaRepository<VideoUploadRecord, Long> {
    boolean existsByMovieId(Long movieId);
}
