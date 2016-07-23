/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50712
Source Host           : localhost:3306
Source Database       : mysocket

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2016-07-22 21:34:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bans`
-- ----------------------------
DROP TABLE IF EXISTS `bans`;
CREATE TABLE `bans` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) NOT NULL DEFAULT 'utf8',
  `b_username` varchar(10) DEFAULT 'utf8',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bans
-- ----------------------------

-- ----------------------------
-- Table structure for `chats`
-- ----------------------------
DROP TABLE IF EXISTS `chats`;
CREATE TABLE `chats` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) NOT NULL DEFAULT 'utf8',
  `exit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chats
-- ----------------------------
INSERT INTO `chats` VALUES ('1', 'fangrui', '2016-07-22 20:51:21');
INSERT INTO `chats` VALUES ('2', 'rui', '2016-07-20 15:41:48');
INSERT INTO `chats` VALUES ('4', 'try', '2016-07-20 20:30:06');
INSERT INTO `chats` VALUES ('5', 'test', '2016-07-20 16:00:21');
INSERT INTO `chats` VALUES ('6', 'ruifang', '2016-07-21 17:16:31');

-- ----------------------------
-- Table structure for `friends`
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) NOT NULL,
  `friend` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES ('1', 'rui', 'fangrui');
INSERT INTO `friends` VALUES ('2', 'fangrui', 'rui');
INSERT INTO `friends` VALUES ('3', 'ruifang', 'try');
INSERT INTO `friends` VALUES ('4', 'try', 'ruifang');

-- ----------------------------
-- Table structure for `messages`
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `send` varchar(10) DEFAULT NULL,
  `recipient` varchar(10) DEFAULT NULL,
  `content` varchar(50) DEFAULT NULL,
  `send_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of messages
-- ----------------------------
INSERT INTO `messages` VALUES ('19', 'rui', 'try', 'try', '2016-07-20 10:05:38.523363');
INSERT INTO `messages` VALUES ('20', 'fangrui', 'try', '123456', '2016-07-20 10:05:34.857356');
INSERT INTO `messages` VALUES ('21', 'rui', 'try', '123', '2016-07-20 10:05:42.516970');
INSERT INTO `messages` VALUES ('23', 'fangrui', 'all', 'TOrui', '2016-07-20 09:35:29.813876');
INSERT INTO `messages` VALUES ('24', 'fangrui', 'all', '123', '2016-07-20 09:35:47.980916');
INSERT INTO `messages` VALUES ('25', 'fangrui', 'all', '123', '2016-07-20 10:05:48.944181');
INSERT INTO `messages` VALUES ('26', 'fangrui', 'all', '123', '2016-07-20 10:05:52.095387');
INSERT INTO `messages` VALUES ('27', 'fangrui', 'all', '123', '2016-07-20 10:05:57.336996');
INSERT INTO `messages` VALUES ('28', '系统', 'rui', '用户 fangrui 已经拒绝您的好友申请。', '2016-07-20 10:46:50.080905');
INSERT INTO `messages` VALUES ('29', 'fangrui', 'all', 'TOruiFRI', '2016-07-20 10:48:45.290107');
INSERT INTO `messages` VALUES ('30', 'rui', 'all', '3', '2016-07-20 10:50:32.199095');
INSERT INTO `messages` VALUES ('31', 'fangrui', 'all', '124563', '2016-07-20 11:45:40.020279');
INSERT INTO `messages` VALUES ('32', 'try', 'all', '123', '2016-07-20 15:58:47.673446');
INSERT INTO `messages` VALUES ('33', 'test', 'all', '456', '2016-07-20 15:58:51.356657');
INSERT INTO `messages` VALUES ('34', 'try', 'all', '345678', '2016-07-20 15:59:01.136216');
INSERT INTO `messages` VALUES ('35', 'try', 'all', '12345678', '2016-07-20 15:59:04.723421');
INSERT INTO `messages` VALUES ('36', 'test', 'fangrui', '2', '2016-07-20 16:16:14.211304');
INSERT INTO `messages` VALUES ('37', 'ruifang', 'all', '123', '2016-07-21 09:49:53.040668');
INSERT INTO `messages` VALUES ('38', 'try', 'all', '321', '2016-07-21 09:49:56.744880');
INSERT INTO `messages` VALUES ('39', 'ruifang', 'try', '123', '2016-07-21 09:52:08.353407');
INSERT INTO `messages` VALUES ('40', 'try', 'ruifang', '321', '2016-07-21 09:52:21.966186');
INSERT INTO `messages` VALUES ('41', 'ruifang', 'try', '123', '2016-07-21 09:54:51.631746');
INSERT INTO `messages` VALUES ('42', 'ruifang', 'all', '123', '2016-07-21 16:34:44.219856');
INSERT INTO `messages` VALUES ('43', 'ruifang', 'all', '6', '2016-07-21 16:37:44.859188');
INSERT INTO `messages` VALUES ('44', 'ruifang', 'all', '\\', '2016-07-21 17:15:01.372961');
INSERT INTO `messages` VALUES ('45', 'ruifang', 'all', '、', '2016-07-21 17:15:09.459424');
INSERT INTO `messages` VALUES ('46', 'ruifang', 'all', '、、、、、、', '2016-07-21 17:15:19.860019');
INSERT INTO `messages` VALUES ('47', 'ruifang', 'all', 'qQO', '2016-07-21 17:16:25.536775');
INSERT INTO `messages` VALUES ('48', 'fangrui', 'all', '123', '2016-07-22 20:48:13.707905');
INSERT INTO `messages` VALUES ('49', 'fangrui', 'rui', '123', '2016-07-22 20:48:47.158818');
INSERT INTO `messages` VALUES ('50', 'fangrui', 'all', '5', '2016-07-22 20:51:14.584250');
INSERT INTO `messages` VALUES ('51', 'rui', 'fangrui', '123', '2016-07-22 20:53:01.369358');

-- ----------------------------
-- Table structure for `temp_friends`
-- ----------------------------
DROP TABLE IF EXISTS `temp_friends`;
CREATE TABLE `temp_friends` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) NOT NULL,
  `friend` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of temp_friends
-- ----------------------------
INSERT INTO `temp_friends` VALUES ('1', 'ruifang', 'try');
INSERT INTO `temp_friends` VALUES ('2', 'ruifang', 'try');
INSERT INTO `temp_friends` VALUES ('3', 'fangrui', 'rui');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) NOT NULL,
  `password` varchar(10) DEFAULT NULL,
  `exit_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'fangrui', '123456', '2016-07-22 20:56:21.921829');
INSERT INTO `users` VALUES ('2', 'rui', '123456', '2016-07-20 10:50:36.411102');
INSERT INTO `users` VALUES ('7', 'try', '123456', '2016-07-21 10:01:12.309520');
INSERT INTO `users` VALUES ('8', 'test', '123456', '2016-07-20 20:31:47.226449');
INSERT INTO `users` VALUES ('9', 'ruifang', '123456', '2016-07-21 17:17:46.645414');
