package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.yatang.xc.xcr.enums.DeliveryEnum;
import com.yatang.xc.xcr.enums.OrderStateEnum;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.StringUtils;
import com.yatang.xcsm.common.page.PageQuery;
import com.yatang.xcsm.common.response.Response;
import com.yatang.xcsm.remote.api.dto.PushCancelItemDTO;
import com.yatang.xcsm.remote.api.dto.PushCancelOrderDTO;
import com.yatang.xcsm.remote.api.dto.PushCommerceItemDTO;
import com.yatang.xcsm.remote.api.dto.PushOrderDTO;
import com.yatang.xcsm.remote.api.dto.PushOrderStateLogDTO;
import com.yatang.xcsm.remote.api.dto.PushQueryOrderDTO;
import com.yatang.xcsm.remote.api.dubboxservice.PushCancelDubboxService;
import com.yatang.xcsm.remote.api.dubboxservice.PushOrderDubboxService;
/** 
 * @author gaodawei 
 * @Date 2017年7月25日 下午3:00:43 
 * @version 1.0.0
 * @function 外送订单管理类
 */
@Controller
@RequestMapping("/User/")
public class OrderMngAction {

	private static Logger log = LoggerFactory.getLogger(OrderMngAction.class);
	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${INFO_OK}")
	String INFO_OK;

	@Autowired
	private PushOrderDubboxService pOrderDubboxService;
	@Autowired
	private PushCancelDubboxService cancelDubboxService;

	private static final String DELIVERY_CODE_101="101";
	private static final String DELIVERY_CODE_102="102";
	private static final String DELIVERY_CODE_103="103";
	private static final String DELIVERY_CODE_104="104";

	private static final String DISCOUNT_TYPE_PLAT="平台优惠券";
	private static final String DISCOUNT_TYPE_STORE="店铺优惠券";
	private static final String DISCOUNT_TYPE_NEW_CLIENT="新用户首单立减";
	private static final String DISCOUNT_TYPE_FIRST_ORDER_EVERYDAY="每天首单立减";
	private static final String DISCOUNT_TYPE_FIRST_ORDER="首单立减";

	private static final String RETURN_DESC="退货金额等于退货商品总金额减去优惠金额（优惠金额分摊到每一件商品）";

	private static final String GET_ORDER_LIST_FAILED="网络出错，获取订单列表失败";
	private static final String UPDATE_FAILED="更新失败，请重试...";
	private static final String UPDATE_SVIP_FAILED="该订单为超级会员订单，暂无法操作";
	

	/**
	 * 订单列表V1.2
	 * 订单列表V2.1
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "OrderList", method = RequestMethod.POST)
	public void orderList(@RequestBody String msg,HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("*********getAttribute:[]",request.getAttribute("msg"));
		log.info("*********getAttribute:[]",request.getSession().getAttribute("msg"));
		JSONObject jsonTemp = CommonUtil.methodBefore(msg,"OrderList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			PushQueryOrderDTO queryOrderDTO = packOrderListParam(jsonTemp);
			if(!StringUtils.isEmpty(jsonTemp.getString("OrderType"))){
				//默认请求查询订单的数据
				queryOrderDTO.setState(jsonTemp.getIntValue("OrderType"));
				Long startTime2=System.currentTimeMillis();
				log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime2, null)+"开始调用订单列表接口:queryPageOrderList"
						+"\n***********请求数据为："+JSONObject.toJSONString(queryOrderDTO));
				Response<PageQuery<PushOrderDTO>> result=pOrderDubboxService.queryPageOrderList(queryOrderDTO);
				log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime2, null)+"调用的订单接口：queryPageOrderList 调用结束\n***********响应数据为："+JSONObject.toJSONString(result)
				+"\n***************所花费时间为：" + CommonUtil.costTime(startTime2));
				if(result.isSuccess() && result.getResultObject()!=null){
					json=packSaleOrderList(stateJson, jsonTemp, result);
				}else{
					json=CommonUtil.pageStatus(json, STATE_ERR, GET_ORDER_LIST_FAILED);
				}
			}else if(!StringUtils.isEmpty(jsonTemp.getString("RefusedType"))){
				PushQueryOrderDTO pushQueryOrderDTO=JSONObject.parseObject(JSONObject.parseObject(JSONObject.toJSONString(queryOrderDTO)).toString(), PushQueryOrderDTO.class);
				pushQueryOrderDTO.setState(jsonTemp.getIntValue("RefusedType"));
				//开始请求退货订单的数据
				Long startTime=System.currentTimeMillis();
				log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"开始调用退货订单列表接口:queryList"
						+"\n***********请求数据为："+JSONObject.toJSONString(pushQueryOrderDTO));
				Response<PageQuery<PushCancelOrderDTO>> cancelResult=cancelDubboxService.queryList(pushQueryOrderDTO);
				log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"调用的订单接口：queryList 调用结束\n***********响应数据为："+JSONObject.toJSONString(cancelResult)
				+"\n***************所花费时间为：" + CommonUtil.costTime(startTime));
				if(cancelResult.isSuccess() && cancelResult.getResultObject()!=null){
					json=packReturnOrderList(stateJson, jsonTemp, cancelResult);
				}else{
					json=CommonUtil.pageStatus(json, STATE_ERR, GET_ORDER_LIST_FAILED);
				}
			}else{
				log.info("\n**********请求参数出错，OrderType为："+jsonTemp.getString("OrderType")+"  RefusedType为："+jsonTemp.getString("RefusedType"));
				json=CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
			}
		}else{
			json.put("Status", stateJson);
		}
		log.debug("\n**********于"+ DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！"
				+"\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为："+ CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}
	/**
	 * 订单详情V1.2
	 * 订单详情V2.1
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "OrderDetail", method = RequestMethod.POST)
	public void orderDetail(@RequestBody String msg,HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg,"OrderDetail");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			Long startTime=System.currentTimeMillis();
			log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"开始调用订单接口:queryOrderByOrderNo\n***********请求数据为："+jsonTemp.getString("OrderNo"));
			Response<PushOrderDTO> result=pOrderDubboxService.queryOrderByOrderNo(jsonTemp.getString("OrderNo"));
			log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"调用订单接口:queryOrderByOrderNo调用结束\n***********响应数据为："+JSONObject.toJSONString(result)
			+"\n***************所花费时间为：" + CommonUtil.costTime(startTime));
			if(result.isSuccess()){
				json = packOrderDetail(stateJson, result);
			}else{
				json=CommonUtil.pageStatus2(STATE_ERR, StateEnum.STATE_2.getDesc());
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
	 * 订单详情-退货 V2.2
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "OrderReturnDetial", method = RequestMethod.POST)
	public void orderReturnDetial(@RequestBody String msg,HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg,"OrderReturnDetial");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			Long startTime=System.currentTimeMillis();
			log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"开始调用订单接口:getCancelOrderDetail\n***********请求数据为："+jsonTemp.getString("CancelId"));
			Response<PushCancelOrderDTO> result=cancelDubboxService.getCancelOrderDetail(jsonTemp.getString("CancelId"));
			log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"调用订单接口:getCancelOrderDetail调用结束\n***********响应数据为："+JSONObject.toJSONString(result)
			+"\n***************所花费时间为：" + CommonUtil.costTime(startTime));
			if(result.isSuccess()){
				json = packReturnOrderDetail(stateJson, result);
			}else{
				json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
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
	 * 修改订单状态1.2
	 * 修改订单状态2.1
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "UpdateOrder", method = RequestMethod.POST)
	public void updateOrder(@RequestBody String msg, HttpServletResponse response) throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg,"UpdateOrder");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			PushOrderStateLogDTO orderStateLogDTO=new PushOrderStateLogDTO();
			PushCancelOrderDTO pustCancenOrderDto=new PushCancelOrderDTO();
			switch(jsonTemp.getIntValue("Action")){
			case 1:
				orderStateLogDTO.setState(3);
				break;
			case 2:
				orderStateLogDTO.setState(6);
				break;
			case 3:
				orderStateLogDTO.setState(4);
				break;
			case 4:
				orderStateLogDTO.setState(31);
				break;
			case 5:
				orderStateLogDTO.setState(33);
				break;
			case 6:
				pustCancenOrderDto.setCancelState(204);
				break;
			case 7:
				pustCancenOrderDto.setCancelState(203);
				break;
			}
			if(!StringUtils.isEmpty(jsonTemp.getString("OrderNo"))){
				orderStateLogDTO.setOrderNo(jsonTemp.getString("OrderNo"));
				orderStateLogDTO.setOperator(jsonTemp.getString("UserId"));
				if(jsonTemp.getString("RefusedMsg")!=null){
					orderStateLogDTO.setNote(jsonTemp.getString("RefusedMsg"));
				}else if(jsonTemp.getString("ReasonType")!=null){//兼容2.0版本字段
					orderStateLogDTO.setNote(jsonTemp.getString("ReasonType"));
				}
				Long startTime=System.currentTimeMillis();
				log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"开始调用订单接口:syncOrderState\n***********请求数据为："+JSONObject.toJSONString(orderStateLogDTO));
				Response<String> result=pOrderDubboxService.syncOrderState(orderStateLogDTO);
				log.info("\n***********于时间："+DateUtils.getLogDataTime(startTime, null)+"调用订单接口:syncOrderState调用结束\n***********响应数据为："+JSONObject.toJSONString(result)
				+"\n***************所花费时间为：" + CommonUtil.costTime(startTime));
				if(result.isSuccess()){
					json.put("Status", stateJson);
				}else{
					if(jsonTemp.getIntValue("Action")==1){
						json=CommonUtil.pageStatus(json, STATE_ERR, "接单失败,请重试");
					}else{
						if(result.getErrorMessage().contains(UPDATE_SVIP_FAILED)){
							json=CommonUtil.pageStatus(json, STATE_ERR, UPDATE_SVIP_FAILED);
						}else{
							json=CommonUtil.pageStatus(json, STATE_ERR, UPDATE_FAILED);
						}
					}
				}
			}else if(!StringUtils.isEmpty(jsonTemp.getString("CancelId"))){
				pustCancenOrderDto.setCancelId(jsonTemp.getString("CancelId"));
				pustCancenOrderDto.setCancelReason(jsonTemp.getString("RefusedMsg"));
				Long cancelStartTime=System.currentTimeMillis();
				log.info("\n***********于时间："+DateUtils.getLogDataTime(cancelStartTime, null)+"开始调用订单接口:updateCancelOrder"
						+ "\n***********请求数据为："+JSONObject.toJSONString(pustCancenOrderDto));
				Response<String> cancelResult=cancelDubboxService.updateCancelOrder(pustCancenOrderDto);
				log.info("\n***********于时间："+DateUtils.getLogDataTime(cancelStartTime, null)+"调用订单接口:updateCancelOrder调用结束"
						+ "\n***********响应数据为："+JSONObject.toJSONString(cancelResult)
						+"\n***************所花费时间为：" + CommonUtil.costTime(cancelStartTime));
				if(cancelResult.isSuccess()){
					json.put("Status", stateJson);
				}else{
					json=CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
				}
			}else{
				json=CommonUtil.pageStatus(json, STATE_ERR, UPDATE_FAILED);
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
	 * 组装订单列表请求参数
	 * @param jsonTemp
	 * @return
	 */
	private PushQueryOrderDTO packOrderListParam(JSONObject jsonTemp) {
		PushQueryOrderDTO queryOrderDTO=new PushQueryOrderDTO();
		queryOrderDTO.setShopId(jsonTemp.getString("StoreSerialNo"));
		queryOrderDTO.setStartTime(jsonTemp.getDate("StartDate"));
		queryOrderDTO.setEndTime(jsonTemp.getDate("EndDate"));
		queryOrderDTO.setPageNumber(jsonTemp.getInteger("PageIndex"));
		queryOrderDTO.setPageSize(jsonTemp.getInteger("PageSize"));
		return queryOrderDTO;
	}
	/**
	 * 销售订单列表响应数据
	 * @param stateJson
	 * @param jsonTemp
	 * @param result
	 */
	private JSONObject packSaleOrderList(JSONObject stateJson, JSONObject jsonTemp,Response<PageQuery<PushOrderDTO>> result) {
		JSONObject json=new JSONObject();
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		List<PushOrderDTO> jsonSource=result.getResultObject().getRows();
		for (int i = 0; i < jsonSource.size(); i++) {
			Map<String, Object> aOrderMap=driverInfoConvert(jsonSource.get(i));
			aOrderMap=deliveryInfoConvert(jsonSource.get(i),aOrderMap);
			aOrderMap.put("CancelId", "");//v2.2
			aOrderMap.put("SortNo", jsonSource.get(i).getSequenceNumber());//v2.1
			aOrderMap.put("OrderNo", jsonSource.get(i).getOrderNo());
			if(jsonSource.get(i).getRefundState()==null || Integer.valueOf(OrderStateEnum.ORDER_STATE_4.getState())==jsonSource.get(i).getState()){
				aOrderMap.put("OrderStatue", ""+jsonSource.get(i).getState());
			}else{
				aOrderMap.put("OrderStatue", ""+jsonSource.get(i).getRefundState()+jsonSource.get(i).getState());
			}
			aOrderMap.put("OrderType", ""+jsonSource.get(i).getState());

			aOrderMap.put("CreateOrderTime", jsonSource.get(i).getCreateTime());
			aOrderMap.put("PayType", jsonSource.get(i).getPayway());
			aOrderMap.put("AccountName", jsonSource.get(i).getConsignee());
			aOrderMap.put("Phone", jsonSource.get(i).getConsigneeTel());
			aOrderMap.put("Address", jsonSource.get(i).getAddress());
			aOrderMap.put("OrderValue", jsonSource.get(i).getAmount());
			if(jsonSource.get(i).getRemainingTime()==null){
				aOrderMap.put("ResidualOrderTime", "");
			}else{
				aOrderMap.put("ResidualOrderTime", jsonSource.get(i).getRemainingTime()/1000/60);//v2.1 剩余接单时间
			}
			List<PushCommerceItemDTO> picRelList = jsonSource.get(i).getCommerceItems();
			int quality=0;
			JSONArray picList=new JSONArray();
			for (int j = 0; j < picRelList.size(); j++) {
				JSONObject jsonTemp_2=new JSONObject();
				if(picRelList.get(j).getGoodsImgUrl() != null){
					jsonTemp_2.put("PicUrl", picRelList.get(j).getGoodsImgUrl());
				}else{
					jsonTemp_2.put("PicUrl", "");
				}
				picList.add(jsonTemp_2);
				quality+=picRelList.get(j).getGoodsNumber();
			}
			aOrderMap.put("GoodsNum", quality);

			aOrderMap.put("PicList", picList);
			//v2.2配送员相关信息
			aOrderMap.put("ReturnOrderTime", "");
			aOrderMap.put("ReturnOrderPrice", "");
			aOrderMap.put("PicList", picList);
			//2.6.1 VIP标识
			aOrderMap.put("IsSuperVipOrder", jsonSource.get(i).getVip()==1?1:0);
			rowsList.add(StringUtils.replcNULLToStr(aOrderMap));
		}
		listdata.put("rows", rowsList);
		listdata=CommonUtil.pagePackage(listdata, jsonTemp, result.getResultObject().getTotal(), null);
		JSONObject extendJson=JSONObject.parseObject(JSONObject.toJSONString(result.getExtendObject()));
		Map<String, Object> mapdataMap=new HashMap<>();
		mapdataMap.put("NotRecivedNum",extendJson.getIntValue("statePayment"));
		mapdataMap.put("NotDeliveryNum", extendJson.getIntValue("stateDelivery"));
		mapdataMap.put("ShippedNum", extendJson.getIntValue("stateReceive"));//v2.1
		mapdataMap.put("ReturnedNum", extendJson.getIntValue("stateCancel"));//v2.2
		json.put("mapdata", StringUtils.replcNULLToStr(mapdataMap));
		json.put("listdata", listdata);
		json.put("Status", stateJson);
		return json;
	}
	/**
	 * 退货订单列表响应数据
	 * @param stateJson
	 * @param jsonTemp
	 * @param cancelResult
	 */
	private JSONObject packReturnOrderList(JSONObject stateJson, JSONObject jsonTemp,Response<PageQuery<PushCancelOrderDTO>> cancelResult) {
		JSONObject json=new JSONObject();
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		List<PushCancelOrderDTO> jsonSource=cancelResult.getResultObject().getRows();
		for (int i = 0; i < jsonSource.size(); i++) {
			Map<String, Object> aOrderMap=driverInfoConvert(jsonSource.get(i));
			aOrderMap=deliveryInfoConvert(jsonSource.get(i),aOrderMap);
			aOrderMap.put("CancelId", jsonSource.get(i).getCancelId());//v2.2
			aOrderMap.put("SortNo", jsonSource.get(i).getSequenceNumber());//v2.1
			aOrderMap.put("OrderNo", jsonSource.get(i).getHistoryOrderNo());
			aOrderMap.put("OrderStatue", jsonSource.get(i).getCancelState());
			aOrderMap.put("OrderType", jsonSource.get(i).getCancelState());
			aOrderMap.put("CreateOrderTime", jsonSource.get(i).getCreateTime());
			aOrderMap.put("ResidualOrderTime", "");
			aOrderMap.put("PayType", jsonSource.get(i).getPayway());
			aOrderMap.put("OrderValue", "");
			aOrderMap.put("AccountName", jsonSource.get(i).getConsignee());
			aOrderMap.put("Phone", jsonSource.get(i).getConsigneeTel());
			aOrderMap.put("Address", jsonSource.get(i).getAddress());
			//v2.2配送员相关信息
			aOrderMap.put("ReturnOrderTime", jsonSource.get(i).getCreateTime());
			aOrderMap.put("ReturnOrderPrice", jsonSource.get(i).getCancelAmount());
			List<PushCancelItemDTO> picRelList = jsonSource.get(i).getItems();
			int quality=0;
			JSONArray picList=new JSONArray();
			for (int j = 0; j < picRelList.size(); j++) {
				JSONObject tempObj=new JSONObject();
				if(picRelList.get(j).getGoodsImgUrl() != null){
					tempObj.put("PicUrl", picRelList.get(j).getGoodsImgUrl());
				}else{
					tempObj.put("PicUrl", "");
				}
				picList.add(tempObj);
				quality+=picRelList.get(j).getNumber();
			}
			aOrderMap.put("GoodsNum", quality);
			aOrderMap.put("PicList", picList);
			aOrderMap.put("IsSuperVipOrder", jsonSource.get(i).getVip()==1?1:0);
			rowsList.add(StringUtils.replcNULLToStr(aOrderMap));
		}
		listdata.put("rows", rowsList);
		listdata=CommonUtil.pagePackage(listdata, jsonTemp, cancelResult.getResultObject().getTotal(), null);
		Map<String, Object> mapdataMap=new HashMap<>();
		mapdataMap.put("NotRecivedNum","");
		mapdataMap.put("NotDeliveryNum","");
		mapdataMap.put("ShippedNum", "");//v2.1
		mapdataMap.put("ReturnedNum", cancelResult.getExtendObject());//v2.2
		json.put("mapdata", StringUtils.replcNULLToStr(mapdataMap));
		json.put("listdata", listdata);
		json.put("Status", stateJson);
		return json;
	}

	/**订单详情响应数据
	 * @param stateJson
	 * @param json
	 * @param result
	 * @param rowsList
	 */
	private JSONObject packOrderDetail(JSONObject stateJson, Response<PushOrderDTO> result) {
		JSONObject json=new JSONObject();
		JSONArray rowsList = new JSONArray();
		JSONObject listdata=new JSONObject();
		PushOrderDTO object=result.getResultObject();
		List<PushCommerceItemDTO> items=object.getCommerceItems();
		//用于存储此订单商品总件数
		int quality=0;
		for (int i = 0; i < items.size(); i++) {
			Map<String, Object> itemMap=packGoodsInfo(items.get(i));
			rowsList.add(StringUtils.replcNULLToStr(itemMap));
			quality+=items.get(i).getGoodsNumber();
		}
		listdata.put("rows", rowsList);
		Map<String, Object> mdMap=new HashMap<>();
		mdMap=driverInfoConvert(object);
		mdMap=deliveryInfoConvert(object,mdMap);
		if(object.getRefundState()==null){
			mdMap.put("OrderStatue", ""+object.getState());
			mdMap.put("CancleOrderMsg", "");//v2.1
		}else{
			mdMap.put("OrderStatue", ""+object.getRefundState()+object.getState());
			mdMap.put("CancleOrderMsg", object.getRefuseReason());//v2.1
		}
		mdMap.put("SortNo", object.getSequenceNumber());//v2.2
		mdMap.put("OrderStatus", ""+object.getState());
		mdMap.put("TotalNum", quality);
		mdMap.put("GoodAllValue", object.getProductAmount());
		mdMap.put("DeliveryFee", object.getDeliveryFee());
		mdMap.put("FreeDeliveryFee", object.getFreeDeliveryFee());//v2.5.1
		mdMap.put("Discount", object.getDiscount());
		mdMap.put("PaidUpValue", Double.parseDouble(object.getAmount().toString()));
		mdMap.put("PayType", object.getPayway());
		mdMap.put("AccountName", object.getConsignee());
		mdMap.put("Phone", object.getConsigneeTel());
		mdMap.put("Address", object.getAddress());
		mdMap.put("OrderNo", object.getOrderNo());
		mdMap.put("CreateOrderTime", object.getCreateTime());
		mdMap.put("Remarks", object.getBuyerMessage());
		//v2.6.1 是否是超级会员字段
		mdMap.put("IsSuperVipOrder", object.getVip()==1?1:0);
		if(object.getRemainingTime()!=null){
			int residualOrderTime=(int) (object.getRemainingTime()/60000);
			mdMap.put("ResidualOrderTime", residualOrderTime>0?residualOrderTime:0);//v2.5.1
		}else{
			mdMap.put("ResidualOrderTime", 0);//v2.5.1
		}
		mdMap.put("DiscountList", getDiscountDetail(JSONObject.toJSONString(object)));
		json.put("mapdata", StringUtils.replcNULLToStr(mdMap));
		json.put("listdata", listdata);
		json.put("Status", stateJson);
		return json;
	}
	/**退货订单详情响应数据
	 * @param stateJson
	 * @param json
	 * @param result
	 * @param rowsList
	 */
	private JSONObject packReturnOrderDetail(JSONObject stateJson, Response<PushCancelOrderDTO> result) {
		JSONObject json=new JSONObject();
		JSONArray rowsList = new JSONArray();
		JSONObject listdata=new JSONObject();
		PushCancelOrderDTO object=result.getResultObject();
		List<PushCancelItemDTO> items=object.getItems();
		//用于存储此订单商品总件数
		int quality=0;
		for (int i = 0; i < items.size(); i++) {
			Map<String, Object> itemMap = packGoodsInfo(items.get(i));
			rowsList.add(StringUtils.replcNULLToStr(itemMap));
			quality+=items.get(i).getNumber();
		}
		listdata.put("rows", rowsList);
		Map<String, Object> mapdata_map=new HashMap<>();
		mapdata_map.put("SortNo", object.getSequenceNumber());//v2.2
		mapdata_map.put("ReturnReason", ""+object.getCancelReason());
		mapdata_map.put("OrderStatue", ""+object.getCancelState());
		mapdata_map.put("OrderReturnTime", object.getCreateTime());
		mapdata_map.put("ReturnNum", quality);
		mapdata_map.put("GoodAllValue", object.getProductAmount());
		mapdata_map.put("ReturnValue", Double.parseDouble(object.getCancelAmount().toString()));
		mapdata_map.put("ReturnToast", RETURN_DESC);
		mapdata_map.put("AccountName", object.getConsignee());
		mapdata_map.put("Phone", object.getConsigneeTel());
		mapdata_map.put("Address", object.getAddress());
		mapdata_map.put("OrderNo", object.getHistoryOrderNo());
		mapdata_map.put("CancelId", object.getCancelId());
		mapdata_map.put("DeliveryCode", object.getShippingWay());
		//v2.6.1 vip标识
		mapdata_map.put("IsSuperVipOrder", object.getVip()==1?1:0);
		mapdata_map.put("DiscountList", getDiscountDetail(JSONObject.toJSONString(object)));
		json.put("mapdata", StringUtils.replcNULLToStr(mapdata_map));
		json.put("listdata", listdata);
		json.put("Status", stateJson);
		return json;
	}
	/**组装一个商品的信息
	 * @param obj
	 * @return
	 */
	private Map<String, Object> packGoodsInfo(Object obj) {
		JSONObject goodsObj=JSONObject.parseObject(JSONObject.toJSONString(obj));
		Map<String, Object> itemMap=new HashMap<>();
		itemMap.put("GoodName", goodsObj.getString("goodsName"));
		itemMap.put("GoodNum", goodsObj.getInteger("goodsNumber")==null?goodsObj.getInteger("number"):goodsObj.getInteger("goodsNumber"));
		itemMap.put("GoodUnitValue", goodsObj.getDouble("price"));
		itemMap.put("GoodAllValue", goodsObj.getDouble("itemTotalAmount"));
		itemMap.put("PicUrl", goodsObj.getString("goodsImgUrl"));
		if(goodsObj.getInteger("atyType")==-1 
				|| goodsObj.getInteger("atyType")==2
				|| goodsObj.getInteger("atyType")==3
				|| goodsObj.getInteger("atyType")==4){
			itemMap.put("atyType", -1); 
			itemMap.put("GoodAllDisValue", goodsObj.getDouble("itemTotalAmount")); 
		}else{
			itemMap.put("atyType", 1); 
			itemMap.put("GoodAllDisValue",  goodsObj.getDouble("itemSaleTotalAmount")); 
		}
		return itemMap;
	}
	/**
	 * 包装店铺优惠详细信息列表
	 * @param object
	 * @return
	 */
	private JSONArray getDiscountDetail(String str){
		JSONObject object = JSONObject.parseObject(str);
		//v2.4.0新增的店铺优惠信息
		JSONArray discountArr = new JSONArray();
		if(object.getIntValue("activityType")==2
				|| object.getIntValue("activityType")==3
				|| object.getIntValue("activityType")==4){
			if(object.getJSONObject("discountInfo").getDoubleValue("otherDiscount")!=0){
				JSONObject otherJson=new JSONObject();
				switch(object.getIntValue("activityType")){
				case 3:
					otherJson.put("DiscountName", DISCOUNT_TYPE_FIRST_ORDER_EVERYDAY);
					break;
				case 4:
					otherJson.put("DiscountName", DISCOUNT_TYPE_FIRST_ORDER);
					break;
				default:
					otherJson.put("DiscountName", DISCOUNT_TYPE_NEW_CLIENT);
					break;
				}
				otherJson.put("Discount", object.getJSONObject("discountInfo").getDoubleValue("otherDiscount"));
				discountArr.add(otherJson);
			}
		}
		if(object.getJSONObject("discountInfo").getDoubleValue("platformDiscount")!=0){
			JSONObject platJson=new JSONObject();
			platJson.put("DiscountName", DISCOUNT_TYPE_PLAT);
			platJson.put("Discount", object.getJSONObject("discountInfo").getDoubleValue("platformDiscount"));
			discountArr.add(platJson);
		}
		if(object.getJSONObject("discountInfo").getDoubleValue("shopDiscount")!=0){
			JSONObject shopJson=new JSONObject();
			shopJson.put("DiscountName", DISCOUNT_TYPE_STORE);
			shopJson.put("Discount",object.getJSONObject("discountInfo").getDoubleValue("shopDiscount"));
			discountArr.add(shopJson);
		}
		return discountArr;
	}
	/**
	 * 配送员的信息
	 * @param jsonSource
	 * @param aOrderMap
	 * @param i
	 */
	private Map<String, Object> driverInfoConvert(Object obj) {
		JSONObject driverJson=JSONObject.parseObject(JSONObject.toJSONString(obj));
		Map<String, Object> driverMap=new HashMap<>();
		driverMap.put("DistributorName", driverJson.getString("driverName"));
		driverMap.put("DistributorPhone",driverJson.getString("driverPhone"));
		driverMap.put("DeliveryTime", driverJson.getString("driverGetTime"));
		driverMap.put("SucTime", driverJson.getString("driverToTime"));
		return driverMap;
	}
	/**配送信息转换
	 * @param dto
	 * @param deliveryMap
	 * @return
	 */
	private Map<String, Object> deliveryInfoConvert(Object dto,Map<String, Object> deliveryMap) {
		JSONObject deliveryJson=JSONObject.parseObject(JSONObject.toJSONString(dto));
		switch(deliveryJson.getString("shippingWay")){
		case DELIVERY_CODE_101:
			deliveryMap.put("DeliveryType", DeliveryEnum.STYLE_101.getDesc());
			deliveryMap.put("DeliveryCode", deliveryJson.getString("shippingWay"));
			break;
		case DELIVERY_CODE_102:
			deliveryMap.put("DeliveryType", DeliveryEnum.STYLE_102.getDesc());
			deliveryMap.put("DeliveryCode", deliveryJson.getString("shippingWay"));
			break;
		case DELIVERY_CODE_103:
			deliveryMap.put("DeliveryType", DeliveryEnum.STYLE_103.getDesc());
			deliveryMap.put("DeliveryCode", deliveryJson.getString("shippingWay"));
			break;
		case DELIVERY_CODE_104:
			deliveryMap.put("DeliveryType", DeliveryEnum.STYLE_104.getDesc());
			deliveryMap.put("DeliveryCode", deliveryJson.getString("shippingWay"));
			break;
		default:
			deliveryMap.put("DeliveryType", "");
			deliveryMap.put("DeliveryCode", deliveryJson.getString("shippingWay"));
			break;
		}
		return deliveryMap;
	}

}
