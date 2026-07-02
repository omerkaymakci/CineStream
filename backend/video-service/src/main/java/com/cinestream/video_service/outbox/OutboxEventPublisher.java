package com.cinestream.video_service.outbox;

import com.cinestream.video_service.domain.outbox.OutboxEvent;
import com.cinestream.video_service.domain.outbox.OutboxStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Relays NEW outbox rows to the {@code video-events} topic, so movie-service can
 * react once a video upload finishes. Runs on a fixed schedule.
 */
@Component
public class OutboxEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisher.class);

    private static final String VIDEO_EVENT_TOPIC = "video-events";
    private static final int MAX_RETRY = 3;

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public OutboxEventPublisher(OutboxEventRepository repository,
                                KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                repository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                        VIDEO_EVENT_TOPIC,
                        event.getAggregateId(), // key = movieId
                        event.getPayload()
                ).get(); // block → delivery guarantee

                event.markSent();

            } catch (Exception ex) {
                log.error("Failed to publish video outbox event {}", event.getId(), ex);
                event.incrementRetry();
                if (event.getRetryCount() >= MAX_RETRY) {
                    event.markFailed();
                }
            }

            repository.save(event);
        }
    }
}
