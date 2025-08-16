CREATE SCHEMA IF NOT EXISTS media_schema;

CREATE TABLE IF NOT EXISTS media_schema.media (
    media_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    media_url VARCHAR(255) NOT NULL,
    media_type VARCHAR(20) NOT NULL, -- 'IMAGE', 'VIDEO', 'GIF'
    content_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    width INT,
    height INT,
    duration INT, -- for videos
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_media_user ON media_schema.media (user_id);
CREATE INDEX IF NOT EXISTS idx_media_created ON media_schema.media (created_at);