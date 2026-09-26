-- Archive demo data: personal display fields and contacts sanitized; not real business records.
-- Migration: Add new tables and columns for modules 2,3,5,6 and announcements

-- 1. Add image and audit_remark columns to straw_info
ALTER TABLE straw_info ADD COLUMN image VARCHAR(500) DEFAULT NULL COMMENT '秸秆现场图片路径';
ALTER TABLE straw_info ADD COLUMN audit_remark VARCHAR(500) DEFAULT NULL COMMENT '审核意见';
ALTER TABLE straw_info ADD COLUMN plot_id BIGINT DEFAULT NULL COMMENT '关联地块ID';

-- 2. Plot (地块) table
CREATE TABLE IF NOT EXISTS plot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL COMMENT '地块名称',
    area DECIMAL(10,2) NOT NULL COMMENT '面积(亩)',
    location VARCHAR(255) NOT NULL COMMENT '地块位置',
    crop_type VARCHAR(50) COMMENT '主要种植作物',
    description TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Purchase (收购记录) table
CREATE TABLE IF NOT EXISTS purchase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    straw_info_id BIGINT NOT NULL,
    farmer_id BIGINT NOT NULL COMMENT '农户ID',
    buyer_id BIGINT NOT NULL COMMENT '收购方(企业)ID',
    quantity DECIMAL(10,2) NOT NULL COMMENT '收购量(吨)',
    price DECIMAL(10,2) NOT NULL COMMENT '收购单价(元/吨)',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额(元)',
    purchase_time DATETIME NOT NULL COMMENT '收购时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待确认 1-已确认 2-已完成',
    remark TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Announcement (公告) table
CREATE TABLE IF NOT EXISTS announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(50) NOT NULL DEFAULT 'admin',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0-草稿 1-已发布',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Insert sample announcements
INSERT INTO announcement (title, content, author, status) VALUES
('关于2026年春季秸秆离田作业补贴政策的通知', '<p>各市（州）农业农村局、有关单位：</p><p>为加快推进全省秸秆综合利用工作，根据《吉林省秸秆综合利用实施方案（2025-2027年）》要求，现将2026年春季秸秆离田作业补贴有关事项通知如下：</p><p><strong>一、补贴范围</strong><br/>全省范围内实施秸秆离田作业的农户、合作社及作业主体，均可申请补贴。</p><p><strong>二、补贴标准</strong><br/>玉米秸秆离田作业补贴：30元/亩；水稻秸秆离田作业补贴：25元/亩；大豆及其他秸秆离田作业补贴：20元/亩。</p><p><strong>三、申请流程</strong><br/>通过本平台上报秸秆信息并完成离田作业后，系统自动生成补贴申请，经县级农业农村部门审核通过后发放。</p><p><strong>四、截止时间</strong><br/>2026年6月30日前完成作业并提交申请。</p>', '系统管理员', 1),
('平台新增大豆秸秆类型上报功能，欢迎体验', '<p>各位用户：</p><p>为满足秸秆资源多样化管理需求，本平台现已新增"大豆"秸秆类型的上报功能。农户在上报秸秆信息时，可直接选择"大豆"类型，填写产量、面积等信息。</p><p>同时，系统还支持"其他"类型秸秆的上报，覆盖各类农作物秸秆资源。</p><p>如有使用问题，请联系平台客服。</p>', '系统管理员', 1),
('秸秆综合利用技术培训报名通知', '<p>各有关单位及个人：</p><p>吉林省农业农村厅将于2026年5月中旬举办秸秆综合利用技术培训班，现将有关事项通知如下：</p><p><strong>一、培训内容</strong><br/>1. 秸秆离田作业技术规范<br/>2. 秸秆还田与土壤改良技术<br/>3. 秸秆饲料化、燃料化利用技术<br/>4. 秸秆综合利用设备操作与维护</p><p><strong>二、培训时间地点</strong><br/>时间：2026年5月15日-17日<br/>地点：长春市农业科学院培训中心</p><p><strong>三、报名方式</strong><br/>通过本平台在线报名，截止日期为2026年5月10日。名额有限，先到先得。</p>', '系统管理员', 1),
('2026年秸秆资源调查工作启动', '<p>各市（州）农业农村局：</p><p>为全面掌握我省秸秆资源总量及分布情况，科学制定秸秆综合利用规划，2026年全省秸秆资源调查工作正式启动。</p><p><strong>一、调查范围</strong><br/>全省9个市（州）所有县（市、区）的农作物秸秆资源。</p><p><strong>二、调查内容</strong><br/>包括秸秆种类、产量、分布、利用方式、离田比例等指标。</p><p><strong>三、工作要求</strong><br/>请各县（市、区）农业农村局组织辖区内农户和合作社，通过本平台如实上报秸秆信息。数据截止时间为2026年10月31日。</p><p><strong>四、联系方式</strong><br/>省农业农村厅秸秆办：0431-8XXX1234</p>', '系统管理员', 1);
