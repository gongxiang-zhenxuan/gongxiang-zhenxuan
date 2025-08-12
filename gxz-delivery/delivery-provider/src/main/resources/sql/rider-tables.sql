-- 骑手表
CREATE TABLE `tb_rider` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '骑手ID',
    `rider_code` VARCHAR(32) NOT NULL COMMENT '骑手编号',
    `open_id` VARCHAR(64) DEFAULT NULL COMMENT '微信openId',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `vehicle_type` TINYINT DEFAULT NULL COMMENT '车辆类型：1-电动车，2-摩托车，3-汽车',
    `vehicle_no` VARCHAR(20) DEFAULT NULL COMMENT '车牌号',
    `service_area` VARCHAR(100) DEFAULT NULL COMMENT '服务区域',
    `current_longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '当前位置经度',
    `current_latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '当前位置纬度',
    `online_status` TINYINT DEFAULT 3 COMMENT '在线状态：1-在线，2-忙碌，3-离线',
    `max_concurrent_orders` TINYINT DEFAULT 3 COMMENT '最大并发订单数',
    `current_orders_count` TINYINT DEFAULT 0 COMMENT '当前配送订单数',
    `service_range` DECIMAL(5,2) DEFAULT 5.00 COMMENT '服务范围(公里)',
    `rating` DECIMAL(3,2) DEFAULT 5.00 COMMENT '评分',
    `total_orders` INT DEFAULT 0 COMMENT '总配送单数',
    `completed_orders` INT DEFAULT 0 COMMENT '完成配送单数',
    `status` TINYINT DEFAULT 1 COMMENT '账户状态：1-正常，2-冻结，3-停用',
    `register_time` DATETIME DEFAULT NULL COMMENT '注册时间',
    `last_online_time` DATETIME DEFAULT NULL COMMENT '最后上线时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_rider_code` (`rider_code`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status_online` (`status`, `online_status`),
    KEY `idx_location` (`current_longitude`, `current_latitude`),
    KEY `idx_service_area` (`service_area`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='骑手信息表';

-- 配送订单表
CREATE TABLE `tb_delivery_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配送订单ID',
    `order_id` BIGINT NOT NULL COMMENT '主订单ID',
    `delivery_order_no` VARCHAR(32) NOT NULL COMMENT '配送订单编号',
    `rider_id` BIGINT DEFAULT NULL COMMENT '骑手ID',
    `pickup_address` VARCHAR(500) NOT NULL COMMENT '取餐地址',
    `pickup_contact` VARCHAR(100) DEFAULT NULL COMMENT '取餐联系人',
    `pickup_phone` VARCHAR(20) DEFAULT NULL COMMENT '取餐电话',
    `pickup_longitude` DECIMAL(10,7) NOT NULL COMMENT '取餐地址经度',
    `pickup_latitude` DECIMAL(10,7) NOT NULL COMMENT '取餐地址纬度',
    `delivery_address` VARCHAR(500) NOT NULL COMMENT '配送地址',
    `delivery_contact` VARCHAR(100) NOT NULL COMMENT '配送联系人',
    `delivery_phone` VARCHAR(20) NOT NULL COMMENT '配送电话',
    `delivery_longitude` DECIMAL(10,7) NOT NULL COMMENT '配送地址经度',
    `delivery_latitude` DECIMAL(10,7) NOT NULL COMMENT '配送地址纬度',
    `distance` DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '配送距离(公里)',
    `delivery_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '配送费',
    `estimated_time` INT DEFAULT NULL COMMENT '预计配送时长(分钟)',
    `actual_time` INT DEFAULT NULL COMMENT '实际配送时长(分钟)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '订单状态：1-待抢单，2-待取餐，3-骑手已到店，4-已取餐，5-配送中，6-已送达，7-已完成，8-已取消',
    `grab_deadline` DATETIME NOT NULL COMMENT '抢单截止时间',
    `grab_time` DATETIME DEFAULT NULL COMMENT '抢单时间',
    `arrive_time` DATETIME DEFAULT NULL COMMENT '到店时间',
    `pickup_time` DATETIME DEFAULT NULL COMMENT '取餐时间',
    `delivery_start_time` DATETIME DEFAULT NULL COMMENT '配送开始时间',
    `delivered_time` DATETIME DEFAULT NULL COMMENT '送达时间',
    `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_delivery_order_no` (`delivery_order_no`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_rider_id` (`rider_id`),
    KEY `idx_status` (`status`),
    KEY `idx_grab_orders` (`status`, `grab_deadline`, `pickup_longitude`, `pickup_latitude`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配送订单表';

-- 骑手位置轨迹表
CREATE TABLE `tb_rider_location_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '轨迹ID',
    `rider_id` BIGINT NOT NULL COMMENT '骑手ID',
    `delivery_order_id` BIGINT DEFAULT NULL COMMENT '配送订单ID',
    `longitude` DECIMAL(10,7) NOT NULL COMMENT '经度',
    `latitude` DECIMAL(10,7) NOT NULL COMMENT '纬度',
    `accuracy` INT DEFAULT NULL COMMENT '精度(米)',
    `speed` DECIMAL(5,2) DEFAULT NULL COMMENT '速度(km/h)',
    `direction` DECIMAL(5,2) DEFAULT NULL COMMENT '方向角度',
    `report_time` DATETIME NOT NULL COMMENT '上报时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_rider_id` (`rider_id`),
    KEY `idx_delivery_order_id` (`delivery_order_id`),
    KEY `idx_report_time` (`report_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='骑手位置轨迹表';

-- 骑手收入记录表
CREATE TABLE `tb_rider_income` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收入记录ID',
    `rider_id` BIGINT NOT NULL COMMENT '骑手ID',
    `delivery_order_id` BIGINT NOT NULL COMMENT '配送订单ID',
    `base_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '基础配送费',
    `distance_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '距离费',
    `time_bonus` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '时间补贴',
    `weather_bonus` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '恶劣天气补贴',
    `night_bonus` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '夜间配送补贴',
    `rating_bonus` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '好评奖励',
    `platform_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '平台服务费',
    `actual_income` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实际收入',
    `settle_status` TINYINT DEFAULT 0 COMMENT '结算状态：0-待结算，1-已结算',
    `settle_time` DATETIME DEFAULT NULL COMMENT '结算时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_delivery_order_id` (`delivery_order_id`),
    KEY `idx_rider_id` (`rider_id`),
    KEY `idx_settle_status` (`settle_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='骑手收入记录表';

-- 骑手评价表
CREATE TABLE `tb_rider_rating` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `delivery_order_id` BIGINT NOT NULL COMMENT '配送订单ID',
    `rider_id` BIGINT NOT NULL COMMENT '骑手ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `rating` TINYINT NOT NULL COMMENT '评分：1-5分',
    `comment` VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
    `tags` VARCHAR(200) DEFAULT NULL COMMENT '评价标签，逗号分隔',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_delivery_order_id` (`delivery_order_id`),
    KEY `idx_rider_id` (`rider_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='骑手评价表';

-- 插入测试数据
INSERT INTO `tb_rider` (`rider_code`, `phone`, `name`, `vehicle_type`, `service_area`) VALUES 
('R1701234567890', '13800138001', '张三', 1, '朝阳区'),
('R1701234567891', '13800138002', '李四', 1, '海淀区'),
('R1701234567892', '13800138003', '王五', 2, '西城区');
