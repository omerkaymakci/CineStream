package com.cinestream.video_service.service.impl;

import com.cinestream.video_service.service.VideoStorageService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(VideoStorageServiceImpl.class);

    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    @CircuitBreaker(name = "cloudflareService", fallbackMethod = "fallbackUpload")
    @Retry(name = "cloudflareRetry")
    public String uploadVideo(MultipartFile file) {
        String key = "movies/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        log.info("Uploading video to Cloudflare R2: {}", key);

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            log.info("Upload successful: {}", key);
        } catch (IOException e) {
            log.error("Failed to read video file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Video upload failed", e);
        } catch (Exception e) {
            log.error("Cloudflare R2 upload failed for: {}", key, e);
            throw e;
        }

        return key;
    }

    // Circuit breaker fallback
    public String fallbackUpload(MultipartFile file, Throwable t) {
        String failedKey = "movies/" + file.getOriginalFilename();
        log.error("Circuit breaker OPEN. Upload skipped: {}", failedKey, t);
        // Kafka consumer bunu yakalayıp DLT'ye düşer
        throw new RuntimeException("Cloudflare R2 service unavailable, upload skipped.", t);
    }
}