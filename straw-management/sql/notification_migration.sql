-- Archive demo data: personal display fields and contacts sanitized; not real business records.
-- 消息通知表
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '接收通知的用户ID',
    type TINYINT NOT NULL COMMENT '通知类型: 1-审核通知 2-订单状态变更',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content VARCHAR(500) NOT NULL COMMENT '通知内容',
    related_id BIGINT COMMENT '关联业务ID(straw_info.id 或 order.id)',
    is_read TINYINT NOT NULL DEFAULT 0 COMMENT '0-未读 1-已读',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
