
-- 添加时间，任务描述字段
ALTER TABLE `XCR_MISSION_EXECUTE_HISTORY`
ADD COLUMN `VALID_TIME_START` DATETIME NULL COMMENT '任务有效开始时间' AFTER `IS_BACKUP`,
ADD COLUMN `VALID_TIME_END` DATETIME NULL COMMENT '任务有效结束时间' AFTER `VALID_TIME_START`,
ADD COLUMN `DESCRIPTION` VARCHAR(600) NULL COMMENT '任务描述' AFTER `VALID_TIME_END`;

ALTER TABLE `XCR_MISSION_EXECUTE_INFO`
ADD COLUMN `VALID_TIME_START` DATETIME NULL COMMENT '任务有效开始时间' AFTER `IS_BACKUP`,
ADD COLUMN `VALID_TIME_END` DATETIME NULL COMMENT '任务有效结束时间' AFTER `VALID_TIME_START`,
ADD COLUMN `DESCRIPTION` VARCHAR(600) NULL COMMENT '任务描述' AFTER `VALID_TIME_END`;

ALTER TABLE `XCR_MISSION_INFO`
ADD COLUMN `VALID_TIME_START` DATETIME NULL COMMENT '任务有效开始时间' AFTER `SORT`,
ADD COLUMN `VALID_TIME_END` DATETIME NULL COMMENT '任务有效结束时间' AFTER `VALID_TIME_START`,
ADD COLUMN `DURATION_TIME_START` DATETIME NULL COMMENT '有效开始时间' AFTER `VALID_TIME_END`,
ADD COLUMN `DURATION_TIME_END` DATETIME NULL COMMENT '有效结束时间' AFTER `DURATION_TIME_START`;


-- XCR_MISSION_TEMPLATE  表中添加连续签到任务
insert  into `XCR_MISSION_TEMPLATE`
(`ID`,`NAME`,`SPECIAL_AWARD_REMARK`,`TYPE`,`MISSION_TYPE`,`TEMPLATE_CODE`,`LINK`,
`NEED_MANUAL_AUDIT`,`COMPLETE_TIPS`,`TRIGGER_INTERFACE_NAME`,`RULE_TEMPLATE`,`IS_SCHEDULE`,
`START_HOUR`,`DURATION_HOURS`,`SORT`,`IS_DELETED`)
values
(7,'连续签到','','simple','RECOMMEND','T007',NULL,0,'','UserSignTriggerCaller',
'package com.yatang.xc.mission.rule.simple\r\nimport com.yatang.xc.xcr.biz.mission.flow.engine.bo.RuleDefinitionGallery\r\nimport com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SimpleRuleCreator.RuleDefinition\r\n\r\nrule \"definitionRule\"\r\n  no-loop true\r\n  lock-on-active true\r\n  salience 99\r\n  when\r\n    eval(true);\r\n    $gallery:RuleDefinitionGallery();\r\n  then\r\n    RuleDefinition ruleDefine = new RuleDefinition();\r\n    ruleDefine.setMissionFinished({0});\r\n    $gallery.setRuleDefine(ruleDefine);\r\nend\r\n\r\nrule \"calculateRule\"\r\n  no-loop true\r\n  lock-on-active true\r\n  salience 99\r\n  when\r\n    $ruleDefine:RuleDefinition(missionFinished=={0})\r\n  then\r\n    $ruleDefine.setSuccess({1});\r\nend',
0,0,175200,7,NULL);
