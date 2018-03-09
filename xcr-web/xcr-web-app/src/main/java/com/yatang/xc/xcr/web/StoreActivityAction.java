package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.enums.StoreActivityStateEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.StringUtils;
import com.yatang.xcsm.common.page.PageQuery;
import com.yatang.xcsm.common.response.Response;
import com.yatang.xcsm.remote.api.dto.GoodsSaleDTO;
import com.yatang.xcsm.remote.api.dto.ShopActivityDTO;
import com.yatang.xcsm.remote.api.dto.ShopActivityParamDTO;
import com.yatang.xcsm.remote.api.dto.ShopCouponDTO;
import com.yatang.xcsm.remote.api.dubboxservice.PushShopActivityDubboService;

/**
 * @author
 * @Date 2017年10月17日 下午12:39:34
 * @version 2.4.0
 * @function 门店活动基础类
 */
@Controller
@RequestMapping("/User/")
public class StoreActivityAction {

	private static Logger log = LoggerFactory.getLogger(StoreActivityAction.class);

	@Autowired
	private PushShopActivityDubboService shopActivityDubboService;

	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${INFO_OK}")
	String INFO_OK;

	private static String ACTIVITY_UPDATE_FAILED = "作废未成功，请重试...";
	private static String ACTIVITY_ADD_FAILED = "活动添加失败";
	private static String COUPON_ACTIVITY_ALREADY_EXISTED = "已有活动在进行中，不能继续添加...";
	private static String FOR_TEST_SKIP = "limit";
	private static String DATE_DOTTED_FORMAT = "yyyy.MM.dd";
	private static String EXPRESSION_DESC = "活动名称不能包含特殊字符";

	/**
	 * 
	 * <活动列表数据>
	 * 
	 * @author zhongrun
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("EventList")
	public void eventList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "EventList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK) || jsonTemp.getBooleanValue(FOR_TEST_SKIP)) {

			ShopActivityDTO shopActivityDTO = storeActiveJson2DTO(jsonTemp);
			Long startTime = System.currentTimeMillis();
			log.info("Call queryShopActivityByShopAndStatus Request Data Is:"
					+ JSONObject.toJSONString(shopActivityDTO));
			Response<PageQuery<ShopActivityDTO>> dubboResult = shopActivityDubboService
					.queryShopActivity(shopActivityDTO);
			Long endTime = System.currentTimeMillis();
			log.info("Call queryShopActivityByShopAndStatus Response Data Is:" + JSONObject.toJSONString(dubboResult)
					+ " And Call queryShopActivityByShopAndStatus Cast " + (endTime - startTime));
			JSONObject rowList = dubboList2JsonList(response, jsonTemp, dubboResult);
			json.put("listdata", rowList);
		}
		json.put("Status", stateJson);
		response.getWriter().print(json);
	}

	/**
	 * 更新活动状态
	 * 
	 * @author gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "UpdateEventStatus", method = RequestMethod.POST)
	public void updateEventStatus(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "UpdateEventStatus");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK) || jsonTemp.getBooleanValue(FOR_TEST_SKIP)) {
			ShopActivityParamDTO paramDto = new ShopActivityParamDTO();
			paramDto.setActivityId(jsonTemp.getString("EventId"));
			paramDto.setShopCode(jsonTemp.getString("StoreSerialNo"));
			// 放假数据
			long startTime = System.currentTimeMillis();
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "开始调用活动作废接口:cancelShopActivity\n***********请求数据为：" + JSONObject.toJSONString(paramDto));
			Response<String> result = shopActivityDubboService.cancelShopActivity(paramDto);
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "调用活动作废接口:cancelShopActivity调用结束\n***********响应数据为：" + JSONObject.toJSONString(result)
					+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
			if (!result.isSuccess()) {
				stateJson = CommonUtil.pageStatus2(STATE_ERR, ACTIVITY_UPDATE_FAILED);
			}
		}
		json.put("Status", stateJson);
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

	/**
	 * 添加优惠券活动v2.4
	 * 
	 * @author gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "AddCouponEvent", method = RequestMethod.POST)
	public void addCouponEvent(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "AddCouponEvent");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK)) {
			// 再次调用看看有没有优惠券活动数据，有的情况下就不能调用添加接口了
			Response<PageQuery<ShopActivityDTO>> result = invokeDubboGetActivityList(jsonTemp, 1);
			boolean flag = result.getResultObject().getRows().size() > 0 ? false : true;
			if (flag) {
				// 封装添加优惠券活动的参数
				JSONObject checkJson = checkParam(jsonTemp);
				if (!checkJson.getBooleanValue("flag")) {
					ShopActivityDTO actDto = getAddCouponActivityParam(jsonTemp);
					long startTime = System.currentTimeMillis();
					log.info("\n******于时间：" + DateUtils.getLogDataTime(startTime, null)
							+ "开始调用优惠券活动添加接口:addCouponActivityInfo" + "\n*****请求数据为："
							+ JSONObject.toJSONString(actDto));
					Response<String> addResult = shopActivityDubboService.addCouponActivityInfo(actDto);
					log.info("\n******于时间：" + DateUtils.getLogDataTime(startTime, null)
							+ "调用优惠券活动添加接口:addCouponActivityInfo调用结束" + "\n*****响应数据为："
							+ JSONObject.toJSONString(addResult) + "\n******所花费时间为：" + CommonUtil.costTime(startTime));
					if (addResult.isSuccess()) {
						JSONObject mapdata = new JSONObject();
						mapdata.put("EventId", addResult.getResultObject());
						json.put("mapdata", mapdata);
					} else {
						stateJson = CommonUtil.pageStatus2(STATE_ERR, ACTIVITY_ADD_FAILED);
					}
				} else {
					stateJson = CommonUtil.pageStatus2(STATE_ERR, checkJson.getString("desc"));
				}
			} else {
				stateJson = CommonUtil.pageStatus2(STATE_ERR, COUPON_ACTIVITY_ALREADY_EXISTED);
			}
		}
		json.put("Status", stateJson);
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

	/**
	 * 优惠券活动详情v2.4
	 * 
	 * @author gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "CouponEventDetail", method = RequestMethod.POST)
	public void couponEventDetail(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "CouponEventDetail");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK) || jsonTemp.getBooleanValue(FOR_TEST_SKIP)) {
			// 组装优惠劵活动假数据json=StoreActivityBaseActionMock.getEventCoupondetailMockData(jsonTemp);
			ShopActivityDTO paramDto = getDetailParam(jsonTemp);
			long startTime = System.currentTimeMillis();
			log.info("\n******于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "开始调用优惠券活动详情接口:queryShopActivityCouponDetail" + "\n*****请求数据为："
					+ JSONObject.toJSONString(paramDto));
			Response<ShopActivityDTO> detailResult = shopActivityDubboService.queryShopActivityCouponDetail(paramDto);
			log.info("\n******于时间：" + DateUtils.getLogDataTime(startTime, null)
					+ "调用优惠券活动详情接口:queryShopActivityCouponDetail调用结束\n***********响应数据为："
					+ JSONObject.toJSONString(detailResult) + "\n******所花费时间为：" + CommonUtil.costTime(startTime));

			long statisticsStartTime = System.currentTimeMillis();
			log.info("\n******于时间：" + DateUtils.getLogDataTime(statisticsStartTime, null)
					+ "开始调用优惠券活动详情统计接口:statisticsShopCouponActivity" + "\n*****请求数据为："
					+ JSONObject.toJSONString(paramDto));
			Response<ShopActivityDTO> statisticsResult = shopActivityDubboService
					.statisticsShopCouponActivity(paramDto);
			log.info("\n******于时间：" + DateUtils.getLogDataTime(statisticsStartTime, null)
					+ "调用优惠券活动详情统计接口:statisticsShopCouponActivity调用结束\n***********响应数据为："
					+ JSONObject.toJSONString(statisticsResult) + "\n******所花费时间为："
					+ CommonUtil.costTime(statisticsStartTime));

			if (detailResult.isSuccess() && statisticsResult.isSuccess()) {
				ShopActivityDTO detailDto = detailResult.getResultObject();
				JSONObject swapJson = JSONObject.parseObject(JSONObject.toJSONString(detailDto));
				JSONObject statisticSwapJson = JSONObject
						.parseObject(JSONObject.toJSONString(statisticsResult.getResultObject()));
				JSONObject listdata = new JSONObject();
				Map<String, Object> mapdataMap = new HashMap<>();
				mapdataMap.put("EventId", swapJson.getString("activityId"));
				mapdataMap.put("EventName", detailDto.getActivityName());
				// 当活动状态为4：已作废状态是转换为3：已结束状态，APP只显示已结束，不认识已作废
				Integer activityStatus = detailDto.getActivityStatus();
				if (activityStatus == 4 || DateUtils.sameDate(detailDto.getEndDate(), new Date())==1) {
					activityStatus = 3;
				}
				mapdataMap.put("EventStatus", activityStatus);
				mapdataMap.put("StartDate", DateUtils.dateFormat(detailDto.getStartDate(), DATE_DOTTED_FORMAT));
				mapdataMap.put("EndDate", DateUtils.dateFormat(detailDto.getEndDate(), DATE_DOTTED_FORMAT));
				mapdataMap.put("ReceivedCount", statisticSwapJson.getString("receivedNum"));
				mapdataMap.put("TotalCount", statisticSwapJson.getString("totalNum"));
				mapdataMap.put("UsedCouponCount", statisticSwapJson.getString("usedUserNum"));
				mapdataMap.put("ReceivedCouponCount", statisticSwapJson.getString("receivedUserNum"));

				JSONArray rowsList = new JSONArray();
				for (int i = 0; i < statisticsResult.getResultObject().getCouponList().size(); i++) {
					JSONObject shopCouponTransToJson = JSONObject.parseObject(
							JSONObject.toJSONString(statisticsResult.getResultObject().getCouponList().get(i)));
					JSONObject subRow = new JSONObject();
					subRow.put("CouponBalance", shopCouponTransToJson.getString("discountAmount"));
					subRow.put("Duration", shopCouponTransToJson.getString("periodOfValidity"));
					subRow.put("ReceivedCount", shopCouponTransToJson.getString("receivedNum"));
					subRow.put("UsedCount", shopCouponTransToJson.getString("usedNum"));
					subRow.put("UseCondition", shopCouponTransToJson.getString("orderAmountLimit"));
					rowsList.add(subRow);
				}
				listdata.put("rows", rowsList);
				json.put("mapdata", StringUtils.replcNULLToStr(mapdataMap));
				json.put("listdata", listdata);

			} else {
				stateJson = CommonUtil.pageStatus2(STATE_ERR, StateEnum.STATE_2.getDesc());
			}
		}
		json.put("Status", stateJson);
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

	/**
	 * 是否可以添加优惠券活动v2.4
	 * 
	 * @author gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "CheckCanAddCouponEvent", method = RequestMethod.POST)
	public void checkCanAddCouponEvent(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "checkCanAddCouponEvent");
		JSONObject json = checkActivity(jsonTemp, 1);
		response.getWriter().print(json);
	}

	@RequestMapping(value = "CheckCanAddSpecialPriceEvent", method = RequestMethod.POST)
	public void checkCanAddSpecialPriceEvent(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "CheckCanAddSpecialPriceEvent");
		JSONObject json = checkActivity(jsonTemp, 2);
		response.getWriter().print(json);
	}

	/**
	 * msg={"UserId":"A901004","StoreSerialNo":"jms_901004","Token":"1111",
	 * "EventName":"折扣活动","EventId":"28"} 商品折扣活动详情
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "SpecialPriceEventDetail", method = RequestMethod.POST)
	public void goodsDiscountDetail(@RequestBody String msg, HttpServletResponse response) throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SpecialPriceEventDetail");
		JSONObject json = new JSONObject();
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		if (stateJson.getString("State").equals(STATE_OK)) {
			ShopActivityDTO shActivityDTO = getDetailParam(jsonTemp);
			Long getSignArrayStartTime = System.currentTimeMillis();
			log.info("\n******请求接口GoodsAction.goodsDiscountDetail第一个参数"+ JSONObject.parseObject(JSONObject.toJSONString(shActivityDTO)) + "时间为：" + DateUtils.getLogDataTime(getSignArrayStartTime, null));
			Response<ShopActivityDTO> resDetail = shopActivityDubboService.queryShopActivityGoodsSaleDetail(shActivityDTO);
			log.info("\n******于时间"+DateUtils.getLogDataTime(getSignArrayStartTime, null)+"请求接口queryShopActivityGoodsSaleDetail结束"
					+ "\n*******响应数据为："+JSONObject.toJSONString(resDetail));
			
			
			Response<ShopActivityDTO> resStatistics  = statisticDiscount(shActivityDTO);
			if (resDetail != null && resStatistics  != null && resDetail.isSuccess() && resStatistics.isSuccess()) {
				ShopActivityDTO shopActivityDTO = resDetail.getResultObject();
				Map<String, Object> jsMap = new HashMap<>();
				jsMap.put("EventId", shopActivityDTO.getActivityId());
				jsMap.put("EventName", shopActivityDTO.getActivityName());
				// 当活动状态为4：已作废状态是转换为3：已结束状态，APP只显示已结束，不认识已作废
				Integer eventStatus = shopActivityDTO.getActivityStatus();
				if (eventStatus == 4 || DateUtils.sameDate(shopActivityDTO.getEndDate(), new Date())==1) {
					eventStatus = 3;
				}
				jsMap.put("EventStatus", eventStatus);
				jsMap.put("StartDate", DateUtils.dateFormat(shopActivityDTO.getStartDate(), DATE_DOTTED_FORMAT));
				jsMap.put("EndDate", DateUtils.dateFormat(shopActivityDTO.getEndDate(), DATE_DOTTED_FORMAT));
				
				ShopActivityDTO shopActivityDTOs = resStatistics.getResultObject();
				jsMap.put("TotalSalesNum", shopActivityDTOs.getTotalAmount());//销量
				jsMap.put("SalesBalance", shopActivityDTOs.getTotalSalesAmount());//销售额
				jsMap.put("PromotionBalance", shopActivityDTOs.getDiscoutnAcmout());//优惠金额
				
				
				List<GoodsSaleDTO> list = shopActivityDTO.getGoodsSaleList();
				List<GoodsSaleDTO> goodsSaleList = shopActivityDTOs.getGoodsSaleList();
				JSONArray jsonArray = new JSONArray();
				JSONObject listdata = new JSONObject();
				for (int j = 0; j < list.size(); j++) {
					GoodsSaleDTO goodsSaleDTO=list.get(j);
					Map<String, Object> item = new HashMap<>();
					item.put("GoodsId", goodsSaleDTO.getGoodsId());
					item.put("GoodsCode", goodsSaleDTO.getGoodsCode());
					item.put("GoodsName", goodsSaleDTO.getGoodsName());
					if (goodsSaleDTO.getSpecialPrice() != 0) {
						item.put("DiscountType", "1");
						item.put("SpecialPrice", goodsSaleDTO.getSpecialPrice());
					} else if (goodsSaleDTO.getSpecialDistcount() != 0) {
						item.put("DiscountType", "2");
						item.put("Discount", goodsSaleDTO.getSpecialDistcount());
					}
					item.put("Price", goodsSaleDTO.getOriginalPrice());// 原价
					item.put("UnitName", goodsSaleDTO.getUnit());
					item.put("LimitCount", goodsSaleDTO.getOrderLimit());
					item.put("TotalCount", goodsSaleDTO.getSpecialStock());
					for (int i = 0; i < goodsSaleList.size(); i++) {
						if(goodsSaleList.get(i).getGoodsCode().equals(goodsSaleDTO.getGoodsCode())){
							item.put("GoodsSalesNum", goodsSaleList.get(i).getSalesNum());
							break;
						}
					}
					jsonArray.add(StringUtils.replcNULLToStr(item));
				}
				listdata.put("rows", jsonArray);
				json.put("listdata", listdata);
				json.put("mapdata", StringUtils.replcNULLToStr(jsMap));
				json.put("Status", stateJson);

			} else if (resDetail == null || !resDetail.isSuccess()) {
				json = CommonUtil.pageStatus(json, "M02", "获取数据详情失败");
			} else if (resStatistics == null || !resStatistics.isSuccess()) {
				json = CommonUtil.pageStatus(json, "M02", "获取数据统计信息失败");
			} else {
				json = CommonUtil.pageStatus(json, "M02", "获取数据获取失败");
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
	 * msg={"UserId":"jms_902003","ShopName":"dsd","StoreSerialNo":"A902003","Token":"1111","EventName":"折扣活动2","StartDate":"2017-05-11","EndDate":"2017-06-11","GoodsList":[{"GoodsId":"121","GoodsCode":"232","Price":"22","DiscountType":"1","GoodsName":"faf","SpecialPrice":"20","Discount":"85","LimitCount":"3","TotalCount":"300","UnitName":"袋"}]} 添加商品折扣活动
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "AddSpecialPriceEvent", method = RequestMethod.POST)
	public void addGoodsDiscount(@RequestBody String msg, HttpServletResponse response) throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "AddSpecialPriceEvent");
		JSONObject json = new JSONObject();
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		if (stateJson.getString("State").equals(STATE_OK)) {
			//校验是否包含特殊字符
			JSONObject checkJson = checkParam(jsonTemp);
			if (!checkJson.getBooleanValue("flag")) {
				//请求参数的封装
				ShopActivityDTO shopActivityDTO = new ShopActivityDTO();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				shopActivityDTO.setShopCode(jsonTemp.getString("StoreSerialNo"));
				shopActivityDTO.setActivityName(jsonTemp.getString("EventName"));
				shopActivityDTO.setShopName(jsonTemp.getString("ShopName"));
				String startDateS = jsonTemp.getString("StartDate");
				String endDateS = jsonTemp.getString("EndDate");
				startDateS = startDateS + " 00:00:00";
				endDateS = endDateS + " 23:59:59";

				try {
					shopActivityDTO.setStartDate(dateFormat.parse(startDateS));
					shopActivityDTO.setEndDate(dateFormat.parse(endDateS));
				} catch (Exception e) {
					log.info("接口DiscountActivityAction.addGoodsDiscount日期转换异常，开始日期startDateS:" + startDateS
							+ "结束日期endDateS" + endDateS);
				}
				// shopActivityDTO.setCreateTime("dsd");
				// shopActivityDTO.setLastUpdate("dsd2");
				JSONArray goodsListArray = jsonTemp.getJSONArray("GoodsList");
				List<GoodsSaleDTO> list = new ArrayList<>();
				for (Object object : goodsListArray) {
					GoodsSaleDTO goodsSaleDTO = new GoodsSaleDTO();
					JSONObject js = (JSONObject) object;
					if (js.getString("DiscountType").equals("1")) {// 特价
						goodsSaleDTO.setSpecialPrice(Double.parseDouble(js.getString("SpecialPrice")));
						goodsSaleDTO.setSaleType(1);
					} else if (js.getString("DiscountType").equals("2")) {// 折扣
						goodsSaleDTO.setSpecialDistcount(Double.parseDouble(js.getString("Discount")));
						goodsSaleDTO.setSaleType(2);
					}
					goodsSaleDTO.setSpecialStock(Integer.parseInt(js.getString("TotalCount")));
					goodsSaleDTO.setGoodsCode(js.getString("GoodsCode"));
					goodsSaleDTO.setOrderLimit(Integer.parseInt(js.getString("LimitCount")));
					goodsSaleDTO.setGoodsName(js.getString("GoodsName"));
					goodsSaleDTO.setGoodsImageUrl(js.getString("GoodsPic"));
					goodsSaleDTO.setUnit(js.getString("UnitName"));
					goodsSaleDTO.setOriginalPrice(js.getString("Price"));

					list.add(goodsSaleDTO);
				}
				shopActivityDTO.setGoodsSaleList(list);
				Long getSignArrayStartTime = System.currentTimeMillis();
				log.info("接口GoodsAction.addGoodsDiscount第一个参数"
						+ JSONObject.parseObject(JSONObject.toJSONString(shopActivityDTO)) + "时间为："
						+ getSignArrayStartTime);
				Response<String> res = shopActivityDubboService.addGoodsSaleActivityInfo(shopActivityDTO);
				log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
						+ "调用GoodsAction.addGoodsDiscount接口   调用结束" + "\n*****************响应数据是："
						+ JSONObject.toJSONString(res) + "\n***************所花费时间为："
						+ CommonUtil.costTime(getSignArrayStartTime));
				if (res != null && res.isSuccess()) {
					JSONObject mapdata=new JSONObject();
					mapdata.put("EventId", res.getResultObject());
					json.put("mapdata", mapdata);
					json = CommonUtil.pageStatus(json, STATE_OK, "添加成功");
				} else {
					json = CommonUtil.pageStatus(json, STATE_ERR, "添加失败");
				}
			} else {
				json = CommonUtil.pageStatus(json, STATE_ERR, EXPRESSION_DESC);
			}
		} else {
			json.put("Status", stateJson);
		}
		log.debug("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);

	}

	/**
	 * 统计折扣数据
	 */
	public Response<ShopActivityDTO> statisticDiscount(ShopActivityDTO shopActivityDTO) {
		Long getSignArrayStartTime = System.currentTimeMillis();
		log.info(
				"\n*****************调用statisticDiscount接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
						+ "\n*****************请求数据是：" + JSONObject.toJSONString(shopActivityDTO));
		Response<ShopActivityDTO> response = shopActivityDubboService.statisticsShopGoodsSaleActivity(shopActivityDTO);
		log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
				+ "调用statisticDiscount接口   调用结束" + "\n*****************响应数据是：" + JSONObject.toJSONString(response)
				+ "\n***************所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));
		return response;
	}

	/**
	 * <DTO转json数据>
	 * 
	 * @author zhongrun
	 * @param response
	 * @param jsonTemp
	 * @param dubboResult
	 * @return
	 * @throws IOException
	 */
	private JSONObject dubboList2JsonList(HttpServletResponse response, JSONObject jsonTemp,
			Response<PageQuery<ShopActivityDTO>> dubboResult) throws IOException {
		JSONObject rowList = new JSONObject();
		if (!dubboResult.isSuccess()) {
			log.error("Call queryShopActivityByShopAndStatus Failed!!");
			JSONObject json1 = new JSONObject();
			json1.put("Status", CommonUtil.pageStatus2("M02", "系统异常"));
			response.getWriter().print(json1);
		}

		JSONArray activeList = new JSONArray();
		List<ShopActivityDTO> shopActivityDTOs = dubboResult.getResultObject().getRows();
		for (ShopActivityDTO shopActivityDTO2 : shopActivityDTOs) {
			JSONObject jsonObject = activeTransfer(shopActivityDTO2);
			activeList.add(jsonObject);
		}
		rowList.put("rows", activeList);
		rowList.put("totalcount", dubboResult.getResultObject().getTotal());
		rowList.put("pagesize", jsonTemp.getInteger("PageSize"));
		rowList.put("pageindex", jsonTemp.getInteger("PageIndex"));
		rowList.put("totalpage", dubboResult.getResultObject().getPageCount());
		return rowList;
	}

	/**
	 * <单个数据包装>
	 * 
	 * @author zhongrun
	 * @param shopActivityDTO2
	 * @return
	 */
	private JSONObject activeTransfer(ShopActivityDTO shopActivityDTO2) {
		Map<String, Object> item_map = new HashMap<>();
		item_map.put("EventId", shopActivityDTO2.getActivityId());
		item_map.put("EventType", shopActivityDTO2.getActivityType());
		item_map.put("EventName", shopActivityDTO2.getActivityName());
		item_map.put("StartTime", DateUtils.dateFormat(shopActivityDTO2.getStartDate(), DATE_DOTTED_FORMAT));
		item_map.put("EndTime", DateUtils.dateFormat(shopActivityDTO2.getEndDate(), DATE_DOTTED_FORMAT));
		if (shopActivityDTO2.getActivityStatus() == 4) {
			item_map.put("RemainDays", 0);
		} else {
			item_map.put("RemainDays", (DateUtils.differentDays(new Date(), shopActivityDTO2.getEndDate()) + 1) > 0
					? (DateUtils.differentDays(new Date(), shopActivityDTO2.getEndDate()) + 1) : 0);
		}
		item_map.put("IsNewUserCanUse", Integer.parseInt(
				shopActivityDTO2.getIsNewLimitString() == null ? "0" : shopActivityDTO2.getIsNewLimitString()) == 1 ? 1
						: 0);
		JSONObject jsonObject = StringUtils.replcNULLToStr(item_map);
		return jsonObject;
	}

	/**
	 * <查询条件转换>
	 * 
	 * @author zhongrun
	 * @param jsonTemp
	 * @return
	 */
	private ShopActivityDTO storeActiveJson2DTO(JSONObject jsonTemp) {
		String condition = jsonTemp.getString("EventStatus");
		ShopActivityDTO shopActivityDTO = new ShopActivityDTO();
		shopActivityDTO.setShopCode(jsonTemp.getString("StoreSerialNo"));
		shopActivityDTO.setPageNumber(Integer.parseInt(jsonTemp.getString("PageIndex")));
		shopActivityDTO.setPageSize(Integer.parseInt(jsonTemp.getString("PageSize")));
		// Switch Condition
		if ("3".equals(condition)) {
			condition = "3,4";
		}
		shopActivityDTO.setActivityStatusArray(condition);
		return shopActivityDTO;
	}

	/**
	 * <调用深圳C端服务获得店铺优惠券活动列表>】
	 * 
	 * @author gaodawei
	 * @param jsonTemp
	 * @return
	 */
	private Response<PageQuery<ShopActivityDTO>> invokeDubboGetActivityList(JSONObject jsonTemp, Integer activityType) {
		ShopActivityDTO paramDto = new ShopActivityDTO();
		paramDto.setShopCode(jsonTemp.getString("StoreSerialNo"));
		// 组合参数，为了获得是否有已在活动中的优惠券活动，请求状态为1未开始、2进行中状态，活动类型为1（优惠券活动类型）的数据
		paramDto.setActivityStatusArray(StoreActivityStateEnum.STORE_ACT_STATE_1.getCode() + ","
				+ StoreActivityStateEnum.STORE_ACT_STATE_2.getCode());
		paramDto.setActivityStatus(StoreActivityStateEnum.STORE_ACT_STATE_1.getCode());
		paramDto.setActivityType(activityType);
		long startTime = System.currentTimeMillis();
		log.info("\n******于时间：" + DateUtils.getLogDataTime(startTime, null) + "开始调用活动列表接口:queryShopActivity"
				+ "\n******请求数据为：" + JSONObject.toJSONString(paramDto));
		Response<PageQuery<ShopActivityDTO>> result = shopActivityDubboService.queryShopActivity(paramDto);
		log.info("\n******于时间：" + DateUtils.getLogDataTime(startTime, null) + "调用活动列表接口:queryShopActivity调用结束"
				+ "\n*****响应数据为：" + JSONObject.toJSONString(result) + "\n******所花费时间为："
				+ CommonUtil.costTime(startTime));
		return result;
	}

	/**
	 * <获得添加优惠券活动的参数>
	 * 
	 * @author gaodawei
	 * @param jsonTemp
	 * @return
	 */
	private ShopActivityDTO getAddCouponActivityParam(JSONObject jsonTemp) {
		ShopActivityDTO actDto = new ShopActivityDTO();
		actDto.setShopCode(jsonTemp.getString("StoreSerialNo"));
		actDto.setShopName(jsonTemp.getString("ShopName"));
		actDto.setActivityName(jsonTemp.getString("EventName"));
		actDto.setStartDate(jsonTemp.getDate("StartDate"));
		actDto.setEndDate(DateUtils.stringToDefaultDateFormat(jsonTemp.getString("EndDate") + " 23:59:59"));
		actDto.setChannelList(new ArrayList<Integer>());
		actDto.setTotalNum(jsonTemp.getIntValue("TotalCount"));
		actDto.setLimitType(jsonTemp.getIntValue("CountForEveryOne"));
		actDto.setIsNewLimitString(jsonTemp.getIntValue("IsNewUserCanUse") == 1 ? "1" : "2");
		List<ShopCouponDTO> list = new ArrayList<>();
		for (int i = 0; i < jsonTemp.getJSONArray("CouponList").size(); i++) {
			JSONObject couponJson = (JSONObject) jsonTemp.getJSONArray("CouponList").get(i);
			ShopCouponDTO shopCouponDto = new ShopCouponDTO();
			shopCouponDto.setDiscountAmount(couponJson.getDoubleValue("CouponBalance"));
			shopCouponDto.setOrderAmountLimit(couponJson.getDoubleValue("UseCondition"));
			shopCouponDto.setPeriodOfValidity(couponJson.getIntValue("Duration"));
			list.add(shopCouponDto);
		}
		actDto.setCouponList(list);
		return actDto;
	}

	/**
	 * 校验特殊字符，表情符号
	 * gaodawei
	 * @param jsonTemp
	 * @return false没有包含
	 */
	private JSONObject checkParam(JSONObject jsonTemp) {
		JSONObject checkResultJson = new JSONObject();
		checkResultJson.put("flag", false);
		if (CommonUtil.containsEmoji(jsonTemp.getString("EventName"))) {
			checkResultJson.put("flag", true);
			checkResultJson.put("desc", EXPRESSION_DESC);
		}
		return checkResultJson;
	}

	/**
	 * 获得请求详情的请求参数
	 * 
	 * @param jsonTemp
	 * @return
	 */
	private ShopActivityDTO getDetailParam(JSONObject jsonTemp) {
		ShopActivityDTO shActivityDTO = new ShopActivityDTO();
		shActivityDTO.setActivityId(jsonTemp.getString("EventId"));
		shActivityDTO.setShopCode(jsonTemp.getString("StoreSerialNo"));
		return shActivityDTO;
	}

	/**
	 * 判断领卷活动/折扣活动有无
	 * @param jsonTemp
	 * @return
	 */
	private JSONObject checkActivity(JSONObject jsonTemp, Integer activityType) {
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK) || jsonTemp.getBooleanValue(FOR_TEST_SKIP)) {
			Response<PageQuery<ShopActivityDTO>> result = invokeDubboGetActivityList(jsonTemp, activityType);
			/*
			 * 上面接口请求优惠券活动在未开始或者进行中的状态有无数据 如果有则向APP返回0，不能添加优惠券活动了
			 * 否则向APP返回1可以添加优惠券活动
			 */
			if (result.isSuccess()) {
				Integer flag = result.getResultObject().getRows().size() > 0 ? 0 : 1;
				JSONObject mapdata = new JSONObject();
				mapdata.put("CanAddEvent", flag);
				json.put("mapdata", mapdata);
			} else {
				stateJson = CommonUtil.pageStatus2(STATE_ERR, StateEnum.STATE_2.getDesc());
			}
		}
		json.put("Status", stateJson);
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		return json;
	}
}
