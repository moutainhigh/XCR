package test.com.yatang.xc.xcr.biz;

import com.yatang.xc.xcr.biz.pay.util.MD5;
import com.yatang.xc.xcr.biz.pay.util.SignUtils;
import com.yatang.xc.xcr.biz.pay.util.XmlUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by wangyang on 2018/1/22.
 */
public class SearchPayment {

    public static final String no1 = "507371_1517299188194";
    public static final String no2 = "507371_1517299709112";


    public static void main(String[] args) {

        String mchId = "102594396689";
        //获取秘钥
        String key = "27f60174dba2f544fcd08843a05d18d2";
        String outTradeNo = no1;


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
                System.err.println("swiftpass -> app内部支付 -> 查询订单详情 -> 请求返回结果 -> res:" + res);

                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, key)) {
                        System.err.println("验证签名不通过 !!!");
                    }
                    if ("0".equals(resultMap.get("status"))) {

                        System.err.println("payResult : " + resultMap.get("trade_state"));
                    }
                }
            } else {
                System.err.println("操作失败-发起订单查询请求失败");
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


    /**
     * 查询订单参数组装
     *
     * @param mchId
     * @param outTradeNo
     * @param key
     * @return
     */
    private static SortedMap<String, String> getQueryOrderParamsMap(String mchId, String outTradeNo, String key) {
        SortedMap<String, String> map = new TreeMap();
        map.put("service", "unified.trade.query"); //接口类型，固定
        map.put("mch_id", mchId); //商户号
        map.put("out_trade_no", outTradeNo); //商户订单号
        map.put("nonce_str", String.valueOf(new Date().getTime())); //商品描述
        map.put("sign", getSign(map, key));
        return map;
    }

    private static String getSign(SortedMap<String, String> map, String key) {
        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        return MD5.sign(preStr, "&key=" + key, "utf-8");
    }

}
