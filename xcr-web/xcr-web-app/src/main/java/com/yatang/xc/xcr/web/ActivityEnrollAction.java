package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.FranchiseeDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.biz.core.dto.ActivityDto;
import com.yatang.xc.xcr.biz.core.dubboservice.ActivityDubboService;
import com.yatang.xc.xcr.enums.ActivityEnum;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.TokenUtil;

/** 
 * @author gaodawei 
 * @Date 2017年7月25日 下午3:00:43 
 * @version 1.0.0
 * @function 年会报名登记管理类
 */
@Controller
@RequestMapping("activity")
public class ActivityEnrollAction {

	private static Logger log = LoggerFactory.getLogger(ActivityEnrollAction.class);

	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${STATE_SEVEN}")
	String STATE_SEVEN;
	@Value("${TEMP_TOKEN}")
	long TEMP_TOKEN;
	@Value("${ACTIVITY_STARTTIME}")
	String ACTIVITY_STARTTIME;
	@Value("${ACTIVITY_ENDTIME}")
	String ACTIVITY_ENDTIME;
	@Value("${ACTIVITY_PERSON_NUM}")
	int ACTIVITY_PERSON_NUM;

	@Autowired
	private ActivityDubboService activityDubboService;
	@Autowired
	private OrganizationService organizationDubboService;
	/**
	 * 验证链接是否还有效
	 * @param userId
	 * @param token
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "skipEnroll.htm", method = RequestMethod.POST)
	public void skipEnroll(String userId,String token,Long back, HttpServletResponse response) throws IOException {
		Long methodStartTime=System.currentTimeMillis();
		log.info("\n***********于时间"+DateUtils.getLogDataTime(methodStartTime, null)+"开始执行的skipEnroll.htm方法\n************请求的参数是userId："+userId+",token:"+token+",tempToken:"+back);
		JSONObject json=isLogin(userId,token);
		if(json.getJSONObject("Status").getString("State").equals(STATE_OK)){
			if((System.currentTimeMillis()-(back-1)/3)>TEMP_TOKEN){
				json=CommonUtil.pageStatus(json,STATE_ERR, "链接失效，请重新访问");
			}
		}
		log.info("\n**********于"+DateUtils.getLogDataTime(methodStartTime,null)+" 执行的方法skipEnroll.htm执行结束！"
				+"\n**********response to XCR_APP data is:  " + json
				+"\n**********用时为："+CommonUtil.costTime(methodStartTime));
		response.getWriter().print(json);
	}
	/**
	 * 活动报名登记
	 * @param dto
	 * @param token
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	@RequestMapping(value = "enrollInfo.htm", method = RequestMethod.POST)
	public void enrollInfo(ActivityDto dto,String token,Long back,String type, HttpServletResponse response) throws IOException, ServletException {
		Long methodStartTime=System.currentTimeMillis();
		log.info("\n***********于时间"+DateUtils.getLogDataTime(methodStartTime, null)+"开始执行的enrollInfo.htm方法\n************请求的参数是ActivityDto："+dto.toString());
		JSONObject json=isLogin(dto.getUserId(),token);
		if(json.getJSONObject("Status").getString("State").equals(STATE_OK)){
			if((System.currentTimeMillis()-(back-1)/3)>TEMP_TOKEN){
				json=CommonUtil.pageStatus(json, STATE_ERR, "链接失效，请重新访问");
			}else{
				if(judgeParamIsNull(dto)){
					JSONObject tokenJson=TokenUtil.getTokenFromRedis(dto.getUserId());
					Response<?> resultCheck=activityDubboService.getInfoByUserId(tokenJson.getString("jmsCode"),type);
					if(resultCheck.isSuccess() && resultCheck.getResultObject().toString().equals("false")){
						Response<?> resultCheckCount=activityDubboService.getEnrollCount(type);
						Response<FranchiseeDto> res =getOrgData(tokenJson.getString("jmsCode"));
						dto.setUserId(tokenJson.getString("jmsCode"));
						if(ACTIVITY_PERSON_NUM<0){
							json=packData(res,dto,json);
						}else if(Integer.valueOf(resultCheckCount.getResultObject().toString())<ACTIVITY_PERSON_NUM){
							json=packData(res,dto,json);
						}else{
							json=CommonUtil.pageStatus(json, STATE_ERR, "名额已满");
						}
					}else{
						json=CommonUtil.pageStatus(json, STATE_ERR, "已报名，不能重复报名");
					}
				}else{
					json=CommonUtil.pageStatus(json, STATE_ERR, "相关参数为空，请校验");
				}
			}
		}
		log.info("\n**********于"+DateUtils.getLogDataTime(methodStartTime,null)+" 执行的方法enrollInfo.htm执行结束！"
				+"\n**********response to XCR_APP data is:  " + json
				+"\n**********用时为："+CommonUtil.costTime(methodStartTime));
		response.getWriter().print(json);
	}
	/**
	 * 判断加盟商是否已经报名
	 * @param userId
	 * @param token
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "isEnroll.htm", method = RequestMethod.POST)
	public void isEnroll(String userId,String token,String type,HttpServletResponse response) throws IOException {
		Long methodStartTime=System.currentTimeMillis();
		log.info("\n***********于时间"+DateUtils.getLogDataTime(methodStartTime, null)+"开始执行的isEnroll.htm方法\n************请求的参数是userId："+userId+",token:"+token);
		JSONObject json=isLogin(userId,token);
		if(json.getJSONObject("Status").getString("State").equals(STATE_OK)){
			if(type==null){type="1";}
			JSONObject tokenJson=TokenUtil.getTokenFromRedis(userId);
			Long startTime = System.currentTimeMillis();
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
			+ "开始调用xcr-core-client接口:enroll \n***********请求数据是：" + tokenJson.getString("jmsCode"));
			Response<?> result=activityDubboService.getInfoByUserId(tokenJson.getString("jmsCode"),type);
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
			+ "调用xcr-core-client接口：enroll 调用结束 \n***********响应数据是：" + JSONObject.toJSONString(result)
			+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
			Long countTime = System.currentTimeMillis();
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(countTime, null)
			+ "开始调用xcr-core-client接口:enroll \n***********请求数据是：" + type);
			Response<?> resultCount=activityDubboService.getEnrollCount(type);
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(countTime, null)
			+ "调用xcr-core-client接口：enroll 调用结束 \n***********响应数据是：" + JSONObject.toJSONString(result)
			+ "\n***************所花费时间为：" + CommonUtil.costTime(countTime));
			if(result.isSuccess() && resultCount.isSuccess()){
				Response<FranchiseeDto> res =getOrgData(tokenJson.getString("jmsCode"));
				json.put("storeNo", res.getResultObject().getDefaultStoreId());
				json.put("enrollResult", result.getResultObject());
				json.put("enrollCount", resultCount.getResultObject());
				Date currentDate=new Date();
				if(DateUtils.stringToSimpleDateFormat(ACTIVITY_STARTTIME).before(currentDate)
						&& currentDate.before(DateUtils.stringToSimpleDateFormat(ACTIVITY_ENDTIME))){
					json.put("isActivityDate", 1);
				}else if(!DateUtils.stringToSimpleDateFormat(ACTIVITY_STARTTIME).before(currentDate)){
					json.put("isActivityDate", 0);
				}else if(!currentDate.before(DateUtils.stringToSimpleDateFormat(ACTIVITY_ENDTIME))){
					json.put("isActivityDate", 2);
				}
			}else{
				json=CommonUtil.pageStatus(json,STATE_ERR,StateEnum.STATE_2.getDesc());
			}
		}
		log.info("\n**********于"+DateUtils.getLogDataTime(methodStartTime,null)+" 执行的方法isEnroll.htm执行结束！"
				+"\n**********response to XCR_APP data is:  " + json
				+"\n**********用时为："+CommonUtil.costTime(methodStartTime));
		response.getWriter().print(json);
	}
	/**
	 * token验证以及数据包装
	 * @param jmsCode
	 * @param token
	 * @return
	 */
	public JSONObject isLogin(String loginId,String token){
		JSONObject returnJson=new JSONObject();
		JSONObject json=new JSONObject();
		if(loginId!=null && !"".equals(loginId) && token!=null && !"".equals(token)){
			String flag = TokenUtil.volidateToken(loginId, token);
			switch(flag){
			case "M00":
				json.put("StateDesc", StateEnum.STATE_0.getDesc());
				break;
			case "M01":
				json.put("StateDesc", StateEnum.STATE_1.getDesc());
				break;
			case "M05":
				json.put("StateDesc", StateEnum.STATE_5.getDesc());
				break;
			default:
				break;
			}
			json.put("StateID", flag.substring(1));
			json.put("StateValue",  flag.substring(1));
			json.put("State", flag);
			returnJson.put("Status", json);
			return returnJson;
		}else{
			return CommonUtil.pageStatus(json, STATE_ERR, "用户信息异常");
		}
	}
	/**
	 * 判断请求参数是否为空
	 * @param dto
	 * @return
	 */
	public boolean judgeParamIsNull(ActivityDto dto){
		if(dto.getProvince()==null || dto.getProvince().equals("") ||
				dto.getCity()==null || dto.getCity().equals("") ||
				dto.getUserId()==null || dto.getUserId().equals("") ||
				dto.getStoreNo()==null || dto.getStoreNo().equals("") ||
				dto.getUsername()==null || dto.getUsername().equals("") ||
				dto.getPhone()==null || dto.getPhone().equals("")){
			if(dto.getType().equals(ActivityEnum.ACTIVITY_2.getCode())){
				return true;
			}else if(dto.getType().equals(ActivityEnum.ACTIVITY_1.getCode()) || dto.getType()==null){
				if(dto.getStorePhoto() ==null || dto.getUserPhoto().equals("") ||
						dto.getUserPhoto() ==null || dto.getUserPhoto().equals("")){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	/**
	 * 获取组织中心的用户详细信息
	 * @param jmsCode
	 * @return
	 */
	public Response<FranchiseeDto> getOrgData(String jmsCode){
		Long orgStartTime=System.currentTimeMillis();
		log.info("\n*****************于"+DateUtils.getLogDataTime(orgStartTime,null)+"组织中心接口queryFranchiseeById请求数据为:" + jmsCode);
		Response<FranchiseeDto> res = organizationDubboService.queryFranchiseeById(jmsCode);
		log.info("\n*****************于"+DateUtils.getLogDataTime(orgStartTime,null)+"组织中心接口queryFranchiseeById响应数据为:" + JSON.toJSONString(res)
		+"\n*************花费时间为："+CommonUtil.costTime(orgStartTime));
		return res;
	}

	public JSONObject packData(Response<FranchiseeDto> res,ActivityDto dto,JSONObject json){
		if(res.getResultObject()!=null){
			for (int i = 0; i < res.getResultObject().getStores().size(); i++) {
				if(res.getResultObject().getDefaultStoreId().equals(res.getResultObject().getStores().get(i).getCode())){
					dto.setBranch_company(res.getResultObject().getStores().get(i).getBranchCompanyName());
					dto.setStoreNo(res.getResultObject().getDefaultStoreId());
				}
			}
			Date currentTime=new Date();
			if(dto.getType()==null){dto.setType(ActivityEnum.ACTIVITY_1.getCode());}
			dto.setCreateTime(currentTime);
			dto.setUpdateTime(currentTime);
			Long startTime = System.currentTimeMillis();
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
			+ "开始调用xcr-core-client接口:enroll \n***********请求数据是：" + dto.toString());
			Response<?> result=activityDubboService.enroll(dto,ACTIVITY_PERSON_NUM);
			log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
			+ "调用xcr-core-client接口：enroll 调用结束 \n***********响应数据是：" + JSONObject.toJSONString(result)
			+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
			if(!result.isSuccess()){
				json=CommonUtil.pageStatus(json,STATE_ERR,"登记失败，请重新登记");
			}
			if(result.isSuccess() && result.getResultObject().equals("false")){
				json=CommonUtil.pageStatus(json,STATE_ERR,"登记失败，请重新登记");
			}
		}else{
			json=CommonUtil.pageStatus(json,STATE_ERR,"获取用户信息异常");
		}
		return json;
	}
}
