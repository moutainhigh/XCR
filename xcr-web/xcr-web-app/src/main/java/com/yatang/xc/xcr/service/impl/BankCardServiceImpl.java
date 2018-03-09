package com.yatang.xc.xcr.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.HttpsClientUtil;
import com.itbt.common.EncryptUtils;
import com.yatang.xc.mbd.biz.org.dto.StoreSettlementInfoDto;
import com.yatang.xc.mbd.biz.org.dubboservice.StoreSettlementInfoDubboService;
import com.yatang.xc.xcr.annotations.ReqLimitEnumAnno;
import com.yatang.xc.xcr.annotations.ReqTimesLimitAnno;
import com.yatang.xc.xcr.enums.BankCardErrEnum;
import com.yatang.xc.xcr.enums.LimitRoleEnum;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.BankCardService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.TokenUtil;

/** 
* @author gaodawei 
* @Date 2018年1月5日 下午4:33:19 
* @version 1.0.0
* @function 
*/
@Service
public class BankCardServiceImpl implements BankCardService {
	
	private static Logger log = LoggerFactory.getLogger(BankCardServiceImpl.class);
	
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
	
	
	@Autowired
	private StoreSettlementInfoDubboService storeSettlementInfoDubboService;
	
	private static final String VALIDATE_METHOD="userBankCardAction.saveUserBankCardInfo";
	private static final String ACCOUNTTYPE="个人";
	private static final String VALIDATE_ORIGIN="7";

	/**未审核*/
	private final Integer UNAUDITED  = 0;
	
	private static final String NO_SETTLE_WARN="无结算信息使绑定银行卡不成功";
	private static final String SETTLEERR_MUST_CONTACT_CUS="无结算信息使绑定银行卡不成功导致信息出错，请务必截图联系客服处理";
	private static final String SERVICE_TIP="服务开小差使绑卡不成功，请稍后重试";
	private static final String SERVICEERR_MUST_CONTACT_CUS="服务出错使绑定银行卡不成功导致信息出错，请务必截图联系客服处理";

//	 @ReqTimesLimitAnno.ReqTimesList({
//	 @ReqTimesLimitAnno(limitMethod="orderList",limitTimeLength=30,limitTimes=1,limit_Tip="验证请求需间隔30秒，请稍后再试"),
//	 @ReqTimesLimitAnno(limitMethod="orderList",limitTimeLength=86400,limitTimes=10,limit_Tip="一天最多可验证10次，请明天再试吧")
//	 })
	@ReqLimitEnumAnno.LimitList({@ReqLimitEnumAnno(role=LimitRoleEnum.SEC_LIMIT),@ReqLimitEnumAnno(role=LimitRoleEnum.DAY_LIMIT)})
	@Override
	public JSONObject setBankCard(JSONObject jsonTemp,JSONObject stateJson) throws Exception {
		 //调用C端校验信息
		String result = validate(jsonTemp);
		if(result!=null && !result.equals("")){
			JSONObject js = JSONObject.parseObject(result);

			if (js.getString("code").equals(BankCardErrEnum.CK_SUC_0.getCk_code())) {//四要素校验成功

				StoreSettlementInfoDto storeSettlementInfoDto = packToSaveBankInfoDto(jsonTemp);
				storeSettlementInfoDto.setIsAudit(UNAUDITED);

				Long sStartTime = System.currentTimeMillis();
				log.info("\n******于时间"+DateUtils.getLogDataTime(sStartTime, null)+"调用updateStoreSettlementInfo接口" +"\n******请求数据是："+ JSONObject.toJSONString(storeSettlementInfoDto));
				Response<Integer> resp = storeSettlementInfoDubboService.updateStoreSettlementInfo(storeSettlementInfoDto);
				log.info("\n******于时间:" + DateUtils.getLogDataTime(sStartTime, null)+"调用updateStoreSettlementInfo接口   调用结束" +"\n******响应数据是："+ JSONObject.toJSONString(resp) +"\n******所花费时间为："+ CommonUtil.costTime(sStartTime));

				if(resp!=null){
					JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
					if(resp.getCode().equals("200")){
					}else if(resp.getCode().equals("403")){
						//没有相关结算信息，删除C端银行卡信息
						Boolean flag = deleteBankData(jsonTemp);
						if (flag) {//删除成功
							stateJson= CommonUtil.pageStatus2(STATE_ERR, NO_SETTLE_WARN);
						} else {
							log.info("\n*******无结算信息使绑定银行卡不成功然后在删除深圳银行卡记录信息时候出错，加盟商："+tokenJson.getString("jmsCode"));
							stateJson= CommonUtil.pageStatus2(STATE_ERR, SETTLEERR_MUST_CONTACT_CUS);
						}
					}else{
						log.info("\n*******主数据dubbo返回非常规信息，加盟商："+tokenJson.getString("jmsCode"));
						stateJson=CommonUtil.pageStatus2(STATE_ERR, BankCardErrEnum.CK_ERR_5.getCk_errMsg());
					}
				}else{
					//绑定银行卡失败，删除C端银行卡信息
					Boolean flag = deleteBankData(jsonTemp);
					if (flag) {//删除成功
						stateJson=CommonUtil.pageStatus2(STATE_ERR, SERVICE_TIP);
					} else {
						stateJson= CommonUtil.pageStatus2(STATE_ERR, SERVICEERR_MUST_CONTACT_CUS);
					}
				}
			}else{

				log.info("\n*******于时间"+DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) +"\n*******请求C端参数为："+JSONObject.toJSONString(checkBankCardParam(jsonTemp)));
				log.info("\n*******于时间"+DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) +"\n*******结束C端请求，响应数据为："+js);
				stateJson=cardErrHandle(js);
			}
		}else{
			log.error("\n*******于时间"+DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime"))+"  C端返回的数据为null或者空字符串");
			stateJson= CommonUtil.pageStatus2(STATE_ERR, BankCardErrEnum.CK_ERR_5.getCk_errMsg());
		}
		return stateJson;
	}
	
	
	/**
	 * 校验银行卡四要素
	 * @author dongshengde
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public String validate(JSONObject jsonTemp) throws Exception {
		Map<String, String> params=checkBankCardParam(jsonTemp);
		Long startTime = System.currentTimeMillis();
		log.info("\n*******调用BINDINGBANKTEST_URL接口的开始时间："+ DateUtils.getLogDataTime(startTime, null) +"\n*******请求数据是"+JSONObject.toJSONString(params.toString()));
		String result ="";
		try{
			result = HttpsClientUtil.sendHttpsPost(BINDINGBANKTEST_URL, params);
		}catch(Exception e){
			log.error("\n*******于时间:" + DateUtils.getLogDataTime(startTime, null)+ "调用BINDINGBANKTEST_URL接口   调用结束" +"\n*******响应数据是：" + JSONObject.toJSONString(result));
			new RuntimeException("\n*******方法SetBankCardMsg--->BINDINGBANKTEST_URL抛出异常", e);
		}
		log.info("\n*******于时间:" + DateUtils.getLogDataTime(startTime, null)+ "调用BINDINGBANKTEST_URL接口   调用结束" +"\n*******响应数据是：" + JSONObject.toJSONString(result) +"\n*******花费时间为：" + CommonUtil.costTime(startTime));
		return result;
	}
	
	/**
	 * 包装请求银行卡的参数
	 * gaodawei
	 * @param jsonTemp
	 * @return
	 */
	private Map<String, String> checkBankCardParam(JSONObject jsonTemp){
		Map<String, String> params = getParams();
		params.put("method", VALIDATE_METHOD);
		params.put("appSecret", ACCESSKEY_BANK);
		params.put("userName", jsonTemp.getString("OpenAccount"));
		params.put("origin", VALIDATE_ORIGIN);
		params.put("name", jsonTemp.getString("Cardholder"));
		params.put("idCard", jsonTemp.getString("ID"));
		params.put("accountNO", jsonTemp.getString("BankCardNum"));
		params.put("bankPreMobile", jsonTemp.getString("PhoneNum"));
		params.put("sign", EncryptUtils.sign(params, ACCESSKEY_BANK));
		return params;
	}
	
	/**
	 * 对要存储的用户银行卡相关信息打包
	 * gaodawei
	 * @param jsonTemp
	 * @return
	 */
	private StoreSettlementInfoDto packToSaveBankInfoDto(JSONObject jsonTemp){
		StoreSettlementInfoDto dto = new StoreSettlementInfoDto();
		dto.setModifyDate(new Date());
		dto.setSettleAccountBankProvince(jsonTemp.getString("Province"));
		dto.setSettleAccountBankCity(jsonTemp.getString("City"));
		dto.setBankBranchLineName(jsonTemp.getString("BankCardBranch"));
		dto.setBankBranchLineNumber(jsonTemp.getString("BankCardBranchId"));
		dto.setStoreCode(jsonTemp.getString("StoreSerialNo"));
		dto.setSettleAccountName(jsonTemp.getString("Cardholder"));
		dto.setPayeeName(jsonTemp.getString("Cardholder"));
		dto.setPayeeIdentityCardNumber(jsonTemp.getString("ID"));
		dto.setPayeePhone(jsonTemp.getString("PhoneNum"));
		dto.setSettleAccountType(ACCOUNTTYPE);
		dto.setSettleAccountBank(jsonTemp.getString("BankCardName"));
		dto.setSettleAccountBankId(jsonTemp.getString("BankCardId"));
		dto.setSettleAccount(jsonTemp.getString("BankCardNum"));
        /**
         * @since  2.7.1
         * 银行卡正面照
         */
        dto.setBankImageUrl(jsonTemp.getString("BankCardImageUrl"));
		return dto;
	}
	/**
	 * 对错误信息友好提示处理
	 * @param needCheckJson
	 * @return
	 */
	private JSONObject cardErrHandle(JSONObject needCheckJson){
		JSONObject stateJson = new JSONObject();
		stateJson=CommonUtil.apiRequestHandleResult(false,BankCardErrEnum.CK_ERR_5.getCk_errMsg());
		for(BankCardErrEnum e : BankCardErrEnum.values()){
			if(e.getCk_code().equals(needCheckJson.getString("code"))){
				stateJson=CommonUtil.pageStatus2(StateEnum.STATE_2.getState(),e.getCk_errMsg());
				break;
			}
		}
		return stateJson;
	}
	
	/**
	 * 校验银行卡需要的公共信息
	 * dongshengde
	 * @return
	 */
	private Map<String, String> getParams() {
		Map<String, String> params = new HashMap<>();
		params.put("url", BINDINGBANKTEST_URL);
		params.put("v", "1.0");
		params.put("format", "json");
		params.put("appKey", APPKEY_BANK);
		params.put("timestamp", DateUtils.dateDefaultFormat(new Date()));
		return params;
	}
	
	
	/**
	 * 如果主数据没有保存成功银行卡信息，则删除C端银行卡信息
	 * @param jsonTemp
	 * @return
	 */
	public Boolean deleteBankData(JSONObject jsonTemp) {
		String result = null;
		Map<String, String> params = getParams();
		params.put("origin", VALIDATE_ORIGIN);
		params.put("method", "userBankCardAction.deleteUserBankCardInfo");
		params.put("appSecret", ACCESSKEY_BANK);
		params.put("userName", jsonTemp.getString("OpenAccount"));
		params.put("accountNO", jsonTemp.getString("BankCardNum"));
		String sign = EncryptUtils.sign(params, ACCESSKEY_BANK);
		params.put("sign", sign);

		Long getSignArrayStartTime = System.currentTimeMillis();
		log.info("\n*******调用BINDINGBANKTEST_URL接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null) +"\n*******请求用户" + jsonTemp.getString("UserId") + "请求数据是：" + JSONObject.toJSONString(params));
		result = HttpsClientUtil.sendHttpsPost(BINDINGBANKTEST_URL, params);
		log.info("\n*******于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "调用BINDINGBANKTEST_URL" +"\n*******" + jsonTemp.getString("UserId") + "响应数据是：" + JSONObject.toJSONString(result) +"\n*******所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));

		JSONObject js = JSONObject.parseObject(result);
		if (js!=null && js.getString("code").equals(BankCardErrEnum.CK_SUC_0.getCk_code())) {
			return true;
		}
		return false;
	}

}
 