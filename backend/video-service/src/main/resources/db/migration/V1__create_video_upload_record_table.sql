CREATE TABLE video_upload_record (
    id BIGSERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE INDEX idx_video_upload_record_movie_id ON video_upload_record(movie_id);