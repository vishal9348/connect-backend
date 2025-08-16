CREATE SCHEMA IF NOT EXISTS feed_schema;

CREATE TABLE IF NOT EXISTS feed_schema.user_feeds (
    feed_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    tweet_id UUID NOT NULL,
    feed_type VARCHAR(20) NOT NULL, -- 'HOME', 'USER', 'HASHTAG', etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE,
    FOREIGN KEY (tweet_id) REFERENCES tweet_schema.tweets (tweet_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_feed_user ON feed_schema.user_feeds (user_id);
CREATE INDEX IF NOT EXISTS idx_feed_type ON feed_schema.user_feeds (feed_type);
CREATE INDEX IF NOT EXISTS idx_feed_created ON feed_schema.user_feeds (created_at);