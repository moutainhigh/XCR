package com.yatang.xc.xcr.biz.message.schedule;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.busi.common.utils.StringUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessage;
import com.yatang.xc.xcr.biz.message.mq.producer.DcProducer;
import com.yatang.xc.xcr.biz.message.service.ScanPaymentMessageService;
import com.yatang.xc.xcr.biz.message.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务Handler的一个Demo（Bean模式）
 * <p>
 * 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解；
 * 3、添加 “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，该名称对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHander(value = "scanPaymentMsgDataReSendJobHandler")
@Component
public class ScanPaymentMsgDataReSendJobHandler extends IJobHandler {


    @Autowired
    private ScanPaymentMessageService scanPaymentMessageService;
    @Autowired
    private DcProducer dcProducer;
    @Resource(name = "dcMessage")
    private Message dcMessage;

    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("【mq发送失败的记录定时重发】 ...start .");
        //查询前15天商品
        Date now = new Date();
        String startTime = getStartTime(now);
        String endTime = getEndTime(now);
        List<SwiftPassMessage> swiftPassMessageList = scanPaymentMessageService.getListWithFailedToSendMQ(startTime, endTime);
        if (CollectionUtils.isEmpty(swiftPassMessageList)) {
            XxlJobLogger.log("【mq发送失败的记录定时重发】 -> 目前没有失败的记录 ...");
            return ReturnT.SUCCESS;
        }
        for (SwiftPassMessage swiftPassMessage : swiftPassMessageList) {
            XxlJobLogger.log("【mq发送失败的记录定时重发】 -> 失败记录 -> swiftPassMessage:" + JSONObject.toJSONString(swiftPassMessage));
            send(swiftPassMessage);
        }
        XxlJobLogger.log("【mq发送失败的记录定时重发】 ...end .");
        return ReturnT.SUCCESS;
    }

    /**
     * 获取查询结束时间
     * yyyyMMddhhmmss
     *
     * @param now
     * @return
     */
    private String getEndTime(Date now) {
        return DateUtils.dateFormat(now, "yyyyMMdd") + "000000";
    }

    /**
     * 获取查询开始时间
     * yyyyMMddhhmmss
     *
     * @param now
     * @return
     */
    private String getStartTime(Date now) {
        Date startDay = DateUtils.addOneDate(now, -15);
        return DateUtils.dateFormat(startDay, "yyyyMMdd") + "000000";
    }

    @PostConstruct
    public void init() {
        XxlJobLogger.log("XXL-JOB, Hello World.");
        System.out.println("ScanPaymentMsgDataReSendJobHandler init");
    }

    private void send(SwiftPassMessage swiftPassMessage) {
        dcMessage.setKeys(swiftPassMessage.getOut_trade_no());
        dcMessage.setBody(getMessageBody(swiftPassMessage).getBytes());
        swiftPassMessage.setSendDcMqResult("1");
        try {
            SendResult sendResult = dcProducer.getDefaultMQProducer().send(dcMessage);
            if (sendResult != null && sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                XxlJobLogger.log("【mq发送失败的记录定时重发】 -> 发送成功 -> swiftPassMessage:" + JSONObject.toJSONString(swiftPassMessage));
                swiftPassMessage.setSendDcMqResult("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanPaymentMessageService.updateSwiftPassMessage(swiftPassMessage);
    }

    private String getMessageBody(SwiftPassMessage swiftPassMessage) {
        Map<String, Object> mapS = new HashMap<>();
        mapS.put("id", swiftPassMessage.getOut_trade_no());
        mapS.put("haveShopCode", swiftPassMessage.getHaveShopCode());
        mapS.put("shopCode", swiftPassMessage.getMch_id());
        mapS.put("openId", swiftPassMessage.getOpenid());
        mapS.put("outTradeNo", swiftPassMessage.getOut_trade_no());
        mapS.put("totalFee", getAmountDouble(swiftPassMessage.getTotal_fee()));
        mapS.put("tradeType", swiftPassMessage.getTrade_type());
        mapS.put("timeEnd", getDateStr(swiftPassMessage.getTime_end()));
        XxlJobLogger.log("给数据中心发消息 -> body -> " + JSONObject.toJSONString(mapS));
        return JSONObject.toJSONString(mapS);

    }

    private double getAmountDouble(int totalFee) {
        String amount = new DecimalFormat("0.00").format((double) totalFee / 100);
        return Double.valueOf(amount);
    }

    private static String getDateStr(String time) {
        XxlJobLogger.log("getDateStr -> time:" + time);
        if (StringUtils.isEmpty(time)) {
            return "";
        }
        if (time.length() != 14) {
            return "";
        }
        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);
        String hour = time.substring(8, 10);
        String minute = time.substring(10, 12);
        String second = time.substring(12, 14);
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
}