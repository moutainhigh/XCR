package com.yatang.xc.xcr.biz.message.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.busi.common.resp.Response;
import com.busi.common.utils.DateUtils;
import com.yatang.xc.xcr.biz.message.dto.MqPushDTO;
import com.yatang.xc.xcr.biz.message.dto.RateLimit;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnDto;
import com.yatang.xc.xcr.biz.message.dubboservice.JpushMsgDubboService;
import com.yatang.xc.xcr.biz.message.dubboservice.SwiftPassDubboService;
import com.yatang.xc.xcr.biz.message.service.RedisService;
import com.yatang.xc.xcr.biz.message.util.PropUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息定向发送消费者 Created by wangyang on 2017/7/4.
 */
@Component
public class MessageConsumer extends BaseConsumer implements ApplicationContextAware {
    private final Log logger = LogFactory.getLog(this.getClass());

    private final static String mqUrl = PropUtil.use("mq.properties").get("mq.rocketmq.url");
    private final static String consumerName = PropUtil.use("mq.properties").get("mq.consumer.name");
    private final static String topic = PropUtil.use("mq.properties").get("mq.consumer.topic");
    private final static String tags = PropUtil.use("mq.properties").get("mq.consumer.tags");
    private final static String instanceName = PropUtil.use("mq.properties").get("mq.instance.name");

    @Autowired
    private RateLimit rateLimit;

    @Autowired
    private JpushMsgDubboService jpushMsgDubboService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SwiftPassDubboService swiftPassDubboService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        logger.info("启动 MQ Consumer ... ");
        logger.info("mqUrl:" + mqUrl);
        logger.info("consumerName:" + consumerName);
        logger.info("topic:" + topic);
        logger.info("tags:" + tags);

        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerName);
            consumer.setNamesrvAddr(mqUrl);
            consumer.subscribe(topic, tags);
            consumer.setInstanceName(instanceName);

            /**
             * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
             * 如果非第一次启动，那么按照上次消费的位置继续消费
             */
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            logger.info("MqConsumer start listening topic:" + topic + "  tags:" + tags);
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                /**
                 * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                 */
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                ConsumeConcurrentlyContext context) {
                    logger.info("有新消息 来了  ... ");
                    MessageExt msg = msgs.get(0);
                    String body = new String(msg.getBody());
                    logger.info("接收到的 messagebody：" + body);
                    if (StringUtils.isEmpty(body)) {
                        logger.info("消息体为空!!!");
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    MqPushDTO mqPushDTO;
                    try {
                        mqPushDTO = JSONObject.parseObject(body, MqPushDTO.class);
                        if (mqPushDTO == null) {
                            logger.error("消息解析异常 -> body:" + body);
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    } catch (Exception e) {
                        logger.error("消息解析异常 -> body:" + body);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    logger.info("消息解析成功 -> mqPushDTO:" + mqPushDTO);

                    //组装title
                    String title = getPushTitle(mqPushDTO);
                    int type = mqPushDTO.getType();
                    if (type != PushTitleType.WFT_MESSAGE.getCode() && type != PushTitleType.WAITING_LIST.getCode()) {
                        if (StringUtils.isEmpty(title)) {
                            logger.error("组装title 异常 -> title:" + title);
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
                    logger.info("组装title 完成 -> title:" + title);

                    String content = getPushContent(mqPushDTO);
                    if (StringUtils.isEmpty(content)) {
                        logger.error("content 异常 -> content:" + content);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    logger.info("组装content 完成 -> content:" + content);

                    //组装推送参数
                    Map<String, String> extras = getExtras(mqPushDTO);
                    if (CollectionUtils.isEmpty(extras)) {
                        logger.error("组装推送参数 异常 -> extras:" + extras);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    logger.info("组装推送参数 完成 -> extras:" + extras);

                    //获取推送标识
                    List<String> regesterIdList = getRegesterIdByShopCode(mqPushDTO.getShopCode());
                    if (CollectionUtils.isEmpty(regesterIdList)) {
                        logger.error("没有获取到推送地址 !!! regesterIdList == null or size == 0 !mqPushDTO:" + mqPushDTO);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    // 消息定向推送
                    Response response = jpushMsgDubboService.sendPushByRegesterId(regesterIdList, title, content, extras);
                    logger.info("推送结果 -> " + JSONObject.toJSONString(response));
                    // 更新流水消息发送时间
                    if (response != null && response.isSuccess()) {
                        updateSwiftPassMessage(mqPushDTO);
                    }
                    logger.info("限速配置 -> rateLimit:" + JSONObject.toJSONString(rateLimit));
                    long time = redisService.checkSpeedBylimiter(rateLimit);
                    if (time > 0) {
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            /**
             * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
             */
            consumer.start();
            logger.info("Consumer Started.");
        } catch (MQClientException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 更新收钱码流水消息发送时间
     *
     * @param mqPushDTO
     * @return
     */
    private boolean updateSwiftPassMessage(MqPushDTO mqPushDTO) {
        if (mqPushDTO.getType() != PushTitleType.WFT_MESSAGE.getCode()) {
            return false;
        }
        SwiftPassReturnDto swiftPassReturnDto = new SwiftPassReturnDto();
        swiftPassReturnDto.setOut_trade_no(mqPushDTO.getOrderId());
        swiftPassReturnDto.setPushSendTime(DateUtils.dateDefaultFormat(new Date()));
        logger.info("更新收钱码流水 -> swiftPassReturnDto:" + JSONObject.toJSONString(swiftPassReturnDto));
        Response response = swiftPassDubboService.updateSwiftPassMessage(swiftPassReturnDto);
        logger.info("更新收钱码流水 -> response:" + JSONObject.toJSONString(response));
        return response != null && response.isSuccess();
    }

    /**
     * 组装推送参数
     *
     * @param mqPushDTO
     * @return
     */
    private Map<String, String> getExtras(MqPushDTO mqPushDTO) {
        Map<String, String> extras = new HashMap<>();
        int type = mqPushDTO.getType();
        String orderID = mqPushDTO.getOrderId();
        String transId = mqPushDTO.getTransId();
        String transContent = getTransContent(mqPushDTO);
        extras.put("OrderNo", orderID == null ? "" : orderID);
        if (type == PushTitleType.ORDER_RECEIVING.getCode()) {
            extras.put("Type", "1");
            return extras;
        }
        if (type == PushTitleType.EXPRESS_RECEIVING.getCode()) {
            extras.put("Type", "2");
            return extras;
        }
        if (type == PushTitleType.EXPRESS_REFUSE.getCode()) {
            extras.put("Type", "3");
            return extras;
        }
        if (type == PushTitleType.SALES_RETURN.getCode()) {
            extras.put("Type", "4");
            String cancleId = mqPushDTO.getCancelId();
            extras.put("CancelId", cancleId == null ? "" : cancleId);
            return extras;
        }
        if (type == PushTitleType.WFT_MESSAGE.getCode()) {
            extras.put("Type", "6");
            extras.put("TransId", transId == null ? "" : transId);
            extras.put("TransContent", transContent);
            return extras;
        }
        if (type == PushTitleType.WAITING_LIST.getCode()) {
            extras.put("Type", "7");
            return extras;
        }
        extras.put("Type", "5");
        return extras;
    }

    /**
     * 获取收钱码语音
     *
     * @param mqPushDTO
     * @return
     */
    private String getTransContent(MqPushDTO mqPushDTO) {
        String amountStr = getAmountStr(mqPushDTO.getAmount());
        return TransContentType.WFT_TRANS_CONTENT.getInfo().replace("{1}", amountStr);
    }

    /**
     * 组装Title
     *
     * @param mqPushDTO
     * @return
     */
    private String getPushTitle(MqPushDTO mqPushDTO) {
        int type = mqPushDTO.getType();
        String amountStr = getAmountStr(mqPushDTO.getAmount());
        String orderId = "#" + mqPushDTO.getOrder_number();

        if (type == PushTitleType.ORDER_RECEIVING.getCode()) {
            return PushTitleType.ORDER_RECEIVING.getInfo().replace("{1}", amountStr);
        }
        if (type == PushTitleType.EXPRESS_RECEIVING.getCode()) {
            return PushTitleType.EXPRESS_RECEIVING.getInfo().replace("{1}", orderId);
        }
        if (type == PushTitleType.ORDER_CANCEL.getCode()) {
            return PushTitleType.ORDER_CANCEL.getInfo().replace("{1}", orderId);
        }
        if (type == PushTitleType.ORDER_OVERTIME.getCode()) {
            return PushTitleType.ORDER_OVERTIME.getInfo().replace("{1}", orderId);
        }
        if (type == PushTitleType.EXPRESS_DONE.getCode()) {
            return PushTitleType.EXPRESS_DONE.getInfo().replace("{1}", orderId);
        }
        if (type == PushTitleType.SALES_RETURN.getCode()) {
            return PushTitleType.SALES_RETURN.getInfo().replace("{1}", amountStr);
        }
        if (type == PushTitleType.EXPRESS_REFUSE.getCode()) {
            return PushTitleType.EXPRESS_REFUSE.getInfo();
        }
        if (type == PushTitleType.WFT_MESSAGE.getCode()) {
            return PushTitleType.WFT_MESSAGE.getInfo();
        }
        if (type == PushTitleType.WAITING_LIST.getCode()) {
            return PushTitleType.WAITING_LIST.getInfo();
        }
        return null;
    }

    /**
     * 组装content
     *
     * @param mqPushDTO
     * @return
     */
    private String getPushContent(MqPushDTO mqPushDTO) {
        int type = mqPushDTO.getType();
        String amountStr = getAmountStr(mqPushDTO.getAmount());
        String orderId = "#" + mqPushDTO.getOrder_number();
        String shippingMethod = mqPushDTO.getShippingMethod();
        String shippingAddress = mqPushDTO.getShippingAddress();
        if (StringUtils.isEmpty(shippingMethod)) {
            shippingMethod = "";
        }
        if (StringUtils.isEmpty(shippingAddress)) {
            shippingAddress = "";
        }
        if (type == PushContentType.ORDER_RECEIVING_CONTENT.getCode()) {
            logger.info("type:" + type + "   shippingMethod:" + shippingMethod + "  shippingAddress:" + shippingAddress);

            return PushContentType.ORDER_RECEIVING_CONTENT.getInfo().replace("{2}", shippingMethod).replace("{3}", shippingAddress);
        }
        if (type == PushContentType.EXPRESS_RECEIVING_CONTENT.getCode()) {
            return PushContentType.EXPRESS_RECEIVING_CONTENT.getInfo();
        }
        if (type == PushContentType.ORDER_CANCEL_CONTENT.getCode()) {
            logger.info("type:" + type + "   shippingMethod:" + shippingMethod + "  amountStr:" + amountStr);
            return PushContentType.ORDER_CANCEL_CONTENT.getInfo().replace("{2}", shippingMethod).replace("{3}", amountStr);
        }
        if (type == PushContentType.ORDER_OVERTIME_CONTENT.getCode()) {
            logger.info("type:" + type + "  shippingMethod:" + shippingMethod + " amountStr:" + amountStr);
            return PushContentType.ORDER_OVERTIME_CONTENT.getInfo().replace("{2}", shippingMethod).replace("{3}", amountStr);
        }
        if (type == PushContentType.EXPRESS_DONE_CONTENT.getCode()) {
            logger.info("type:" + type + "  amountStr:" + amountStr);
            return PushContentType.EXPRESS_DONE_CONTENT.getInfo().replace("{2}", amountStr);
        }
        if (type == PushContentType.SALES_RETURN_CONTENT.getCode()) {
            StringBuilder products = new StringBuilder();
            List<String> productList = mqPushDTO.getReturnProducts();
            if (!CollectionUtils.isEmpty(productList)) {
                for (String product : productList) {
                    products.append(product);
                    products.append(",");
                }
                String productStr = products.toString();
                return PushContentType.SALES_RETURN_CONTENT.getInfo().replace("{2}", productStr.substring(0, productStr.length() - 1));
            }
        }
        if (type == PushContentType.EXPRESS_REFUSE_CONTENT.getCode()) {
            return PushContentType.EXPRESS_REFUSE_CONTENT.getInfo().replace("{1}", orderId).replace("{2}", shippingAddress);
        }
        if (type == PushContentType.WFT_MESSAGE_COMTENT.getCode()) {
            return PushContentType.WFT_MESSAGE_COMTENT.getInfo().replace("{1}", amountStr);
        }
        if (type == PushContentType.WAITING_LIST_COMTENT.getCode()) {
            return PushContentType.WAITING_LIST_COMTENT.getInfo().replace("{1}", orderId).replace("{2}", shippingMethod).replace("{3}", shippingAddress);
        }
        return null;
    }

    /**
     * 金额换算
     *
     * @param amount
     * @return
     */
    private String getAmountStr(double amount) {
        return new DecimalFormat("######0.00").format(amount);
    }


    /**
     * 推送TITLE枚举
     */
    private enum PushTitleType {

        ORDER_RECEIVING(1, "您有一笔{1}元的外送订单"),                   //1订单接单提醒
        EXPRESS_RECEIVING(2, "{1}订单已被蜂鸟接单"),   //2蜂鸟配送接单提醒
        ORDER_CANCEL(3, "{1}订单已被用户取消"),                //3用户取消订单提示
        ORDER_OVERTIME(4, "{1}订单超时未接单自动取消"),         //4超时未接单提示
        EXPRESS_DONE(5, "{1}订单已配送完成"),                  //5蜂鸟配送完成提醒
        SALES_RETURN(6, "用户发起了一笔{1}元退货单"),                 //6新退货单提醒
        EXPRESS_REFUSE(7, "很抱歉，蜂鸟无法配送该订单"),       //7蜂鸟配送拒单提醒
        WFT_MESSAGE(8, ""),                               //8威富通没有消息title
        WAITING_LIST(9, "");                               //9待结单提醒没有消息title


        private int code;
        private String info;

        PushTitleType(int code, String info) {
            this.code = code;
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

    }

    /**
     * 推送CONTENT枚举
     */
    private enum PushContentType {
        ORDER_RECEIVING_CONTENT(1, "【{2}】{3}"),                   //1订单接单提醒
        EXPRESS_RECEIVING_CONTENT(2, "请准备好商品，配送员正在飞奔而来"),   //2蜂鸟配送接单提醒
        ORDER_CANCEL_CONTENT(3, "【{2}】订单金额{3}元"),                //3用户取消订单提示
        ORDER_OVERTIME_CONTENT(4, "【{2}】订单金额{3}元"),         //4超时未接单提示
        EXPRESS_DONE_CONTENT(5, "恭喜又获得了{2}元收入"),                  //5蜂鸟配送完成提醒
        SALES_RETURN_CONTENT(6, "退货商品[{2}]"),                 //6新退货单提醒
        EXPRESS_REFUSE_CONTENT(7, "{1}订单，配送地址[{2}]"),       //7蜂鸟配送拒单提醒
        WFT_MESSAGE_COMTENT(8, "收钱码入账{1}元"),                 //8威富通
        WAITING_LIST_COMTENT(9, "{1}订单暂未接单，请及时处理【{2}】{3}");     //9待结单提醒
        private int code;
        private String info;

        PushContentType(int code, String info) {
            this.code = code;
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

    }

    /**
     * 收钱码语音文本
     */
    private enum TransContentType {
        WFT_TRANS_CONTENT(8, "雅堂小超到账{1}元");                 //8威富通语音
        private int code;
        private String info;

        TransContentType(int code, String info) {
            this.code = code;
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

    }

}
