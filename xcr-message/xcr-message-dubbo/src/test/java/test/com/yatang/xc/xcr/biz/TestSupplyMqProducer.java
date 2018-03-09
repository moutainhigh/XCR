package test.com.yatang.xc.xcr.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyang on 2017/12/1.
 */
public class TestSupplyMqProducer {


    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {


        /**
         private String title;     //消息标题
         private String content;   //消息内容
         private Boolean status;   //心愿结果 true：已完成(跳转)  false：已关闭(不跳转)
         private String productDetailUrl;       //跳转链接
         private List<String> storeIds;  //门店集合

         */
        Map<String, Object> mapS = new HashMap<>();
        mapS.put("title", "小超人供应链");
        mapS.put("content", "小超人供应链发送的消息");
        mapS.put("status", true);
        mapS.put("productDetailUrl", "http://www.baidu.com");
        mapS.put("storeIds", new ArrayList<String>() {
            private static final long serialVersionUID = -5446324763251050971L;

            {
                add("A061749");
                add("A901004");
                add("Z001584");
            }
        });
        String msgS = JSONObject.toJSONString(mapS);

        /**
         * sit ->
         *
         mq.supply.consumer.name=xcr-supply-consumer
         mq.supply.consumer.topic=c-xcr-supply
         mq.supply.consumer.tags=XCRC-SUPPLY
         mq.supply.instance.name=XCSupplyInstance
         */
        final DefaultMQProducer producer = new DefaultMQProducer("xcrSupplyProducer");
        producer.setNamesrvAddr("uat.mq.com:9876");
//        producer.setNamesrvAddr("172.30.10.50:9876");
        producer.setInstanceName("XCSupplyInstance");
        producer.start();

        Message msg = new Message("c-xcr-supply", "XCRC-SUPPLY", "SUPPLY_22", msgS.getBytes());
        System.out.println(msg.toString());
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);

        producer.shutdown();
        System.exit(0);


    }

}
