package test.com.yatang.xc.xcr.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyang on 2017/12/1.
 */
public class TestAuditMqProducer {


    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {


        /**
         * {"shopCode":"A000006","result":0,"explain":"审核通过","auditType":2
         */
        Map<String, Object> mapS = new HashMap<>();
        mapS.put("shopCode", "A001701");
        mapS.put("result", "1");
        mapS.put("explain", "审核不通过");
        mapS.put("auditType", "2");
        String msgS = JSONObject.toJSONString(mapS);

        /**
         * sit ->
         *
         * mq.rocketmq.url=172.30.10.50:9876
         mq.toc.consumer.topic=c-xcr-audit
         mq.toc.consumer.tags=XCRC-AUDIT
         mq.toc.instance.name=XCAuditInstance
         */
        final DefaultMQProducer producer = new DefaultMQProducer("o2oXCProducer");
        producer.setNamesrvAddr("172.30.10.50:9876");
//        producer.setNamesrvAddr("uat.mq.com:9876");
        producer.setInstanceName("XCAuditInstance");
        producer.start();

        Message msg = new Message("c-xcr-audit", "XCRC-AUDIT", "audit_noticeA000006", msgS.getBytes());
        System.out.println(msg.toString());
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);

        producer.shutdown();
        System.exit(0);


    }

}
