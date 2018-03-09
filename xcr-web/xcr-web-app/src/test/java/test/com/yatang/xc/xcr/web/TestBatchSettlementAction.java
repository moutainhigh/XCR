package test.com.yatang.xc.xcr.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.yatang.xc.oc.biz.adapter.RedisAdapterServie;
import com.yatang.xc.oc.biz.redis.dubboservice.RedisPlatform;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.util.HttpClientUtil;
import com.yatang.xc.xcr.enums.RedisKeyEnum;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
public class TestBatchSettlementAction {
	
	@Autowired
	private RedisService<String> resRedisService;
	@Autowired
	private RedisAdapterServie<String, String> redisAdapter;
	
	// private static String appKey="00000026";
	private static String appKey = "00000025";
	private static String accessKey = "8F3387A7C291D05480BEA33611418A9C";
	private static String ForPaymentManageListURL = "http://172.30.111.207:8080/ec-erp/PaymentCreateAction.do?method=selectBatchBrush"; // 结款列表-戴富有-DONE
	private static String ForPaymentDetialListURL = "http://172.30.40.61:8080/ec-erp/QueryFinanceLineAction.do?method=doExecute"; // 结款明细-宋健-DONE
	private static String NoForPaymentListURL = "http://172.30.40.172:8080/ec-erp/batchBrushAction.do?method=queryTicketList"; // 未结款列表
	private static String AddForPaymentURL = "http://172.30.111.207:8080/ec-erp/PaymentCreateAction.do?method=doExecute"; // 新增结款单--戴富有-DONE
	private static String DeleteForPaymentURL = "http://172.30.40.82:8080/ec-erp/BatchBrushDeleteAction.do?method=doExecute"; // 删除结款单-DONE
	private static String ForPayURL = ""; // TODO 支付（暂时不做）
	private static String CheckForPayURL = "http://172.30.40.188:8080/ec-erp/UpdateBrushStatusAction.do?method=doExecute"; // 支付结款单成功回调接口-沉默-DONE

	
	@Test
	public void testSaveRedis() {
		redisAdapter.setex(RedisPlatform.common,"xcr-echo", "xx", 10000);
		//resRedisService.saveObject(RedisKeyEnum.Key, "xcr", 100000, "xxxx");
	}
	
	@Test
	public void testGetRedis() {
		//String re = resRedisService.getKeyForObject(RedisKeyEnum.Key, "xcr", String.class);
		String re = redisAdapter.get(RedisPlatform.common,"xcr-echo", String.class);
		Log.info("re:"+re);
	}
	
	@Test
	public void testRemoveRedis() {
		redisAdapter.setex(RedisPlatform.common,"xcr-echo", null, 1);
		//redisAdapter.del(RedisPlatform.common,"xcr-echo");
		//resRedisService.saveObject(RedisKeyEnum.Key, "xcr", 100000, "xxyy");
	}
	
	@Test
	public void testRedis() {
		testSaveRedis();
		testGetRedis();
		testRemoveRedis();
		testGetRedis();
	}
	
	
	
	/**
	 * 结款列表 <method description>
	 *
	 */
	@Test
	public void testForPaymentManageList() {
		/**
		 * appKey=00000025& accessKey=8F3387A7C291D05480BEA33611418A9C&
		 * alliBusiId=900003& shopCode=A900003& statusList=[{"brushStatus":0}]&
		 * start=0& end=5& startDate=2017-01-01& endDate=2017-06-10
		 */
		System.err.println("************testForPaymentManageList************");
		String param = getParam("ForPaymentManageList");
		Object result = HttpClientUtil.okHttpPost(ForPaymentManageListURL, param.toString());
		System.err.println("************result：" + result);
	}

	/**
	 * 结款明细 <method description>
	 *
	 */
	@Test
	public void testForPaymentDetialList() {
		System.err.println("************testForPaymentDetialList************");
		String param = getParam("ForPaymentDetialList");
		Object result = HttpClientUtil.okHttpPost(ForPaymentDetialListURL, param.toString());
		System.err.println("************result：" + result);
	}

	/**
	 * 未结款列表 <method description>
	 *
	 */
	@Test
	public void testNoForPaymentList() {
		/**
		 * appKey=00000025& accessKey=8F3387A7C291D05480BEA33611418A9C&
		 * alliBusiId=900003& shopCode=A900003& status=0& start=0& end=5
		 */
		System.err.println("************testNoForPaymentList************");
		String param = getParam("NoForPaymentList");
		System.out.println(param.toString());
		Object result = HttpClientUtil.okHttpPost(NoForPaymentListURL, param.toString());
		System.err.println("************result：" + result);
	}

	/**
	 * 新增结款单 <method description>
	 *
	 */
	@Test
	public void testAddForPayment() {
		System.err.println("************testAddForPayment************");
		String param = getParam("AddForPayment");
		System.out.println(param.toString());
		Object result = HttpClientUtil.okHttpPost(AddForPaymentURL, param.toString());
		System.err.println("************result：" + result);
	}

	/**
	 * 删除结款单 <method description>
	 *
	 */
	@Test
	public void testDeleteForPayment() {
		System.err.println("************testDeleteForPayment************");
		String param = getParam("DeleteForPayment");
		Object result = HttpClientUtil.okHttpPost(DeleteForPaymentURL, param.toString());
		System.err.println("************result：" + result);
	}

	/**
	 * 支付 <method description>
	 *
	 */
	@Test
	public void testForPay() {
		System.err.println("************testForPay************");
		String param = getParam("ForPay");
		Object result = HttpClientUtil.okHttpPost(ForPayURL, param.toString());
		System.err.println("************result：" + result);
	}

	/**
	 * 支付成功回调，查询支付结果 <method description>
	 *
	 */
	@Test
	public void testCheckForPay() {
		System.err.println("************testCheckForPay************");
		String param = getParam("CheckForPay");
		System.out.println(param.toString());
		Object result = HttpClientUtil.okHttpPost(CheckForPayURL, param.toString());
		System.err.println("************result：" + result);
	}

	public String getParam(String interfaceParam) {
		StringBuffer param = new StringBuffer();
		// 通用参数
		param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey);

		// 结款列表
		if (interfaceParam.equals("ForPaymentManageList")) {
			// 查询结款列表——>结款状态List 0 未结算，1已结算 3支付中 4结算失败
			List<JSONObject> statusListList = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				JSONObject json = new JSONObject();
				json.put("brushStatus", i);
				statusListList.add(json);
			}

			param.append("&alliBusiId=").append("900003") // 加盟商ID
					.append("&shopCode=").append("A900003") // 门店编号
					.append("&statusList=").append(statusListList) // 结款状态列表
					.append("&start=").append("0") // 开始条数
					.append("&end=").append("5") // 结束条数
					.append("&startDate=").append("2014-01-01") // 开始时间
					.append("&endDate=").append("2017-06-10"); // 结束时间
		}

		// 结款明细
		if (interfaceParam.equals("ForPaymentDetialList")) {
			param.append("&alliBusiId=").append("900003") // 加盟商ID
					.append("&shopCode=").append("A900003") // 门店编号
					.append("&depositId=").append("63288"); // 结算id
		}

		// 未结款列表
		if (interfaceParam.equals("NoForPaymentList")) {
			param.append("&alliBusiId=").append("900003") // 加盟商ID
					.append("&shopCode=").append("A900003") // 门店编号
					.append("&status=").append("0") // 状态 0:未结款，1:已结款，默认0
					.append("&start=").append("0") // 开始条数
					.append("&end=").append("5"); // 结束条数
		}

		// 新增结款单
		if (interfaceParam.equals("AddForPayment")) {
			// 小票号列表
			// ticketId":2028}, {"ticketId":2032}, {"ticketId":2167},
			// {"ticketId":2193}
			List<JSONObject> ticketIdList = new ArrayList<>();
			JSONObject t1 = new JSONObject();
			JSONObject t2 = new JSONObject();
			JSONObject t3 = new JSONObject();
			JSONObject t4 = new JSONObject();
			t1.put("ticketId", 2028);
			t2.put("ticketId", 2032);
			t3.put("ticketId", 2167);
			t4.put("ticketId", 2193);
			ticketIdList.add(t1);
			ticketIdList.add(t2);
			ticketIdList.add(t3);
			ticketIdList.add(t4);

			param.append("&alliBusiId=").append("jms_000356") // 加盟商ID
					.append("&shopCode=").append("A000356") // 门店编号
					.append("&ticketIdList=").append(ticketIdList); // 小票列表
		}

		// 删除结款单
		if (interfaceParam.equals("DeleteForPayment")) {
			// A000094 jms_000094 941
			param.append("&alliBusiId=").append("jms_000094") // 加盟商ID
					.append("&shopCode=").append("A000094") // 门店编号
					.append("&depositId=").append("941"); // 代刷单ID
		}

		// 支付
		if (interfaceParam.equals("ForPay")) {
			// TODO
		}

		// 支付结款单成功回调接口
		if (interfaceParam.equals("CheckForPay")) {
			String dateStr = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateStr = sdf.format(new Date());

			param.append("&alliBusiId=").append("900001") // 加盟商ID
					.append("&shopCode=").append("A900001") // 门店编号
					.append("&depositId=").append("37461") // 结款单编号
					.append("&remark=").append("附加信息") // 附加信息
					.append("&commitDate=").append(dateStr); // 结算时间

		}

		System.err.println("************接口" + interfaceParam);
		System.err.println("请求的参数为：" + param);
		return param.toString();
	}

	public static void main(String[] args) {
		String dateStr = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateStr = sdf.format(new Date());
		System.out.println(dateStr);
	}

}
