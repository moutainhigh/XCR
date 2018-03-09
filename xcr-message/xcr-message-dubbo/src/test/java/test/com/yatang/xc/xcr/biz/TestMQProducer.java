package test.com.yatang.xc.xcr.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestMQProducer {

    /**
     * mq.rocketmq.url=172.30.10.204:9876 mq.consumer.name=xcr-consumer
     * mq.consumer.topic=corder mq.consumer.tags=XCRC-ORDER
     */

    public static void main(String[] args) throws MQClientException, InterruptedException {
        /**
         * type:
         *   1订单接单提醒
         *   2蜂鸟配送接单提醒
         *   3用户取消订单提示
         *   4超时未接单提示
         *   5蜂鸟配送完成提醒
         *   6新退货单提醒
         *   7蜂鸟配送拒单提醒
         *
         *   8威富通消息
         *
         *   9 五分钟未接单提醒
         */
        Map<String, Object> mapS = new HashMap<>();
        mapS.put("shopCode", "A001592");
        mapS.put("amount", "40000.00");
        mapS.put("orderId", "uat-Z0009990000476");
        mapS.put("cancelId", ""); //退货ID，做新退货推送跳转
        mapS.put("shippingMethod", "蜂鸟配送");
        mapS.put("order_number", "15");
        mapS.put("shippingAddress", "广东省深圳市南山区平山一路");
        mapS.put("returnProducts", new ArrayList<>(Arrays.asList("商品1", "商品2", "商品3")));
        mapS.put("type", 4);
        String msgS = JSONObject.toJSONString(mapS);

        /**
         * sit ->
         *
         * mq.rocketmq.url=172.30.10.50:9876
         mq.consumer.name=xcr-consumer
         mq.consumer.topic=corder
         mq.consumer.tags=XCRC-ORDER
         mq.instance.name=XCOrderInstance
         */
        final DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
//        producer.setNamesrvAddr("uat.mq.com:9876");  //uat
//        producer.setNamesrvAddr("prd.mq.com:9876"); //prd
        producer.setNamesrvAddr("172.30.10.50:9876"); //sit
        producer.setInstanceName("XCOrderInstance");
        producer.start();
        try {
            Message msg = new Message("corder", "XCRC-ORDER", "XCR-MQ", msgS.getBytes());
            System.out.println(msg.toString());
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();
        System.exit(0);
    }
}
