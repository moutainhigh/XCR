package com.yatang.xc.xcr.web;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.annotations.Payload;
import com.yatang.xc.xcr.annotations.SessionToken;
import com.yatang.xc.xcr.dto.inputs.AddToStockDto;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IStockService;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.HttpClientUtil;
import com.yatang.xc.xcr.util.NoDataClass;
import com.yatang.xc.xcr.util.StringUtils;
import com.yatang.xc.xcr.util.TokenUtil;
import com.yatang.xc.xcr.web.interceptor.BuryingPoint;

/**
 * @author dongshengde 库存web类
 */
@Controller
@RequestMapping("/User/")
public class StockAction {

	// 库存总金额
	private @Value("${APPKEY}") String				appKey;
	private @Value("${STATE_OK}") String			STATE_OK;
	private @Value("${ACCESSKEY}") String			accessKey;
	private @Value("${STOCKLIST_URL}") String		STOCKLIST_URL;
	private @Value("${TOTALMONEY_URL}") String		TOTALMONEY_URL;
	private @Value("${STOCKRECORDLIST_URL}") String	STOCKRECORDLIST_URL;
	private static Logger							log	= LoggerFactory.getLogger(StockAction.class);

	@Autowired
	private IStockService							stockService;



	/// <Summary>
	/// 商品入库
	/// </Summary>
	@BuryingPoint
	@SessionToken
	@RequestMapping(value = "AddToStock", method = RequestMethod.POST)
	public ResultMap addToStock(@Payload AddToStockDto dto) {
		return stockService.addToStock(dto);
	}



	/**
	 * 库存商品列表 dongshengde
	 *
	 * @param msg
	 * @param response
	 * @throws Exception
	 *             msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":
	 *             "1111","PageIndex":"1","PageSize":"5","Search":"彩票",
	 *             "SortType":"1"}
	 */
	@RequestMapping(value = "StockList", method = RequestMethod.POST)
	public void stockList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "StockList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		JSONObject listData = new JSONObject();
		JSONObject mapD = new JSONObject();
		if (stateJson.get("State").toString().equals(STATE_OK)) {
			JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
			StringBuffer param = new StringBuffer();
			param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
					.append(tokenJson.getString("jmsCode")).append("&shopCode=")
					.append(jsonTemp.getString("StoreSerialNo"));
			String index = jsonTemp.getString("PageIndex");
			String size = jsonTemp.getString("PageSize");
			int pageIndex = Integer.parseInt(index) - 1;
			int pageSize = Integer.parseInt(size);
			int start = pageIndex * pageSize;
			int end = start + pageSize;
			param.append("&start=").append(start).append("&end=").append(end);
			if (jsonTemp.getString("Search") != null) {
				String search = jsonTemp.getString("Search");
				search = URLEncoder.encode(search, "UTF-8");
				param.append("&search=").append(search);
			}
			if (jsonTemp.getString("SortType") != null && !jsonTemp.getString("SortType").equals("")) {
				if (jsonTemp.getString("SortType").equals("0")) {
					param.append("&sort=").append("1");
					param.append("&sortType=").append("2");
				} else if (jsonTemp.getString("SortType").equals("1")) {
					param.append("&sort=").append("2");
					param.append("&sortType=").append("2");
				} else if (jsonTemp.getString("SortType").equals("-1")) {
				}
			}
			Long getSignArrayStartTime = System.currentTimeMillis();
			log.info("调用StockAction.StockList接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
					+ "请求数据是：" + JSONObject.toJSONString(param));
			JSONObject jsonResult = HttpClientUtil.okHttpPost(STOCKLIST_URL, param.toString());
			if (jsonResult != null && jsonResult.getString("responseCode").equals("200")) {
				List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
				JSONObject js = JSONObject.parseObject(jsonResult.getString("data"));
				JSONArray jsonArray = (JSONArray) js.get("rows");
				for (Object object : jsonArray) {
					JSONObject jsonObject = new JSONObject();
					JSONObject jObject = (JSONObject) object;
					jsonObject.put("GoodsId", StringUtils.replaceNULLToStr(jObject.get("GOODSID")));
					jsonObject.put("GoodsName", StringUtils.replaceNULLToStr(jObject.get("ITEMDESC")));
					jsonObject.put("GoodsCode", StringUtils.replaceNULLToStr(jObject.get("ITEMNUM")));
					jsonObject.put("UnitName", StringUtils.replaceNULLToStr(jObject.getString("ORDERUNIT")));
					jsonObject.put("GoodsStock", StringUtils.replaceNULLToStr(jObject.get("STOCK")));
					jsonObjects.add(jsonObject);
				}
				listData.put("rows", jsonObjects);
				String totalMoney = getTotalMoney(jsonTemp, tokenJson.getString("jmsCode"));
				mapD.put("StockAllValues", StringUtils.replaceNULLToStr(totalMoney));
				listData.put("pageindex", jsonTemp.getString("PageIndex"));
				listData.put("pagesize", jsonTemp.getString("PageSize"));
				listData.put("totalpage",
						Integer.parseInt(js.getString("records")) / Integer.parseInt(jsonTemp.getString("PageSize"))
								+ 1);
				listData.put("totalcount", js.getString("records"));
				json.put("listdata", listData);
				json.put("mapdata", mapD);
				json.put("Status", stateJson);
			} else
				json = judgeResultMethod(json, jsonResult);
		} else {
			json.put("Status", stateJson);
		}
		log.info("于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "response to XCR_APP data is:  " + json + "用时为："
				+ CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}



	/**
	 * 查询库存总金额
	 */
	public String getTotalMoney(JSONObject jsonTemp, String jmsCode) throws Exception {
		StringBuffer param = new StringBuffer();
		param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
				.append(jmsCode).append("&shopCode=").append(jsonTemp.getString("StoreSerialNo"));
		Long getSignArrayStartTime = System.currentTimeMillis();
		log.info("调用StockAction.getTotalMoney接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
				+ "请求数据是：" + JSONObject.toJSONString(param.toString()));
		JSONObject jsonResult = HttpClientUtil.okHttpPost(TOTALMONEY_URL, param.toString());
		log.info("于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "调用StockAction.getTotalMoney接口,调用结束"
				+ "响应数据是：" + jsonResult + "*****所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));
		String totalMoney = jsonResult.getString("total");
		return totalMoney;
	}



	/**
	 * 入库商品记录 dongshengde
	 *
	 * @param msg
	 * @param response
	 * @throws Exception
	 *             msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":
	 *             "1111","PageIndex":"1","PageSize":"5","Search":"彩票"}
	 */
	@RequestMapping(value = "StockRecordList", method = RequestMethod.POST)
	public void stockRecordList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "StockRecordList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		JSONObject listData = new JSONObject();

		if (stateJson.getString("State").equals(STATE_OK)) {
			JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
			StringBuffer param = new StringBuffer();
			param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
					.append(tokenJson.getString("jmsCode")).append("&shopCode=")
					.append(jsonTemp.getString("StoreSerialNo"));
			String index = jsonTemp.getString("PageIndex");
			String size = jsonTemp.getString("PageSize");
			int pageIndex = Integer.parseInt(index) - 1;
			int pageSize = Integer.parseInt(size);
			int start = pageIndex * pageSize;
			int end = start + pageSize;
			param.append("&start=").append(start).append("&end=").append(end);
			if (jsonTemp.getString("Search") != null && !jsonTemp.getString("Search").equals("")) {
				String search = jsonTemp.getString("Search");
				search = URLEncoder.encode(search, "UTF-8");
				param.append("&search=").append(search);
			} else {
				param.append("&search=").append("");// 全部
			}
			param.append("&optionType=").append("");// 空字符串是全部，1是出库，2是入库
			Long getSignArrayStartTime = System.currentTimeMillis();
			log.info("调用StockAction.StockRecordList接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
					+ "请求数据是：" + JSONObject.toJSONString(param));
			JSONObject jsonResult = HttpClientUtil.okHttpPost(STOCKRECORDLIST_URL, param.toString());
			if (jsonResult != null && jsonResult.getString("responseCode").equals("200")) {
				List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
				JSONObject js = JSONObject.parseObject(jsonResult.getString("data"));
				JSONArray jsonArray = (JSONArray) js.get("rows");
				for (Object object : jsonArray) {
					JSONObject json1 = new JSONObject();
					JSONObject jObject = (JSONObject) object;
					json1.put("GoodsId", StringUtils.replaceNULLToStr(jObject.get("GOODSID")));
					json1.put("GoodsName", StringUtils.replaceNULLToStr(jObject.get("ITEMDESC")));
					json1.put("GoodsCode", StringUtils.replaceNULLToStr(jObject.get("ITEMNUM")));
					json1.put("GoodsCostPrice", StringUtils.replaceNULLToStr(jObject.get("COSTPRICE")));
					json1.put("UnitName", StringUtils.replaceNULLToStr(jObject.getString("ORDERUNIT")));
					json1.put("Num", StringUtils.replaceNULLToStr(jObject.get("STOCKNUM")));
					String time = StringUtils.replaceNULLToStr(jObject.getString("STOCKTIME"));
					String time2 = "20" + time;
					String time3 = time2.replace("-", ".");
					json1.put("Time", time3);
					String recordsType = StringUtils.replaceNULLToStr(jObject.get("OPTIONTYPE"));
					if (recordsType.equals("1")) {
						json1.put("Type", "1");
					} else if (recordsType.equals("2")) {
						json1.put("Type", "0");
					} else {
						json1.put("Type", "出入库类型错误");
					}
					jsonObjects.add(json1);
				}
				listData.put("rows", jsonObjects);
				listData.put("totalcount", js.getString("records"));
				listData.put("pageindex", Integer.parseInt(jsonTemp.getString("PageIndex")));
				listData.put("pagesize", Integer.parseInt(jsonTemp.getString("PageSize")));
				// 计算总页数
				int records = Integer.parseInt(js.getString("records"));
				if (records == 0) {
					listData.put("totalpage", 0);
				} else {
					int totalpage = (records - 1) / Integer.parseInt(jsonTemp.getString("PageSize")) + 1;
					listData.put("totalpage", totalpage);
				}
				json.put("listdata", listData);
				json.put("Status", stateJson);
			} else
				json = judgeResultMethod(json, jsonResult);
		} else {
			json.put("Status", stateJson);
		}
		response.getWriter().print(json);
	}



	private JSONObject judgeResultMethod(JSONObject json, JSONObject jsonResult) {
		if (jsonResult != null && jsonResult.getString("responseCode").equals("101")) {
			json = NoDataClass.addKeyValue();
		} else {
			JSONObject jsResult = new JSONObject();
			jsResult.put("State", "M03");
			jsResult.put("StateID", "03");
			jsResult.put("StateValue", "03");
			jsResult.put("StateDesc", "获取列表失败");
			json.put("Status", jsResult);
		}
		return json;
	}

}
