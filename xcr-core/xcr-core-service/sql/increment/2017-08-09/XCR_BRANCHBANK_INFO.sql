DROP TABLE IF EXISTS `XCR_BRANCHBANK_INFO`;

CREATE TABLE `XCR_BRANCHBANK_INFO` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `bank_no` varchar(15) NOT NULL COMMENT '支行银行账号',
  `branchbank_name` varchar(50) NOT NULL COMMENT '支行银行名称',
  `bank_name` varchar(50) NOT NULL COMMENT '银行名称',
  `bank_id` varchar(10) NOT NULL COMMENT '银行id',
  `province` varchar(20) DEFAULT NULL COMMENT '省份',
  `province_id` varchar(10) DEFAULT NULL COMMENT '省份id',
  `city` varchar(20) DEFAULT NULL COMMENT '城市',
  `city_id` varchar(10) DEFAULT NULL COMMENT '城市id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `XCR_BRANCHBANK_INFO` ADD INDEX index_name ( `bank_id` ) 