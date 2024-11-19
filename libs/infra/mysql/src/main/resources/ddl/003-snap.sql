CREATE TABLE snap (
    id VARCHAR(32) PRIMARY KEY DEFAULT (REPLACE(UUID(), '-', '')),
    topic_id VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    snap_at DATE NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);

create index snap_idx_topic_id on snap(topic_id);
