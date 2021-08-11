/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 11/08/2021 15:05:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (2, '隔壁老王', 25, '老王长大了一岁了！');
INSERT INTO `user` VALUES (6, 'wills', 24, '一个酷酷的boy');
INSERT INTO `user` VALUES (10, 'wills', 24, '一个酷酷的boy - 测试注解更新');
INSERT INTO `user` VALUES (15, 'wills', 24, '一个酷酷的boy - 测试注解插入');
INSERT INTO `user` VALUES (16, 'wills', 24, '一个酷酷的boy - 测试注解插入');
INSERT INTO `user` VALUES (17, 'wills', 24, '一个酷酷的boy - 测试注解更新');
INSERT INTO `user` VALUES (19, 'wills', 24, '一个酷酷的boy - 测试注解插入');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
