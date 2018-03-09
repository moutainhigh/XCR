package com.yatang.xc.xcr.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.oles.biz.business.dto.DepositInfoDTO;
import com.yatang.xc.oles.biz.business.dto.ElecContractDTO;
import com.yatang.xc.oles.biz.business.dto.PaymentInfoDTO;
import com.yatang.xc.oles.biz.business.dto.StatusDTO;
import com.yatang.xc.oles.biz.business.dubboservice.ContractVerificationDubboService;
import com.yatang.xc.xcr.biz.pay.dto.PayParamDto;
import com.yatang.xc.xcr.biz.pay.dto.PayReturnDto;
import com.yatang.xc.xcr.biz.pay.dubboservice.SwiftPassPayDubboService;
import com.yatang.xc.xcr.biz.pay.enums.PayRecordState;
import com.yatang.xc.xcr.enums.JoinStateEnum;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.enums.XCCheckEnum;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.web.interceptor.IdempotencyLock;

/** 
* @author gaodawei 
* @Date 2017年11月2日 下午2:16:07 
* @version 1.0.0
* @function 小超商家新加盟签约入驻
*/
@Controller
@RequestMapping("/System/")
public class ApplyJoinXCAction {
	
	private static Logger log = LoggerFactory.getLogger(ApplyJoinXCAction.class);
	
	@Value("${CHECK_CODE_TIME}")
	String CHECK_CODE_TIME;
	@Value("${CONTRACT_URL}")
	String CONTRACT_URL;
	
	@Autowired
	private ContractVerificationDubboService contVerificationDubboService;
	@Autowired
	private SwiftPassPayDubboService wPayDubboService;
	
	private static String USERSTATE_ERR="用户加盟状态还未更新，请稍后再试";
	private static String CONTRACT_DISCREATE="合同信息有误，联系客服处理";
	private static String CONTRACT_DISCREATE1="加盟合同不存在，请联系客服处理";
	private static String CODE_CHECK_FAILED="校验失败";
	private static String MARKET_INFO_ERR="信息有误，请联系客服处理";
	private static String BONDLIST_GETERR="保证金列表获取失败";
	private static String PAYINFO_GETERR="获取支付信息出错，请稍后重试";
	private static String JOINSTATE_ERR="加盟数据还未更新，可稍后重试";
//	private static String TOPAY_BUSY="支付忙不过来了，可稍后重试";
	private static String TOPAY_ERR="支付失败";
	private static String UPDATE_ERR="支付结果同步异常，请联系客服协调处理";
	/**
	 * 门店编号校验并获取验证码v2.5
	 * @param Type
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"IdentityMarketNo"}, method = RequestMethod.POST)
	public void identityMarketNo(@CookieValue(value = "Type") int Type,String msg, HttpServletResponse response) throws Exception {
		long iStartTime=System.currentTimeMillis();
		log.info("\n******于时间"+DateUtils.getLogDataTime(iStartTime, null)
				+"请求IdentityMarketNo获得当前APP的数据是：" +msg+"    Type: "+(Type==1?"安卓":"IOS"));
		JSONObject appReqJson=JSONObject.parseObject(msg);
		long startTime=System.currentTimeMillis();
		log.info("\n******于时间"+DateUtils.getLogDataTime(startTime, null)+" 请求CRM接口ContractContractVerification    请求参数为：Phone："
					+appReqJson.getString("Phone")+"    小超编号："+appReqJson.getString("MarketNo"));
		Response<Boolean> result=contVerificationDubboService.ContractContractVerification(appReqJson.getString("Phone"), appReqJson.getString("MarketNo"));
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
		+ "调用CRM接口:ContractContractVerification结束\n***********响应数据为：" + JSONObject.toJSONString(result));
		JSONObject json=new JSONObject();
		if(result!=null){
			if(result.isSuccess()){//校验成功
				JSONObject mapdata = new JSONObject();
				mapdata.put("CountDown", CHECK_CODE_TIME);
				json.put("mapdata", mapdata);
				json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
			}else{
				log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
				+ "调用CRM接口:ContractContractVerification结束\n***********响应数据为：" + JSONObject.toJSONString(result));
				json=getStandardErr(result);
			}
		}else{
			log.error("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+ "调用CRM接口:ContractContractVerification结束返回null");
			json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), CODE_CHECK_FAILED);
		}
		log.info("\n******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  IdentityMarketNo接口请求结束"
				+"\n******响应数据为："+json);
		response.getWriter().print(json);
	}
	
	/**
	 * 身份验证v2.5
	 * @param Type
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@IdempotencyLock(idempotLock="msg")
	@RequestMapping(value = {"IdentityVerify"}, method = RequestMethod.POST)
	public void identityVerify( @CookieValue(value = "Type") int Type,String msg, HttpServletResponse response) throws Exception {
		long iStartTime=System.currentTimeMillis();
		log.info("\n*****于时间："+DateUtils.getLogDataTime(iStartTime, null)+"  APP请求接口IdentityVerify获得当前APP的请求数据是："+msg+"    Type: "+Type);
		JSONObject appReqJson=JSONObject.parseObject(msg);
		long startTime=System.currentTimeMillis();
		log.info("\n******于时间"+DateUtils.getLogDataTime(startTime, null)+" 请求CRM接口SMSVerification    请求参数为：Phone"
					+appReqJson.getString("Phone")+"    小超编号："+appReqJson.getString("MarketNo")+"   验证码："+appReqJson.getString("Code"));
		Response<StatusDTO> result=contVerificationDubboService.SMSVerification(appReqJson.getString("Phone"), appReqJson.getString("MarketNo"),appReqJson.getString("Code"));
		log.info("\n*******于时间：" + DateUtils.getLogDataTime(startTime, null)+"调用CRM接口:SMSVerification结束"
				+ "\n*******响应数据为：" + JSONObject.toJSONString(result)
				+ "\n*******所花费时间为：" + CommonUtil.costTime(startTime));
		JSONObject json=new JSONObject();
		if(result!=null){
			if(result.isSuccess()){//校验成功
				JSONObject mapdata = new JSONObject();
				mapdata=swapJoinState(result.getResultObject().getCode(),result.getResultObject().getNeedPayDeposit(),appReqJson);
				//CRM返回的小超code非正常code处理
				if(mapdata.getString("JoinStatue").equals(StateEnum.STATE_2.getState())){
					json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), MARKET_INFO_ERR);
				}
				//签约或者支付逾期
				else if(mapdata.getString("JoinStatue").equals(JoinStateEnum.joinState_9.getAppState())){
					mapdata.put("JoinStatue", "");
					json=CommonUtil.pageStatus(json, XCCheckEnum.CHECK_2005.getAppState(), XCCheckEnum.CHECK_2005.getTipMsg());
				}
				//已加盟
				else if(mapdata.getString("JoinStatue").equals(JoinStateEnum.joinState_12.getAppState())){
					json=CommonUtil.pageStatus(json, XCCheckEnum.CHECK_2006.getAppState(), XCCheckEnum.CHECK_2006.getTipMsg());
				}else{
					json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
					if(CONTRACT_DISCREATE.equals(mapdata.getString("ContractUrl"))){
						json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), CONTRACT_DISCREATE);
					}
					if(CONTRACT_DISCREATE1.equals(mapdata.getString("ContractUrl"))){
						json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), CONTRACT_DISCREATE1);
					}
					json.put("mapdata", mapdata);
				}
			}else{
				log.info("\n*******于时间：" + DateUtils.getLogDataTime(startTime, null)+"调用CRM接口:SMSVerification结束"
						+ "\n*******响应数据为：" + JSONObject.toJSONString(result)
						+ "\n*******所花费时间为：" + CommonUtil.costTime(startTime));
				if(StateEnum.STATE_11.getDesc().equals(result.getErrorMessage())){
					json=CommonUtil.pageStatus(json, StateEnum.STATE_11.getState(), StateEnum.STATE_11.getDesc());
				}else{
					json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
				}
			}
		}else{
			log.error("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+ "调用CRM接口:SMSVerification结束,CRM出错返回的数据为null");
			json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
		}
		log.info("\n*****于时间："+DateUtils.getLogDataTime(iStartTime, null)+"  接口IdentityVerify请求结束"
				+"\n*****响应数据是："+json);
		response.getWriter().print(json);
	}
	
	/**
	 * 保证金列表v2.5
	 * @param Type
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"BondList"}, method = RequestMethod.POST)
	public void bondList(HttpServletResponse response) throws Exception {
		long startTime=System.currentTimeMillis();
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+ "调用CRM接口:CashDepositList无参数");
		Response<List<DepositInfoDTO>> result=contVerificationDubboService.CashDepositList();
		JSONObject json=new JSONObject();
		if(result!=null){
			if(result.isSuccess()){
				json.put("listdata", packBondListListdata(result));
				json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
			}else{
				log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
				+ "调用CRM接口:CashDepositList结束\n***********响应数据为：" + JSONObject.toJSONString(result));
				json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(),  BONDLIST_GETERR);
			}
		}else{
			log.error("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
						+ "调用CRM接口:CashDepositList结束返回的数据为null");
			json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(),BONDLIST_GETERR);
		}
		log.info("\n*****于时间："+DateUtils.getLogDataTime(startTime, null)+"  接口BondList请求结束"
				+"\n*****响应数据是："+json);
		response.getWriter().print(json);
	}
	
	/**
	 * 选择加盟保证金v2.5
	 * @param Type
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"ChoiceBond"}, method = RequestMethod.POST)
	public void choiceBond( @CookieValue(value = "Type") int Type,String msg, HttpServletResponse response) throws Exception {
		long iStartTime=System.currentTimeMillis();
		JSONObject appReqJson=JSONObject.parseObject(msg);
		JSONObject json=new JSONObject();
		json=joinOutDateJudge(appReqJson.getString("MarketNo"));
		if(json.getJSONObject("Status")==null){
			log.info("\n*******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  CRM接口SaveElecBond请求参数：小超编号："
					+appReqJson.getString("MarketNo")+"  保证金id:"+appReqJson.getString("BondCode"));
			Response<Boolean> result=contVerificationDubboService.SaveElecBond(appReqJson.getString("MarketNo"),appReqJson.getString("BondCode"));
			if(result!=null && result.isSuccess()){
				Map<String, Object> map=handleContractList(appReqJson.getString("MarketNo"),1,"");
				JSONObject mapdata=new JSONObject();
				//正确数据
				json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
				@SuppressWarnings("unchecked")
				List<ElecContractDTO> list =(List<ElecContractDTO>) map.get("list");
				if(list.size()>0){
					mapdata.put("BondId", map.get("bondId"));
					mapdata.put("ContractUrl", list.get(0).getSignUrl());
				}else{
					log.error("\n*******小超编号："+appReqJson.getString("MarketNo")+"加盟合同不存在，但是CRM没有相关合同");
					mapdata.put("ContractUrl", CONTRACT_DISCREATE);
					json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), CONTRACT_DISCREATE1);
				}
				//测试数据
				//mapdata.put("ContractUrl", "http://1j61w94090.51mypc.cn/xcr-web-app/html/payCallback.html");
				json.put("mapdata", mapdata);
			}else{
				log.info("\n*******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  CRM接口SaveElecBond结束"
						+"响应数据为："+JSONObject.toJSONString(result));
				json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), CONTRACT_DISCREATE);
			}
		}
		log.info("\n*****于时间："+DateUtils.getLogDataTime(iStartTime, null)+"  接口ChoiceBond请求结束"
				+"\n*****响应数据是："+json);
		response.getWriter().print(json);
	}
	/**
	 * 查询合同签订进展v2.5
	 * @param Type
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"CheckBondProgress"}, method = RequestMethod.POST)
	public void checkBondProgress( @CookieValue(value = "Type") int Type,String msg, HttpServletResponse response) throws Exception {
		long iStartTime=System.currentTimeMillis();
		JSONObject appReqJson=JSONObject.parseObject(msg);
		Map<String, Object> map=handleContractList(appReqJson.getString("MarketNo"),1,appReqJson.getString("BondId"));
		@SuppressWarnings("unchecked")
		List<ElecContractDTO> list =(List<ElecContractDTO>) map.get("list");
		JSONObject json=new JSONObject();
		JSONObject mapdata=new JSONObject();
		json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
		if((boolean) map.get("flag")){
			if(list.size()==0){
				log.info("\n*******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  CRM接口getMerchantStauts请求参数："+appReqJson.getString("MarketNo"));
				Response<StatusDTO> result=getMerchantStauts(appReqJson.getString("MarketNo"));
				for (int i = 0; i < 3; i++) {
					if(result.getResultObject().getCode().equals(JoinStateEnum.joinState_12.getState())
							|| result.getResultObject().getCode().equals(JoinStateEnum.joinState_13.getState()) ){
						break;
					}else{
						Thread.sleep(1000);
						log.info("\n*******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  CRM接口getMerchantStauts请求第"+(i+1)+"次");
						result=getMerchantStauts(appReqJson.getString("MarketNo"));
					}
				}
				if(result!=null){
					/**
					 * 判断是否加盟已完成，或者是否到支付保证金界面
					 * 其实可以不用判断joinState_13，但是保险起见
					 */
					if(result.isSuccess() && 
							(result.getResultObject().getCode().equals(JoinStateEnum.joinState_12.getState())
									|| result.getResultObject().getCode().equals(JoinStateEnum.joinState_13.getState())
									|| result.getResultObject().getCode().equals(JoinStateEnum.joinState_10.getState()))){
						mapdata.put("ProStatue","-1");
						mapdata.put("ProStatue",1);
						mapdata.put("ContractUrl", "");
					}
					//加盟超时状态处理
					else if(result.isSuccess() 
							&& result.getResultObject().getCode().equals(JoinStateEnum.joinState_9.getState())
							&& result.getResultObject().getCode().equals(JoinStateEnum.joinState_11.getState())){
						log.info("\n*******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  CRM接口getMerchantStauts返回机器加盟超时"
								+"\n*******响应的数据为："+JSONObject.toJSONString(result));
						json=CommonUtil.pageStatus(json, XCCheckEnum.CHECK_2005.getAppState(), XCCheckEnum.CHECK_2005.getTipMsg());
					}else{
						log.info("\n*******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  CRM接口getMerchantStauts返回数据有误"
								+"\n*******响应的数据为："+JSONObject.toJSONString(result));
						json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), JOINSTATE_ERR);
					}
				}else{
					log.info("\n*******于时间"+DateUtils.getLogDataTime(iStartTime, null)+"  CRM接口getMerchantStauts返回数据有误"
							+"\n*******响应的数据为："+JSONObject.toJSONString(result));
					json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), JOINSTATE_ERR);
				}
			}else{
				mapdata.put("BondId",map.get("bondId"));
				mapdata.put("ProStatue",0);
				mapdata.put("ContractUrl", list.get(0).getSignUrl());
			}
		}else{
			json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), CONTRACT_DISCREATE);
		}
		json.put("mapdata", mapdata);
		log.info("\n*****于时间："+DateUtils.getLogDataTime(iStartTime, null)+"  接口CheckBondProgress请求结束"
				+"\n*****响应数据是："+json);
		response.getWriter().print(json);
	}
	/**
	 * 获取保证金支付信息v2.5
	 * @param Type
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"GetBondPayInfo"}, method = RequestMethod.POST)
	public void getBondPayInfo( @CookieValue(value = "Type") int Type,String msg, HttpServletResponse response) throws Exception {
		long iStartTime=System.currentTimeMillis();
		JSONObject appReqJson=JSONObject.parseObject(msg);
		JSONObject json=new JSONObject();
		json=joinOutDateJudge(appReqJson.getString("MarketNo"));
		if(json.getJSONObject("Status")==null){
			long startTime=System.currentTimeMillis();
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+"调用CRM接口:obtainPaymentInfoByXcNumber请求参数"+appReqJson.getString("MarketNo"));
			Response<PaymentInfoDTO> result=contVerificationDubboService.obtainPaymentInfoByXcNumber(appReqJson.getString("MarketNo"));
			if(result!=null){
				if(result.isSuccess()){
					JSONObject mapdata=new JSONObject();
					mapdata.put("ContractInfoUrl", CONTRACT_URL+"?MarketNo="+appReqJson.getString("MarketNo"));
					mapdata.put("BondValue", result.getResultObject().getAmount());
					json.put("mapdata", mapdata);
					json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
				}else{
					log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+"调用CRM接口:obtainPaymentInfoByXcNumber出错"
							+"\n***********响应数据为：" + JSONObject.toJSONString(result));
					json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), result.getErrorMessage());
				}
			}else{
				log.error("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+"调用CRM接口:obtainPaymentInfoByXcNumber出错,返回null");
				json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(),  PAYINFO_GETERR);
			}
		}
		log.info("\n*****于时间："+DateUtils.getLogDataTime(iStartTime, null)+"  接口GetBondPayInfo请求结束"
				+"\n*****响应数据是："+json);
		response.getWriter().print(json);
	}
	
	/**
	 * 威富通支付预下单v2.5
	 * 预留：针对这个接口需要详细错误提示
	 * @param Type
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"PayToOrder"}, method = RequestMethod.POST)
	public void payToOrder( @CookieValue(value = "Type") int Type,String msg, HttpServletResponse response) throws Exception {
		long startTime=System.currentTimeMillis();
		JSONObject appReqJson=JSONObject.parseObject(msg);
		PayParamDto payParamDto=getPayParamDto(appReqJson);
		JSONObject json=new JSONObject();
		json=joinOutDateJudge(appReqJson.getString("MarketNo"));
		if(json.getJSONObject("Status")==null){
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null) + "调用xcr-pay-client接口:payment"+JSONObject.toJSONString(payParamDto));
			Response<PayReturnDto> result=wPayDubboService.payment(payParamDto);
			if(result!=null){
				if(result.isSuccess()){
					JSONObject mapdata=new JSONObject();
					mapdata.put("token_id", result.getResultObject().getTokenId());
					json.put("mapdata", mapdata);
					json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
				}else{
					log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+ "调用xcr-pay-client接口:payment结束"
							+"\n***********响应数据为：" + JSONObject.toJSONString(result));
					json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), result.getErrorMessage());
				}
			}else{
				log.error("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+ "调用xcr-pay-client接口:payment结束返回的数据为null");
				json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(),  TOPAY_ERR);
			}
		}
		log.info("\n*****于时间："+DateUtils.getLogDataTime(startTime, null)+"  接口PayToOrder请求结束"
				+"\n*****响应数据是："+json);
		response.getWriter().print(json);
	}
	/**
	 * 支付结果主动查询，补偿机制v2.5.0
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"UpdatePayToOrder"}, method = RequestMethod.POST)
	public void updatePayToOrder(String msg,HttpServletResponse response) throws Exception {
		JSONObject appReqJson=JSONObject.parseObject(msg);
		log.info("\n******UpdatePayToOrder--->payReturnRestitution请求数据为："+appReqJson);
		Response<String> result=wPayDubboService.payReturnRestitution(appReqJson.getString("token_id"),
				appReqJson.getIntValue("Statue")==0?PayRecordState.PAY_SUCCESS:PayRecordState.PAY_FAIL);
		JSONObject json=new JSONObject();
		if(result!=null){
			if(result.isSuccess()){
				json=CommonUtil.pageStatus(json, StateEnum.STATE_0.getState(), StateEnum.STATE_0.getDesc());
			}else{
				log.info("\n******UpdatePayToOrder失败\n******请求参数："+appReqJson
						+"\n******响应数据："+JSONObject.toJSONString(result));
				json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), result.getErrorMessage());
			}
		}else{
			log.error("\n******UpdatePayToOrder失败----->接口返回数据为null");
			json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), UPDATE_ERR);
		}
		log.info("\n******接口UpdatePayToOrder请求结束，返回APP数据为："+json);
		response.getWriter().print(json);
	}
	/**
	 * 获得所签署合同的所有连接
	 * @param MarketNo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"getContractList"}, method = RequestMethod.POST)
	public void getContractList(String MarketNo,HttpServletResponse response) throws Exception {
		long startTime=System.currentTimeMillis();
		JSONObject reqJson=new JSONObject();
		reqJson.put("MarketNo", MarketNo);
		Map<String, Object> map=handleContractList(reqJson.getString("MarketNo"),2,"");
		@SuppressWarnings("unchecked")
		List<ElecContractDTO> list =(List<ElecContractDTO>) map.get("list");
		JSONArray jsonArr = new JSONArray();
        for(ElecContractDTO eContract : list){
            JSONObject jo = new JSONObject();
            String str= eContract.getContractName();
            jo.put("name",str.length()>31?str.substring(0, 31)+"...":str.substring(0, str.length()));
            jo.put("url", eContract.getSignUrl());
            jsonArr.add(jo);
        }
        log.info("\n*******于时间"+DateUtils.getLogDataTime(startTime, null)+" 小超："+MarketNo+"调用getContractList结束，响应数据为："+jsonArr);
		response.getWriter().print(jsonArr);
	}
	
	/***********************以下是方法的具体转换实现************************/
	/**
	 * 返回identityMarketNo标准错误信息
	 * @param result
	 * @return
	 */
	private JSONObject getStandardErr(Response<?> result){
		JSONObject json=new JSONObject();
		json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), CODE_CHECK_FAILED);
		for(XCCheckEnum e : XCCheckEnum.values()){
			if(e.getCk_code().equals(result.getCode()) || e.getTipMsg().equals(result.getErrorMessage())){
				if(result.getCode().equals("2007")){
					json=CommonUtil.pageStatus(json, e.getAppState(), result.getErrorMessage());
				}else{
					json=CommonUtil.pageStatus(json, e.getAppState(), e.getTipMsg());
				}
				break;
			}
		}
		return json;
	}
	
	/**
	 * 用于装换CRM的状态为APP
	 * @param joinState
	 * @return
	 */
	private JSONObject swapJoinState(String joinState,Integer payBond,JSONObject appReqJson){
		JSONObject mapdata=new JSONObject();
		mapdata.put("ContractUrl", "");
		mapdata.put("IsPayBond", payBond==0?1:0);
		mapdata.put("JoinStatue", "");
		mapdata.put("BondId", "");
		//不需要保证金情况
		if(payBond==1){
			if(JoinStateEnum.joinState_9.getState().equals(joinState) 
					|| JoinStateEnum.joinState_11.getState().equals(joinState)){
				mapdata.put("JoinStatue", JoinStateEnum.joinState_9.getAppState());
				return mapdata;
			}
			Map<String, Object> map=handleContractList(appReqJson.getString("MarketNo"),1,"");
			@SuppressWarnings("unchecked")
			List<ElecContractDTO> list =(List<ElecContractDTO>) map.get("list");
			if(list.size()>0){
				mapdata.put("BondId", map.get("bondId"));
				mapdata.put("ContractUrl", list.get(0).getSignUrl());
			}else{
				mapdata.put("ContractUrl", CONTRACT_DISCREATE1);
				log.info("\n*******小超编号："+appReqJson.getString("MarketNo")+"不需要加盟保证金，但是CRM没有相关合同");
			}
			return mapdata;
		}
		/**
		 * 需要保证金具体解析
		 */
		if(JoinStateEnum.joinState_9.getState().equals(joinState) 
				|| JoinStateEnum.joinState_11.getState().equals(joinState)){
			mapdata.put("JoinStatue", JoinStateEnum.joinState_9.getAppState());
			return mapdata;
		}
		//去选择加盟保证金档位
		else if(JoinStateEnum.joinState_ChooseBonds.getState().equals(joinState)){
			mapdata.put("JoinStatue", JoinStateEnum.joinState_ChooseBonds.getAppState());
			return mapdata;
		}
		//去获取加盟合同连接
		else if(JoinStateEnum.joinState_sign_waiting.getState().equals(joinState)){
			Map<String, Object> map=handleContractList(appReqJson.getString("MarketNo"),1,"-1");
			@SuppressWarnings("unchecked")
			List<ElecContractDTO> list =(List<ElecContractDTO>) map.get("list");
			mapdata.put("JoinStatue", JoinStateEnum.joinState_sign_waiting.getAppState());
			if(list.size()>0){
				mapdata.put("BondId", map.get("bondId"));
				mapdata.put("ContractUrl", list.get(0).getSignUrl());
			}else{
				mapdata.put("ContractUrl", CONTRACT_DISCREATE1);
				log.info("\n*******小超编号："+appReqJson.getString("MarketNo")+"需要加盟保证金，但是CRM没有相关合同");
			}
			return mapdata;
		}
		//已经签订合同,待支付
		else if(JoinStateEnum.joinState_10.getState().equals(joinState)){
			mapdata.put("JoinStatue", JoinStateEnum.joinState_10.getAppState());
			return mapdata;
		}
		//已加盟状态
		else if(JoinStateEnum.joinState_12.getState().equals(joinState)
				|| JoinStateEnum.joinState_13.getState().equals(joinState)){
			mapdata.put("JoinStatue", JoinStateEnum.joinState_12.getAppState());
			return mapdata;
		}else{
			mapdata.put("JoinStatue", StateEnum.STATE_2.getState());
			return mapdata;
		}
	}
	
	/**
	 * 打包保证金的列表
	 * @param result
	 * @return
	 */
	private JSONObject packBondListListdata(Response<List<DepositInfoDTO>> result){
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		for (int i = 0; i < result.getResultObject().size(); i++) {
			DepositInfoDTO obj=result.getResultObject().get(i);
			JSONObject subRow = new JSONObject();
			int amount=new Double(obj.getBondAmount()).intValue();
			subRow.put("BondCode", obj.getId());
			subRow.put("BondName", bongNameContact(amount));
			subRow.put("BondInfo", obj.getBondRemarks());
			subRow.put("BondValue", amount);
			rowsList.add(subRow);
		}
		listdata.put("rows", rowsList);
		return listdata;
	}
	/**
	 * 拼接返回保证金的名称
	 * @param mny
	 * @return
	 */
	private String bongNameContact(Integer mny){
		return "交纳保证金"+mny+"元";
	}
	
	/**
	 * 获得活动列表
	 * @param appReqJson
	 * @param signStatus:	
	 * @return
	 */
	Map<String, Object> handleContractList(String MarketNo,Integer signStatus,String bondId){
		Map<String, Object> map = new HashMap<>();
		long startTime=System.currentTimeMillis();
		signStatus=signStatus==1?0:signStatus;
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
			+ "调用CRM接口:getElecontractInfoDetailList开始\n***********请求的数据为：" + MarketNo+"  signStatus:"+signStatus);
		Response<List<ElecContractDTO>> result=contVerificationDubboService.getElecontractInfoDetailList(MarketNo,signStatus);
		log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
				+ "调用CRM接口:getElecontractInfoDetailList结束\n***********响应数据为：" + JSONObject.toJSONString(result)
				+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
		List<ElecContractDTO> list=new ArrayList<>();
		boolean flag=true;
		if(result!=null && result.getResultObject()!=null){
			if(signStatus==0){
				//兼容以前版本，或者是签完部分合同然后继续签约的情况
				if(bondId==null || "-1".equals(bondId)){
					for (int i = 0; i < result.getResultObject().size(); i++) {
						if(Integer.parseInt(result.getResultObject().get(i).getSignStatus())==1){
							list.add(result.getResultObject().get(i));
							bondId=result.getResultObject().get(i).getId()+"";
							break;
						}
					}
				}
				//说明是第一次请求来签合同
				else if("".equals(bondId)){
					if(result.getResultObject().size()>0){
						list.add(result.getResultObject().get(0));
						bondId=result.getResultObject().get(0).getId()+"";
					}
				}else{
					outer:for (int i = 0; i < result.getResultObject().size(); i++) {
						if(Integer.parseInt(bondId)==result.getResultObject().get(i).getId()){
							if(Integer.parseInt(result.getResultObject().get(i).getSignStatus())==2){
								if(i!=(result.getResultObject().size()-1)){
									list.add(result.getResultObject().get(i+1));
									bondId=result.getResultObject().get(i+1).getId()+"";
									break;
								}
							}else{
								for (int j = 0; j < 3; j++) {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										throw new RuntimeException("\n*******重复请求合同出现异常", e);
									}
									log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+" 循环第"+(j+1)+"次调用getElecontractInfoDetailList");
									result=contVerificationDubboService.getElecontractInfoDetailList(MarketNo,signStatus);
									log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+" 循环第"+(j+1)+"次调用getElecontractInfoDetailList"
											+"\n***********响应数据为："+JSONObject.toJSONString(result));
									for (int j2 = 0; j2 < result.getResultObject().size(); j2++) {
										if(Integer.parseInt(bondId)==result.getResultObject().get(j2).getId()){
											if(Integer.parseInt(result.getResultObject().get(j2).getSignStatus())==2){
												if(j2!=(result.getResultObject().size()-1)){
													list.add(result.getResultObject().get(j2+1));
													bondId=result.getResultObject().get(j2+1).getId()+"";
													break outer;
												}
											}else{
												if(j2==(result.getResultObject().size()-1) && j==2){
													flag=false;
													break outer;
												}
											}
											break;
										}
									}
								}
							}
							break;
						}
					}
				}
			}else{
				list=result.getResultObject();
			}
		}else{
			flag=false;
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
			+ "调用CRM接口:getElecontractInfoDetailList结束   返回的数据出错，出错返回数据为："+JSONObject.toJSONString(result));
		}
		map.put("flag", flag);
		map.put("list", list);
		map.put("bondId", bondId);
		return map;
	}
	/**
	 * 支付信息预下单请求参数包装
	 * @param appReqJson
	 * @return
	 */
	private PayParamDto getPayParamDto(JSONObject appReqJson){
		PayParamDto paramDto=new PayParamDto();
		paramDto.setShopNo(appReqJson.getString("MarketNo"));
		Integer cash=(int) (appReqJson.getDoubleValue("BondValue")*100);
		paramDto.setTotalFee(cash);//金额的单位是分
		paramDto.setPhone(appReqJson.getString("Phone"));
		paramDto.setMchCreateIp(appReqJson.getString("IPAddress"));
		return paramDto;
	}
	
	/**
	 * 判断加盟商有没有加盟超时的方法
	 * @param MarketNo 小超编号
	 * @return
	 */
	private JSONObject joinOutDateJudge(String MarketNo){
		JSONObject json=new JSONObject();
		long startTime=System.currentTimeMillis();
		log.info("\n*******于时间"+DateUtils.getLogDataTime(startTime, null)+"  CRM接口getMerchantStauts请求参数："+MarketNo);
		Response<StatusDTO> result=contVerificationDubboService.getMerchantStauts(MarketNo);
		if(result!=null && result.isSuccess()){
			if(result.getResultObject().getCode().equals(JoinStateEnum.joinState_9.getState()) 
					|| result.getResultObject().getCode().equals(JoinStateEnum.joinState_11.getState())){
				json=CommonUtil.pageStatus(json, XCCheckEnum.CHECK_2005.getAppState(), XCCheckEnum.CHECK_2005.getTipMsg());
			}else if(result.getResultObject().getCode().equals(JoinStateEnum.joinState_sign_waiting.getState())){
				json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(),JoinStateEnum.joinState_sign_waiting.getDesc());
			}
		}else{
			log.info("\n*******于时间"+DateUtils.getLogDataTime(startTime, null)+" CRM接口getMerchantStauts查询小超编号"+MarketNo+"加盟状态时返回数据出错"
					+ "\n*******响应数据为："+JSONObject.toJSONString(result));
			json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), USERSTATE_ERR);
		}
		return json;
	}
	
	/**
	 * 有可能一次请求加盟状态会还没有更新，所以请求3次；
	 * 如果五次都没有成功，那么返回失败
	 */
	private Response<StatusDTO> getMerchantStauts(String MarketNo){
		return contVerificationDubboService.getMerchantStauts(MarketNo);
	}
	
	/******************************************************************/
}
 