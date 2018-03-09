package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.MonthBuyAmountRuleCreator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 供应链采购金额
 * Created by wangyang on 2017/10/31.
 */
@Service("monthBuyAmountTriggerCaller")
public class MonthBuyAmountTriggerCaller extends BaseTriggerCaller {

    @Autowired
    private DataCenterXcrDubboService dataCenterXcrDubboService;

    @Override
    public RuleCalculateDto call(MissionExecuteInfoPO executePO) {
        log.info("供应链采购金额是否完成 -> in MonthBuyAmountTriggerCaller.call() -> MissionExecuteInfoPO:" + JSONObject.toJSONString(executePO));

        MonthBuyAmountRuleCreator.RuleDefinition ruleDefinition = new MonthBuyAmountRuleCreator.RuleDefinition();

        String merchantId = executePO.getMerchantId(); //门店编号
        Date startTime = executePO.getValidTimeStart();
        Date endTime = executePO.getValidTimeEnd();
        String start = DateUtil.formatDateDefaule(startTime);
        String end = DateUtil.formatDateDefaule(endTime);

        log.info("供应链采购金额是否完成 -> 数据中心查询参数 -> merchantId" + merchantId + "  startTime:" + start + "  endTime:" + end);
        Response<String> integerResponse = dataCenterXcrDubboService.querySupportChainAmountByTimeAndShopId(merchantId, start, end);
        if (integerResponse == null) {
            log.error("供应链采购金额是否完成 -> 通过时间和加盟商id查询供应链订单金额异常 -> integerResponse == null");
            return ruleDefinition;
        }
        if (!integerResponse.isSuccess()) {
            log.error("供应链采购金额是否完成 -> integerResponse.isSuccess():" + integerResponse.isSuccess() + "  msg:" + integerResponse.getErrorMessage());
            return ruleDefinition;
        }
        String buyAmount = integerResponse.getResultObject();
        if (StringUtils.isEmpty(buyAmount)) {
            log.error("供应链采购金额是否完成 -> 获取金额异常 -> buyAmount：" + buyAmount);
            return ruleDefinition;
        }
        log.info("查询出来的统计金额为：" + buyAmount + "  startTime:" + start + "  endTime:" + endTime);
        ruleDefinition.setBuyAmount(Double.valueOf(buyAmount));
        return ruleDefinition;
    }

}
