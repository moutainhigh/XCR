ALTER TABLE `YT_XCR_MSG_APUSH`
ADD COLUMN `PUSH_TYPE` VARCHAR(2) DEFAULT '0' NULL COMMENT '推送类型0：所有，1：定向' AFTER `RELEASES_TIME`,
ADD COLUMN `PUSH_TO` VARCHAR(100) DEFAULT '' NULL COMMENT '推送对象(后台定向推送为门店编号)' AFTER `PUSH_TYPE`;