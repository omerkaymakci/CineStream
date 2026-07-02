package com.cinestream.video_service.consumer;

import com.cinestream.common.proto.MovieEvent;
import com.cinestream.video_service.service.CloudFlareService;
import com.cinestream.video_service.service.VideoUploadProcessor;
import com.cinestream.video_service.service.VideoUploadRecordService;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieEventDltConsumer {

    private final CloudFlareService cloudFlareService;
    private final VideoUploadRecordService uploadRecordService;
    private final VideoUploadProcessor videoUploadProcessor;

    private static final Logger log = LoggerFactory.getLogger(MovieEventDltConsumer.class);

    @KafkaListener(topics = "movie-events.DLT", groupId = "video-service-dlt-group")
    public void replayDlt(ConsumerRecord<String, byte[]> record) throws InvalidProtocolBufferException {

        MovieEvent event = MovieEvent.parseFrom(record.value());
        Long movieId = event.getMovieId();
        String videoUrl = event.getVideoUrl();

        // Eğer zaten işlenmişse atla
        if (uploadRecordService.existsByMovieId(movieId)) {
            log.info("[DLT Replay] Movie already processed, skipping movieId={}", movieId);
            return;
        }
        try {
            // Tekrar upload denemesi
            String videoKey = cloudFlareService.upload(videoUrl, movieId);
            videoUploadProcessor.completeUpload(movieId, videoUrl, videoKey);
            log.info("[DLT Replay] Movie processed successfully, movieId={}", movieId);
        } catch (Exception e) {
            // Upload başarısız olursa FAILED kaydet + FAILED event yay
            videoUploadProcessor.failUpload(movieId, videoUrl);
            log.error("[DLT Replay] Failed to process MovieEvent, movieId={}, reason={}", movieId, e.getMessage());
        }
    }
}