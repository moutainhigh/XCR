package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.DayFacePayRuleCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 日当面付
 * Created by wangyang on 2017/10/30.
 */
@Service("dayFacePayTriggerCaller")
public class DayFacePayTriggerCaller extends BaseTriggerCaller {

    @Autowired
    private DataCenterXcrDubboService dataCenterXcrDubboService;

    @Override
    public RuleCalculateDto call(MissionExecuteInfoPO executePO) {
        log.info("日当面付笔数任务是否完成 -> in DayFacePayTriggerCaller.call() -> MissionExecuteInfoPO:" + JSONObject.toJSONString(executePO));
        DayFacePayRuleCreator.RuleDefinition ruleDefinition = new DayFacePayRuleCreator.RuleDefinition();
        String merchantId = executePO.getMerchantId(); //门店编号
        Date startTime = executePO.getValidTimeStart();
        Date endTime = executePO.getValidTimeEnd();
        String start = DateUtil.formatDateDefaule(startTime);
        String end = DateUtil.formatDateDefaule(endTime);
        log.info("日当面付笔数是否完成 -> 数据中心查询参数 -> merchantId" + merchantId + "  startTime:" + start + "  endTime:" + end);
        Response<Integer> integerResponse = dataCenterXcrDubboService.queryFacetofaceCountByTimeAndShopId(merchantId, start, end);

        if (integerResponse == null) {
            log.error("日当面付笔数是否完成 -> 通过时间和加盟商id查询日当面付笔数异常 -> integerResponse == null");
            return ruleDefinition;
        }
        if (!integerResponse.isSuccess()) {
            log.error("日当面付笔数是否完成 -> integerResponse.isSuccess():" + integerResponse.isSuccess() + "  msg:" + integerResponse.getErrorMessage());
            return ruleDefinition;
        }
        Integer buyNumber = integerResponse.getResultObject();
        if (buyNumber == null || buyNumber < 0) {
            log.error("日当面付笔数是否完成 -> 获取笔数异常 -> buyNumber：" + buyNumber);
            return ruleDefinition;
        }
        log.info("日当面付笔数是否完成 -> 统计出的日当面付笔数为:" + buyNumber + "  starTime:" + start + "  endTime:" + end);
        ruleDefinition.setFacePayNumber(buyNumber);
        return ruleDefinition;
    }
}
