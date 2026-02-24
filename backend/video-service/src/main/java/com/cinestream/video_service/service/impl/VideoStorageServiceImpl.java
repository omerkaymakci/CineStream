package com.cinestream.video_service.service.impl;

import com.cinestream.video_service.service.VideoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VideoStorageServiceImpl implements VideoStorageService {
    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    public String uploadVideo(MultipartFile file) {

        String key = "movies/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (IOException e) {
            throw new RuntimeException("Video upload failed", e);
        }

        return key;
    }
}
