package com.cinestream.movie_service.outbox;

import com.cinestream.movie_service.domain.outbox.OutboxEvent;
import com.cinestream.movie_service.domain.outbox.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.UUID;


public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    /**
     * Fetch NEW events in order, used by Outbox Publisher
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select e
        from OutboxEvent e
        where e.status = :status
        order by e.createdAt asc
    """)
    List<OutboxEvent> findByStatusForUpdate(
            @Param("status") OutboxStatus status
    );

    /**
     * Batch-friendly version (recommended)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select e
        from OutboxEvent e
        where e.status = :status
        order by e.createdAt asc
    """)
    List<OutboxEvent> findTop100ByStatusForUpdate(
            @Param("status") OutboxStatus status
    );

    List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);

}

