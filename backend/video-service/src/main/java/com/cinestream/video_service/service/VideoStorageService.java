package com.cinestream.video_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoStorageService {
    String uploadVideo(MultipartFile file);
}
