package com.yatang.xc.xcr.web;

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

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.biz.core.dto.UserSignDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.UserSignDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.StringUtils;
import com.yatang.xc.xcr.util.TokenUtil;

/**
 * @author gaodawei
 * @Date   2017年7月7日(星期五)
 * @function 签到处理相关功能
 */
@Controller
@RequestMapping("/User/")
public class SignInMngAction {
	
	private static Logger log = LoggerFactory.getLogger(SignInMngAction.class);

	@Value("${SYSTEM_CODE}")
    String SYSTEM_CODE;
	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${TOKEN_OUTTIME}")
	private Integer TOKEN_OUTTIME;
	
	@Autowired
	private OrganizationService organizationDubboService;
	 @Autowired
	private RedisService<JSONObject> redisJsonService;
	@Autowired
	private UserSignDubboService userSignDubboService;
	@Autowired
	private MissionExecuteDubboService missionExecuteDubboService;

	/**
	 * 签到v2.0
	 * gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "Sign", method = RequestMethod.POST)
	public void sign(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "Sign");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK)) {
			JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
			UserSignDTO userSignDTO=new UserSignDTO();
			userSignDTO.setShopCode(jsonTemp.getString("StoreSerialNo"));
			userSignDTO.setUserId(tokenJson.getString("jmsCode"));
			Long signStartTime=System.currentTimeMillis();
			log.info("\n*****************调用userSignDubboService.sign接口的开始时间："+DateUtils.getLogDataTime(signStartTime, null)
			+"\n*****************请求数据是："+JSONObject.toJSONString(userSignDTO));
			Response<?> result = userSignDubboService.sign(userSignDTO);
			log.info("\n*****************于时间:"+DateUtils.getLogDataTime(signStartTime, null)+"调用userSignDubboService.sign接口   调用结束"
					+"\n*****************响应数据是："+JSONObject.toJSONString(result)
					+ "\n***************所花费时间为：" + CommonUtil.costTime(signStartTime));
			if(result!=null){
				if(result.isSuccess()){
					String key = SYSTEM_CODE + tokenJson.getString("jmsCode");
					JSONObject datajson = CommonUtil.getRedisUserInfo(redisJsonService, key, organizationDubboService, tokenJson.getString("jmsCode"), SYSTEM_CODE, TOKEN_OUTTIME);
					Long startTime=System.currentTimeMillis();
					log.info("\n***************** autoAuditMissionByMerchantIdWithoutCache.sign 接口的开始时间： "+DateUtils.getLogDataTime(startTime, null)
					+"\n*****************请求数据是,门店号："+jsonTemp.getString("StoreSerialNo")+"    ytAccount:"+datajson.getString("ytAccount"));
					
					log.info("autoAuditMissionByMerchantIdWithoutCache : shopCode:"+ jsonTemp.getString("StoreSerialNo")+" login:"+ datajson.getString("ytAccount"));
					Response<List<String>> res=missionExecuteDubboService.autoAuditMissionByMerchantIdWithoutCache(jsonTemp.getString("StoreSerialNo"));
					log.info("\n*****************于时间:"+DateUtils.getLogDataTime(startTime, null)+"调用autoAuditMissionByMerchantIdWithoutCache接口   调用结束"
							+"\n*****************响应数据是："+JSONObject.toJSONString(res)
							+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
					JSONObject mapdata=new JSONObject();
					Map<String, Object> mapdata_Map = new HashMap<>();
					mapdata_Map.put("SignResultMsg", result.getResultObject());
					mapdata = StringUtils.replcNULLToStr(mapdata_Map);
					json.put("mapdata", mapdata);
					json.put("Status", stateJson);
				}else{
					json=CommonUtil.pageStatus(json, STATE_ERR, result.getErrorMessage());
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
	 * 历史签到信息v2.0
	 * gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "SignHistoryMsg", method = RequestMethod.POST)
	public void signHistoryMsg(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SignHistoryMsg");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(STATE_OK)) {
			JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
			UserSignDTO userSignDTO=new UserSignDTO();
			userSignDTO.setShopCode(jsonTemp.getString("StoreSerialNo"));
			userSignDTO.setUserId(tokenJson.getString("jmsCode"));
			Long getSignArrayStartTime=System.currentTimeMillis();
			log.info("\n*****************调用userSignDubboService.getSignArray接口的开始时间："+DateUtils.getLogDataTime(getSignArrayStartTime, null)
			+"\n*****************请求数据是："+JSONObject.toJSONString(userSignDTO));
			Response<?> result = userSignDubboService.getSignArray(userSignDTO);
			log.info("\n*****************于时间:"+DateUtils.getLogDataTime(getSignArrayStartTime, null)+"调用userSignDubboService.getSignArray接口   调用结束"
					+"\n*****************响应数据是："+JSONObject.toJSONString(result)
					+ "\n***************所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));
			if(result!=null){
				if(result.isSuccess() && result.getResultObject()!=null){
					JSONObject resultJson=JSONObject.parseObject(JSONObject.toJSONString(result.getResultObject()));
					JSONObject mapdata=new JSONObject();
					Map<String, Object> subJson_Map = new HashMap<>();
					subJson_Map.put("CurrentDate", resultJson.getString("currentDate"));
					subJson_Map.put("IsCurrentDateSign", resultJson.getIntValue("isCurrentDateSign"));
					subJson_Map.put("ContinueSignDays", resultJson.getIntValue("continueSignDays"));
					subJson_Map.put("SignReward", resultJson.getString("signReward"));
					subJson_Map.put("RewardUnit", resultJson.getString("rewardUtil"));
					subJson_Map.put("SignMsg", resultJson.getString("signMsg"));
					subJson_Map.put("ContinueSignArrayDays", resultJson.getJSONArray("continueSignArrayDays"));
					mapdata = StringUtils.replcNULLToStr(subJson_Map);
					json.put("mapdata", mapdata);
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

}
