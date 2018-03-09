package com.yatang.xc.xcr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.FranchiseeDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.RedisKeyEnum;
import com.yatang.xc.xcr.pojo.Key;
import com.yatang.xc.xcr.pojo.UserInfoVo;
/**
 * 公共类
 * @author caotao
 */
@Component
public class CommonUtil {
	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
	
	private static String ONE_FORMAT="#.0";
	private static String TWO_FORMAT="#.00";
	private static String THD_FORMAT="#.000";

	@Autowired
	private static RedisService<?> redisService;
	
	public static String getTokenId(String msg){
		try {
			msg = URLDecoder.decode(msg.substring(4,msg.length()),"UTF-8");
			JSONObject jsonTemp = JSONObject.parseObject(msg);
			return jsonTemp.getString("Token");
		} catch (UnsupportedEncodingException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/**
	 * 用于解析请求数据
	 * @param msg
	 * @return
	 */
	public static JSONObject methodBefore(String msg,String method){
		Date date=new Date();
		try {
			msg = URLDecoder.decode(msg.substring(4,msg.length()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("\n*******msg："+msg);
		JSONObject jsonTemp = JSONObject.parseObject(msg);
		String tokenData = jsonTemp.getString("Token");
		String userId = jsonTemp.getString("UserId");
		log.info("\n****************"+method+"开始执行方法的时间为："+DateUtils.getLogDataTime(null, date)+"****************\n***************当前APP请求的数据是："+jsonTemp);
		String flag = TokenUtil.volidateToken(userId, tokenData);
		
		jsonTemp.put("flag", flag);
		jsonTemp.put("startExecuteTime", date);
		jsonTemp.put("method", method);
		return jsonTemp;
	}
	
	/**
	 * 用于图片接口的解析数据
	 * @param msg
	 * @return
	 */
	public static JSONObject methodBeforeImg(String msg){
		try {
			msg = URLDecoder.decode(msg.substring(4,msg.length()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject jsonTemp = JSONObject.parseObject(msg);
		return jsonTemp;
	}
	/**
	 * gaodawei
	 * 用于封装返回分页数据data
	 * @param listdata
	 * @param jsonTemp
	 * @param totalCount
	 * @param back
	 * @return
	 */
	public static JSONObject pagePackage(JSONObject listdata,JSONObject jsonTemp,int totalCount,String back){
		listdata.put("pageindex", jsonTemp.getIntValue("PageIndex"));
		if(jsonTemp.getIntValue("PageIndex")*jsonTemp.getIntValue("PageSize")>totalCount){
			listdata.put("pagesize", totalCount%jsonTemp.getIntValue("PageSize"));
		}else{
			listdata.put("pagesize", jsonTemp.getIntValue("PageSize"));
		}
		listdata.put("totalcount", totalCount);
		int totalpage = totalCount/jsonTemp.getIntValue("PageSize");
		if(totalCount%jsonTemp.getIntValue("PageSize")==0){
			listdata.put("totalpage", totalpage);
		}else{
			listdata.put("totalpage", totalpage+1);
		}
		return listdata;
	}
	/**
	 * gaodawei
	 * 用于封装每个M__状态
	 * @param json
	 * @param status
	 * @param errMsg
	 * @return
	 */
	public static JSONObject pageStatus(JSONObject json,String status,String errMsg){
		JSONObject stateJson=new JSONObject();
		stateJson.put("State", status);
		stateJson.put("StateID", status.substring(1));
		stateJson.put("StateValue", status.substring(1));
		stateJson.put("StateDesc", errMsg);
		json.put("Status", stateJson);
		return json;
	}
	/**
	 * gaodawei
	 * 用于封装每个M__状态
	 * @param json
	 * @param status
	 * @param errMsg
	 * @return
	 */
	public static JSONObject pageStatus2(String status,String errMsg){
		JSONObject stateJson=new JSONObject();
		stateJson.put("State", status);
		stateJson.put("StateID", status.substring(1));
		stateJson.put("StateValue", status.substring(1));
		stateJson.put("StateDesc", errMsg);
		return stateJson;
	}
	/**
	 * 返回请求接口花费时间
	 * @param startTime
	 * @return
	 */
	public static String costTime(Long startTime){
		return Double.valueOf(System.currentTimeMillis()-startTime)/1000+"s";
	}
	/**
	 * 获取Redis中UserInfo
	 * @param redisService
	 * @param key
	 * @param organizationDubboService
	 * @param jmsCode
	 * @param system_code
	 * @param token_outTime
	 * @return
	 */
	public static JSONObject getRedisUserInfo(RedisService<JSONObject> redisService,String key,OrganizationService organizationDubboService,String jmsCode,String system_code,int token_outTime){
		JSONObject userInfoJson = redisService.getKeyForObject(RedisKeyEnum.Key, key, JSONObject.class);
		long startTime=System.currentTimeMillis();
		log.info("\n***********于时间"+DateUtils.getLogDataTime(startTime, null)+"第一次获得的getRedisUserInfo的数据是："+userInfoJson);
		if(userInfoJson==null || userInfoJson.isEmpty()){
			Long orgStartTime=System.currentTimeMillis();
			log.info("\n*****************于"+DateUtils.getLogDataTime(orgStartTime,null)+"组织中心接口queryFranchiseeById请求数据为:" + jmsCode);
			Response<FranchiseeDto> res = organizationDubboService.queryFranchiseeById(jmsCode);
			log.info("\n*****************于"+DateUtils.getLogDataTime(orgStartTime,null)+"组织中心接口queryFranchiseeById响应数据为:" + JSON.toJSONString(res)
			+"\n*************花费时间为："+CommonUtil.costTime(orgStartTime));
			if(res.isSuccess() && res.getResultObject() != null){
				FranchiseeDto allianceBusinessDTO=res.getResultObject();
				UserInfoVo userInfo=userInfoPack(res.getResultObject());
				String suffix = system_code + allianceBusinessDTO.getId();
				synchronized (redisService) {
					redisService.saveObject(RedisKeyEnum.Key, suffix, token_outTime, (JSONObject)JSON.toJSON(userInfo));
				}
			}else{
				throw new RuntimeException("组织中心接口queryFranchiseeById请求出异常");
			}
			userInfoJson = redisService.getKeyForObject(RedisKeyEnum.Key, key, JSONObject.class);
			log.info("\n***********于时间"+DateUtils.getLogDataTime(startTime, null)+"第二次获得的getRedisUserInfo的数据是："+userInfoJson);
		}
		String ytAccount=userInfoJson.getString("ytAccount")==null?userInfoJson.getString("bankAccount"):userInfoJson.getString("ytAccount");
		String ytAccount2=(ytAccount==null?userInfoJson.getString("BankAccount"):ytAccount);
		if(userInfoJson.getString("ytAccount")==null){
			userInfoJson.put("ytAccount", ytAccount2);
		}
		if(userInfoJson.getString("userId")==null){
			userInfoJson.put("userId", userInfoJson.getString("UserId"));
		}
		if(userInfoJson.getString("userName")==null){
			userInfoJson.put("userName", userInfoJson.getString("UserName"));
		}
		if(userInfoJson.getString("loginId")==null){
			userInfoJson.put("loginId", userInfoJson.getString("LoginId"));
		}
		if(userInfoJson.getString("identityCard")==null){
			userInfoJson.put("identityCard", userInfoJson.getString("IdentityCard"));
		}
		if(userInfoJson.getString("status")==null){
			userInfoJson.put("status", userInfoJson.getString("Status"));
		}
		if(userInfoJson.getString("delFlag")==null){
			userInfoJson.put("delFlag", userInfoJson.getString("DelFlag"));
		}
		if(userInfoJson.getString("subCompanyId")==null){
			userInfoJson.put("subCompanyId", userInfoJson.getString("SubCompanyId"));
		}
		log.info("\n***********getRedisUserInfo于时间"+DateUtils.getLogDataTime(startTime, null)+"最后返回的数据是："+userInfoJson);
		return userInfoJson;
	}
	/**
	 * 包装用户信息
	 * gaodawei
	 * @param fDto
	 * @return
	 */
	public static UserInfoVo userInfoPack(FranchiseeDto fDto){
		UserInfoVo userVo=new UserInfoVo();
		userVo.setUserId(fDto.getId());
		userVo.setUserName(fDto.getName());
		userVo.setLoginId(fDto.getId());
		userVo.setIdentityCard(fDto.getIdentityCard());
		userVo.setStatus(fDto.getStatus());
		userVo.setDelFlag(fDto.getDeleteFlag());
		userVo.setYtAccount(fDto.getYtAccount());
		userVo.setBankAccount(fDto.getBankAccount());
		userVo.setSubCompanyId(fDto.getBranchCompanyId());
		return userVo;
	}
	
	/**
	 * gaodawei
	 * 判断字符串中是否包含特殊字符，包含则返回true
	 * @param source
	 * @return
	 */
	 public static boolean containsEmoji(String source) {
	        int len = source.length();
	        boolean isEmoji = false;
	        for (int i = 0; i < len; i++) {
	            char hs = source.charAt(i);
	            if (0xd800 <= hs && hs <= 0xdbff) {
	                if (source.length() > 1) {
	                    char ls = source.charAt(i + 1);
	                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
	                    if (0x1d000 <= uc && uc <= 0x1f77f) {
	                        return true;
	                    }
	                }
	            } else {
	                // non surrogate
	                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
	                    return true;
	                } else if (0x2B05 <= hs && hs <= 0x2b07) {
	                    return true;
	                } else if (0x2934 <= hs && hs <= 0x2935) {
	                    return true;
	                } else if (0x3297 <= hs && hs <= 0x3299) {
	                    return true;
	                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
	                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
	                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
	                    return true;
	                }
	                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
	                    char ls = source.charAt(i + 1);
	                    if (ls == 0x20e3) {
	                        return true;
	                    }
	                }
	            }
	        }
	        return isEmoji;
	    }
	 
	 
	 /**
	 * 请求其他系统返回参数校验响应数据统一格式
	 * gaodawei
	 * @param jsonTemp
	 * @return false没有包含
	 */
	public static JSONObject apiRequestHandleResult(boolean flag,String desc) {
		JSONObject checkResultJson = new JSONObject();
		checkResultJson.put("flag", flag);
		checkResultJson.put("desc", desc);
		return checkResultJson;
	}
	
	/**
	 * 活得DESKey用于解密
	 * @param deviceId 设备ID
	 * @param T 
	 * @return
	 */
	public static Key getDESKeyFromRedis(String deviceId,Class<?> T){
		long startTime=System.currentTimeMillis();
		Key key=null;
		try {
			key = (Key) redisService.getKeyForObject(RedisKeyEnum.Key, deviceId, T);
		} catch (Exception e) {
			log.error("\n********于时间"+DateUtils.getLogDataTime(startTime, null)+"方法获取私钥抛出异常"
					 +"\n********参数：key:" + key.getDesKey() + ",参数DeviceId：" + deviceId + ",异常信息："+ e.toString());
			throw new RuntimeException("\n*******方法SetBankCardMsg解密抛出异常", e);
		}
		return key;
	}
	
	/**
	 * 
	 * @param d   需要格式化的double数据
	 * @param num 格式化位数，最多三位，默认两位
	 * @return
	 */
	public static String getFormatDouble(double d,int num){
		if(d==0){
			return "0.00";
		}
		String FORMAT_STYLE=TWO_FORMAT;
		if(num==1){
			FORMAT_STYLE=ONE_FORMAT;
		}else if(num==3){
			FORMAT_STYLE=THD_FORMAT;
		}
		return new java.text.DecimalFormat(FORMAT_STYLE).format(d);
	}
}
