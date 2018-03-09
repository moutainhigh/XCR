package com.yatang.xc.xcr.biz.message.dubboservice.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.busi.common.resp.Response;
import com.busi.common.utils.DateUtils;
import com.busi.common.utils.StringUtils;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessage;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessageQuery;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnDto;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnQuery;
import com.yatang.xc.xcr.biz.message.dubboservice.SwiftPassDubboService;
import com.yatang.xc.xcr.biz.message.mq.producer.DcProducer;
import com.yatang.xc.xcr.biz.message.mq.producer.Producer;
import com.yatang.xc.xcr.biz.message.service.ScanPaymentMessageService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 威富通实现
 * Created by wangyang on 2017/9/25.
 */
@Service("swiftPassDubboService")
public class SwiftPassDubboServiceImpl implements SwiftPassDubboService {

    private final Log log = LogFactory.getLog(this.getClass());

    private static final int SWIFTPASS_TYPE = 8; //威富通消息

    @Autowired
    private ScanPaymentMessageService scanPaymentMessageService;
    @Autowired
    private Producer producer;
    @Autowired
    private Message message;
    @Autowired
    private DcProducer dcProducer;
    @Resource(name = "dcMessage")
    private Message dcMessage;


    @Override
    public Response<String> saveStreamAndSendMessage(SwiftPassReturnDto swiftPassReturnDto) {

        log.info("saveStreamAndSendMessage -> scanPaymentMessageDto:" + JSONObject.toJSONString(swiftPassReturnDto));
        SwiftPassMessage swiftPassMessage = new SwiftPassMessage();
        try {
            BeanUtils.copyProperties(swiftPassMessage, swiftPassReturnDto);
            log.info("BeanUtils.copyProperties(scanPaymentMessage, scanPaymentMessageDto) -> scanPaymentMessage:" + JSONObject.toJSONString(swiftPassMessage));

            swiftPassMessage.setCreateTime(DateUtils.dateDefaultFormat(new Date()));
            boolean success = scanPaymentMessageService.saveSwiftPassMessage(swiftPassMessage);
            log.info("scanPaymentMessageService.saveScanPaymentMessage(scanPaymentMessage) -> success:" + success);
            if (success) {
                String messageBody = getMqMessage(swiftPassMessage);
                log.info("getMqMessage(scanPaymentMessage); -> messageBody:" + messageBody);
                SendResult result = sendMqMessage(messageBody);
                log.info("sendMqMessage(messageBody) -> result:" + JSONObject.toJSONString(result));
                //给数据中心发消息
                log.info("数据中心发消息 -> dcProducer.isOpen():" + dcProducer.isOpen());
                if (dcProducer.isOpen()) {
                    sendMessageToDc(swiftPassMessage);
                }
                return new Response<>(true, "操作成功");
            }
            return new Response<>(false, "存流水失败，该数据已存在");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "数据存库或发送消息异常!");
        }
    }

    /**
     * 分页查询消息流水
     *
     * @param shopCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Response<SwiftPassReturnQuery> getSwiftPassMessageList(String shopCode, int pageNum, int pageSize) {
        log.info("传入参数 -> shopCode:" + shopCode + "  pageNum:" + pageNum + "  pageSize:" + pageSize);
        Response checkResult = checkParams(shopCode, pageNum, pageSize);
        if (!checkResult.isSuccess()) {
            log.error(checkResult.getResultObject());
            return new Response<>(false, "参数校验不通过!", "参数校验不通过!");
        }
        List<SwiftPassMessage> swiftPassMessageList = scanPaymentMessageService.getSwiftPassMessageList(getSwiftPassMessageQuery(shopCode, pageNum, pageSize));
        long totalCount = scanPaymentMessageService.querySwiftPassMessageTotalSize(getSwiftPassMessageQuery(shopCode, 0, 0));
        if (totalCount <= 0) {
            log.info("scanPaymentMessageService.querySwiftPassMessageTotalSize(getSwiftPassMessageQuery(shopCode, 0, 0)); -> 查询总数异常!");
            return new Response<>(false, "查询总数异常!", "查询总数异常!");
        }
        return new Response<>(true, new SwiftPassReturnQuery(totalCount, getSwiftPassReturnDtoList(swiftPassMessageList)));
    }

    /**
     * 更新收钱码流水
     *
     * @param swiftPassReturnDto
     * @return
     */
    @Override
    public Response<Boolean> updateSwiftPassMessage(SwiftPassReturnDto swiftPassReturnDto) {
        log.info("更新收钱码流水 -> 传入参数:swiftPassReturnDto:" + JSONObject.toJSONString(swiftPassReturnDto));
        SwiftPassMessage swiftPassMessage = new SwiftPassMessage();
        try {
            BeanUtils.copyProperties(swiftPassMessage, swiftPassReturnDto);
            log.info("scanPaymentMessageService.updateSwiftPassMessage(swiftPassMessage) -> swiftPassMessage:" + JSONObject.toJSONString(swiftPassMessage));
            boolean success = scanPaymentMessageService.updateSwiftPassMessage(swiftPassMessage);
            log.info("更新收钱码流水 -> success:" + success);
            if (success) {
                return new Response<>(true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, false);
        }
        return new Response<>(false, false);
    }

    /**
     * 给数据中心发消息
     *
     * @param swiftPassMessage
     */
    private void sendMessageToDc(SwiftPassMessage swiftPassMessage) {
        dcMessage.setKeys(swiftPassMessage.getOut_trade_no());
        dcMessage.setBody(getMessageBody(swiftPassMessage).getBytes());
        swiftPassMessage.setSendDcMqResult("1");
        try {
            SendResult sendResult = dcProducer.getDefaultMQProducer().send(dcMessage);
            if (sendResult != null && sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                swiftPassMessage.setSendDcMqResult("0");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        scanPaymentMessageService.updateSwiftPassMessage(swiftPassMessage);
    }

    /**
     * 组装messageBody
     *
     * @param swiftPassMessage
     * @return
     */
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
        log.info("给数据中心发消息 -> body -> " + JSONObject.toJSONString(mapS));
        return JSONObject.toJSONString(mapS);

    }

    /**
     * 组装时间
     *
     * @param time
     * @return
     */
    private static String getDateStr(String time) {
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

    /**
     * 组装返回集合
     *
     * @param swiftPassMessageList
     * @return
     */
    private List<SwiftPassReturnDto> getSwiftPassReturnDtoList(List<SwiftPassMessage> swiftPassMessageList) {
        List<SwiftPassReturnDto> swiftPassReturnDtoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(swiftPassMessageList)) {
            return swiftPassReturnDtoList;
        }
        for (SwiftPassMessage swiftPassMessage : swiftPassMessageList) {
            SwiftPassReturnDto swiftPassReturnDto = new SwiftPassReturnDto();
            try {
                BeanUtils.copyProperties(swiftPassReturnDto, swiftPassMessage);
                swiftPassReturnDtoList.add(swiftPassReturnDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return swiftPassReturnDtoList;
    }

    /**
     * 查询参数校验
     *
     * @param shopCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    private Response<String> checkParams(String shopCode, int pageNum, int pageSize) {
        if (StringUtils.isEmpty(shopCode)) {
            return new Response<>(false, "参数校验不通过-门店编号为空");
        }
        if (pageNum <= 0 || pageSize <= 0) {
            return new Response<>(false, "参数校验不通过-分页参数有误");
        }
        return new Response<>(true, "参数校验成功!");
    }

    /**
     * 组装分页查询参宿
     *
     * @param shopCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    private SwiftPassMessageQuery getSwiftPassMessageQuery(String shopCode, int pageNum, int pageSize) {
        SwiftPassMessageQuery swiftPassMessageQuery = new SwiftPassMessageQuery();
        if (StringUtils.isNotEmpty(shopCode)) {
            swiftPassMessageQuery.setMch_id(shopCode);
        }
        if (pageNum > 0) {
            swiftPassMessageQuery.setPageNum(pageNum);
        }
        if (pageSize > 0) {
            swiftPassMessageQuery.setPageSize(pageSize);
        }
        return swiftPassMessageQuery;
    }

    /**
     * 组装MQ消息
     *
     * @param swiftPassMessage
     * @return
     */
    private String getMqMessage(SwiftPassMessage swiftPassMessage) {
        Map<String, Object> mapS = new HashMap<>();
        mapS.put("shopCode", swiftPassMessage.getMch_id());
        mapS.put("amount", getAmountDouble(swiftPassMessage.getTotal_fee()));
        mapS.put("orderId", swiftPassMessage.getOut_trade_no());
        mapS.put("transId", swiftPassMessage.getOpenid());
        mapS.put("type", SWIFTPASS_TYPE);
        return JSONObject.toJSONString(mapS);
    }

    /**
     * 金额换算
     *
     * @param totalFee
     * @return
     */
    private double getAmountDouble(int totalFee) {
        String amount = new DecimalFormat("0.00").format((double) totalFee / 100);
        return Double.valueOf(amount);
    }

    /**
     * 发消息
     *
     * @param messageBody
     * @return
     */
    private SendResult sendMqMessage(String messageBody) {
        try {
            message.setBody(messageBody.getBytes());
            return producer.getDefaultMQProducer().send(this.message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
