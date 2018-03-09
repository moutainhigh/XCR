package test.com.yatang.xc.xcr.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.utils.HttpsClientUtil;
import com.itbt.common.EncryptUtils;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;

/**
 * 
 * @author dongshengde
 *
 */
@Controller
@RequestMapping("/User/")
public class BranchBankListActionTest {
	private static Logger log = LoggerFactory.getLogger(BranchBankListActionTest.class);
	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${INFO_OK}")
	String INFO_OK;
	@Value("${BINDINGBANKTEST_URL}")
	String BINDINGBANKTEST_URL;
	@Value("${APPKEY_BANK}")
	private String APPKEY_BANK;
	@Value("${ACCESSKEY_BANK}")
	private String ACCESSKEY_BANK;
	@Value("${ADDBANK_PROTOCOL}")
	private String ADDBANK_PROTOCOL;
	private static final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Map<String, String> getParams() {
		Map<String, String> params = new HashMap<>();
		params.put("url", "https://yztapi.yatang.cn/yluser");
		params.put("method", "userBankCardAction.saveUserBankCardInfo");
		params.put("v", "1.0");
		params.put("format", "json");
		params.put("appKey", "07007");
		params.put("timestamp", f.format(new Date()));
		return params;
	}

	public String validate(JSONObject jsonObject) throws Exception {
		String result;

		Map<String, String> params = getParams();
		params.put("userName", jsonObject.getString("OpenAccount"));
		//params.put("password", "79350968");
		params.put("origin", "7");
		params.put("name", jsonObject.getString("Cardholder"));
		params.put("idCard", jsonObject.getString("ID"));
		params.put("appSecret", "yt_77#xx$xiaochao");
		params.put("accountNO", jsonObject.getString("BankCardNum"));
		params.put("bankPreMobile", jsonObject.getString("PhoneNum"));
		String sign = EncryptUtils.sign(params, "yt_77#xx$xiaochao");
		params.put("sign", sign);
		//String sign = EncryptUtils.sign(params, "");
		//params.put("sign", sign);
		Long getSignArrayStartTime = System.currentTimeMillis();
		log.info("\n*****************调用HttpsClientUtil.sendHttpsPost接口的开始时间："
				+ DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求用户"
				+ jsonObject.getString("UserId") + "请求数据是："
				+ JSONObject.toJSONString(JSONObject.toJSONString(params.toString())));
		result = HttpsClientUtil.sendHttpsPost("https://yztapi.yatang.cn/yluser", params);
		log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
				+ "调用HttpsClientUtil.sendHttpsPost接口   调用结束" + "\n*****************" + jsonObject.getString("UserId")
				+ "响应数据是：" + JSONObject.toJSONString(result) + "\n***************所花费时间为："
				+ CommonUtil.costTime(getSignArrayStartTime));
		return result;
	}

	@Test
	public void TestMethod() throws Exception {
		JSONObject js = new JSONObject();
		js.put("OpenAccount", "XC008863");
		js.put("Cardholder", "李守鹏");
		js.put("UserId", "jms_2332");
		js.put("ID", "332502199307162872");
		js.put("BankCardNum", "6214835713042768");
		js.put("PhoneNum", "15157162766");
		validate(js);
	}

	@Test
	public void TestMethod2() throws Exception {
		JSONObject js = new JSONObject();
		js.put("OpenAccount", "XC027771");
		js.put("Cardholder", "卢开发");
		js.put("ID", "35210119740816825X");
		js.put("UserId", "jms_2332");
		js.put("BankCardNum", "621799390008095961");
		js.put("PhoneNum", "18106965280");
		validate(js);
	}
}
