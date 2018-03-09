package com.yatang.xc.xcr.biz.mission.flow.engine.calc;

import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;

/**
 * 任务计算
 * 
 * @author yangqingsong
 *
 */
public interface RuleCalculate {

	/**
	 * 计算条件信息
	 * 
	 * @param calParam @return @throws
	 */
	public RuleDefinitionDto calcDefinition(String rule);



	/**
	 * 计算是否满足条件，当满足条件时的奖励信息
	 * 
	 * @param missionPo
	 * @param calParam
	 * @return
	 */
	public RuleCalculateDto calculate(String rule, RuleCalculateDto calParam);
}
