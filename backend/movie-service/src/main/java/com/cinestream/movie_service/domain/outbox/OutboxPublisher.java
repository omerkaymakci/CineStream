package com.cinestream.movie_service.domain.outbox;

import com.cinestream.movie_service.domain.outbox.OutboxEvent;
import com.cinestream.movie_service.domain.outbox.OutboxStatus;
import com.cinestream.movie_service.outbox.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    private static final String TOPIC = "movie-events";
    private static final int MAX_BATCH_SIZE = 100;
    private static final int MAX_RETRY = 3;

    public OutboxPublisher(OutboxEventRepository repository,
                           KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Her 3 saniyede bir çalışacak scheduler.
     * DB’den NEW statüsündeki eventleri alır, Kafka’ya gönderir.
     */
    @Scheduled(fixedDelay = 3000)
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                repository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);

        for (OutboxEvent event : events) {
            try {
                // Kafka'ya gönder
                kafkaTemplate.send(TOPIC, event.getAggregateId(), event.getPayload());

                // Gönderim başarılı → SENT yap
                event.markSent();
                event.setProcessedAt(Instant.now());

            } catch (Exception ex) {
                // Hata olursa retry sayısını artır
                int retries = event.getRetryCount() + 1;
                event.setRetryCount(retries);

                if (retries >= MAX_RETRY) {
                    // Retry hakkı doldu → FAILED
                    event.markFailed();
                }
            }

            // Her durumda DB’ye kaydet
            repository.save(event);
        }
    }
}