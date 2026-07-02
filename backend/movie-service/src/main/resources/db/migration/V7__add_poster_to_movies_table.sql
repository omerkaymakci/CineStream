-- Poster image stored as a base64 data URL (data:image/...;base64,....)
ALTER TABLE movies
    ADD COLUMN IF NOT EXISTS poster_image TEXT;
