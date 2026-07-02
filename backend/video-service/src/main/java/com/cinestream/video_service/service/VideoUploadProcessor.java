package com.cinestream.video_service.service;

import com.cinestream.common.proto.VideoEvent;
import com.cinestream.common.proto.VideoEventType;
import com.cinestream.video_service.domain.outbox.OutboxEvent;
import com.cinestream.video_service.outbox.OutboxEventRepository;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Records the outcome of a video upload and stages the corresponding VideoEvent
 * in the outbox — both in a single DB transaction (transactional outbox), so the
 * event can never be lost relative to the upload record.
 */
@Service
@RequiredArgsConstructor
public class VideoUploadProcessor {

    private static final String AGGREGATE_TYPE = "VIDEO";

    private final VideoUploadRecordService uploadRecordService;
    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void completeUpload(Long movieId, String videoUrl, String videoKey) {
        uploadRecordService.saveVideoUploadRecord(movieId, "UPLOADED", videoUrl);

        VideoEvent event = buildEvent(VideoEventType.VIDEO_READY, movieId, videoKey, "READY");
        outboxEventRepository.save(
                OutboxEvent.create(AGGREGATE_TYPE, String.valueOf(movieId), "READY", event.toByteArray())
        );
    }

    @Transactional
    public void failUpload(Long movieId, String videoUrl) {
        uploadRecordService.saveVideoUploadRecord(movieId, "FAILED", videoUrl);

        VideoEvent event = buildEvent(VideoEventType.VIDEO_FAILED, movieId, "", "FAILED");
        outboxEventRepository.save(
                OutboxEvent.create(AGGREGATE_TYPE, String.valueOf(movieId), "FAILED", event.toByteArray())
        );
    }

    private VideoEvent buildEvent(VideoEventType type, Long movieId, String videoKey, String status) {
        Instant now = Instant.now();
        return VideoEvent.newBuilder()
                .setEventType(type)
                .setMovieId(movieId)
                .setVideoKey(videoKey == null ? "" : videoKey)
                .setStatus(status)
                .setOccurredAt(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano())
                        .build())
                .build();
    }
}
