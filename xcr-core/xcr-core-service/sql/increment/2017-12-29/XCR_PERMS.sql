/*
Navicat MySQL Data Transfer

Source Server         : caiwu
Source Server Version : 50636
Source Host           : sit.db.com:3306
Source Database       : xcrdb

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-12-22 09:13:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for XCR_PERMS
-- ----------------------------
DROP TABLE IF EXISTS `XCR_PERMS`;
CREATE TABLE `XCR_PERMS` (
  `ID` bigint(20) NOT NULL,
  `PERMS_URL` varchar(255) DEFAULT NULL COMMENT '权限链接',
  `PERMS_NAME` varchar(255) DEFAULT NULL COMMENT '权限名字',
  `PERMS_CODE` int(11) DEFAULT NULL COMMENT '权限编码',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of XCR_PERMS
-- ----------------------------
INSERT INTO `XCR_PERMS` VALUES ('1', '/User/StoreList1', '门店列表', null);
INSERT INTO `XCR_PERMS` VALUES ('2', '/User/CheckBankCard', '添加银行卡', null);
INSERT INTO `XCR_PERMS` VALUES ('3', '/User/SetStoreDefault', '设置默认门店', null);
INSERT INTO `XCR_PERMS` VALUES ('4', '/null0', '门店详情', null);
INSERT INTO `XCR_PERMS` VALUES ('5', '/null1', '电子合同', null);
INSERT INTO `XCR_PERMS` VALUES ('6', '/null2', '加盟保证金', null);
INSERT INTO `XCR_PERMS` VALUES ('7', '/User/MemberInfo', '会员中心查看', null);
INSERT INTO `XCR_PERMS` VALUES ('8', '/User/Sign', '签到', null);
INSERT INTO `XCR_PERMS` VALUES ('9', '/User/GetShopInfo', '线上店铺管理', null);
INSERT INTO `XCR_PERMS` VALUES ('10', '/User/TaskList', '我的任务', null);
INSERT INTO `XCR_PERMS` VALUES ('11', '/User/TaskClassDetial', '小超课堂', null);
INSERT INTO `XCR_PERMS` VALUES ('12', '/User/SettlementManageList', '结算管理', null);
INSERT INTO `XCR_PERMS` VALUES ('13', '/User/StatisticsList', '数据统计', null);
INSERT INTO `XCR_PERMS` VALUES ('14', '/User/RevenueOrOutDetial', '门店收入查看', null);
INSERT INTO `XCR_PERMS` VALUES ('15', '/null13', '外送收入查看', null);
INSERT INTO `XCR_PERMS` VALUES ('16', '/User/TransactionList', '交易流水查看', null);
INSERT INTO `XCR_PERMS` VALUES ('17', '/User/TicketDetial', '小票明细查看', null);
INSERT INTO `XCR_PERMS` VALUES ('18', '/User/ClassifyList', '门店商品查看', null);
INSERT INTO `XCR_PERMS` VALUES ('19', '/User/ModifyGoodsFrameType', '门店商品上架', null);
INSERT INTO `XCR_PERMS` VALUES ('20', '/null11', '门店商品下家', null);
INSERT INTO `XCR_PERMS` VALUES ('21', '/null12', '门店商品上下架', null);
INSERT INTO `XCR_PERMS` VALUES ('22', '/User/AddNewGoods', '新增商品', null);
INSERT INTO `XCR_PERMS` VALUES ('23', '/User/NewGoodsDetail', '扫一扫查看', null);
INSERT INTO `XCR_PERMS` VALUES ('24', '/User/AddNewGoods1', '扫一扫新增', null);
INSERT INTO `XCR_PERMS` VALUES ('25', '/null8', '编辑商品查看', null);
INSERT INTO `XCR_PERMS` VALUES ('26', '/User/EditNewGoods', '编辑商品允许退货', null);
INSERT INTO `XCR_PERMS` VALUES ('27', '/User/EditNewGoods1', '编辑商品关闭允许退货', null);
INSERT INTO `XCR_PERMS` VALUES ('28', '/User/StockList', '库存管理商品库存查看', null);
INSERT INTO `XCR_PERMS` VALUES ('29', '/null5', '库存管理进货单查看', null);
INSERT INTO `XCR_PERMS` VALUES ('30', '/null6', '库存管理新增进货单', null);
INSERT INTO `XCR_PERMS` VALUES ('31', '/User/OrderList', '外送订单查看', null);
INSERT INTO `XCR_PERMS` VALUES ('32', '/User/GoodsOutList', '外送商品查看', null);
INSERT INTO `XCR_PERMS` VALUES ('33', '/User/EventList', '店铺活动查看', null);
INSERT INTO `XCR_PERMS` VALUES ('34', '/null7', '我要进货查看', null);
