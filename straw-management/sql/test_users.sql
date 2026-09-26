-- Archive demo data: personal display fields and contacts sanitized; not real business records.
-- =============================================
-- 测试用户数据 (密码统一为 123456, BCrypt加密)
-- admin 用户已在 schema.sql 中创建 (密码: admin123)
-- =============================================

INSERT INTO `user` (id, username, password, role, real_name, phone, status, verified) VALUES
-- 农户 (role=0)
(2, 'zhangsan', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-zhangsan', '00000000000', 1, 1),
(3, 'lisi', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-lisi', '00000000000', 1, 1),
(4, 'wangwu', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-wangwu', '00000000000', 1, 1),
(5, 'zhaoliu', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-zhaoliu', '00000000000', 1, 1),
(6, 'sunqi', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-sunqi', '00000000000', 1, 1),
(12, 'test', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-test', '00000000000', 1, 1),
(13, 'test02', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-test02', '00000000000', 1, 1),
(14, 'liuwei', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-liuwei', '00000000000', 1, 1),
(15, 'chenxia', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-chenxia', '00000000000', 1, 1),
(16, 'yangfan', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-yangfan', '00000000000', 1, 1),
(17, 'huanglin', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-huanglin', '00000000000', 1, 1),
(18, 'zhoumei', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-zhoumei', '00000000000', 1, 1),
(19, 'wugang', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-wugang', '00000000000', 1, 1),
(20, 'xujie', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-xujie', '00000000000', 1, 1),
(21, 'hezhi', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-hezhi', '00000000000', 1, 1),
(22, 'lina', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-lina', '00000000000', 1, 1),
(23, 'gaoqiang', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 0, '演示用户-gaoqiang', '00000000000', 1, 1),
-- 合作社 (role=1)
(7, 'coop01', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 1, '演示用户-coop01', '00000000000', 1, 1),
(8, 'coop02', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 1, '演示用户-coop02', '00000000000', 1, 1),
(9, 'buyer01', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 1, '演示用户-buyer01', '00000000000', 1, 1),
(24, 'coop03', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 1, '演示用户-coop03', '00000000000', 1, 1),
(25, 'coop04', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 1, '演示用户-coop04', '00000000000', 1, 1),
-- 作业主体 (role=2)
(10, 'worker01', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 2, '演示用户-worker01', '00000000000', 1, 1),
(11, 'worker02', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 2, '演示用户-worker02', '00000000000', 1, 1),
(26, 'worker03', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 2, '演示用户-worker03', '00000000000', 1, 1),
(27, 'worker04', '$2a$10$D7xPXeNGObOIoQzteSjqnez.oNRUTFnbQMOELzDA3snVEnt/MpZiu', 2, '演示用户-worker04', '00000000000', 1, 1);
