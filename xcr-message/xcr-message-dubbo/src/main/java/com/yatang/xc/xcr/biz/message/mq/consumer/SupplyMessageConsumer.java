package com.yatang.xc.xcr.biz.message.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.busi.common.resp.Response;
import com.busi.common.utils.StringUtils;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgDubboService;
import com.yatang.xc.xcr.biz.message.dto.AuditResultDTO;
import com.yatang.xc.xcr.biz.message.dto.RateLimit;
import com.yatang.xc.xcr.biz.message.dto.SupplyPushDTO;
import com.yatang.xc.xcr.biz.message.dubboservice.JpushMsgDubboService;
import com.yatang.xc.xcr.biz.message.service.RedisService;
import com.yatang.xc.xcr.biz.message.util.PropUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应链消息
 * Created by wangyang on 2018/1/4.
 */
@Component
public class SupplyMessageConsumer extends BaseConsumer implements ApplicationContextAware {

    private final Log logger = LogFactory.getLog(this.getClass());

    private final static String mqUrl = PropUtil.use("mq.properties").get("mq.rocketmq.url");
    private final static String consumerName = PropUtil.use("mq.properties").get("mq.supply.consumer.name");
    private final static String topic = PropUtil.use("mq.properties").get("mq.supply.consumer.topic");
    private final static String tags = PropUtil.use("mq.properties").get("mq.supply.consumer.tags");


    @Autowired
    private RateLimit rateLimit;
    @Autowired
    private JpushMsgDubboService jpushMsgDubboService;
    @Autowired
    private MsgDubboService msgDubboService;
    @Autowired
    private RedisService redisService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        logger.info("【供应链消息】 MQ Consumer start ... ");
        logger.info("【供应链消息】 MQ Consumer mqUrl:" + mqUrl);
        logger.info("【供应链消息】 MQ Consumer consumerName:" + consumerName);
        logger.info("【供应链消息】 MQ Consumer topic:" + topic);
        logger.info("【供应链消息】 MQ Consumer tags:" + tags);
        try {
            DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer(consumerName);
            mqPushConsumer.setNamesrvAddr(mqUrl);
            mqPushConsumer.subscribe(topic, tags);
            mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            mqPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                ConsumeConcurrentlyContext context) {
                    MessageExt msg = msgs.get(0);
                    String body = new String(msg.getBody());
                    logger.info("【供应链消息】接收到的 messagebody：" + body);
                    SupplyPushDTO supplyPushDTO = JSONObject.parseObject(body, SupplyPushDTO.class);
                    if (supplyPushDTO == null) {
                        logger.error("消息解析异常 -> body:" + body);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    logger.info("消息解析结果:" + JSONObject.toJSONString(supplyPushDTO));
                    List<String> regesterIds = getRegesterIdByShopCodeList(supplyPushDTO.getStoreIds());
                    if (CollectionUtils.isEmpty(regesterIds)) {
                        logger.error("没有获取到推送地址 ... regesterIds:" + JSONObject.toJSONString(regesterIds));
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    String title = getTitle(supplyPushDTO.getTitle());
                    String content = supplyPushDTO.getContent();
                    Map<String, String> extras = getExtras(supplyPushDTO);

                    logger.info("消息title：" + title);
                    logger.info("消息content：" + content);
                    logger.info("消息regesterIdList：" + JSONObject.toJSONString(regesterIds));
                    logger.info("消息extras：" + JSONObject.toJSONString(extras));
                    Response res = jpushMsgDubboService.sendPushByRegesterId(regesterIds, title, content, extras);
                    logger.info("推送结果 -> " + JSONObject.toJSONString(res));
                    logger.info("限速配置 -> rateLimit:" + JSONObject.toJSONString(rateLimit));

                    saveMessage(supplyPushDTO);
                    long time = redisService.checkSpeedBylimiter(rateLimit);
                    if (time > 0) {
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

            });
            mqPushConsumer.start();
            logger.info("Consumer Started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取通知title
     *
     * @param title
     * @return
     */
    private String getTitle(String title) {
        return StringUtils.isEmpty(title) ? "小超人" : title;
    }

    /**
     * 组装跳转链接
     *
     * @param supplyPushDTO
     * @return
     */
    private Map<String, String> getExtras(SupplyPushDTO supplyPushDTO) {
        Map<String, String> extras = new HashMap<>();
        Boolean status = supplyPushDTO.getStatus();
        extras.put("Type", "0");
        extras.put("AdUrl", status != null && status ? supplyPushDTO.getProductDetailUrl() : "");
        return extras;
    }

    /**
     * 保存消息(在消息列表中展示)
     *
     * @param SupplyPushDTO
     */
    private void saveMessage(SupplyPushDTO SupplyPushDTO) {
        MsgPushDTO msgPushDTO = new MsgPushDTO();
        msgPushDTO.setType("3"); //0:有图有跳转1:无图不跳转3:无图有跳转
        msgPushDTO.setStatus("1"); //0未发布,1已发布
        msgPushDTO.setCreateUid(0);
        msgPushDTO.setPushType("1"); //0：所有，1：定向，2:区域
        msgPushDTO.setCreateTime(new Date());
        msgPushDTO.setModifyTime(new Date());
        msgPushDTO.setReleasesTime(new Date());
        msgPushDTO.setContentFrom("-1");//-1链接，1编辑内容
        msgPushDTO.setMsgUrl(SupplyPushDTO.getProductDetailUrl());
        msgPushDTO.setTitle(SupplyPushDTO.getTitle());
        msgPushDTO.setContentHtml(SupplyPushDTO.getContent());
        List<String> shopCodeList = SupplyPushDTO.getStoreIds();
        for (String shopCode : shopCodeList) {
            msgPushDTO.setPushTo(shopCode);
            msgPushDTO.setShopNo(shopCode);
            logger.info("消息存库 -> msgPushDTO:" + JSONObject.toJSONString(msgPushDTO));
            Response<MsgPushDTO> response = msgDubboService.editMsg(msgPushDTO);
            logger.info("消息存库 -> editMsg:" + response.isSuccess());
        }
    }
}
