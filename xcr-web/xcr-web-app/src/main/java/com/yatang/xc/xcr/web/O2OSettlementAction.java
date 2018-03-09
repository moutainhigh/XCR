package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.busi.common.datatable.TableDataResult;
import com.busi.common.resp.Response;
import com.yatang.fc.facade.dto.XCRDiscountSettleDTO;
import com.yatang.fc.facade.dto.XCRSettleQueryDTO;
import com.yatang.fc.facade.dto.XCRStoreSettleDTO;
import com.yatang.fc.facade.dubboservice.FCXCRSettleDubboService;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;

/**
 * 
* 结算管理
*		
* @author: zhongrun
* @version: 1.0, 2017年8月14日
 */
@Controller
@RequestMapping("/User/")
public class O2OSettlementAction {
	
	private static Logger log = LoggerFactory.getLogger(O2OSettlementAction.class);
	
	@Autowired
	private FCXCRSettleDubboService fcxcrSettleDubboService;
	
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
	
	/**
	 * 
	* 根据门店查询结算单列表
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping(value="SettlementManageList", method = RequestMethod.POST)
	public void settlementManageList(@RequestBody String msg,
			HttpServletResponse response) throws Exception{
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SettlementManageList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			
			XCRSettleQueryDTO xcrSettleQueryDTO = settleListJson2DTO(jsonTemp);
			Long startTime = System.currentTimeMillis();
			log.info("\n*****Start At：" + DateUtils.getLogDataTime(startTime, null)+ "Call Financial Center GetStoreSettleList Interface"
					+"Financial Center Request Data is:"+JSONObject.toJSONString(xcrSettleQueryDTO));
			Response<TableDataResult<XCRStoreSettleDTO>> dubboResult = fcxcrSettleDubboService.getStoreSettleList(xcrSettleQueryDTO);
			Long endTime = System.currentTimeMillis();
			log.debug("Call GetStoreSettleList Last_"+(endTime-startTime)+"_Time"+"Financial Center Response Data is:"+JSONObject.toJSONString(dubboResult));
			settleListDTO2json(response, json, dubboResult,jsonTemp);
		}
			
		json.put("Status", stateJson);
		response.getWriter().print(json);
	}


	private void settleListDTO2json(HttpServletResponse response,
			JSONObject json,
			Response<TableDataResult<XCRStoreSettleDTO>> dubboResult,JSONObject jsonTemp)
			throws IOException {
		if (dubboResult==null) {
			log.error("<Financial Center Error> getStoreSettleList Return Null!!");
			return;
		}
		if (!dubboResult.isSuccess()) {
			log.error("Financial Center GetStoreSettleList Failure");
			json.put("Status", CommonUtil.pageStatus2("M02", "系统错误"));
			response.getWriter().print(json);
		}else {				
			List<XCRStoreSettleDTO> xcrStoreSettleDTOs = dubboResult.getResultObject().getData();
			JSONArray settleList = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			if (!xcrStoreSettleDTOs.isEmpty()) {
				for (XCRStoreSettleDTO xcrStoreSettleDTO : xcrStoreSettleDTOs) {
					JSONObject jsonObject1 = settleListDTO2JsonArray(xcrStoreSettleDTO);
					settleList.add(jsonObject1);
				}
				
			}
			jsonObject.put("rows", settleList);
			jsonObject.put("totalcount", dubboResult.getResultObject().getRecordsTotal());
			jsonObject.put("pagesize", Integer.parseInt(jsonTemp.getString("PageSize")));//TODO
			jsonObject.put("pageindex",Integer.parseInt( jsonTemp.getString("PageIndex")));
			jsonObject.put("totalpage", dubboResult.getResultObject().getRecordsTotal()/(Integer.parseInt(jsonTemp.getString("PageSize"))+1)+1);
			json.put("listdata", jsonObject);
		}
	}


	private JSONObject settleListDTO2JsonArray(
			XCRStoreSettleDTO xcrStoreSettleDTO) {
		JSONObject jsonObject1 = new JSONObject();
		
		String settleId = xcrStoreSettleDTO.getSettleNo();
		Double settleValue = Double.parseDouble(xcrStoreSettleDTO.getSettleAmount());
		String date = xcrStoreSettleDTO.getPaidDate();
		Double settlementSucValue = xcrStoreSettleDTO.getSellAmount();
		Double settlementRefuseValue = xcrStoreSettleDTO.getBackAmount();
		String settlementErrorMsg = xcrStoreSettleDTO.getFailReason();
		
		jsonObject1.put("SettlementId", settleId);
		jsonObject1.put("SettlementValue", settleValue);
		jsonObject1.put("Date", date);
		jsonObject1.put("SettlementSucValue", settlementSucValue);
		jsonObject1.put("SettlementRefuseValue", settlementRefuseValue);
		jsonObject1.put("SettlementErrorMsg", settlementErrorMsg==null?"":settlementErrorMsg);		
		Integer state = xcrStoreSettleDTO.getSettleStatus();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
		Date confirmTime = xcrStoreSettleDTO.getConfirmTime();
		Date settleTime = xcrStoreSettleDTO.getSettleTime();
		String settleDate = systemTimeTransfer(simpleDateFormat, settleTime);
		String confirmdate = systemTimeTransfer(simpleDateFormat, confirmTime);
		switch (state) {						
		case 1:
			jsonObject1.put("IsFinished", 0);
			jsonObject1.put("SettlementStatue", 0);
			jsonObject1.put("SettlementTime", "");
			break;
		case 2:
			jsonObject1.put("IsFinished", 1);
			jsonObject1.put("SettlementStatue", 0);
			jsonObject1.put("SettlementTime", confirmdate);
			break;							
		case 3:
			jsonObject1.put("IsFinished", 1);
			jsonObject1.put("SettlementStatue", 2);
			jsonObject1.put("SettlementTime", settleDate);
			break;
		case 4:
			jsonObject1.put("IsFinished", 1);
			jsonObject1.put("SettlementStatue", 3);
			jsonObject1.put("SettlementTime", settleDate);
			break;
		default:
			break;
		}
		return jsonObject1;
	}

	
	private XCRSettleQueryDTO settleListJson2DTO(JSONObject jsonTemp) {
		Integer pageIndex = Integer.parseInt(jsonTemp.getString("PageIndex"));
		Integer pageSize = Integer.parseInt(jsonTemp.getString("PageSize"));
		String shopNo = jsonTemp.getString("StoreSerialNo");
		String startDate = jsonTemp.getString("StartDate");
		String endDate = jsonTemp.getString("EndDate");	

		XCRSettleQueryDTO  xcrSettleQueryDTO = new XCRSettleQueryDTO();
		
		xcrSettleQueryDTO.setPageSize(pageSize);
		xcrSettleQueryDTO.setPageNumber(pageIndex);   
		xcrSettleQueryDTO.setStoreNo(shopNo);
		xcrSettleQueryDTO.setStartdate(startDate);
		xcrSettleQueryDTO.setEnddate(endDate);
		return xcrSettleQueryDTO;
	}
	
	/**
	 * 
	* 确认结算单
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping("UpdateSettlement")
	public void updateSettlement(@RequestBody String msg,
			HttpServletResponse response) throws Exception{
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "UpdateSettlement");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			//Remaind Data
			/*String shopNo = jsonTemp.getString("StoreSerialNo");*/			
			List<String> arg0 = confirmJson2DTO(jsonTemp);
			Long startTime = System.currentTimeMillis();
			log.info("\n*****Start At：" + DateUtils.getLogDataTime(startTime, null)+ "Call Financial Center ConfirmSettle Interface"
					+"Financial Center Request Data is:"+JSONObject.toJSONString(arg0));
			Response<Boolean> dubboResult = fcxcrSettleDubboService.confirmSettle(arg0);
			Long endTime = System.currentTimeMillis();
			log.info("Call ConfirmSettle Last_"+(endTime-startTime)+"_Time"+"Financial Center Response Data is:"+JSONObject.toJSONString(dubboResult));
			updateDubboTransfer(stateJson, json, dubboResult);			
		}else{
			json.put("Status", stateJson);
		}
		response.getWriter().print(json);
		
	}


	private void updateDubboTransfer(JSONObject stateJson, JSONObject json, Response<Boolean> dubboResult) {
		if (dubboResult==null) {
			log.error("<Financial Center Error> confirmSettle Return Null!!");
			return;
		}
		if (!dubboResult.isSuccess()) {
			log.error("Financial Center Confirm Failure");
			json.put("Status", CommonUtil.pageStatus2("M02", "系统错误"));
		}else {
			if (dubboResult.getResultObject()) {
				json.put("Status", stateJson);
			}else {
				log.error("Financial Center Confirm Failure");
				json.put("Status", CommonUtil.pageStatus2("M02", "系统错误"));
			}
		}
	}


	private List<String> confirmJson2DTO(JSONObject jsonTemp) {
		String settlementId = jsonTemp.getString("SettlementId");
		List<String> arg0 = new ArrayList<String>();
		arg0.add(settlementId);
		return arg0;
	}
	
	/**
	 * 
	* <优惠券结算>
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping(value="SettlementCouponList", method = RequestMethod.POST)
	public void settlementCouponList(@RequestBody String msg,
			HttpServletResponse response) throws Exception{
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SettlementManageList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			XCRSettleQueryDTO xcrSettleQueryDTO = settleListJson2DTO(jsonTemp);
			Long startTime = System.currentTimeMillis();
			log.info("\n*****Start At：" + DateUtils.getLogDataTime(startTime, null)+ "Call Financial Center GetDiscountSettleList Interface"
					+"Financial Center Request Data is:"+JSONObject.toJSONString(xcrSettleQueryDTO));
			Response<TableDataResult<XCRDiscountSettleDTO>> dubboResult = fcxcrSettleDubboService.getDiscountSettleList(xcrSettleQueryDTO);
			Long endTime = System.currentTimeMillis();
			log.info("Call GetDiscountSettleList Last_"+(endTime-startTime)+"_Time"+"Financial Center Response Data is:"+JSONObject.toJSONString(dubboResult));
			settleCountDTO2json(response, json, dubboResult,jsonTemp);
			
		}
	
		json.put("Status", stateJson);
		response.getWriter().print(json);
	}
	
	private void settleCountDTO2json(HttpServletResponse response,
			JSONObject json,
			Response<TableDataResult<XCRDiscountSettleDTO>> dubboResult,JSONObject jsonTemp)
			throws IOException {
		if (dubboResult==null) {
			log.error("<Financial Center Error> getDiscountSettleList Return Null!!");
			return;
		}
		if (!dubboResult.isSuccess()) {
			log.error("Financial Center GetDiscountSettleList Failure");
			json.put("Status", CommonUtil.pageStatus2("M02", "系统错误"));
			response.getWriter().print(json);
		}else {				
			List<XCRDiscountSettleDTO> xcrStoreSettleDTOs = dubboResult.getResultObject().getData();
			JSONArray settleList = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			if (!xcrStoreSettleDTOs.isEmpty()) {
				for (XCRDiscountSettleDTO xcrStoreSettleDTO : xcrStoreSettleDTOs) {
					JSONObject jsonObject1 = settleCountListDTO2JsonArray(xcrStoreSettleDTO);
					settleList.add(jsonObject1);
				}
				
			}
			jsonObject.put("rows", settleList);
			jsonObject.put("totalcount", dubboResult.getResultObject().getRecordsTotal());
			jsonObject.put("pagesize", Integer.parseInt(jsonTemp.getString("PageSize")));//TODO
			jsonObject.put("pageindex",Integer.parseInt( jsonTemp.getString("PageIndex")));
			jsonObject.put("totalpage", dubboResult.getResultObject().getRecordsTotal()/(Integer.parseInt(jsonTemp.getString("PageSize"))+1)+1);
			json.put("listdata", jsonObject);
		}
	}
	
	
	private JSONObject settleCountListDTO2JsonArray(
			XCRDiscountSettleDTO xcrStoreSettleDTO) {
		JSONObject jsonObject1 = new JSONObject();
		
		String settleId = xcrStoreSettleDTO.getSettleNo();
		Double settleValue = Double.parseDouble(xcrStoreSettleDTO.getSettleAmount());
		String date = xcrStoreSettleDTO.getPaidDate();
		Double settlementSucValue = Double.parseDouble(xcrStoreSettleDTO.getBenefitAmount());
		Double settlementRefuseValue = xcrStoreSettleDTO.getBackAmount();
		String settlementErrorMsg = xcrStoreSettleDTO.getFailReason();
		
		jsonObject1.put("SettlementId", settleId);
		jsonObject1.put("SettlementValue", settleValue);
		jsonObject1.put("Date", date);
		jsonObject1.put("SettlementSucValue", settlementSucValue);
		jsonObject1.put("SettlementRefuseValue", settlementRefuseValue);
		jsonObject1.put("SettlementErrorMsg", settlementErrorMsg==null?"":settlementErrorMsg);		
		Integer state = xcrStoreSettleDTO.getSettleStatus();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		Date dateTime = xcrStoreSettleDTO.getSettleTime();
		//系统稳定设置
		String settleTime = systemTimeTransfer(simpleDateFormat, dateTime);
		switch (state) {						
		case 1:
			jsonObject1.put("SettlementStatue", 0);
			jsonObject1.put("SettlementTime", "");
			break;
		case 2:
			jsonObject1.put("SettlementStatue", 2);
			jsonObject1.put("SettlementTime", settleTime);
			break;							
		case 3:
			jsonObject1.put("SettlementStatue", 3);
			jsonObject1.put("SettlementTime", settleTime);
			break;
		default:
			break;
		}
		return jsonObject1;
	}


	/**
	 * 
	* <优化时间稳定：兼容系统错误的时候没有时间导致的接口转换错误情况>
	*
	* @param simpleDateFormat
	* @param dateTime
	* @return
	 */
	private String systemTimeTransfer(SimpleDateFormat simpleDateFormat, Date dateTime) {
		String settleTime = (dateTime!=null?
				simpleDateFormat.format(dateTime).toString():"");
		return settleTime;
	}
	
	
	

}
