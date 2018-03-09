

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for XCR_ADVERTISEMENT
-- ----------------------------
DROP TABLE IF EXISTS `XCR_ADVERTISEMENT`;
CREATE TABLE `XCR_ADVERTISEMENT` (
  `ID` bigint(255) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `PIC_NAME` varchar(255) DEFAULT NULL COMMENT '活动图片名字\r\n',
  `PIC_URL` mediumtext COMMENT '图片地址',
  `STATE` int(20) DEFAULT NULL COMMENT '活动状态\r\n0：失效\r\n1：发布\r\n2：删除',
  `ACTIVITY_URL` varchar(255) DEFAULT NULL COMMENT '活动链接',
  `CREATE_TIME` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `REMAIND` varchar(255) DEFAULT NULL COMMENT '广告组名',
  `GROUP_ID` bigint(255) DEFAULT NULL COMMENT '广告组id',
  `TYPE` int(255) DEFAULT NULL COMMENT '广告类型1：图片 2：文字     其余待定',
  `SORE` varchar(40) DEFAULT NULL COMMENT '排序位置',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8mb4;
