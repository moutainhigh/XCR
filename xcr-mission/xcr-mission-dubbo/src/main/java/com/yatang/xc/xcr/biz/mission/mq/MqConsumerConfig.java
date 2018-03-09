package com.yatang.xc.xcr.biz.mission.mq;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.busi.common.utils.SerializeUtil;
import com.yatang.xc.mbd.biz.bpm.dto.AuditDataDTO;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionBPMDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumAuditResult;

/**
 * Created by dunmengjun on 2017/3/29.
 */

@Component
public class MqConsumerConfig implements ApplicationContextAware {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private DefaultMQPushConsumer consumer;

    @Value("${mq.rocketmq.url}")
    private String mqUrl;

    @Value("${mq.consumer.name}")
    private String consumerName;

    @Value("${mq.consumer.topic}")
    private String topic;

    @Value("${mq.consumer.tags}")
    private String tags;

    private boolean enable = false;
    
    @Autowired
    private MissionBPMDubboService bpmService;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
    	//任务审核不上注释
    	if(!enable){
    		return;
    	}
        try {

            consumer = new DefaultMQPushConsumer(consumerName);
            consumer.setNamesrvAddr(mqUrl);
            consumer.subscribe(topic, tags);
            logger.info("MqConsumer start listening topic:" + topic + "  tags:" + tags);
            /**
             * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
             * 如果非第一次启动，那么按照上次消费的位置继续消费
             */
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                /**
                 * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                 */
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    MessageExt msg = msgs.get(0);
                    byte[] body = msg.getBody();
                    logger.info(Thread.currentThread().getName() + " Receive New Messages: " + body);
                    if (body != null) {
                        Object obj = SerializeUtil.unserialize(body);
                        if (!(obj instanceof AuditDataDTO)) {
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                        AuditDataDTO auditDataDTO = (AuditDataDTO) obj;
                        String bpmId = auditDataDTO.getApplyId();
                        String executeId = auditDataDTO.getIntentionId();
                        String type = auditDataDTO.getType();
                        String status = auditDataDTO.getProcessStatus();
                        String reason = auditDataDTO.getAuditMessage();
                        auditDataDTO.getProcessType();
                        logger.info(Thread.currentThread().getName() + " auditDataDTO" + JSON.toJSONString(auditDataDTO));
                        if ("1".equals(type)) {
                            bpmService.manualAuditMissionCallback(executeId, bpmId, EnumAuditResult.AUDIT_RESULT_REJECT.getCode(), reason);
                        } else if ("0".equals(type) && "end".equals(status)) {
                            bpmService.manualAuditMissionCallback(executeId, bpmId, EnumAuditResult.AUDIT_RESULT_APPROVE.getCode(), reason);
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    } else {
                        logger.info("消息体为空");
                    }
                    // 消费者向mq服务器返回消费成功的消息
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            });

            /**
             * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
             */
            consumer.start();
            logger.info("Consumer Started.");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
