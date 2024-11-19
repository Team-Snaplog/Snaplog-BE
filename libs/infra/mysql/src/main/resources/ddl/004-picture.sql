CREATE TABLE picture (
    id VARCHAR(32) PRIMARY KEY DEFAULT (REPLACE(UUID(), '-', '')),
    snap_id VARCHAR(32) NOT NULL,
    url VARCHAR(255) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);

create index picture_idx_snap_id on picture(snap_id);
