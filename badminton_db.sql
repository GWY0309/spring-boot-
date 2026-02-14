/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : badminton_db

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 05/01/2026 22:20:58
*/
CREATE DATABASE IF NOT EXISTS badminton_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE badminton_db;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_announcement
-- ----------------------------
DROP TABLE IF EXISTS `t_announcement`;
CREATE TABLE `t_announcement`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '公告唯一ID (主键, 自增)',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '公告正文',
  `user_id` bigint NOT NULL COMMENT '发布者ID (外键, 管理员)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_announcement
-- ----------------------------
INSERT INTO `t_announcement` VALUES (1, '国庆节期间场馆开放时间调整通知', '尊敬的各位用户：\n\n金秋送爽，丹桂飘香，国庆佳节将至！为方便广大球友在假期期间尽享运动乐趣，现将场馆国庆节期间（10月1日-10月7日）的开放时间调整如下：\n\n每日开放时间：**上午 10:00 - 晚上 20:00**\n\n请各位用户提前规划好您的运动时间，并通过本系统提前预约场地。祝大家度过一个健康、快乐的国庆假期！\n\n场馆管理中心\n2025年9月26日', 1, '2025-09-26 20:02:56', '2025-09-26 20:02:56');
INSERT INTO `t_announcement` VALUES (2, '【喜讯】VIP专享！全新升级VIP训练场正式开放！', '好消息！\n\n为了提供更优质的运动体验，我们的4号场地——VIP专属训练场现已完成全面升级，并于今日起正式投入使用！\n\n**新场地特色：**\n- 采用国际赛事级专业防滑地胶\n- 配备更高标准的照明系统，光线均匀不刺眼\n- 附设独立休息区和更衣柜\n\n欢迎各位VIP用户前来体验，感受专业级的运动快感！\n\n场馆管理中心\n2025年9月26日', 1, '2025-09-26 20:03:14', '2025-09-26 20:03:14');
INSERT INTO `t_announcement` VALUES (3, '秋季特惠！工作日白天场次8折优惠！', '秋高气爽，正是挥洒汗水的好时节！\n\n为鼓励大家在工作日也能坚持锻炼，即日起至10月底，凡预约周一至周五 **14:00之前** 的场地，均可享受 **8折** 优惠！\n\n约上三五好友，来一场酣畅淋漓的对决吧！\n\n*（注：本优惠不与其他活动同享）*\n\n场馆管理中心\n2025年9月26日', 1, '2025-09-26 20:03:40', '2025-09-26 20:03:40');
INSERT INTO `t_announcement` VALUES (4, '系统维护通知', '各位用户请注意：\n\n为提升系统性能及稳定性，我们计划于 **2025年9月29日（星期一）凌晨 02:00 - 04:00** 对预约系统进行停机维护。\n\n届时，系统的所有在线预约、查询等功能将暂时无法使用。请您提前安排好相关操作，避开此时间段。\n\n对于给您带来的不便，我们深表歉意。感谢您的理解与支持！\n\n技术部\n2025年9月26日', 1, '2025-09-26 20:03:54', '2025-09-26 20:03:54');

-- ----------------------------
-- Table structure for t_court
-- ----------------------------
DROP TABLE IF EXISTS `t_court`;
CREATE TABLE `t_court`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '场地唯一ID (主键, 自增)',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '场地名称 (如: 1号标准场)',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '场地类型 (如: 室内, 室外)',
  `status` int NOT NULL DEFAULT 0 COMMENT '场地状态 (0-可用, 1-维修中, 2-已停用)',
  `price_per_hour` decimal(10, 2) NOT NULL COMMENT '每小时价格',
  `open_time` time NULL DEFAULT NULL COMMENT '每日开放时间',
  `close_time` time NULL DEFAULT NULL COMMENT '每日关闭时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '场地信息创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '场地信息最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '场地表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_court
-- ----------------------------
INSERT INTO `t_court` VALUES (1, '1号场地(已升级)', '室内', 0, 60.00, '09:00:00', '22:00:00', '2025-09-24 21:24:17', '2025-09-26 18:11:55');
INSERT INTO `t_court` VALUES (2, '2号标准室内场', '室内', 0, 50.00, '09:00:00', '22:00:00', '2025-09-24 21:24:17', '2025-09-24 21:24:17');
INSERT INTO `t_court` VALUES (3, '室外练习场A', '室外', 0, 30.00, '08:00:00', '20:00:00', '2025-09-24 21:24:17', '2025-09-26 18:11:59');
INSERT INTO `t_court` VALUES (4, 'VIP 1号专属训练场', '室内', 0, 100.00, '08:00:00', '23:00:00', '2025-09-25 13:51:14', '2025-09-26 12:05:16');

-- ----------------------------
-- Table structure for t_racket
-- ----------------------------
DROP TABLE IF EXISTS `t_racket`;
CREATE TABLE `t_racket`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '球拍唯一ID (主键, 自增)',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '型号',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态 (0-可用, 1-已租出, 2-维修中)',
  `rental_price_per_hour` decimal(10, 2) NOT NULL COMMENT '每小时租金',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '信息录入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '球拍信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_racket
-- ----------------------------
INSERT INTO `t_racket` VALUES (1, '尤尼克斯', '天斧99 (送去维修)', 0, 15.00, '2025-09-25 13:04:55', '2025-09-28 16:52:51');
INSERT INTO `t_racket` VALUES (2, '胜利', '亮剑12', 0, 12.00, '2025-09-25 13:04:55', '2025-09-26 00:39:16');
INSERT INTO `t_racket` VALUES (3, '李宁', 'N90三代', 0, 18.00, '2025-09-25 13:04:55', '2025-09-26 13:03:48');
INSERT INTO `t_racket` VALUES (4, '高端品牌', '专业选手款 Z-100', 0, 25.00, '2025-09-25 14:31:28', '2025-09-25 14:31:28');

-- ----------------------------
-- Table structure for t_racket_rental
-- ----------------------------
DROP TABLE IF EXISTS `t_racket_rental`;
CREATE TABLE `t_racket_rental`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '租借记录ID (主键, 自增)',
  `racket_id` int NOT NULL COMMENT '租借球拍的ID (外键)',
  `user_id` bigint NOT NULL COMMENT '租借用户的ID (外键)',
  `rental_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '租借开始时间',
  `return_time` datetime NULL DEFAULT NULL COMMENT '归还时间 (未归还时为NULL)',
  `total_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '总费用 (归还时计算)',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态 (0-租借中, 1-已归还)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_racket_id`(`racket_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '球拍租借记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_racket_rental
-- ----------------------------
INSERT INTO `t_racket_rental` VALUES (1, 1, 2, '2025-09-25 13:15:36', '2025-09-26 13:32:27', 375.00, 1);
INSERT INTO `t_racket_rental` VALUES (2, 2, 1, '2025-09-25 23:49:07', '2025-09-25 23:49:27', 12.00, 1);
INSERT INTO `t_racket_rental` VALUES (3, 2, 1, '2025-09-26 00:39:13', '2025-09-26 00:39:16', 12.00, 1);
INSERT INTO `t_racket_rental` VALUES (4, 1, 1, '2025-09-26 13:04:03', '2025-09-26 13:04:11', 15.00, 1);
INSERT INTO `t_racket_rental` VALUES (5, 1, 1, '2025-09-26 13:32:49', '2025-09-26 13:33:01', 15.00, 1);
INSERT INTO `t_racket_rental` VALUES (6, 1, 1, '2025-09-28 08:25:27', '2025-09-28 08:25:36', 15.00, 1);
INSERT INTO `t_racket_rental` VALUES (7, 1, 1, '2025-09-28 16:52:36', '2025-09-28 16:52:52', 15.00, 1);

-- ----------------------------
-- Table structure for t_reservation
-- ----------------------------
DROP TABLE IF EXISTS `t_reservation`;
CREATE TABLE `t_reservation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预约记录唯一ID (主键, 自增)',
  `user_id` bigint NOT NULL COMMENT '预约用户的ID (外键)',
  `court_id` int NOT NULL COMMENT '预约场地的ID (外键)',
  `reservation_date` date NOT NULL COMMENT '预约的日期',
  `start_time` time NOT NULL COMMENT '预约开始时间',
  `end_time` time NOT NULL COMMENT '预约结束时间',
  `total_cost` decimal(10, 2) NOT NULL COMMENT '总费用',
  `status` int NOT NULL DEFAULT 0 COMMENT '预约状态 (0-已预约, 1-已完成, 2-已取消)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预约创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '预约状态最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_court_id`(`court_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_reservation
-- ----------------------------
INSERT INTO `t_reservation` VALUES (1, 2, 1, '2025-09-25', '14:00:00', '15:00:00', 50.00, 2, '2025-09-25 12:13:13', '2025-09-25 12:44:04');
INSERT INTO `t_reservation` VALUES (2, 2, 2, '2025-09-26', '10:00:00', '11:00:00', 50.00, 2, '2025-09-25 12:36:48', '2025-09-25 12:44:09');
INSERT INTO `t_reservation` VALUES (3, 2, 2, '2025-09-27', '11:00:00', '12:00:00', 50.00, 2, '2025-09-25 12:40:34', '2025-09-25 12:41:37');
INSERT INTO `t_reservation` VALUES (4, 1, 2, '2025-09-25', '11:00:00', '15:00:00', 200.00, 2, '2025-09-25 22:10:04', '2025-09-26 14:03:27');
INSERT INTO `t_reservation` VALUES (5, 1, 2, '2025-09-26', '08:00:00', '10:00:00', 100.00, 2, '2025-09-25 22:45:10', '2025-09-25 22:45:16');
INSERT INTO `t_reservation` VALUES (6, 4, 1, '2025-09-27', '08:00:00', '10:00:00', 120.00, 2, '2025-09-26 19:12:03', '2025-09-26 19:12:09');
INSERT INTO `t_reservation` VALUES (7, 1, 1, '2025-10-25', '08:00:00', '12:00:00', 240.00, 0, '2025-10-24 19:47:47', '2025-10-24 19:47:47');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID (主键, 自增)',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录用户名 (唯一)',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录密码 (加密后)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'USER' COMMENT '角色 (USER, ADMIN)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', '$2a$10$27wFiRQQXxnFS5URPeUtiefGU94VpIvEvlFRyMM9/aQ2JQfHeuQAS', '超级管理员', '12342892398', 'ADMIN', '2025-09-25 18:46:01', '2025-09-26 17:24:57');
INSERT INTO `t_user` VALUES (4, 'zxj', '$2a$10$IJwROD7HQuFkOJo0akEU6.Rj/5rAcnzw0otKx3yMLDUP3dPFuPU26', '朱雄健', NULL, 'USER', '2025-09-26 14:14:30', '2025-09-28 16:54:07');

SET FOREIGN_KEY_CHECKS = 1;
