CREATE TABLE picture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    url VARCHAR(50) NOT NULL,
    create_at DATETIME NULL,
    updated_at DATETIME NULL
);

CREATE INDEX picture_idx_subject_id on picture (subject_id);