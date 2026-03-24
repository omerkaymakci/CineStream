package com.cinestream.video_service.consumer;

import com.cinestream.common.proto.MovieEvent;
import com.cinestream.video_service.config.KafkaConfig;
import com.cinestream.video_service.service.CloudFlareService;
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
        cloudFlareService.upload(videoUrl, movieId);

        // İşlem tamamlandı → record ekle
        uploadRecordService.saveVideoUploadRecord(movieId, "UPLOADED", videoUrl);

        log.info("Movie processed successfully, movieId=" + movieId);

    }
}