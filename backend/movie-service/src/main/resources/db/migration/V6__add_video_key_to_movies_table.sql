-- NOTE: This file was accidentally authored as a duplicate of V5 (re-adding the
-- same outbox_event columns) instead of adding a video_key column. Kept as an
-- idempotent no-op so the Flyway version chain stays intact.
ALTER TABLE outbox_event
    ADD COLUMN IF NOT EXISTS processed_at TIMESTAMP;

ALTER TABLE outbox_event
    ADD COLUMN IF NOT EXISTS retry_count INTEGER NOT NULL DEFAULT 0;
