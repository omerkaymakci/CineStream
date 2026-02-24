package com.cinestream.movie_service.domain.outbox;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregate_type", nullable = false, length = 50)
    private AggregateType aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 50)
    private String aggregateId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    /**
     * Protobuf event serialized as JSON or bytes (bytea / jsonb)
     */
    @Lob
    @Column(nullable = false)
    private byte[] payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected OutboxEvent() {
        // JPA
    }

    private OutboxEvent(
            UUID id,
            AggregateType aggregateType,
            String aggregateId,
            String eventType,
            byte[] payload
    ) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = OutboxStatus.NEW;
    }

    public static OutboxEvent create(
            AggregateType aggregateType,
            String aggregateId,
            String eventType,
            byte[] payload
    ) {
        return new OutboxEvent(
                UUID.randomUUID(),
                aggregateType,
                aggregateId,
                eventType,
                payload
        );
    }

    public void markSent() {
        this.status = OutboxStatus.SENT;
    }

    public void markFailed() {
        this.status = OutboxStatus.FAILED;
    }

    // --- Getters ---

    public UUID getId() {
        return id;
    }

    public AggregateType getAggregateType() {
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
}

