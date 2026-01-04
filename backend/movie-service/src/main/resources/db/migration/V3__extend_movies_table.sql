ALTER TABLE movies
ADD COLUMN IF NOT EXISTS video_url VARCHAR(500);

CREATE TABLE IF NOT EXISTS genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS movie_genres (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres(id),
    PRIMARY KEY (movie_id, genre_id)
);

CREATE TABLE IF NOT EXISTS actors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS movie_actors (
    movie_id BIGINT REFERENCES actors(id) ON DELETE CASCADE,
    actor_id BIGINT REFERENCES actors(id),
    role VARCHAR(100),
    PRIMARY KEY (movie_id, actor_id)
);