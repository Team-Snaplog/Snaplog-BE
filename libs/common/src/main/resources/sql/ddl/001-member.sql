CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    provider ENUM('GOOGLE', 'APPLE') NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);
