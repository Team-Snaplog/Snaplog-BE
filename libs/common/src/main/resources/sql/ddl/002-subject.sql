CREATE TABLE subject (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     member_id BIGINT NOT NULL ,
     name VARCHAR(20) NOT NULL,
     emoji VARCHAR(10) NOT NULL,
     created_at DATETIME,
     updated_at DATETIME
);
