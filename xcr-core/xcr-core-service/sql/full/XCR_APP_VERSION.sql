/*
Navicat MySQL Data Transfer

Source Server         : caiwu
Source Server Version : 50636
Source Host           : sit.db.com:3306
Source Database       : xcrdb

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-08-08 16:27:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for XCR_APP_VERSION
-- ----------------------------
DROP TABLE IF EXISTS `XCR_APP_VERSION`;
CREATE TABLE `XCR_APP_VERSION` (
  `ID` bigint(255) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TYPE` int(10) DEFAULT NULL COMMENT '类型：0：IOS 1：安卓 2：供应链资源包',
  `VERSION_CODE` varchar(20) DEFAULT NULL COMMENT '版本号',
  `PUBLISH_TIME` datetime DEFAULT NULL COMMENT '发布时间',
  `IS_LIVE_UP` int(10) DEFAULT NULL COMMENT '是否强制升级1：是 0：否',
  `DESCRIPTION` text COMMENT '描述信息',
  `STATE` int(10) DEFAULT NULL COMMENT '发布状态0：未发布 1:发布',
  `APK_URL` varchar(255) DEFAULT NULL COMMENT '下载链接',
  `IS_DELATE` int(10) DEFAULT NULL COMMENT '是否删除 1：是  0：否',
  `CODE` int(255) DEFAULT NULL COMMENT '序号（后台做逻辑判断用的）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
