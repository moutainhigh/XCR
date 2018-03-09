package com.yatang.xc.xcr.biz.mission.flow.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busi.common.exception.BusinessException;
import com.esotericsoftware.minlog.Log;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.BuildMissionRuleDto;
import com.yatang.xc.xcr.biz.mission.flow.MissionEngineService;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.MissionTriggerCaller;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.RuleCalculate;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.RuleCreator;

/**
 * 任务引擎实现类
 * 
 * @author yangqingsong
 *
 */
@Service
public class MissionEngineServiceImpl implements MissionEngineService {

	// 任务触发调用类
	@Autowired
	private List<MissionTriggerCaller>	callers;

	// 规则计算器
	@Autowired
	private RuleCalculate				ruleCalculate;

	// Drools规则生成器
	@Autowired
	private List<RuleCreator>			droolRuleCreators;


	/**
	 * 生成任务 创建任务
	 */
	@Override
	public String buildMissionRule(BuildMissionRuleDto ruleDto) {
		String ruleStr = null;

		// 生成规则
		if (null == ruleDto) {
			throw new BusinessException("BIZERROR00001", "没有规则创建参数对象");
		}
		for (RuleCreator ruleCreator : this.droolRuleCreators) {
			ruleStr = ruleCreator.build(ruleDto);
			if (!RuleCreator.NOT_HANDLER.equals(ruleStr)) { // 只要一个匹配则退出循环
				break;
			}
		}

		return ruleStr;
	}



	/**
	 * 计算任务是否完成
	 */
	@Override
	public RuleCalculateDto calculateMission(MissionExecuteInfoPO execute) {
		try {
			// 调用其他服务
			for (MissionTriggerCaller caller : this.callers) {// 责任链模式
				boolean flag = caller.isHandle(execute);
				if (flag) {
					RuleCalculateDto calParam = caller.call(execute);
					// 计算任务是否完成
					return ruleCalculate.calculate(execute.getRule(), calParam);
				}
			}
		} catch (Exception e) {
			Log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return null;
	}

}
