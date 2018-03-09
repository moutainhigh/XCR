package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SimpleRuleCreator;

/**
 * 学习任务接口<br>
 * 调用指定的接口，只需要传入门店信息，不需要传入其他信息
 * 
 * @author yangqingsong
 *
 */
//@Service("studyMissionTriggerCaller")
public class StudyMissionTriggerCaller extends BaseTriggerCaller {
    protected final Log log = LogFactory.getLog(this.getClass());
    @Value("${mission.trigger.study.useMock}")        
    private boolean useMock;

	/**
	 * 执行调用操作
	 */
	@Override
	public RuleCalculateDto call( MissionExecuteInfoPO executePO) {
	    log.info("Calculate run:"+this.getClass().getSimpleName());
	    SimpleRuleCreator.RuleDefinition ruleDefinition = new SimpleRuleCreator.RuleDefinition();
        if (useMock) {
            ruleDefinition.setMissionFinished(false);
        } else if(EnumMissionExecuteStatus.STATUS_FINISHED.getCode().equals(executePO.getStatus())){
          //学习任务完成小超课堂主动回调传回结果，规则引擎不做操作
        }
	    return ruleDefinition;
	}

}
