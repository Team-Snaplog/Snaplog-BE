CREATE TABLE member (
    id VARCHAR(32) PRIMARY KEY DEFAULT (REPLACE(UUID(), '-', '')),
    email VARCHAR(255) NOT NULL,
    provider ENUM('GOOGLE', 'APPLE') NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);

create index member_idx_email on member(email);
