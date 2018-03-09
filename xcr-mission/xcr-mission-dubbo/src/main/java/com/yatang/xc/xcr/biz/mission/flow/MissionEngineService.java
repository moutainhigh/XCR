package com.yatang.xc.xcr.biz.mission.flow;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.BuildMissionRuleDto;

/**
 * 任务引擎
 * 
 * @author yangqingsong
 *
 */
public interface MissionEngineService {

    /**
     * 创建任务
     * 
     * @param template
     * 
     */
    public String buildMissionRule(BuildMissionRuleDto rule);


    public RuleCalculateDto calculateMission(MissionExecuteInfoPO execute);
}
