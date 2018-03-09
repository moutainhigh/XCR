package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.MissionTriggerCaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 简单的调用接口<br>
 * 调用指定的接口，只需要传入门店信息，不需要传入其他信息 注意：此接口只做drools演示 或测试，不做其他真实业务场景的用途， 真实业务需
 * 在call方法中 组装真实的业务数据
 *
 * @author yangqingsong
 */
//@Service("baseTriggerCaller")
public abstract class BaseTriggerCaller implements MissionTriggerCaller {
    protected final Log log = LogFactory.getLog(this.getClass());

    /**
     * 判断是否可以由此调用者处理
     */
    @Override
    public boolean isHandle(MissionExecuteInfoPO missionPo) {
        if (missionPo != null && this.getClass().getSimpleName().equals(missionPo.getTriggerInterfaceName())) {
            log.info("Calculate run:" + this.getClass().getSimpleName());
            return true;
        }
        return false;
    }


    /**
     * 执行调用操作
     */
    @Override
    public abstract RuleCalculateDto call(MissionExecuteInfoPO executePO);


}
