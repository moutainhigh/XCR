package test.com.yatang.xc.xcr.web.action;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

//@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
public class SwiftPassActionTest {

    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers;

    private String simplyScrKey = "secret";

    private String clientBatchCode;

    	private String facadeHome = "http://localhost:8087/xcr/api/swiftpass/";
//	private String facadeHome = "https://xcrapitest.yatang.com.cn:14443/xcr/api/swiftpass";
//    private String facadeHome = "https://172.30.10.213:14443/xcr/api/swiftpass/";


    protected final Log log = LogFactory.getLog(this.getClass());

    @Before
    public void before() {
        headers = new HttpHeaders();
        headers.set("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        clientBatchCode = UUID.randomUUID().toString();
    }

    private HttpEntity<String> setMD5ToHeader(HttpHeaders headers, String json) {
        String signature = MD5Util.md5(json + simplyScrKey);
        headers.set("signature", signature);
        log.info("MD5字符串" + signature);
        log.info("json字符串" + json);
        HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
        return formEntity;
    }

    private HttpEntity<String> setMD5ToHeader(HttpHeaders headers, String json, String encode) {
        String signature = MD5Util.md5(json + simplyScrKey);
        headers.set("signature", signature);
        // 有中文字符，对字符串进行UTF-8编码
        System.out.println("json字符串没有编码" + json);
        try {
            json = URLEncoder.encode(json, encode);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        log.info("MD5字符串" + signature);
        log.info("json字符串" + json);
        HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
        return formEntity;
    }

    /**
     * 测试 威富通消息
     */
    @Test
    public void swiftPassAction() {
        String json = "<xml>" +
                "<bank_type><![CDATA[CFT]]></bank_type>" +
                "<charset><![CDATA[UTF-8]]></charset>" +
                "<device_info><![CDATA[QR_CODE]]></device_info>" +
                "<fee_type><![CDATA[CNY]]></fee_type>" +
                "<is_subscribe><![CDATA[Y]]></is_subscribe>" +
                "<mch_id><![CDATA[102512398649]]></mch_id>" +
                "<nonce_str><![CDATA[1506650553957]]></nonce_str>" +
                "<openid><![CDATA[oMJGHs4Qf4SuX21SvAyQvBZTEUKo]]></openid>" +
                "<out_trade_no><![CDATA[10255224311416229117483068611]]></out_trade_no>" +
                "<out_transaction_id><![CDATA[4200000024201709294894949164]]></out_transaction_id>" +
                "<pay_result><![CDATA[0]]></pay_result>" +
                "<result_code><![CDATA[0]]></result_code>" +
                "<sign><![CDATA[7A33660B4B1E2F0460C1F0DE08D27238]]></sign>" +
                "<sign_type><![CDATA[MD5]]></sign_type>" +
                "<status><![CDATA[0]]></status>" +
                "<time_end><![CDATA[20170929100233]]></time_end>" +
                "<total_fee><![CDATA[1]]></total_fee>" +
                "<trade_type><![CDATA[pay.weixin.jspay]]></trade_type>" +
                "<transaction_id><![CDATA[102552243114201709293217924859]]></transaction_id>" +
                "<version><![CDATA[2.0]]></version>" +
                "</xml>";

        HttpEntity<String> formEntity = setMD5ToHeader(headers, json);

        ResponseEntity<String> result = restTemplate.exchange(facadeHome + "/returnMessage", HttpMethod.POST,
                formEntity, String.class);
        if (result.getStatusCodeValue() != 200) {
            fail("result:" + JSONObject.toJSONString(result));
        }
        log.info("message : " + result.getBody());
    }
}
