CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    provider VARCHAR(20) NOT NULL,
    create_at DATETIME NULL,
    updated_at DATETIME NULL
);

CREATE INDEX member_idx_email on member (email);