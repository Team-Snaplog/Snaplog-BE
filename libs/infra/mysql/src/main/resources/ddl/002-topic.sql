CREATE TABLE topic (
    id VARCHAR(32) PRIMARY KEY DEFAULT (REPLACE(UUID(), '-', '')),
    member_id VARCHAR(32) NOT NULL,
    name VARCHAR(20) NOT NULL,
    emoji VARCHAR(10) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);

create index topic_idx_member_id_name on topic(member_id);
