/*
Navicat MySQL Data Transfer

Source Server         : caiwu
Source Server Version : 50636
Source Host           : sit.db.com:3306
Source Database       : xcrdb

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-12-22 09:13:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for XCR_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `XCR_ROLE`;
CREATE TABLE `XCR_ROLE` (
  `ID` int(11) NOT NULL,
  `ROLE_NAME` varchar(255) DEFAULT NULL COMMENT '角色名字',
  `ROLE_CODE` int(11) DEFAULT NULL COMMENT '角色编码',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of XCR_ROLE
-- ----------------------------
INSERT INTO `XCR_ROLE` VALUES ('0', '加盟商', '0');
INSERT INTO `XCR_ROLE` VALUES ('1', '管理员', '1');
INSERT INTO `XCR_ROLE` VALUES ('2', '店长', '2');
INSERT INTO `XCR_ROLE` VALUES ('3', '收银员', '3');
