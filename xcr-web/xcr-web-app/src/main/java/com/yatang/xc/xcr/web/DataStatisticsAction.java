package com.yatang.xc.xcr.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.busi.common.resp.Response;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.QueryProductWithOrderByDto;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.StringUtils;
/**
 * 数据统计v2.0->v2.3添加单位
 * @author gaodawei
 * 2017年6月30日(星期五)
 */
@Controller
@RequestMapping("/User/")
public class DataStatisticsAction {

	private static Logger log = LoggerFactory.getLogger(DataStatisticsAction.class);

	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	
	@Autowired
	private DataCenterXcrDubboService dataCenterXcrDubboService;
	/**
	 * 统计列表v2.0
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "StatisticsList", method = RequestMethod.POST)
	public void statisticsList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg,"StatisticsList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK)) {
			//封装假数据listdata
//			json=DataStatisticsActionMockData.getStatisticsListMockData(jsonTemp);
			QueryProductWithOrderByDto dto=getParamDto(jsonTemp);
			Long saleStartTime=System.currentTimeMillis();
			log.info("\n*****调用数据中心queryProductOrderBySales接口的开始时间："+DateUtils.getLogDataTime(saleStartTime, null)
			+"\n*****请求数据是："+JSONObject.toJSONString(dto));
			Response<?> saleResult = dataCenterXcrDubboService.queryProductOrderBySales(dto);
			log.info("\n*****于时间:"+DateUtils.getLogDataTime(saleStartTime, null)+"调用数据中心queryProductOrderBySales接口   调用结束"
					+"\n*****响应数据是："+JSONObject.toJSONString(saleResult)
					+ "\n*****所花费时间为：" + CommonUtil.costTime(saleStartTime));
			Long profitStartTime=System.currentTimeMillis();
			log.info("\n*****调用数据中心queryProductOrderByProfit接口的开始时间："+DateUtils.getLogDataTime(profitStartTime, null)
			+"\n*****请求数据是："+JSONObject.toJSONString(dto));
			Response<?> profitResult = dataCenterXcrDubboService.queryProductOrderByProfit(dto);
			log.info("\n*****于时间:"+DateUtils.getLogDataTime(profitStartTime, null)+"调用数据中心queryProductOrderByProfit接口   调用结束"
					+"\n*****响应数据是："+JSONObject.toJSONString(profitResult)
					+ "\n*****所花费时间为：" + CommonUtil.costTime(profitStartTime));
			
			if(saleResult!=null && profitResult!=null){
				if(saleResult.isSuccess() && profitResult.isSuccess()){
					JSONObject mapdata=new JSONObject();
					JSONArray rowsList01 = new JSONArray();
					JSONArray rowsList02 = new JSONArray();
					Map<String, Object> MerchandiseSales_Map=new HashMap<>();
					Map<String, Object> MerchandiseProfits_Map=new HashMap<>();
					JSONObject subJson = new JSONObject();
					JSONArray resultSaleJson=JSONObject.parseObject(JSONObject.toJSONString(saleResult.getResultObject())).getJSONArray("products");
					JSONArray resultProfitJson=JSONObject.parseObject(JSONObject.toJSONString(profitResult.getResultObject())).getJSONArray("products");
					if(resultSaleJson!=null && resultProfitJson!=null){
						int saleSize=resultSaleJson.size()>5?5:resultSaleJson.size();
						int ProfitSize=resultProfitJson.size()>5?5:resultProfitJson.size();
						for (int i = 0; i < saleSize; i++) {
							JSONObject sub=JSONObject.parseObject(resultSaleJson.get(i).toString());
							MerchandiseSales_Map.put("GoodsName", sub.getString("name"));
							MerchandiseSales_Map.put("GoodsCode", sub.getString("internationalCode"));
							MerchandiseSales_Map.put("GoodsSaleVaule", sub.getIntValue("sale"));
							MerchandiseSales_Map.put("GoodsUnit", sub.getString("unit"));
							subJson=StringUtils.replcNULLToStr(MerchandiseSales_Map);
							rowsList01.add(subJson);
						}
						for (int i = 0; i < ProfitSize; i++) {
							JSONObject sub=JSONObject.parseObject(resultProfitJson.get(i).toString());
							MerchandiseProfits_Map.put("GoodsName", sub.getString("name"));
							MerchandiseProfits_Map.put("GoodsCode", sub.getString("internationalCode"));
							MerchandiseProfits_Map.put("GoodsProfitsVaule", sub.getDoubleValue("profit"));
							MerchandiseProfits_Map.put("GoodsUnit", sub.getString("unit"));
							subJson=StringUtils.replcNULLToStr(MerchandiseProfits_Map);
							rowsList02.add(subJson);
						}
					}
					mapdata.put("MerchandiseSales", rowsList01);
					mapdata.put("MerchandiseProfits", rowsList02);
					
					json.put("mapdata", mapdata);
					json.put("Status", stateJson);
				}else{
					if(!saleResult.isSuccess()){
						json=CommonUtil.pageStatus(json, STATE_ERR, saleResult.getErrorMessage());
					}
					if(!profitResult.isSuccess()){
						json=CommonUtil.pageStatus(json, STATE_ERR, profitResult.getErrorMessage());
					}
				}
			}else{
				json=CommonUtil.pageStatus(json, STATE_ERR, "服务器异常");
			}
		}else{
			json.put("Status", stateJson);
		}
		log.info("\n**********于"+ DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！"
				+"\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为："+ CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}
	/**
	 * 统计详情列表v2.0->v2.3添加单位
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "StatisticsDetialList", method = RequestMethod.POST)
	public void statisticsDetialList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg,"StatisticsDetialList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		
		//用于定义放回APP的json
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK)) {
//			json=DataStatisticsActionMockData.getStatisticsDetialListMockData(jsonTemp);//假数据
			QueryProductWithOrderByDto dto=getParamDto(jsonTemp);
			Response<?> result=null;
			long startTime=System.currentTimeMillis();
			switch(jsonTemp.getIntValue("StatisticsType")){
			case 1:
				log.info("\n*****调用数据中心queryProductOrderBySales接口的开始时间："+DateUtils.getLogDataTime(startTime, null)
				+"\n*****请求数据是："+JSONObject.toJSONString(dto));
				result = dataCenterXcrDubboService.queryProductOrderBySales(dto);
				break;
			case 2:
				log.info("\n*****调用数据中心queryProductOrderByProfit接口的开始时间："+DateUtils.getLogDataTime(startTime, null)
				+"\n*****请求数据是："+JSONObject.toJSONString(dto));
				result = dataCenterXcrDubboService.queryProductOrderByProfit(dto);
				break;
			default:
				log.info("\n*****于时间:"+DateUtils.getLogDataTime(startTime, null)+"查询失败，输入条件StatisticsType有误");
				json=CommonUtil.pageStatus(json, STATE_ERR, "查询条件出错");
				response.getWriter().print(json);
				return;
			}
			log.info("\n*****于时间:"+DateUtils.getLogDataTime(startTime, null)+"调用的数据中心接口   调用结束"
					+"\n*****响应数据是："+JSONObject.toJSONString(result)
					+ "\n*****所花费时间为：" + CommonUtil.costTime(startTime));
			if(result!=null){
				if(result.isSuccess()){
					JSONObject listdata=new JSONObject();
					int startIndex=(jsonTemp.getIntValue("PageIndex")-1)*jsonTemp.getIntValue("PageSize");
					JSONArray productsJsonArr=JSONObject.parseObject(JSONObject.toJSONString(result.getResultObject())).getJSONArray("products");
					JSONArray rowsList = new JSONArray();
					Map<String, Object> sub_Map=new HashMap<>();
					JSONObject tempSwapJson=new JSONObject();
					int endTag=0;
					endTag=(productsJsonArr.size()>(jsonTemp.getIntValue("PageIndex")*jsonTemp.getIntValue("PageSize")))?
							(jsonTemp.getIntValue("PageIndex")*jsonTemp.getIntValue("PageSize")):productsJsonArr.size();
					if(productsJsonArr!=null){
						for (int i = startIndex; i < endTag; i++) {
							tempSwapJson=JSONObject.parseObject(productsJsonArr.get(i).toString());
							sub_Map.put("GoodsName", tempSwapJson.getString("name"));
							sub_Map.put("GoodsCode", tempSwapJson.getString("internationalCode"));
							sub_Map.put("GoodsUnit", tempSwapJson.getString("unit"));
							switch(jsonTemp.getIntValue("StatisticsType")){
								case 1:
									sub_Map.put("GoodsVaule", tempSwapJson.getIntValue("sale"));
									break;
								case 2:
									sub_Map.put("GoodsVaule", tempSwapJson.getDoubleValue("profit"));
									break;
								default:
									sub_Map.put("GoodsVaule", 0);
									break;
							}
							tempSwapJson=StringUtils.replcNULLToStr(sub_Map);
							rowsList.add(tempSwapJson);
						}
					}
					listdata.put("rows", rowsList);
					listdata=CommonUtil.pagePackage(listdata, jsonTemp, JSONObject.parseObject(JSONObject.toJSONString(result.getResultObject())).getIntValue("total"), null);
					json.put("listdata", listdata);
					json.put("Status", stateJson);
				}else{
					json=CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
				}
			}else{
				json=CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
			}
		}else{
			json.put("Status", stateJson);
		}
		log.info("\n**********于"+ DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！"
				+"\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为："+ CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}
	/**
	 * 获取筛选条件的方法
	 * @param jsonTemp
	 * @return
	 */
	public QueryProductWithOrderByDto getParamDto(JSONObject jsonTemp){
		QueryProductWithOrderByDto dto=new QueryProductWithOrderByDto();
		dto.setShopCode(jsonTemp.getString("StoreSerialNo"));
		if(jsonTemp.getString("Sort")!=null){
			dto.setSort(jsonTemp.getIntValue("Sort"));
		}else{
			dto.setSort(0);
		}
		if(jsonTemp.getString("StatisticsType")!=null){
			dto.setTotal(100);
		}else{
			dto.setTotal(5);
		}
		switch(jsonTemp.getIntValue("ScreenStatue")){
		case 0:
			dto.setStartDay(DateUtils.getDateBefore(new Date(), 1,"yyyy-MM-dd"));
			dto.setEndDay(DateUtils.getDateBefore(new Date(), 1,"yyyy-MM-dd"));
			break;
		case 1:
			dto.setStartDay(DateUtils.dateSimpleFormat(new Date()));
			dto.setEndDay(DateUtils.dateSimpleFormat(new Date()));
			break;
		case 2:
			dto.setStartDay(DateUtils.getFirstMonthDay(new SimpleDateFormat("yyyy-MM-dd")));
			dto.setEndDay(DateUtils.dateSimpleFormat(new Date()));
			break;
		case 3:
			dto.setStartDay(DateUtils.getDateBefore(new Date(), 7,"yyyy-MM-dd"));
			dto.setEndDay(DateUtils.getDateBefore(new Date(), 1,"yyyy-MM-dd"));
			break;
		case 4:
			dto.setStartDay(DateUtils.getDateBefore(new Date(), 30,"yyyy-MM-dd"));
			dto.setEndDay(DateUtils.getDateBefore(new Date(), 1,"yyyy-MM-dd"));
			break;
		case 5:
			dto.setStartDay(jsonTemp.getString("StartDate"));
			dto.setEndDay(jsonTemp.getString("EndDate"));
			break;
		default:
			dto.setStartDay(DateUtils.dateSimpleFormat(new Date()));
			dto.setEndDay(DateUtils.dateSimpleFormat(new Date()));
			break;
		}
		return dto;
	}
	
}
