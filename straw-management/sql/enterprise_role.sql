-- Archive demo data: personal display fields and contacts sanitized; not real business records.
-- Add enterprise role (role=4)
ALTER TABLE `user` MODIFY COLUMN role TINYINT NOT NULL DEFAULT 0
  COMMENT '0-农户 1-合作社 2-作业主体 3-政府管理员 4-企业端';

-- Insert enterprise test account (password: admin123)
INSERT INTO `user` (username, password, role, real_name, status, verified) VALUES
('enterprise', '$2a$10$RWGhjNfhS59v8qISzS32vONPgmdBralnXxpJGRs4zvcjPXnbmSzlO', 4, '演示用户-enterprise', 1, 1);
