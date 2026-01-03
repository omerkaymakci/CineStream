CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    release_date DATE,
    duration_minutes INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX idx_movie_title ON movies(title);