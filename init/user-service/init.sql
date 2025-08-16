CREATE SCHEMA IF NOT EXISTS user_schema;

CREATE TABLE IF NOT EXISTS user_schema."user-tbl" (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    bio TEXT,
    profile_picture_url VARCHAR(255),
    header_image_url VARCHAR(255),
    location VARCHAR(100),
    website VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT phone_unique UNIQUE (phone)
);

CREATE TABLE IF NOT EXISTS user_schema.user_roles (
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_schema.followers (
    follower_id UUID NOT NULL,
    following_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, following_id),
    FOREIGN KEY (follower_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES user_schema."user-tbl" (user_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_email ON user_schema."user-tbl" (email);
CREATE INDEX IF NOT EXISTS idx_user_phone ON user_schema."user-tbl" (phone);
CREATE INDEX IF NOT EXISTS idx_follower ON user_schema.followers (follower_id);
CREATE INDEX IF NOT EXISTS idx_following ON user_schema.followers (following_id);

CREATE EXTENSION IF NOT EXISTS "pgcrypto";