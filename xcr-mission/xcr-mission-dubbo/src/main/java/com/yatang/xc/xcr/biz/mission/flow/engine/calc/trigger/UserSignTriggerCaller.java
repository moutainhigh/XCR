package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SignRuleCreator;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

/**
 * Created by wangyang on 2017/7/14.
 */
@Service("userSignTriggerCaller")
public class UserSignTriggerCaller extends BaseTriggerCaller {
    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionService missionService;


    @Override
    public RuleCalculateDto call(MissionExecuteInfoPO executePO) {
        SignRuleCreator.RuleDefinition ruleDefinition = new SignRuleCreator.RuleDefinition();

        // 判断签到任务是否完成
        String merchantId = executePO.getMerchantId();
        Date validStartDay = executePO.getValidTimeStart();
        Date validEndDay = executePO.getValidTimeEnd();

        log.info("missionService.checkSignFinish -> merchantId:" + merchantId + "  validStartDay:" + validStartDay + "  validEndDay:" + validEndDay);
        int continueDate = missionService.checkSignFinish(merchantId, validStartDay, validEndDay);
        log.info("连续签到 ->  merchantId:" + executePO.getMerchantId() + " continueDate:" + continueDate + " execute:" + executePO.getId());
        ruleDefinition.setContinueDate(continueDate);
        return ruleDefinition;
    }

    public static void main(String[] args) {
        Date date = DateUtil.addOneDate(new Date(), 1);
        String day = DateUtil.formatDate(date);
        System.out.println(day);
    }

}
