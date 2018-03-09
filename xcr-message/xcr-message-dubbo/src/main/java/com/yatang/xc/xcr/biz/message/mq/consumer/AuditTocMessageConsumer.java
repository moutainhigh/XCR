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
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgDubboService;
import com.yatang.xc.xcr.biz.message.dto.AuditResultDTO;
import com.yatang.xc.xcr.biz.message.dto.RateLimit;
import com.yatang.xc.xcr.biz.message.dubboservice.JpushMsgDubboService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * C端店铺审核Consumer
 * <p>
 * Created by wangyang on 2017/11/29.
 */
@Component
public class AuditTocMessageConsumer extends BaseConsumer implements ApplicationContextAware {

    private final Log logger = LogFactory.getLog(this.getClass());

    private final static String mqUrl = PropUtil.use("mq.properties").get("mq.rocketmq.url");
    private final static String consumerName = PropUtil.use("mq.properties").get("mq.toc.consumer.name");
    private final static String topic = PropUtil.use("mq.properties").get("mq.toc.consumer.topic");
    private final static String tags = PropUtil.use("mq.properties").get("mq.toc.consumer.tags");
    private final static String instanceName = PropUtil.use("mq.properties").get("mq.toc.instance.name");


    private static final String AUDIT_PASS = "审核通过";
    private static final String INTORDUCE_AUDIT_PASS = "店铺介绍审核通过，发布成功";
    private static final String INTORDUCE_NON_APPROVAL = "店铺介绍审核未通过，请重新修改发布";

    private static final String NOTICE_AUDIT_PASS = "店铺公告审核通过，发布成功";
    private static final String NOTICE_NON_APPROVAL = "店铺公告审核未通过，请重新修改发布";

    private static final String BANK_PIC_AUDIT_PASS = "银行卡信息审核通过，请耐心等待平台办理手续！";
    private static final String BANK_PIC_NON_APPROVAL = "银行卡信息审核未通过";

    private static final String AUDIT_TYPE = "1";//消息类型0:有图有跳转1:无图不跳转
    private static final String AUDIT_STATE_SEND = "1"; //0:未发布1:已发布

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

        try {
            DefaultMQPushConsumer auditTocMQPushConsumer = new DefaultMQPushConsumer(consumerName);
            auditTocMQPushConsumer.setNamesrvAddr(mqUrl);
            auditTocMQPushConsumer.subscribe(topic, tags);
            auditTocMQPushConsumer.setInstanceName(instanceName);

            auditTocMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            auditTocMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                ConsumeConcurrentlyContext context) {


                    MessageExt msg = msgs.get(0);
                    String body = new String(msg.getBody());
                    logger.info("接收到的 messagebody：" + body);
                    if (StringUtils.isEmpty(body)) {
                        logger.info("消息体为空!!!");
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    AuditResultDTO auditResultDTO = JSONObject.parseObject(body, AuditResultDTO.class);
                    if (auditResultDTO == null) {
                        logger.error("消息解析异常 -> body:" + body);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    logger.info("消息解析结果:" + JSONObject.toJSONString(auditResultDTO));

                    saveMessage(auditResultDTO);
                    List<String> regesterIdList = getRegesterIdByShopCode(auditResultDTO.getShopCode());
                    // 消息定向推送
                    String title = getTitle(auditResultDTO);
                    String content = getContent(auditResultDTO);
                    Map<String, String> extras = getExtras();
                    logger.info("消息title：" + title);
                    logger.info("消息content：" + content);
                    logger.info("消息regesterIdList：" + JSONObject.toJSONString(regesterIdList));
                    logger.info("消息extras：" + JSONObject.toJSONString(extras));
                    Response res = jpushMsgDubboService.sendPushByRegesterId(regesterIdList, title, content, extras);
                    logger.info("推送结果 -> " + JSONObject.toJSONString(res));
                    logger.info("限速配置 -> rateLimit:" + JSONObject.toJSONString(rateLimit));
                    long time = redisService.checkSpeedBylimiter(rateLimit);
                    if (time > 0) {
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            logger.info("errorMessage:" + e.getMessage());
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            auditTocMQPushConsumer.start();
            logger.info("Consumer Started.");
        } catch (MQClientException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 消息存库
     *
     * @param auditResultDTO
     */
    private void saveMessage(AuditResultDTO auditResultDTO) {
        MsgPushDTO msgPushDTO = new MsgPushDTO();
        msgPushDTO.setType(AUDIT_TYPE);
        msgPushDTO.setStatus(AUDIT_STATE_SEND);
        msgPushDTO.setCreateUid(0);
        msgPushDTO.setPushType("1"); //0：所有，1：定向，2:区域
        msgPushDTO.setPushTo(auditResultDTO.getShopCode());
        msgPushDTO.setTitle(getTitle(auditResultDTO));
        msgPushDTO.setCreateTime(new Date());
        msgPushDTO.setModifyTime(new Date());
        msgPushDTO.setReleasesTime(new Date());
        msgPushDTO.setContentHtml(auditResultDTO.getExplain());
        msgPushDTO.setShopNo(auditResultDTO.getShopCode());
        logger.info("审核消息存库 -> msgPushDTO:" + JSONObject.toJSONString(msgPushDTO));
        Response<MsgPushDTO> response = msgDubboService.editMsg(msgPushDTO);
        logger.info("审核消息存库 -> editMsg:" + response.isSuccess());
    }

    /**
     * 获取消息title
     * result: 0:审核通过   1：审核不通过
     * auditType: 1:店铺内容  2：店铺公告
     *
     * @param auditResultDTO
     * @return
     */
    private String getTitle(AuditResultDTO auditResultDTO) {
        Integer result = auditResultDTO.getResult();
        Integer auditType = auditResultDTO.getAuditType();
        if (auditType == 1) {
            return result == 0 ? INTORDUCE_AUDIT_PASS : INTORDUCE_NON_APPROVAL;
        }
        if (auditType == 2) {
            return result == 0 ? NOTICE_AUDIT_PASS : NOTICE_NON_APPROVAL;
        }
        return result == 0 ? BANK_PIC_AUDIT_PASS : BANK_PIC_NON_APPROVAL;
    }

    /**
     * 获取消息内容
     *
     * @param auditResultDTO
     * @return
     */
    private String getContent(AuditResultDTO auditResultDTO) {
        Integer result = auditResultDTO.getResult();
        if (result == 1) {
            return auditResultDTO.getExplain();
        }
        return AUDIT_PASS;
    }

    /**
     * 消息类型 0普通消息
     *
     * @return
     */
    private Map<String, String> getExtras() {
        Map<String, String> extras = new HashMap<>();
        extras.put("Type", "0");
        return extras;
    }

}
