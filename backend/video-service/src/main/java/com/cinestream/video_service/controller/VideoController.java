package com.cinestream.video_service.controller;

import com.cinestream.video_service.service.VideoStorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.Map;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoStorageService videoStorageService;
    private final S3Client s3Client;

    @PostConstruct
    public void testConnection() {
        try {
            s3Client.headBucket(b -> b.bucket("media-storage"));
            System.out.println("Bucket erişimi OK");
        } catch (S3Exception e) {
            System.err.println("Bucket erişimi başarısız: " + e.awsErrorDetails().errorMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile file) {

        String key = videoStorageService.uploadVideo(file);

        return ResponseEntity.ok(
                Map.of("videoKey", key)
        );
    }
}
