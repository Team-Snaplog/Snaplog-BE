CREATE TABLE picture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    snap_id BIGINT,
    url VARCHAR(255) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);
