-- Archive demo data: personal display fields and contacts sanitized; not real business records.
CREATE DATABASE IF NOT EXISTS straw_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE straw_management;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role TINYINT NOT NULL DEFAULT 0 COMMENT '0-农户 1-合作社 2-作业主体 3-管理员',
    phone VARCHAR(20),
    real_name VARCHAR(50),
    avatar VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用 1-正常',
    verified TINYINT NOT NULL DEFAULT 0 COMMENT '0-未认证 1-已认证',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 秸秆信息表
CREATE TABLE IF NOT EXISTS straw_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    region_id BIGINT,
    straw_type VARCHAR(50) NOT NULL COMMENT '玉米/水稻/大豆等',
    quantity DECIMAL(10,2) NOT NULL COMMENT '产量(吨)',
    area DECIMAL(10,2) COMMENT '面积(亩)',
    location VARCHAR(255),
    description TEXT,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待审核 1-已通过 2-已驳回',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 作业主体表
CREATE TABLE IF NOT EXISTS job_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    qualification VARCHAR(255),
    service_area VARCHAR(255),
    equipment VARCHAR(255),
    contact VARCHAR(20),
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待审核 1-正常',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(32) NOT NULL UNIQUE,
    straw_info_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    job_entity_id BIGINT,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待接单 1-进行中 2-已完成 3-已结算 4-已取消',
    fee DECIMAL(10,2),
    remark TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accept_time DATETIME,
    complete_time DATETIME,
    settle_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 作业调度表
CREATE TABLE IF NOT EXISTS job_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    plan_date DATE NOT NULL,
    workers INT NOT NULL DEFAULT 0,
    progress TINYINT NOT NULL DEFAULT 0 COMMENT '0-100',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待执行 1-执行中 2-已完成',
    remark TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 区域表
CREATE TABLE IF NOT EXISTS region (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    parent_id BIGINT NOT NULL DEFAULT 0,
    level TINYINT NOT NULL COMMENT '1-省级 2-市级 3-区/县级'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始管理员 (密码: admin123, BCrypt加密)
INSERT INTO `user` (username, password, role, real_name, status, verified) VALUES
('admin', '$2a$10$RWGhjNfhS59v8qISzS32vONPgmdBralnXxpJGRs4zvcjPXnbmSzlO', 3, '演示用户-admin', 1, 1);
UPDATE `user` SET real_name='系统管理员' WHERE username='admin';

-- 吉林省区域数据
INSERT INTO region (name, parent_id, level) VALUES ('吉林省', 0, 1);
INSERT INTO region (name, parent_id, level) VALUES ('长春市', 1, 2), ('吉林市', 1, 2), ('四平市', 1, 2), ('辽源市', 1, 2), ('通化市', 1, 2), ('白山市', 1, 2), ('松原市', 1, 2), ('白城市', 1, 2), ('延边朝鲜族自治州', 1, 2);
