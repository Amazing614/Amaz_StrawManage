-- Archive demo data: personal display fields and contacts sanitized; not real business records.
-- FAQ智能问答表
CREATE TABLE IF NOT EXISTS faq (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(500) NOT NULL COMMENT '问题',
    answer TEXT NOT NULL COMMENT '回答',
    category VARCHAR(50) NOT NULL DEFAULT '通用' COMMENT '分类',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 预置常见问题
INSERT INTO faq (question, answer, category, sort_order) VALUES
('秸秆的收购价格是多少？',
 '秸秆收购价格因地区和秸秆类型而异。玉米秸秆一般200-350元/吨，水稻秸秆150-280元/吨，大豆秸秆180-300元/吨。具体价格请咨询当地收购企业。',
 '价格', 1),
('秸秆离田的流程是什么？',
 '秸秆离田流程：1.农户上报秸秆信息 → 2.管理员审核 → 3.审核通过后预约作业主体 → 4.签订订单 → 5.作业主体执行离田作业 → 6.验收完成。',
 '流程', 2),
('为什么要禁止秸秆焚烧？',
 '秸秆焚烧会产生大量有害气体和颗粒物，严重污染空气，危害人体健康。同时易引发火灾，造成交通安全隐患。国家已出台相关法律法规禁止秸秆焚烧。',
 '政策', 3),
('秸秆有哪些利用方式？',
 '秸秆综合利用途径包括：1.秸秆还田（肥料化）2.秸秆饲料（饲料化）3.秸秆燃料（燃料化）4.秸秆原料（原料化）5.秸秆基料（基料化）。',
 '知识', 4),
('如何注册和登录系统？',
 '点击登录页面的"注册账号"，填写用户名、密码和手机号即可完成注册。注册后使用用户名和密码登录。',
 '使用', 5),
('秸秆离田作业如何收费？',
 '离田作业费用根据作业面积和秸秆类型而定，一般为30-80元/亩。具体费用在下单时与作业主体协商确定。',
 '价格', 6),
('补贴政策是什么？',
 '吉林省对秸秆离田作业给予补贴：玉米秸秆30元/亩，水稻秸秆25元/亩，大豆秸秆20元/亩。通过平台完成作业后系统自动生成补贴申请。',
 '政策', 7),
('订单状态有哪些？',
 '订单状态包括：待接单、进行中、已完成、已结算、已取消。',
 '使用', 8);
