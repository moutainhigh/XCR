package com.yatang.xc.xcr.biz.mission.flow.engine.creator;

import com.yatang.xc.xcr.biz.mission.dto.manage.BuildMissionRuleDto;

/**
 * 规则生成器
 * @author yangqingsong
 *
 */
public interface RuleCreator {
	//未处理标示符号
	public String NOT_HANDLER = "notHandler";
	
	/**
	 * 基于drools生成任务规则
	 * @return
	 */
	public String build(BuildMissionRuleDto param);
}
