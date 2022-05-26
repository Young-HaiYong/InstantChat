/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80028
Source Host           : localhost:3306
Source Database       : chat

Target Server Type    : MYSQL
Target Server Version : 80028
File Encoding         : 65001

Date: 2022-05-26 15:59:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `user_id1` int DEFAULT NULL,
  `user_id2` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES ('1', '3');
INSERT INTO `friends` VALUES ('1', '4');
INSERT INTO `friends` VALUES ('3', '2');
INSERT INTO `friends` VALUES ('1', '2');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `age` int DEFAULT NULL,
  `birthday` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex` int DEFAULT NULL,
  `imgpath` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sign` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'y1', '111111', '22', '2002-06-12', '0', 'pictures\\h3.png', '1号用户');
INSERT INTO `user` VALUES ('2', 'y2', '111111', '12', '2010-08-01', '0', 'pictures\\h1.png', '2号用户');
INSERT INTO `user` VALUES ('3', 'y3', '111111', '20', '2002-06-21', '1', 'pictures\\h4.png', '3号用户');
INSERT INTO `user` VALUES ('4', 'y4', '111111', '24', '1998-06-21', '1', 'pictures\\h3.png', '4号用户');
INSERT INTO `user` VALUES ('5', 'y5', '111111', '2', '2020-04-26 00:11', '0', 'pictures\\h1.png', '');
