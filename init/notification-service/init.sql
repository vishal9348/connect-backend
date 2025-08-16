CREATE SCHEMA IF NOT EXISTS notification_schema;

CREATE TABLE IF NOT EXISTS notification_schema.notifications (
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipient_id UUID NOT NULL,
    sender_id UUID,
    tweet_id UUID,
    notification_type VARCHAR(50) NOT NULL, -- 'LIKE', 'RETWEET', 'REPLY', 'FOLLOW'
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipient_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE SET NULL,
    FOREIGN KEY (tweet_id) REFERENCES tweet_schema.tweets (tweet_id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_notification_recipient ON notification_schema.notifications (recipient_id);
CREATE INDEX IF NOT EXISTS idx_notification_read ON notification_schema.notifications (is_read);
CREATE INDEX IF NOT EXISTS idx_notification_created ON notification_schema.notifications (created_at);