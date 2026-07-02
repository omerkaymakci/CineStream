package com.cinestream.video_service.service;

public interface CloudFlareService {
    /**
     * Verilen video URL'sini Cloudflare R2/S3'e upload eder
     * @param videoUrl video kaynağı URL
     * @param movieId hangi film ile ilişkili
     * @return depolamadaki nesne anahtarı (storage key)
     */
    String upload(String videoUrl, Long movieId);
}
