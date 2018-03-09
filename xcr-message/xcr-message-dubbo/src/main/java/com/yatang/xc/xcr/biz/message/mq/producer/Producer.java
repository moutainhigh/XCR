package com.yatang.xc.xcr.biz.message.mq.producer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 生产者
 * Created by wangyang on 2017/9/29.
 */
public class Producer {

    private final Log log = LogFactory.getLog(this.getClass());

    private DefaultMQProducer defaultMQProducer;
    private String producerGroup;
    private String namesrvAddr;
    private String instanceName;

    public void init() throws MQClientException {
        // 参数信息
        log.info("DefaultMQProducer initialize!");
        log.info(producerGroup);
        log.info(namesrvAddr);
        log.info(instanceName);
        // 初始化
        defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setInstanceName(instanceName);
        defaultMQProducer.start();
        log.info("DefaultMQProudcer start success!");

    }

    public void destroy() {
        defaultMQProducer.shutdown();
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }


    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
