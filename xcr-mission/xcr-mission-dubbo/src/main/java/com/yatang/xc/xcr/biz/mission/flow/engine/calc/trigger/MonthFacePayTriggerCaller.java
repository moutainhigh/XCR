package com.yatang.xc.xcr.biz.mission.flow.engine.calc.trigger;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.MonthFacePayRuleCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日均当面付
 * Created by wangyang on 2017/10/30.
 */
@Service("monthFacePayTriggerCaller")
public class MonthFacePayTriggerCaller extends BaseTriggerCaller {

    @Autowired
    private DataCenterXcrDubboService dataCenterXcrDubboService;

    @Override
    public RuleCalculateDto call(MissionExecuteInfoPO executePO) {
        log.info("日均当面付任务是否完成 -> in MonthFacePayTriggerCaller.call() -> MissionExecuteInfoPO:" + JSONObject.toJSONString(executePO));

        MonthFacePayRuleCreator.RuleDefinition ruleDefinition = new MonthFacePayRuleCreator.RuleDefinition();

        String merchantId = executePO.getMerchantId(); //门店编号
        Date startTime = executePO.getValidTimeStart();
        Date endTime = executePO.getValidTimeEnd();
        Date now = new Date();
        //判断统计结束时间与当前时间比较
        if (endTime.getTime() > now.getTime()) {
            log.info("日均当面付任务是否完成 -> 当前时间小于统计时间，还没到统计时候 ... == endTime:" + endTime + "  now:" + now);
            return ruleDefinition;
        }

        String start = DateUtil.formatDateDefaule(startTime);
        String end = DateUtil.formatDateDefaule(endTime);

        //2:查询该加盟商时间段内当面付的总金额
        log.info("日均当面付任务是否完成 -> 数据中心查询参数 -> merchantId" + merchantId + "  startTime:" + start + "  endTime:" + end);
        Response<String> stringResponse = dataCenterXcrDubboService.queryShopFacetofacemnyByTime(merchantId, start, end);
        if (stringResponse == null) {
            log.info("查询该加盟商时间段内当面付的总金额异常 -> stringResponse == null");
            return ruleDefinition;
        }
        if (!stringResponse.isSuccess()) {
            log.info("查询该加盟商时间段内当面付的总金额异常 -> stringResponse.isSuccess():" + stringResponse.isSuccess() + "  msg:" + stringResponse.getErrorMessage());
            return ruleDefinition;
        }
        double amount = Double.valueOf(stringResponse.getResultObject());
        log.info("日均当面付任务是否完成 -> 查询出当面付总金额金额为 -> amount:" + amount + ",统计区间 -> startTime:" + start + "  endTime:" + end);
        Integer days = DateUtil.getDay(startTime, endTime);
        log.info("日均当面付任务是否完成 -> 统计天数为：days：" + days);
        double amountAvg = amount;
        if (days > 0) {
            amountAvg = new BigDecimal(amount / days).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        log.info("日均当面付任务是否完成 -> 计算出日均当面付金额为 -> amountAvg:" + amountAvg);
        ruleDefinition.setFacePayAmount(amountAvg);
        return ruleDefinition;
    }

    public static void main(String[] args) {
        double amount = 0.019999997;
        double amountAvg = new BigDecimal(amount / 2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(amountAvg);

        System.out.println(0.2 / 0.3);
    }

}
