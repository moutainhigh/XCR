package test.com.yatang.xc.xcr.biz;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.message.dto.MqPushDTO;
import com.yatang.xc.xcr.biz.message.dubboservice.JpushMsgDubboService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 推送测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test/applicationContext-test.xml"})
public class TestJpushMsgDubboService {
    @Autowired
    private JpushMsgDubboService service;


    @Test
    public void testPushAll() {

        System.err.println("*******开始执行*******");
        Map<String, String> extras = new HashMap<>();
        extras.put("Type", "0");
        extras.put("OrderNo", "0000001");
        Response res = service.sendPushAll("小超人", "小超人javaSDK-TEST0420推送测试32", extras);
        System.out.println(res.isSuccess());
    }

    @Test
    public void testPushMsgAll() {
        Response res = service.sendPushMsgAll("小超人", "小超人javaSDK全平台消息推送");
        System.out.println(res.isSuccess());
    }

    @Test
    public void testPushByDeviceId() {

        MqPushDTO dto = new MqPushDTO();
        dto.setAmount(66.66);
        dto.setOrderId("sitA0015920002919");
        dto.setOrder_number("#1");
        dto.setCancelId("S00001303");
        dto.setShippingAddress("一个地方");
        dto.setShippingMethod("怎么买的");
        dto.setShopCode("A001592");
        dto.setTransId("102513761628201709274228294279");
        dto.setTransContent("收钱码入账66.66元");
        dto.setReturnProducts(new ArrayList<>(Arrays.asList("商品1", "商品2", "商品3")));

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
         *   9待结单提示
         */
        dto.setType(8);

        String title = getPushTitle(dto);
        String content = getPushContent(dto);


        Map<String, String> extars = getExtras(dto);
        System.out.println(title);
        System.out.println(content);
        System.out.println(extars);

        Response res = service.sendPushByRegesterId(getDeviceIds("何敏"), title, content, extars);
        System.out.println(res.isSuccess());
        System.out.println(res.getResultObject());
        System.out.println(res.getCode());
    }

    @Test
    public void test3() {
        String title = "店铺介绍审核通过，发布成功";
        String content = "审核通过";
        Map<String, String> extras = new HashMap<>();
        extras.put("Type", "0");
        System.err.println("消息title：" + title);
        System.err.println("消息content：" + content);
        System.err.println("消息regesterIdList：" + JSONObject.toJSONString(getDeviceIds("my")));
        System.err.println("消息extras：" + JSONObject.toJSONString(extras));
        Response res = service.sendPushByRegesterId(getDeviceIds("何敏"), title, content, extras);
        System.err.println(res.isSuccess());
        System.err.println(JSONObject.toJSONString(res.getResultObject()));
        System.err.println(res.getCode());
    }


    private List<String> getDeviceIds(String name) {
        List<String> deviceIds = new ArrayList<>();
        if (name.equals("小义")) {
            deviceIds.add("13065ffa4e3b31fc5a2");//小义
            return deviceIds;
        }
        if (name.equals("李响")) {
            deviceIds.add("191e35f7e07d1834c45");//李响
            return deviceIds;
        }
        if (name.equals("my")) {
            //1104a8979296a206ae9
            deviceIds.add("1104a8979296a206ae9");//自己
            return deviceIds;
        }
        if (name.equals("WRY")) {
            deviceIds.add("171976fa8ab90d122f0");
            return deviceIds;
        }
        if (name.equals("何敏")) {
            deviceIds.add("18171adc0332db7f565");
            return deviceIds;
        }
        if (name.equals("吴娟")) {
            //1507bfd3f7f13ee6f6c
            deviceIds.add("121c83f7601a7e95834");
            return deviceIds;
        }
        deviceIds.add("13065ffa4e3b31fc5a2");//小义
        deviceIds.add("171976fa8abab34314d");//李响
        return deviceIds;
    }


    /**
     * 组装推送参数
     *
     * @param mqPushDTO
     * @return
     */
    private Map<String, String> getExtras(MqPushDTO mqPushDTO) {
        Map<String, String> extras = new HashMap<>();
        System.out.println("");
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
            System.out.println("");
            extras.put("Type", "4");
            String cancleId = mqPushDTO.getCancelId();
            extras.put("CancelId", cancleId == null ? "" : cancleId);
            return extras;
        }
        if (type == PushTitleType.WFT_MESSAGE.getCode()) {
            System.out.println("");
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
     * 组装Title
     *
     * @param mqPushDTO
     * @return
     */
    private String getPushTitle(MqPushDTO mqPushDTO) {
        System.out.println("test");
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
     * 组装content
     *
     * @param mqPushDTO
     * @return
     */
    private String getPushContent(MqPushDTO mqPushDTO) {
        int type = mqPushDTO.getType();
        String amountStr = getAmountStr(mqPushDTO.getAmount());
        String orderId = mqPushDTO.getOrder_number();
        String shippingMethod = mqPushDTO.getShippingMethod();
        String shippingAddress = mqPushDTO.getShippingAddress();

        if (type == PushContentType.ORDER_RECEIVING_CONTENT.getCode()) {
            return PushContentType.ORDER_RECEIVING_CONTENT.getInfo().replace("{2}", shippingMethod).replace("{3}", shippingAddress);
        }
        if (type == PushContentType.EXPRESS_RECEIVING_CONTENT.getCode()) {
            return PushContentType.EXPRESS_RECEIVING_CONTENT.getInfo();
        }
        if (type == PushContentType.ORDER_CANCEL_CONTENT.getCode()) {
            return PushContentType.ORDER_CANCEL_CONTENT.getInfo().replace("{2}", shippingMethod).replace("{3}", amountStr);
        }
        if (type == PushContentType.ORDER_OVERTIME_CONTENT.getCode()) {
            return PushContentType.ORDER_OVERTIME_CONTENT.getInfo().replace("{2}", shippingMethod).replace("{3}", amountStr);
        }
        if (type == PushContentType.EXPRESS_DONE_CONTENT.getCode()) {
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
            }
            String productStr = products.toString();
            return PushContentType.SALES_RETURN_CONTENT.getInfo().replace("{2}", productStr.substring(0, productStr.length() - 1));
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
