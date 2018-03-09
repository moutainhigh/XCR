-- ----------------------------
-- 添加消息编辑器相关两个字段
-- ----------------------------
ALTER TABLE `YT_XCR_MSG_APUSH`
    ADD COLUMN `MSG_CONTENT` TEXT NULL COMMENT '消息编辑内容' AFTER `AREA_STR`,
    ADD COLUMN `MSG_CONTENT_TYPE` INT(2) DEFAULT -1 COMMENT '-1链接，1编辑内容' AFTER `MSG_CONTENT`;

-- ----------------------------
-- 多区域推送修改PUSH_TO，AREA_STR两字段长度
-- ----------------------------
ALTER TABLE `YT_XCR_MSG_APUSH`
    CHANGE `PUSH_TO` `PUSH_TO` VARCHAR(300) CHARSET utf8 COLLATE utf8_general_ci DEFAULT '' NULL COMMENT '推送对象(后台定向推送为门店编号)',
    CHANGE `AREA_STR` `AREA_STR` VARCHAR(300) CHARSET utf8 COLLATE utf8_general_ci DEFAULT '' NULL COMMENT '区域编号';