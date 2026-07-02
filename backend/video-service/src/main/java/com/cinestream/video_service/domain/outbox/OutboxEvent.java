package com.cinestream.video_service.domain.outbox;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Transactional outbox row for video-service. A VideoEvent is serialized into
 * {@code payload} within the same DB transaction that records the upload, then
 * relayed to Kafka by {@code OutboxEventPublisher}.
 */
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "aggregate_type", nullable = false, length = 50)
    private String aggregateType; // VIDEO

    @Column(name = "aggregate_id", nullable = false, length = 50)
    private String aggregateId;   // movieId

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;     // READY / FAILED

    @Column(nullable = false)
    private byte[] payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    protected OutboxEvent() {
        // JPA
    }

    private OutboxEvent(UUID id,
                        String aggregateType,
                        String aggregateId,
                        String eventType,
                        byte[] payload) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = OutboxStatus.NEW;
    }

    public static OutboxEvent create(String aggregateType,
                                     String aggregateId,
                                     String eventType,
                                     byte[] payload) {
        return new OutboxEvent(UUID.randomUUID(), aggregateType, aggregateId, eventType, payload);
    }

    public void markSent() {
        this.status = OutboxStatus.SENT;
        this.processedAt = Instant.now();
    }

    public void markFailed() {
        this.status = OutboxStatus.FAILED;
    }

    public void incrementRetry() {
        this.retryCount++;
    }

    public UUID getId() {
        return id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public byte[] getPayload() {
        return payload;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public int getRetryCount() {
        return retryCount;
    }
}
