package com.cinestream.video_service.service.impl;

import com.cinestream.video_service.file.InMemoryMultipartFile;
import com.cinestream.video_service.service.CloudFlareService;
import com.cinestream.video_service.service.VideoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class CloudFlareServiceImpl implements CloudFlareService {

    private final VideoStorageService videoStorageService;

    /**
     * Upload video from a URL or local file path
     *
     * @param videoPathOrUrl Local file path or HTTP/HTTPS URL
     * @param movieId Movie ID
     */
    @Override
    public void upload(String videoPathOrUrl, Long movieId) {
        try (InputStream in = openStream(videoPathOrUrl)) {
            MultipartFile file = new InMemoryMultipartFile(
                    "file",
                    "movie-" + movieId + ".mp4",
                    "video/mp4",
                    in.readAllBytes()
            );

            String key = videoStorageService.uploadVideo(file);
            System.out.println("Video uploaded for movieId=" + movieId + ", key=" + key);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload video for movieId=" + movieId, e);
        }
    }

    /**
     * Open InputStream from a URL or local file
     */
    private InputStream openStream(String pathOrUrl) throws Exception {
        if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
            return new URL(pathOrUrl).openStream();
        } else {
            return Files.newInputStream(Path.of(pathOrUrl));
        }
    }
}
