package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.busi.common.utils.SerializeUtil;
import com.yatang.xc.mbd.biz.bpm.dto.AuditDataDTO;
import com.yatang.xc.mbd.biz.bpm.enums.ProcessTypeEnum;
import com.yatang.xc.mbd.biz.org.dto.FranchiseeDto;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SimpleRuleCreator;

/**
 * 查询门店小超完成多少订单数量的调用接口<br>
 * 调用指定的接口，只需要传入门店信息，不需要传入其他信息
 * 
 * @author yangqingsong
 *
 */
//@Service("orderCountTriggerCaller")
public class OrderCountTriggerCaller extends BaseTriggerCaller {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Value("${mission.trigger.order.useMock}")
    private boolean useMock;

    @Autowired
    private MissionExecuteFlowService missionExecuteFlowService;

    @Resource(name = "defaultProducer")
    private MQProducer transProducer;


    /**
     * 执行调用操作
     */
    @Override
    public RuleCalculateDto call(MissionExecuteInfoPO executePO) {
        SimpleRuleCreator.RuleDefinition ruleDefinition = new SimpleRuleCreator.RuleDefinition();

        if (useMock) {
            ruleDefinition.setMissionFinished(true);
        } else {
            try {
                boolean result = missionExecuteFlowService.checkOrderCountFromDataCenter(executePO.getMerchantId());

                if (result) {
                    ruleDefinition.setMissionFinished(true);
                    FranchiseeDto dto =  missionExecuteFlowService.getFranchiseeByMerchantId(executePO.getMerchantId());
                    // 为给线下拓展人员发绩效 调用 bpm流程
                    AuditDataDTO auditDataDTO = new AuditDataDTO();
                    auditDataDTO.setOriginatorCompanyId(dto.getBranchCompanyId());
                    auditDataDTO.setFormId(executePO.getMerchantId());
                    auditDataDTO.setIntentionId(String.valueOf(executePO.getId()));
                    auditDataDTO.setProcessType(ProcessTypeEnum.XCRSubsidy.getKey());

                    Message messageNew = new Message("GRANT", "GRANT-PF-CREATE", UUID.randomUUID().toString(), SerializeUtil.serialize(auditDataDTO));
                    transProducer.send(messageNew);
                }

            } catch (Exception e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
        return ruleDefinition;
    }

}
