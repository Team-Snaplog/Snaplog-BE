CREATE TABLE snap (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    snap_at DATE NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);
