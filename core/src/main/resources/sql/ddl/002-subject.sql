CREATE TABLE subject (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    name VARCHAR(20) NOT NULL,
    emoji VARCHAR(4) NOT NULL,
    create_at DATETIME NULL,
    updated_at DATETIME NULL
);

CREATE INDEX subject_idx_member_id on subject (member_id);