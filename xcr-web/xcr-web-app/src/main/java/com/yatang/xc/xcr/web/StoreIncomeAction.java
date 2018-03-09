package com.yatang.xc.xcr.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseFinanceByShopAndDateDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseFinanceByShopAndDateWithXcOrderDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseIncomesAndProfitByShopInSevenDayDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseIncomesAndProfitByShopInSevenDayDto.DateAndValue;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.PriceUtil;
import com.yatang.xc.xcr.web.thread.OnlineIncomeCallable;
import com.yatang.xc.xcr.web.thread.StoreIncomeCallable;

/**
 * 
* 门店收入
*		
* @author: zhongrun
* @version: 1.0, 2017年8月14日
 */
@Controller
@RequestMapping("/User/")
public class StoreIncomeAction extends ApplicationObjectSupport{
	private static Logger log = LoggerFactory.getLogger(StoreIncomeAction.class);
	
	@Autowired
	private DataCenterXcrDubboService dataCenterXcrDubboService;
	
	
	@Value("${SYSTEM_CODE}")
	private String SYSTEM_CODE;
	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_OUTDATE}")
	String STATE_OUTDATE;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${INFO_OK}")
	String INFO_OK;
	
	@RequestMapping("RevenueOrOutDetial")
	public void revenueOrOutDetial(@RequestBody String msg,
			HttpServletResponse response) throws Exception{
		Long s = System.currentTimeMillis();
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "RevenueOrOutDetial");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			String storeSerialNo = jsonTemp.getString("StoreSerialNo");
			String screenStatue = jsonTemp.getString("ScreenStatue");
			JSONObject condition = allTimejson2condition(jsonTemp,screenStatue);
			String type = jsonTemp.getString("Type");
			//Store Income
			if ("1".equals(type)) {
				Long ss = System.currentTimeMillis();
				//Store Sevenday Income And Profit
				Response<ResponseIncomesAndProfitByShopInSevenDayDto>  sevenday = dataCenterXcrDubboService.queryIncomesAndProfitByShopInSevenDay(storeSerialNo);				
				Long ee = System.currentTimeMillis();
				log.info("RevenueOrOutDetial Access queryIncomesAndProfitByShopInSevenDay Time Is:"+(ee-ss)+"_And Data Is:"+JSONObject.toJSONString(sevenday));
				//Current Day-->Yesterday-->Current Month-->Sevenday Before-->Thirtyday Before
				if ("1".equals(screenStatue)) {	
				JSONObject resultJson = transferTimeInfo(sevenday.getResultObject(),condition);
				json.put("mapdata", resultJson);			
				}
				//User Define Time
				else {								
					Long sss = System.currentTimeMillis();
					ResponseFinanceByShopAndDateDto  definedTimeDubboResult = dataCenterXcrDubboService.queryFinanceByShopAndDate(storeSerialNo, condition.getString("start"), condition.getString("end")).getResultObject();
					Long eee = System.currentTimeMillis();
					log.info("RevenueOrOutDetial Access queryFinanceByShopAndDate Time Is:"+(eee-sss)+"_And Data Is:"+JSONObject.toJSONString(definedTimeDubboResult));
					JSONObject resultJson = storeDefinedDetaildubboJson2VOJson(definedTimeDubboResult,sevenday.getResultObject());		
					json.put("mapdata", resultJson);	
				}			
			}
			//Online Income
			else if ("2".equals(type)) {
				Long ss = System.currentTimeMillis();
				//Online Sevenday Income And Profit
				Response<ResponseIncomesAndProfitByShopInSevenDayDto> responseIncomesAndProfitByShopInSevenDayDto = dataCenterXcrDubboService.queryIncomesAndProfitByShopInSevenDayWithXcOrder(storeSerialNo);
				Long ee = System.currentTimeMillis();
				log.info("RevenueOrOutDetial Access queryIncomesAndProfitByShopInSevenDayWithXcOrder Time Is:"+(ee-ss)+"_And Data Is:"+JSONObject.toJSONString(responseIncomesAndProfitByShopInSevenDayDto));
				//Current Day-->Yesterday-->Current Month-->Sevenday Before-->Thirtyday Before
				if ("1".equals(screenStatue)) {					
					JSONObject resultJson = transferTimeInfoInTwo(responseIncomesAndProfitByShopInSevenDayDto.getResultObject(),condition);
					json.put("mapdata", resultJson);						
				}
				//User Define Time
				else {
					Long sss = System.currentTimeMillis();
					Response<ResponseFinanceByShopAndDateWithXcOrderDto> definedTimeDubboResult = dataCenterXcrDubboService.queryFinanceByShopAndDateWithXcOrder(storeSerialNo, condition.getString("start"), condition.getString("end"));
					Long eee = System.currentTimeMillis();
					log.info("RevenueOrOutDetial Access queryFinanceByShopAndDateWithXcOrder Time Is:"+(eee-sss)+"_And Data Is:"+JSONObject.toJSONString(definedTimeDubboResult));						
					JSONObject resultJson = orderDefinedDetaildubbo(definedTimeDubboResult.getResultObject(),responseIncomesAndProfitByShopInSevenDayDto.getResultObject());	
					json.put("mapdata", resultJson);	
				}		
			}	
		}
		
		json.put("Status", stateJson);
		Long e = System.currentTimeMillis();
		log.info("RevenueOrOutDetial Wast Total Time is"+(e-s));
		response.getWriter().print(json);
	}

	/**
	 * 
	* 根据条件转换时间为json信息
	*
	* @param jsonTemp
	* @param screenStatue
	* @return
	 */
	private JSONObject allTimejson2condition(JSONObject jsonTemp,String screenStatue) {
		JSONObject timeStart = new JSONObject();
					
		if ("1".equals(screenStatue)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date systemDay = new Date();
			String yesterDay = simpleDateFormat.format(DateUtils
					.getDateBefore(systemDay, 1));
			String currentDate = simpleDateFormat.format(new Date()).toString();
			
			String currentMonth = DateUtils.getFirstMonthDay(simpleDateFormat);
			
			String sevenDayBefore = simpleDateFormat.format(DateUtils
					.getDateBefore(systemDay, 7));
			String thirtyDayBefore = simpleDateFormat.format(DateUtils
					.getDateBefore(systemDay, 30));				
			timeStart.put("yesterDay", yesterDay);
			timeStart.put("currentDate", currentDate);
			timeStart.put("currentMonth", currentMonth);
			timeStart.put("sevenDayBefore", sevenDayBefore);
			timeStart.put("thirtyDayBefore", thirtyDayBefore);	
		}else if ("5".equals(screenStatue)) {
			String start = jsonTemp.getString("StartDate");
			String end = jsonTemp.getString("EndDate");
			timeStart.put("start", start);
			timeStart.put("end", end);
		}
		timeStart.put("StoreSerialNo", jsonTemp.getString("StoreSerialNo"));
		return timeStart;
	}

	private JSONObject transferTimeInfo(
			ResponseIncomesAndProfitByShopInSevenDayDto sevenday,JSONObject condition)
			throws InterruptedException, ExecutionException {
		ExecutorService es=Executors.newFixedThreadPool(5);
		
		/**
		 * Yesterday 
		 */
		ApplicationContext applicationContext2 = getApplicationContext();
		StoreIncomeCallable yester = (StoreIncomeCallable)applicationContext2.getBean(StoreIncomeCallable.class);
		yester.setEndTime(condition.getString("yesterDay"));
		yester.setStartTime(condition.getString("yesterDay"));
		yester.setStoreNo(condition.getString("StoreSerialNo"));
		
		/**
		 * Today 
		 */
		StoreIncomeCallable currentD = (StoreIncomeCallable)getApplicationContext().getBean("storeIncomeCallable");
		currentD.setEndTime(condition.getString("currentDate"));
		currentD.setStartTime(condition.getString("currentDate"));
		currentD.setStoreNo(condition.getString("StoreSerialNo"));		
		/**
		 * Current Month
		 */
		StoreIncomeCallable currentM = (StoreIncomeCallable)getApplicationContext().getBean("storeIncomeCallable");
		currentM.setEndTime(condition.getString("currentDate"));
		currentM.setStartTime(condition.getString("currentMonth"));
		currentM.setStoreNo(condition.getString("StoreSerialNo"));		
		/**
		 * Seven Day
		 */
		StoreIncomeCallable sevenD = (StoreIncomeCallable)getApplicationContext().getBean("storeIncomeCallable");
		sevenD.setEndTime(condition.getString("yesterDay"));
		sevenD.setStartTime(condition.getString("sevenDayBefore"));
		sevenD.setStoreNo(condition.getString("StoreSerialNo"));	
		
		/**
		 * Thirty Day
		 */
		StoreIncomeCallable thirtyD = (StoreIncomeCallable)getApplicationContext().getBean("storeIncomeCallable");
		thirtyD.setEndTime(condition.getString("yesterDay"));
		thirtyD.setStartTime(condition.getString("thirtyDayBefore"));
		thirtyD.setStoreNo(condition.getString("StoreSerialNo"));	
		
		//Multithreaded Execution
		List<Future<ResponseFinanceByShopAndDateDto>> futureList = new ArrayList<Future<ResponseFinanceByShopAndDateDto>>();
		futureList.add(es.submit(currentD));
		futureList.add(es.submit(yester));
		futureList.add(es.submit(currentM));
		futureList.add(es.submit(sevenD));
		futureList.add(es.submit(thirtyD));
		List<ResponseFinanceByShopAndDateDto> reList = new ArrayList<ResponseFinanceByShopAndDateDto>();
		
		for (Future<ResponseFinanceByShopAndDateDto> future : futureList) {
			reList.add(future.get());
		}
		
		JSONObject resultJson = storeRevenueDetaildubboJson2VOJson(reList,sevenday);
		es.shutdown();
		return resultJson;
	}
	
	private JSONObject transferTimeInfoInTwo(
			ResponseIncomesAndProfitByShopInSevenDayDto sevenday,JSONObject condition)
			throws InterruptedException, ExecutionException {
		ExecutorService es=Executors.newFixedThreadPool(5);
		/**
		 * Yesterday 
		 */
		OnlineIncomeCallable yester = (OnlineIncomeCallable)getApplicationContext().getBean("onlineIncomeCallable");
		yester.setEndTime(condition.getString("yesterDay"));
		yester.setStartTime(condition.getString("yesterDay"));
		yester.setStoreNo(condition.getString("StoreSerialNo"));
		/**
		 * Today 
		 */
		OnlineIncomeCallable currentD = (OnlineIncomeCallable)getApplicationContext().getBean("onlineIncomeCallable");
		currentD.setEndTime(condition.getString("currentDate"));
		currentD.setStartTime(condition.getString("currentDate"));
		currentD.setStoreNo(condition.getString("StoreSerialNo"));
		/**
		 * Current Month
		 */
		OnlineIncomeCallable currentM = (OnlineIncomeCallable)getApplicationContext().getBean("onlineIncomeCallable");
		currentM.setStoreNo(condition.getString("StoreSerialNo"));
		currentM.setStartTime(condition.getString("currentMonth"));
		currentM.setEndTime(condition.getString("currentDate"));
		/**
		 * Seven Day
		 */
		OnlineIncomeCallable sevenD = (OnlineIncomeCallable)getApplicationContext().getBean("onlineIncomeCallable");
		sevenD.setStoreNo(condition.getString("StoreSerialNo"));
		sevenD.setStartTime(condition.getString("sevenDayBefore"));
		sevenD.setEndTime(condition.getString("yesterDay"));	
		
		/**
		 * Thirty Day
		 */
		OnlineIncomeCallable thirtyD = (OnlineIncomeCallable)getApplicationContext().getBean("onlineIncomeCallable");
		thirtyD.setStoreNo(condition.getString("StoreSerialNo"));
		thirtyD.setStartTime(condition.getString("thirtyDayBefore"));
		thirtyD.setEndTime(condition.getString("yesterDay"));	
		
		
		List<Future<ResponseFinanceByShopAndDateWithXcOrderDto>> futureList = new ArrayList<Future<ResponseFinanceByShopAndDateWithXcOrderDto>>();
		futureList.add(es.submit(currentD));
		futureList.add(es.submit(yester));
		futureList.add(es.submit(currentM));
		futureList.add(es.submit(sevenD));
		futureList.add(es.submit(thirtyD));
		
		
		List<ResponseFinanceByShopAndDateWithXcOrderDto> list = new ArrayList<ResponseFinanceByShopAndDateWithXcOrderDto>();
		for (Future<ResponseFinanceByShopAndDateWithXcOrderDto> future : futureList) {
			list.add(future.get());
		}
		
		JSONObject resultJson = orderRevenueDetaildubboJson2VOJsonInTwo(list,sevenday);
		es.shutdown();
		return resultJson;
	}
	
	/**
	 * 
	* 封装七天收入与店铺收入信息
	*
	* @param map
	* @param sevenday
	* @return
	 */
	private JSONObject storeRevenueDetaildubboJson2VOJson(List<ResponseFinanceByShopAndDateDto> list,ResponseIncomesAndProfitByShopInSevenDayDto sevenday) {
		JSONObject jsonResult = new JSONObject();			
		JSONArray rowsListincome = new JSONArray();	
		JSONArray rowsListprofit = new JSONArray();
		transferRevenueList(list, jsonResult);
	
		if (sevenday==null) {
			for (int i = 0; i < 7; i++) {
				rowsListincome.add(0.00);
			}
			for (int i = 0; i < 7; i++) {
				rowsListprofit.add(0.0);
			}
			jsonResult.put("ProfitSevenDay", rowsListprofit);
			jsonResult.put("RevenueSevenDay", rowsListincome);
			
			return jsonResult;
			
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");		

		orderSevendayIncome(sevenday, rowsListincome, simpleDateFormat);
		
		orderSevendayProfit(sevenday, rowsListprofit, simpleDateFormat);
		jsonResult.put("ProfitSevenDay", rowsListprofit);
		jsonResult.put("RevenueSevenDay", rowsListincome);
		return jsonResult;
	}
	/**
	 * 
	* 封装店铺数组信息
	*
	* @param map
	* @param jsonResult
	 */
	private void transferRevenueList(List<ResponseFinanceByShopAndDateDto> list, JSONObject jsonResult) {
		JSONArray rowsListTime = new JSONArray();	
		for (ResponseFinanceByShopAndDateDto responseFinanceByShopAndDateDto : list) {
			rowsListTime.add(transferTimeInfo(responseFinanceByShopAndDateDto));
		}
		
		jsonResult.put("RevenueList", rowsListTime);
	}
	
	private JSONObject orderRevenueDetaildubboJson2VOJsonInTwo(List<ResponseFinanceByShopAndDateWithXcOrderDto> list,ResponseIncomesAndProfitByShopInSevenDayDto sevenday) {
		JSONArray rowsListTime = new JSONArray();		
		JSONArray rowsListincome = new JSONArray();
		JSONArray rowsListprofit = new JSONArray();
		
		JSONObject jsonResult = orderRevenueDetailJson2VO(list, rowsListTime);
		
		jsonResult.put("RevenueList", rowsListTime);
	
		if (sevenday==null) {
			for (int i = 0; i < 7; i++) {
				rowsListincome.add(0.00);
			}
			for (int i = 0; i < 7; i++) {
				rowsListprofit.add(0.0);
			}
			jsonResult.put("ProfitSevenDay", rowsListprofit);
			jsonResult.put("RevenueSevenDay", rowsListincome);
			
			return jsonResult;
			
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");		

		orderSevendayIncome(sevenday, rowsListincome, simpleDateFormat);
		
		orderSevendayProfit(sevenday, rowsListprofit, simpleDateFormat);
		
		jsonResult.put("ProfitSevenDay", rowsListprofit);
		jsonResult.put("RevenueSevenDay", rowsListincome);
		return jsonResult;
	}

	/**
	 * 
	* 封装七天利润信息
	*
	* @param sevenday
	* @param rowsListprofit
	* @param simpleDateFormat
	 */
	private void orderSevendayProfit(ResponseIncomesAndProfitByShopInSevenDayDto sevenday, JSONArray rowsListprofit,
			SimpleDateFormat simpleDateFormat) {
		List<DateAndValue> sevendayProfits = sevenday.getProfit();
	
		sevendayTransfer(rowsListprofit, simpleDateFormat, sevendayProfits);
	}

	/**
	 * 
	* 七天收入信息封装
	*
	* @param rowsListprofit
	* @param simpleDateFormat
	* @param sevendayProfits
	 */
	private void sevendayTransfer(JSONArray rowsListprofit, SimpleDateFormat simpleDateFormat,
			List<DateAndValue> sevendayProfits) {
		if (!sevendayProfits.isEmpty()) {
			//Transformation SevenDay Profit
			int size = sevendayProfits.size();
			//List Pointer
			Integer index = 0;
			for (int i = 6; i >=0; i--) {	
				if (index>=size) {
					rowsListprofit.add(0.0);
				}else{
			for (Integer j = index;j<size;) {
					String sevenDayTime = simpleDateFormat.format(DateUtils
							.getDateBefore(new Date(), i+1));
					if (sevenDayTime.equals(sevendayProfits.get(j).getDate())) {
						rowsListprofit.add(Double.parseDouble(sevendayProfits.get(j).getValue()));
						index++;
						break;
					}else {
						rowsListprofit.add(0.0);
						break;
					}					
				}	
			}
			}
		}else {
			for (int i = 0; i < 7; i++) {
				rowsListprofit.add(0.0);
			}
		}
	}

	/**
	 * 
	* 封装七天收入信息
	*
	* @param sevenday
	* @param rowsListincome
	* @param simpleDateFormat
	 */
	private void orderSevendayIncome(ResponseIncomesAndProfitByShopInSevenDayDto sevenday, JSONArray rowsListincome,
			SimpleDateFormat simpleDateFormat) {
		List<DateAndValue> sevendayIncomes = sevenday.getIncomes();				
		sevendayTransfer(rowsListincome, simpleDateFormat, sevendayIncomes);
	}

	private JSONObject orderRevenueDetailJson2VO(List<ResponseFinanceByShopAndDateWithXcOrderDto> list,
			JSONArray rowsListTime) {
		JSONObject jsonResult = new JSONObject();
		for (ResponseFinanceByShopAndDateWithXcOrderDto responseFinanceByShopAndDateWithXcOrderDto : list) {
			rowsListTime.add(transferTimeInfoInTwo(responseFinanceByShopAndDateWithXcOrderDto));
		}
		
		return jsonResult;
	}
	
	private JSONObject storeDefinedDetaildubboJson2VOJson(ResponseFinanceByShopAndDateDto definedTime,ResponseIncomesAndProfitByShopInSevenDayDto sevenday) {
		JSONObject jsonResult = new JSONObject();
		JSONArray rowsListTime = new JSONArray();		
		JSONArray rowsListincome = new JSONArray();
		JSONArray rowsListprofit = new JSONArray();
		
		JSONObject yesterDay = transferTimeInfo(definedTime);
		rowsListTime.add(yesterDay);
		jsonResult.put("RevenueList", rowsListTime);
	
		if (sevenday==null) {
			for (int i = 0; i < 7; i++) {
				rowsListincome.add(0.00);
			}
			for (int i = 0; i < 7; i++) {
				rowsListprofit.add(0.0);
			}
			jsonResult.put("ProfitSevenDay", rowsListprofit);
			jsonResult.put("RevenueSevenDay", rowsListincome);
			
			return jsonResult;
			
		}
			
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		
		orderSevendayIncome(sevenday, rowsListincome, simpleDateFormat);
		
		orderSevendayProfit(sevenday, rowsListprofit, simpleDateFormat);
		
		jsonResult.put("ProfitSevenDay", rowsListprofit);
		jsonResult.put("RevenueSevenDay", rowsListincome);
		return jsonResult;
	}
	
	/**
	 * 
	* 外送dubbo信息转换成前端json
	*
	* @param definedTime
	* @param sevenday
	* @return
	* @throws Exception
	 */
	private JSONObject orderDefinedDetaildubbo(ResponseFinanceByShopAndDateWithXcOrderDto definedTime,ResponseIncomesAndProfitByShopInSevenDayDto sevenday) throws Exception {
		JSONObject jsonResult = new JSONObject();
		JSONArray rowsListTime = new JSONArray();		
		JSONArray rowsListincome = new JSONArray();
		JSONArray rowsListprofit = new JSONArray();
		
		JSONObject definedDay = transferTimeInfoInTwo(definedTime);
		rowsListTime.add(definedDay);
		jsonResult.put("RevenueList", rowsListTime);
	
		if (sevenday==null) {
			for (int i = 0; i < 7; i++) {
				rowsListincome.add(0.00);
			}
			for (int i = 0; i < 7; i++) {
				rowsListprofit.add(0.0);
			}
			
			jsonResult.put("ProfitSevenDay", rowsListprofit);
			jsonResult.put("RevenueSevenDay", rowsListincome);			
			return jsonResult;		
		}
		
				
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		
		orderSevendayIncome(sevenday, rowsListincome, simpleDateFormat);
		
		orderSevendayProfit(sevenday, rowsListprofit, simpleDateFormat);
		
		jsonResult.put("ProfitSevenDay", rowsListprofit);
		jsonResult.put("RevenueSevenDay", rowsListincome);
		return jsonResult;
	}

	private JSONObject transferTimeInfo(ResponseFinanceByShopAndDateDto definedTime) {
		JSONObject dataJson = new JSONObject();
		if (definedTime==null) {
			dataJson.put("RevenueAllValue", 0.0);
			dataJson.put("RevenueZhifubao", 0.0);
			dataJson.put("RevenueWeixin", 0.0);
			dataJson.put("CouponCash", 0.0);		
			dataJson.put("RevenueCash", 0.0);
			dataJson.put("ProfitValue", 0.0);
			dataJson.put("UnionPayScan", 0.0);
		}else {
			dataJson.put("RevenueAllValue", PriceUtil.formatPrice(definedTime.getIncome()));
			dataJson.put("RevenueZhifubao", PriceUtil.formatPrice(definedTime.getAlipayIncome()));
			dataJson.put("RevenueWeixin", PriceUtil.formatPrice(definedTime.getWechatIncome()));
			dataJson.put("CouponCash", PriceUtil.formatPrice(definedTime.getCouponIncome()));		
			dataJson.put("RevenueCash", PriceUtil.formatPrice(definedTime.getCashIncome()));
			dataJson.put("ProfitValue", PriceUtil.formatPrice(definedTime.getProfit()));
			dataJson.put("UnionPayScan", 0.0);
		}
		return dataJson;
	}
	/**
	 * 
	* 外送收入自定义时间封装
	*
	* @param definedTime
	* @return
	 */
	private JSONObject transferTimeInfoInTwo(ResponseFinanceByShopAndDateWithXcOrderDto definedTime) {
		JSONObject dataJson = new JSONObject();
		if (definedTime==null) {
			dataJson.put("RevenueAllValue", 0.0);
			dataJson.put("DeliveryFee", 0.0);
			dataJson.put("GoodsNum", 0.0);
			dataJson.put("Coupon", 0.0);		
			dataJson.put("ProfitValue", 0.0);
			dataJson.put("UnionPayScan", 0.0);
			
		}else {
			dataJson.put("RevenueAllValue", PriceUtil.formatPrice(definedTime.getTotalMny()));
			dataJson.put("DeliveryFee", PriceUtil.formatPrice(definedTime.getDeliveryFee()));
			dataJson.put("GoodsNum", PriceUtil.formatPrice(definedTime.getPrdRecMny()));
			dataJson.put("Coupon", PriceUtil.formatPrice(definedTime.getDiscount()));		
			dataJson.put("ProfitValue", PriceUtil.formatPrice(definedTime.getProfit()));
			dataJson.put("UnionPayScan", 0.0);
		}
		return dataJson;
	}
	

}
