package test.com.yatang.xc.xcr.biz;

import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.StoreSettlementInfoDto;
import com.yatang.xc.mbd.biz.org.dubboservice.StoreSettlementInfoDubboService;
import com.yatang.xc.oles.biz.business.dto.PaymentInfoDTO;
import com.yatang.xc.xcr.biz.pay.dto.PayParamDto;
import com.yatang.xc.xcr.biz.pay.enums.PayRecordState;
import com.yatang.xc.xcr.biz.pay.service.SwiftPassPayService;
import com.yatang.xc.xcr.biz.pay.util.MD5;
import com.yatang.xc.xcr.biz.pay.util.SignUtils;
import com.yatang.xc.xcr.biz.pay.util.XmlUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 订单查询测试
 * Created by wangyang on 2017/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestPayReturn {

    @Autowired
    private StoreSettlementInfoDubboService storeSettlementInfoDubboService;

    @Autowired
    private SwiftPassPayService swiftPassPayService;

    /**
     * 到威富通查询订单
     */
    @Test
    public void test() {

        String mchId = "102594396689";
        String outTradeNo = "507121_1516432836868";

        //获取秘钥
        String key = getKey(mchId);
        SortedMap<String, String> map = getQueryOrderParamsMap(mchId, outTradeNo, key);
        String reqUrl = "https://pay.swiftpass.cn/pay/gateway";
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        String res;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
            httpPost.setEntity(entityParams);
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if (response != null && response.getEntity() != null) {
                Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                System.out.println("swiftpass -> app内部支付 -> 查询订单详情 -> 请求返回结果 -> res:" + res);

                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, key)) {
                        System.out.println("验证签名不通过 !!!");
                    }
                    if ("0".equals(resultMap.get("status"))) {

                        System.out.println("payResult : " + resultMap.get("trade_state"));
                    }
                }
            } else {
                System.out.println("操作失败-发起订单查询请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test2() {
        System.out.println(PayRecordState.PAY_SUCCESS.getState());
        boolean s = swiftPassPayService.updatePayState("755437000006", "01116911_1510280581143", PayRecordState.PAY_SUCCESS.getState());
        System.out.println(s);

    }

    /**
     * 直接预支付，得到授权码
     */
    @Test
    public void test3() {

        String key = "87c0efd1e9f921d2bc4307016902dea7";
        //组装支付参数调用支付url
        SortedMap<String, String> map = getPayParamsMap(key);
        String reqUrl = "https://pay.swiftpass.cn/pay/gateway";
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
            httpPost.setEntity(entityParams);
            client = HttpClients.createDefault();
            response = client.execute(httpPost);

            if (response != null && response.getEntity() != null) {
                Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");

                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, key)) {
                        System.out.println("验证签名不通过");
                    }
                    if ("0".equals(resultMap.get("status"))) {
                        String tokenId = resultMap.get("token_id");//授权码
                        String services = resultMap.get("services");//支持的支付类型,支持的支付类型，多个以“|”连接
                        System.out.println("tokenId:" + tokenId);
                        System.out.println("支付成功");
                        return;
                    }
                }
            }
            System.out.println("支付失败");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 组装支付参数
     *
     * @param paymentInfoDTO
     * @return
     */
    private SortedMap<String, String> getPayParamsMap(String key) {
        SortedMap<String, String> map = new TreeMap();
        map.put("service", "unified.trade.pay"); //接口类型，固定
        map.put("notify_url", "https://xcrapisit.yatang.com.cn:14442/xcr/api/swiftpass/callback");  //回调地址
        map.put("nonce_str", String.valueOf(new Date().getTime()));  //随机字符串
        map.put("mch_id", "102512243116"); //商户号
        map.put("out_trade_no", "123232_" + new Date().getTime()); //商户订单号
        map.put("body", "商品描述"); //商品描述
        map.put("total_fee", "1");//总金额
        map.put("mch_create_ip", "172.30.10.255"); //订单生成的机器IP
        map.put("sub_appid", "wx9c1bcd9ed6a316c4");
        //获取签名
        map.put("sign", getSign(map, key));
        return map;
    }

    /**
     * 根据商户号获取支付秘钥
     *
     * @param mchId
     * @return
     */
    private String getKey(String mchId) {
        Response<StoreSettlementInfoDto> response = storeSettlementInfoDubboService.queryStoreSettlementInfoByBusinessNumber(mchId);
        if (response != null && response.isSuccess()) {
            return response.getResultObject().getSecretKey();
        }
        return null;
    }


    /**
     * 查询订单参数组装
     *
     * @param mchId
     * @param outTradeNo
     * @param key
     * @return
     */
    private SortedMap<String, String> getQueryOrderParamsMap(String mchId, String outTradeNo, String key) {
        SortedMap<String, String> map = new TreeMap();
        map.put("service", "unified.trade.query"); //接口类型，固定
        map.put("mch_id", mchId); //商户号
        map.put("out_trade_no", outTradeNo); //商户订单号
        map.put("nonce_str", String.valueOf(new Date().getTime())); //商品描述
        map.put("sign", getSign(map, key));
        return map;
    }

    private String getSign(SortedMap<String, String> map, String key) {
        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        return MD5.sign(preStr, "&key=" + key, "utf-8");
    }

}
