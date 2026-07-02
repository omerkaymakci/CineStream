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
public class MovieEventConsumer {

    private final CloudFlareService cloudFlareService;
    private final VideoUploadRecordService uploadRecordService;
    private final VideoUploadProcessor videoUploadProcessor;

    private static final Logger log = LoggerFactory.getLogger(MovieEventConsumer.class);

    @KafkaListener(topics = "movie-events", groupId = "video-service-group")
    public void consumeMovieEvent(ConsumerRecord<String, byte[]> record) throws InvalidProtocolBufferException {

        // Kafka’dan gelen event’i parse et
        MovieEvent event = MovieEvent.parseFrom(record.value());

        Long movieId = event.getMovieId();
        String videoUrl = event.getVideoUrl();

        // Eğer bu movie daha önce işlenmişse atla (idempotency)
        if (uploadRecordService.existsByMovieId(movieId)) {
            System.out.println("Movie already processed, skipping movieId=" + movieId);
            return;
        }

        // Video’yu Cloudflare’a yükle
        String videoKey = cloudFlareService.upload(videoUrl, movieId);

        // Kayıt + outbox event'i tek transaction'da yaz (transactional outbox).
        // Outbox publisher bunu "video-events" topic'ine iletip movie-service'in
        // filmi PUBLISHED yapmasını sağlar.
        videoUploadProcessor.completeUpload(movieId, videoUrl, videoKey);

        log.info("Movie processed successfully, movieId=" + movieId);

    }
}