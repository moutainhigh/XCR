package com.yatang.xc.xcr.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.busi.common.resp.Response;
import com.busi.common.utils.JsonPathTools;
import com.yatang.xc.dc.biz.facade.dubboservice.DataCenterDobboService;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.QuerySmallticketDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.QuerySmallticketIdDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseSmallTicketsXcrDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseSmallTicketsXcrDto.SmallTicketXcrDto;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.NoDataClass;
import com.yatang.xc.xcr.util.StringUtils;

/**
 * @author gaodawei
 * @Date   2017年5月17日(星期三)
 * @function 交易流水相关功能
 */
@Controller
@RequestMapping("/User/")
public class TransactionFlowAction {

	private static Logger log = LoggerFactory.getLogger(TransactionFlowAction.class);

	@Value("${STATE_OK}")
	String STATE_OK;

	@Autowired
	private DataCenterDobboService dataCenterDobboService;
	@Autowired
	private DataCenterXcrDubboService dataCenterXcrDubboService;

	/**
	 * 交易流水
	 * gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 * msg={"Token":"07c3e91c-b3cb-4409-8727-0274c867fb8d","UserId":"900003","PageSize":20,"StoreName":"U751fU4ea7U9a8cU8bc1_U5c0fU8d85U95e8U5e97002","StoreSerialNo":"A900003","PageIndex":1}
	
	 */
	@RequestMapping(value = "TransactionList", method = RequestMethod.POST)
	public void transactionList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "TransactionList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK)) {
			// 调用service层接口
			JSONObject reqJson = new JSONObject();
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
			reqJson.put("StoreSerialNo", jsonTemp.get("StoreSerialNo"));
			reqJson.put("PageIndex", jsonTemp.getIntValue("PageIndex"));
			reqJson.put("PageSize", jsonTemp.getIntValue("PageSize"));
			if (jsonTemp.get("StartDate") != null && !"".equals(jsonTemp.get("StartDate"))) {
				reqJson.put("StartDate", jsonTemp.get("StartDate").toString());
			}
			if (jsonTemp.get("EndDate") != null && !"".equals(jsonTemp.get("EndDate"))) {
				reqJson.put("EndDate", jsonTemp.getString("EndDate"));
			} else {
				reqJson.put("EndDate", sim.format(new Date()));
			}
			Long startTime = System.currentTimeMillis();
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "开始调用数据中心接口:querySmallTicketStatisticsInfo \n***********请求数据是：" + reqJson.toString());
			String result = dataCenterDobboService.querySmallTicketStatisticsInfo(reqJson.toString());
			log.debug("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "调用数据中心接口：querySmallTicketStatisticsInfo 调用结束 \n***********响应数据是：：" + result
					+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
			BigDecimal recmny = new BigDecimal(
					JsonPathTools.getValues("aggregations.TransactionRecmny.value", result).get(0).toString());
			recmny = recmny.setScale(2, BigDecimal.ROUND_HALF_UP);
			List<Object> dataLists = JsonPathTools.getValues("hits.hits._source", result);
			List<Object> totals = JsonPathTools.getValues("hits.total", result);
			JSONObject dataJson = new JSONObject();
			JSONArray rowsList = new JSONArray();
			JSONObject listdata = new JSONObject();
			dataJson.put("TransactionAllValue", recmny.toPlainString());
			for (Object dataList : dataLists) {
				JSONObject subJson = new JSONObject();
				List<Object> recmnys = JsonPathTools.getValues("recmny", dataList.toString());
				String rec = "";
				if (null == recmnys || recmnys.isEmpty()) {
					rec = "0";
				} else {
					rec = recmnys.get(0).toString();
				}
				BigDecimal recmoney = new BigDecimal(rec);
				recmoney = recmoney.setScale(2, BigDecimal.ROUND_HALF_UP);
				subJson.put("TransactionDaily", recmoney.toPlainString());
				String reportday = JsonPathTools.getValues("reportday", dataList.toString()).get(0).toString();
				subJson.put("Date", reportday);
				subJson.put("TicketNum", ((JSONObject)dataList).getIntValue("ticketCount"));
				rowsList.add(subJson);
			}
			listdata.put("rows", rowsList);
			listdata = CommonUtil.pagePackage(listdata, jsonTemp, Integer.parseInt(totals.get(0).toString()), null);
			json.put("Status", stateJson);
			json.put("mapdata", dataJson);
			json.put("listdata", listdata);
		} else {
			json.put("Status", stateJson);
		}
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

	/**
	 * 小票列表
	 * gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 * 根据支付类型：msg={"UserId":"jms_630111","StoreSerialNo":"A630111","Token":"82c43a8e","StartTime":"","PageIndex":1,"Date":"2017-07-07","TicketTypes":[{"TicketType":1}],"PageSize":20,"PayTypes":[{"PayType":2210},{"PayType":2203}],"EndTime":"","StoreName":"U751fU4ea7U9a8cU8bc1_U6e56U5357U95e8U5e9704"}
	 * 
	 * 根据id:msg={"UserId":"jms_630111","StoreSerialNo":"A630111","Token":"82c43a8e","PageIndex":1,"PageSize":20,"Search":"1000014","Date":"2017-07-07"}
	 */
	@RequestMapping(value = "TicketList", method = RequestMethod.POST)
	public void ticketList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "TicketList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK)) {
			//根据小票编码进行查询列表
			if (jsonTemp.getString("Search") != null && !jsonTemp.getString("Search").equals("")) {
				json = querySmallticketById(jsonTemp, stateJson);
			} else {
				json = querySmallticket(jsonTemp, stateJson);
			}
		} else {
			json.put("Status", stateJson);
		}
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

	/**
	 * 根据支付类型得到小票列表
	 * @param jsonTemp
	 * @param stateJson
	 * msg = "{"Token":"1733765c-96dd-43fb-8942-1f9d89eaf7a9","UserId":"900003","StartTime":"","PageIndex":1,"Date":"2017-07-13","StoreSerialNo":"A900003","TicketTypes":[],"PageSize":20,"PayTypes":[],"EndTime":"","StoreName":"U751fU4ea7U9a8cU8bc1_U5c0fU8d85U95e8U5e97002"}"
	 */
	public JSONObject querySmallticket(JSONObject jsonTemp, JSONObject stateJson) {
		JSONObject json = new JSONObject();
		Response<ResponseSmallTicketsXcrDto> querySmallticket;
		QuerySmallticketDto querySmallticketDto = new QuerySmallticketDto();
		querySmallticketDto.setLocation(jsonTemp.getString("StoreSerialNo"));

		if (jsonTemp.getString("StartTime") == null || jsonTemp.getString("StartTime").trim().equals("")) {
			querySmallticketDto.setStartDate(jsonTemp.getString("Date") + " 00:00:01");
		} else {
			querySmallticketDto
					.setStartDate(jsonTemp.getString("Date") + " " + jsonTemp.getString("StartTime") + ":01");
		}

		if (jsonTemp.getString("EndTime") == null || jsonTemp.getString("EndTime").trim().equals("")) {
			querySmallticketDto.setEndDate(jsonTemp.getString("Date") + " 23:59:59");
		} else {
			querySmallticketDto.setEndDate(jsonTemp.getString("Date") + " " + jsonTemp.getString("EndTime") + ":59");

		}
		//单据类型
		JSONArray jsonArr = jsonTemp.getJSONArray("TicketTypes");
		if (jsonArr.size() == 0) {
			querySmallticketDto.setTicketTypes("1,3");
		} else {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < jsonArr.size(); i++) {
				if (i == 0) {
					buffer.append(JSONObject.parseObject(jsonArr.get(i).toString()).getString("TicketType"));
				} else {
					buffer.append(",")
							.append(JSONObject.parseObject(jsonArr.get(i).toString()).getString("TicketType"));
				}
			}
			querySmallticketDto.setTicketTypes(buffer.toString());
		}
		querySmallticketDto.setPageNum(jsonTemp.getIntValue("PageIndex"));
		querySmallticketDto.setPageSize(jsonTemp.getIntValue("PageSize"));
		JSONArray jsonArray = (JSONArray) jsonTemp.get("PayTypes");
		ArrayList<String> arrayList = new ArrayList<String>();
		if (jsonArray != null && !jsonArray.equals("")) {
			for (Object object : jsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				String string = jsonObject.getString("PayType");
				arrayList.add(string);
			}
			querySmallticketDto.setPayWay(arrayList);
		}
		Long startTime = System.currentTimeMillis();
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
				+ "开始调用数据中心接口：querySmallticket \n***********request data is:"
				+ JSONObject.toJSONString(querySmallticketDto).toString());
		//				通过筛选条件调用数据中心的接口返回数据
		querySmallticket = dataCenterXcrDubboService.querySmallticket(querySmallticketDto);
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null) + "开始调用接口：querySmallticket \n******返回的数据："
				+ JSONObject.toJSONString(querySmallticket) + "\n***********耗时为：" + CommonUtil.costTime(startTime));
		if (querySmallticket.getCode().equals("200") && querySmallticket.getResultObject().getTotal() != 0) {
			JSONObject jsResult = new JSONObject();
			ResponseSmallTicketsXcrDto responseSmallTicketsXcrDto = querySmallticket.getResultObject();
			JSONObject jsmap = new JSONObject();
			jsmap.put("TransactionAllValue", responseSmallTicketsXcrDto.getRecMny());
			jsmap.put("TicketDate", jsonTemp.getString("Date"));
			List<SmallTicketXcrDto> smallTicketXcrDtos = responseSmallTicketsXcrDto.getSmallTickets();
			List<JSONObject> ticketReturnClasses = new ArrayList<JSONObject>();
			for (SmallTicketXcrDto smallTicketXcrDto : smallTicketXcrDtos) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("TicketId", StringUtils.replaceNULLToStr(smallTicketXcrDto.getTicketId()));
				jsonObject.put("TicketNo", StringUtils.replaceNULLToStr(smallTicketXcrDto.getTicketNum()));
				jsonObject.put("TicketType", StringUtils.replaceNULLToStr(smallTicketXcrDto.getTicketType()));
				jsonObject.put("PayType", StringUtils.replaceNULLToStr(smallTicketXcrDto.getPayWay()));
				jsonObject.put("TicketAccount", StringUtils.replaceNULLToStr(smallTicketXcrDto.getRecMny()));
				jsonObject.put("Time", StringUtils.replaceNULLToStr(smallTicketXcrDto.getReportDate().substring(11)));
				jsonObject.put("Date", StringUtils.replaceNULLToStr(jsonTemp.getString("Date")));
				ticketReturnClasses.add(jsonObject);
			}
			jsResult.put("rows", ticketReturnClasses);
			jsResult = CommonUtil.pagePackage(jsResult, jsonTemp, responseSmallTicketsXcrDto.getTotal(), null);
			json.put("mapdata", jsmap);
			json.put("listdata", jsResult);
			json.put("Status", stateJson);
		} else if (querySmallticket.getCode().equals("200") && querySmallticket.getResultObject().getTotal() == 0) {
			json = NoDataClass.addKeyValue();
			JSONObject jsmap = new JSONObject();
			if (jsonTemp.getString("PageIndex").equals("1")) {
				jsmap.put("TransactionAllValue", "0.00");
				jsmap.put("TicketDate", jsonTemp.getString("Date"));
			} else {
				jsmap.put("TransactionAllValue", "");
				jsmap.put("TicketDate", "");
			}
			json.put("mapdata", jsmap);
		} else {
			json = CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
		}
		return json;
	}

	/**
	 * 根据id或日期查询小票列表
	 */
	public JSONObject querySmallticketById(JSONObject jsonTemp, JSONObject stateJson) {
		JSONObject json = new JSONObject();
		QuerySmallticketIdDto querySmallticketIdDto = new QuerySmallticketIdDto();
		querySmallticketIdDto.setLocation(jsonTemp.getString("StoreSerialNo"));
		querySmallticketIdDto.setTicketNum(jsonTemp.getString("Search"));
		querySmallticketIdDto.setPageNum(jsonTemp.getIntValue("PageIndex"));
		querySmallticketIdDto.setPageSize(jsonTemp.getIntValue("PageSize"));
		String strJson = "";
		Long startTime = System.currentTimeMillis();
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
				+ "开始调用数据中心接口 :querySmallticketById \n***********request data is:"
				+ JSONObject.toJSONString(querySmallticketIdDto).toString());
		//				通过小票id搜索
		strJson = dataCenterDobboService.querySmallticketById(querySmallticketIdDto);
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
				+ "调用数据中心接口：querySmallticketById  调用结束 \n***********response data is:" + strJson
				+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
		Map<String, Object> mapdataMap = new HashMap<>();
		JSONObject mapdata = new JSONObject();
		JSONObject listdata = new JSONObject();
		List<Object> dataLists = JsonPathTools.getValues("hits.hits._source", strJson);
		List<Object> totals = JsonPathTools.getValues("hits.total", strJson);
		double sum = 0.00;
		for (int i = 0; i < dataLists.size(); i++) {
			sum += Double.parseDouble(JsonPathTools.getValues("recmny", dataLists.get(i).toString()).get(0).toString());
		}
		mapdataMap.put("TransactionAllValue", sum);
		JSONArray rowsList = new JSONArray();
		for (Object dataList : dataLists) {
			Map<String, Object> subJson_Map = new HashMap<>();
			JSONObject subJson = new JSONObject();
			subJson_Map.put("TicketNo", JsonPathTools.getValues("ticketnum", dataList.toString()).get(0).toString());
			subJson_Map.put("TicketType", JsonPathTools.getValues("tickettype", dataList.toString()).get(0).toString());
			subJson_Map.put("Time",
					JsonPathTools.getValues("reportdate", dataList.toString()).get(0).toString().substring(11));
			subJson_Map.put("TicketAccount", JsonPathTools.getValues("recmny", dataList.toString()).get(0).toString());
			subJson_Map.put("TicketId", JsonPathTools.getValues("ticketid", dataList.toString()).get(0).toString());
			//小票类型
			//			List<Object> s = JsonPathTools.getValues("ptlist",dataList.toString());
			//			System.out.println(s+"-------------");
			//			List<Object> strList= JsonPathTools.getValues("paywaytwoid",s.get(0).toString());
			//			System.out.println(strList.get(0)+"++++++++++++");
			String payType = getPayType(dataList.toString());
			subJson_Map.put("PayType", payType);
			subJson = StringUtils.replcNULLToStr(subJson_Map);
			rowsList.add(subJson);
		}
		listdata.put("rows", rowsList);
		listdata = CommonUtil.pagePackage(listdata, jsonTemp, Integer.parseInt(totals.get(0).toString()), null);

		mapdataMap.put("TicketDate", jsonTemp.getString("Date"));
		mapdata = StringUtils.replcNULLToStr(mapdataMap);
		json.put("listdata", listdata);
		json.put("mapdata", mapdata);
		json.put("Status", stateJson);
		return json;
	}

	/**
	 * 判断小票类型
	 * @param ticketJson
	 * @return
	 */
	private String getPayType(String ticketJson) {
		String payWay = "";
		List<Object> paywayoneids = JsonPathTools.getValues("polist.paywayoneid", ticketJson.toString());
		if (CollectionUtils.isNotEmpty(paywayoneids)) {
			if (1 == paywayoneids.size()) {
				String paywayoneid = paywayoneids.get(0).toString();
				if ("1".equals(paywayoneid)) {
					payWay = "1";
				} else if ("3".equals(paywayoneid)) {
					payWay = "3";
				} else if ("4".equals(paywayoneid)) {
					String paywaytwoid = JsonPathTools.getValues("ptlist.paywaytwoid", ticketJson.toString()).get(0)
							.toString();
					if ("2210".equals(paywaytwoid)) {
						payWay = "2210";
					} else if ("2203".equals(paywaytwoid)) {
						payWay = "2203";
					}
				}
			} else if (2 == paywayoneids.size()) {
				Set<String> paywaytwoids = new HashSet<>();
				List<Object> paywaytwoidObjs = JsonPathTools.getValues("ptlist.paywaytwoid", ticketJson.toString());
				for (Object o : paywaytwoidObjs) {
					paywaytwoids.add(String.valueOf(o));
				}
				if (paywaytwoids.contains("2210")) {
					payWay = "6";
				} else if (paywaytwoids.contains("2203")) {
					payWay = "5";
				}
			}
		}
		return payWay;
	}

	/**
	 * 小票明细
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "TicketDetial", method = RequestMethod.POST)
	public void ticketDetial(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "TicketDetial");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);

		//用于定义放回APP的json
		JSONObject json = new JSONObject();

		if (jsonTemp.getString("flag").equals(STATE_OK)) {

			Long startTime = System.currentTimeMillis();
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "开始数据中心接口：queryOneSmallTicketInfo \n***********request data is:"
					+ jsonTemp.getString("TicketId"));
			String strJson = dataCenterDobboService.getSmallticketDetailById(jsonTemp.getString("TicketId"));
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "调用数据中心接口：queryOneSmallTicketInfo调用结束 \n***********response data is:" + strJson
					+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));

			JSONObject jsonData=JSONObject.parseObject(strJson);
			JSONObject _sourceJson = jsonData.getJSONObject("_source");
			Map<String, Object> mapdata_Map = new HashMap<>();
			JSONObject mapdata = new JSONObject();
			mapdata_Map.put("Time", _sourceJson.getString("reportdate"));
			mapdata_Map.put("CashegisterNo", _sourceJson.getString("cashregisterno"));
			mapdata_Map.put("CashierStaffNo", _sourceJson.getString("cashiername"));
			mapdata_Map.put("TicketAccount", _sourceJson.getString("totalmny"));
			mapdata_Map.put("TicketType", _sourceJson.getString("tickettype"));
			mapdata_Map.put("PostingSign", _sourceJson.getString("transferitemsflg"));
			mapdata_Map.put("GoodAllValue", _sourceJson.getString("totalmny"));
			mapdata_Map.put("ReceivableValue", _sourceJson.getString("recmny"));
			mapdata_Map.put("PaidUpValue", _sourceJson.getString("actualmny"));
			mapdata_Map.put("ChangeValue",  _sourceJson.getString("changemny"));
			if (_sourceJson.getString("platformmny")  != null) {
				mapdata_Map.put("YatangAssessedValue",_sourceJson.getString("platformmny"));
			} else {
				mapdata_Map.put("YatangAssessedValue", 0);
			}
			if (_sourceJson.getString("storemny")  != null) {
				mapdata_Map.put("BusinessAssessedValue",_sourceJson.getString("storemny"));
			} else {
				mapdata_Map.put("BusinessAssessedValue", 0);
			}
			mapdata_Map.put("AllDiscountValue",_sourceJson.getString("totaldisctmny"));
			mapdata_Map.put("AllReducteValue",_sourceJson.getString("totalreducemny"));
			mapdata_Map.put("ProfitLossValue",_sourceJson.getString("mnydiiffer"));

			JSONArray mxListJsonArr = jsonData.getJSONObject("_source").getJSONArray("mxlist");
			JSONObject listdata = new JSONObject();

			JSONArray rowsList = new JSONArray();
			Map<String, Object> goods_map = new HashMap<>();
			JSONObject goods_subJson = new JSONObject();
			for (Object obj : mxListJsonArr) {
				JSONObject jsonObj=JSONObject.parseObject(JSONObject.toJSONString(obj));
				goods_map.put("GoodName", jsonObj.getString("goodsname"));
				goods_map.put("GoodNum", Math.abs(jsonObj.getInteger("quantity")));
				goods_map.put("GoodUnitValue", jsonObj.getString("unitcost"));
				goods_map.put("GoodAllValue", jsonObj.getString("recmny"));
				goods_subJson = StringUtils.replcNULLToStr(goods_map);
				rowsList.add(goods_subJson);
			}
			listdata.put("rows", rowsList);

			JSONArray polistJsonArr = jsonData.getJSONObject("_source").getJSONArray("polist");
			JSONArray ptlistJsonArr = jsonData.getJSONObject("_source").getJSONArray("ptlist");
			
			JSONArray rowsList2 = new JSONArray();
			Map<String, Object> payWay_map = new HashMap<>();
			JSONObject payWay_subJson = new JSONObject();
			for (int i = 0; i < polistJsonArr.size(); i++) {
				JSONObject polistJson = JSONObject.parseObject(JSONObject.toJSONString(polistJsonArr.get(i)));

				String val = polistJson.getString("paywayoneid");
				if (val.equals("4")) {
					for (int j = 0; j < ptlistJsonArr.size(); j++) {
						JSONObject ptlistJson=JSONObject.parseObject(JSONObject.toJSONString(ptlistJsonArr.get(j)));
						if (ptlistJson.getString("paywayoneid").equals(val)) {
							payWay_map.put("PayType",ptlistJson.getString("paywaytwoid"));
							payWay_map.put("EqualMny",ptlistJson.getString("paywaytwomny"));
						}
					}
				} else {
					payWay_map.put("PayType", val);
					payWay_map.put("EqualMny", polistJson.getString("equalmny"));
				}
				payWay_subJson = StringUtils.replcNULLToStr(payWay_map);
				rowsList2.add(payWay_subJson);
			}
			mapdata = StringUtils.replcNULLToStr(mapdata_Map);
			mapdata.put("listdata2", rowsList2);

			json.put("listdata", listdata);
			json.put("mapdata", mapdata);
			json.put("Status", stateJson);
		} else {
			json.put("Status", stateJson);
		}
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

}
