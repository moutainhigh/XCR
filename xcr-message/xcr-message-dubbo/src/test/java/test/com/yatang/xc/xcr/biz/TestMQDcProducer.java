package test.com.yatang.xc.xcr.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMQDcProducer {


    private static final String PRD_URL = "prd.dcmq1.com:9876";
    private static final String SIT_URL = "172.30.10.50:9876";
    private static final String UAT_URL = "uat.mq.com:9876";

    private static final String TOPIC = "dc-xrc-order";
    private static final String TAGS = "qrcodeOrder";

    /**
     * mq.dc.open=false
     * mq.dc.producerGroup.name=qrcodeOrderGroupName
     * mq.dc.rocketmq.url=172.30.10.104:9876
     * mq.dc.instance.name=qrcodeOrderInstance
     * mq.dc.consumer.topic=dc-xrc-order
     * mq.dc.consumer.tags=qrcodeOrder
     *
     * @param args
     * @throws MQClientException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws Exception {

        List<String> xmlList = getXmlStrList("C:\\Users\\wangyang\\Desktop\\new.txt");
        if (CollectionUtils.isEmpty(xmlList)) {
            System.out.println("readFileByLines error");
            System.exit(0);
        }
        for (String s : xmlList) {
            Map<String, String> maps = getXmlParams(s);
            Map<String, Object> msgS = getMqMessageMap(maps);
            String msgJson = JSONObject.toJSONString(msgS);

            System.out.println(msgJson);

//            SendResult sendResult = sendMessage(msgJson, PRD_URL, TOPIC, TAGS, maps.get("transaction_id"));
//            System.out.println(JSONObject.toJSONString(sendResult));
        }
    }

    private static SendResult sendMessage(String msgJson, String url, String topic, String tags, String keys) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
        producer.setNamesrvAddr(url);
        producer.setInstanceName("qrcodeOrderInstance");
        producer.start();
        try {
            Message msg = new Message(topic, tags, keys, msgJson.getBytes());

            System.out.println(JSONObject.toJSONString(msg));

//            return producer.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }
        return null;
    }

    private static Map<String, Object> getMqMessageMap(Map<String, String> maps) {
        Map<String, Object> mapS = new HashMap<>();
        mapS.put("_id", maps.get("transaction_id"));
        mapS.put("haveShopCode", 1);
        mapS.put("mch_id", "A027623");
        mapS.put("openid", maps.get("openid"));
        mapS.put("out_trade_no", maps.get("out_trade_no"));
        mapS.put("trade_type", maps.get("trade_type"));
//        mapS.put("total_fee", getAmountDouble(Integer.valueOf(maps.get("total_fee"))));
//        mapS.put("time_end", getDateStr(maps.get("time_end")));
        mapS.put("total_fee", Integer.valueOf(maps.get("total_fee")));
        mapS.put("time_end", maps.get("time_end"));
        return mapS;
    }


    private static List<String> getXmlStrList(String filePath) {
        return readFileByLines(filePath);
    }

    private static Map<String, String> getXmlParams(String xmlStr) throws Exception {
        return toMap(xmlStr.getBytes(), "UTF-8");
    }


    private static List<String> readFileByLines(String fileName) {
        List<String> xmlList = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                xmlList.add(tempString);
            }
            reader.close();
            return xmlList;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return xmlList;
    }

    private static Map<String, String> toMap(byte[] xmlBytes, String charset) throws Exception {
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding(charset);
        Document doc = reader.read(source);
        return toMap(doc.getRootElement());
    }

    private static Map<String, String> toMap(Element element) {
        Map<String, String> rest = new HashMap<>();
        List<Element> els = element.elements();
        for (Element el : els) {
            rest.put(el.getName().toLowerCase(), el.getTextTrim());
        }
        return rest;
    }


    /**
     * 金额换算
     *
     * @param totalFee
     * @return
     */
    private static double getAmountDouble(int totalFee) {
        String amount = new DecimalFormat("0.00").format((double) totalFee / 100);
        return Double.valueOf(amount);
    }

    private static String getDateStr(String time) {
        if (time.length() != 14) {
            return null;
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
