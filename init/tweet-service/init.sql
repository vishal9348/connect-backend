CREATE SCHEMA IF NOT EXISTS tweet_schema;

CREATE TABLE IF NOT EXISTS tweet_schema.tweets (
    tweet_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    content TEXT NOT NULL,
    media_urls TEXT[],
    hashtags TEXT[],
    mentions UUID[],
    is_sensitive BOOLEAN DEFAULT FALSE,
    language VARCHAR(10),
    reply_settings VARCHAR(20) DEFAULT 'EVERYONE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tweet_schema.replies (
    reply_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tweet_id UUID NOT NULL,
    user_id UUID NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tweet_id) REFERENCES tweet_schema.tweets (tweet_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tweet_schema.likes (
    tweet_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (tweet_id, user_id),
    FOREIGN KEY (tweet_id) REFERENCES tweet_schema.tweets (tweet_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tweet_schema.retweets (
    retweet_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    original_tweet_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (original_tweet_id) REFERENCES tweet_schema.tweets (tweet_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_tweet_user ON tweet_schema.tweets (user_id);
CREATE INDEX IF NOT EXISTS idx_tweet_created ON tweet_schema.tweets (created_at);
CREATE INDEX IF NOT EXISTS idx_retweet_user ON tweet_schema.retweets (user_id);
CREATE INDEX IF NOT EXISTS idx_like_user ON tweet_schema.likes (user_id);