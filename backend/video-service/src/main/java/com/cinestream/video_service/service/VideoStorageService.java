package com.cinestream.video_service.service;

import com.cinestream.common.proto.MovieEvent;
import org.springframework.web.multipart.MultipartFile;

public interface VideoStorageService {
    String uploadVideo(MultipartFile file);
}
