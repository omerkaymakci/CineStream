package com.cinestream.video_service.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType; // VIDEO
    private String aggregateId;   // movieId

    private String type; // SUCCESS / FAILED

    @Lob
    private byte[] payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private LocalDateTime createdAt;

    public static OutboxEvent create(String aggregateType,
                                     String aggregateId,
                                     String type,
                                     byte[] payload) {
        return OutboxEvent.builder()
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .type(type)
                .payload(payload)
                .status(OutboxStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void markSent() {
        this.status = OutboxStatus.SENT;
    }

    public void markFailed() {
        this.status = OutboxStatus.FAILED;
    }
}
