DELETE FROM XCR_MISSION_TEMPLATE WHERE TEMPLATE_CODE = 'T007';
INSERT  INTO `XCR_MISSION_TEMPLATE`
(`NAME`,`SPECIAL_AWARD_REMARK`,`TYPE`,`MISSION_TYPE`,`TEMPLATE_CODE`,`LINK`,`NEED_MANUAL_AUDIT`,`COMPLETE_TIPS`,
`TRIGGER_INTERFACE_NAME`,`RULE_TEMPLATE`,`IS_SCHEDULE`,`IS_DELETED`,`START_HOUR`,`DURATION_HOURS`,`SORT`) 
VALUES 
('连续签到','','sign','RECOMMEND','T007',NULL,0,'','UserSignTriggerCaller','package com.yatang.xc.mission.rule.simple\r\nimport com.yatang.xc.xcr.biz.mission.flow.engine.bo.RuleDefinitionGallery\r\nimport com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SignRuleCreator.RuleDefinition\r\n\r\nrule \"definitionRule\"\r\n  no-loop true\r\n  lock-on-active true\r\n  salience 99\r\n  when\r\n    eval(true);\r\n    $gallery:RuleDefinitionGallery();\r\n  then\r\n    RuleDefinition ruleDefine = new RuleDefinition();\r\n    ruleDefine.setContinueDate({0});\r\n    $gallery.setRuleDefine(ruleDefine);\r\nend\r\n\r\nrule \"calculateRule\"\r\n  no-loop true\r\n  lock-on-active true\r\n  salience 99\r\n  when\r\n    $ruleDefine:RuleDefinition(continueDate>={0})\r\n  then\r\n    $ruleDefine.setSuccess({1});\r\nend',0,NULL,0,175200,7);


ALTER TABLE `XCR_MISSION_INFO` CHANGE `DESCRIPTION` `DESCRIPTION` VARCHAR(600) CHARSET utf8 COLLATE utf8_general_ci NULL COMMENT '描述说明';