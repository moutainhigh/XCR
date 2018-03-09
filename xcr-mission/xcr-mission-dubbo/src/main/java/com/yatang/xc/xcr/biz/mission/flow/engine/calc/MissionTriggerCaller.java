package com.yatang.xc.xcr.biz.mission.flow.engine.calc;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;

/**
 * 任务触发调用类
 * 
 * @author yangqingsong
 *
 */
public interface MissionTriggerCaller {
	/**
	 * 是否需要处理
	 * 
	 * @param missionPo
	 * @return
	 */
	public boolean isHandle(MissionExecuteInfoPO executePO);

	/**
	 * 调用接口返回数据
	 * 
	 * @param missionPo
	 * @return
	 */
	public RuleCalculateDto call(MissionExecuteInfoPO executePO);
}
