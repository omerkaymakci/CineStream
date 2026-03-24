package com.cinestream.video_service.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "video_upload_record")
@Data
public class VideoUploadRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;

    private String videoUrl;

    private String status; // READY / FAILED

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}
