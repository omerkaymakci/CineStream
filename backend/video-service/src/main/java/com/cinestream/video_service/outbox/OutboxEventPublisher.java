package com.cinestream.movie_service.outbox;

import com.cinestream.movie_service.domain.outbox.OutboxEvent;
import com.cinestream.movie_service.domain.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private static final String MOVIE_EVENT_TOPIC = "movie.events";

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void publish() {

        List<OutboxEvent> events =
                outboxEventRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                        MOVIE_EVENT_TOPIC,
                        event.getAggregateId(), // key
                        event.getPayload()
                ).get(); //block = guarantee

                event.markSent();

            } catch (Exception ex) {
                log.error("Failed to publish outbox event {}", event.getId(), ex);
                event.markFailed();
            }
        }
    }
}

