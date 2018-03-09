package com.yatang.xc.xcr.web.aop;

import java.text.ParseException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.annotations.ReqLimitEnumAnno;
import com.yatang.xc.xcr.annotations.ReqTimesLimitAnno;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.util.AopUtils;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.TokenUtil;

/**
 * @author gaodawei
 * @Date 2018年1月5日(星期五)
 * @version 1.0.0
 * @function 请求限制处理AOP
 */
@Aspect
@Component
public class ReqTimesLimitAop {

	private static Logger log = LoggerFactory.getLogger(ReqTimesLimitAop.class);

	private static String prefix = "doReq_";
	private static String ERR_TIP = "网络出错，请求异常";

	/**
	 * 默认规则
	 * 
	 * @param pjp
	 * @param reqArrAnno
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(reqArrAnno)")
	public Object before(ProceedingJoinPoint pjp, ReqLimitEnumAnno.LimitList reqArrAnno) throws Throwable {
		if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
			JSONObject paramJson = JSONObject.parseObject(pjp.getArgs()[0].toString());
			JSONObject tokenJson = TokenUtil.getTokenFromRedis(paramJson.getString("UserId"));
			log.info("\n*******before" + paramJson);
			JSONObject limitJson = TokenUtil.getTokenFromRedis(prefix + tokenJson.getString("jmsCode"));
			if (limitJson == null) {
				JSONObject limitTempJson = new JSONObject();
				saveHandle(tokenJson.getString("jmsCode"), limitTempJson, 1);
			} else {
				long timeLength = (System.currentTimeMillis() / 1000) - limitJson.getIntValue("currentTime");
				for (int i = 0; i < reqArrAnno.value().length; i++) {
					int val = limitJson.getIntValue("times");
					if((val+1)>reqArrAnno.value()[reqArrAnno.value().length-1].role().getLimitTimes()){
						return AopUtils.pageStatus("M02", reqArrAnno.value()[reqArrAnno.value().length-1].role().getLimitTip());
					}else{
						if (timeLength < reqArrAnno.value()[i].role().getLimitTimeLength()) {
							if ((++val) > reqArrAnno.value()[i].role().getLimitTimes()) {
								return AopUtils.pageStatus("M02", reqArrAnno.value()[i].role().getLimitTip());
							} else {
								saveHandle(tokenJson.getString("jmsCode"), limitJson, val);
								break;
							}
						}
					}
				}
			}
		} else {
			return  AopUtils.pageStatus("M02", ERR_TIP); 
		}
		return pjp.proceed();
	}

	/**
	 * 所有规则由自己定义
	 * 
	 * @param pjp
	 * @param reqArrAnno
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(reqArrAnno)")
	public Object before(ProceedingJoinPoint pjp, ReqTimesLimitAnno.ReqTimesList reqArrAnno) throws Throwable {
		if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
			JSONObject paramJson = JSONObject.parseObject(pjp.getArgs()[0].toString());
			JSONObject tokenJson = TokenUtil.getTokenFromRedis(paramJson.getString("UserId"));
			log.info("\n**************************before" + paramJson);
			JSONObject limitJson = TokenUtil.getTokenFromRedis(prefix + tokenJson.getString("jmsCode"));
			if (limitJson == null) {
				JSONObject limitTempJson = new JSONObject();
				saveHandle(tokenJson.getString("jmsCode"), limitTempJson, 1);
			} else {
				long timeLength = (System.currentTimeMillis() / 1000) - limitJson.getIntValue("currentTime");
				for (int i = 0; i < reqArrAnno.value().length; i++) {
					int val = limitJson.getIntValue("times");
					if((val+1)>reqArrAnno.value()[reqArrAnno.value().length-1].limitTimes()){
						return AopUtils.pageStatus("M02", reqArrAnno.value()[reqArrAnno.value().length-1].limit_Tip());
					}else{
						if (timeLength < reqArrAnno.value()[i].limitTimeLength()) {
							if ((++val) > reqArrAnno.value()[i].limitTimes()) {
								return AopUtils.pageStatus("M02", reqArrAnno.value()[i].limit_Tip());
							} else {
								saveHandle(tokenJson.getString("jmsCode"), limitJson, val);
								break;
							}
						}
					}
				}
			}
		} else {
			return AopUtils.pageStatus("M02", ERR_TIP); 
		}
		return pjp.proceed();
	}

	/**
	 * 更新当前用于针对当前接口的请求次数
	 * 
	 * @param paramJson
	 * @throws ParseException
	 */
	private void saveHandle(String UserId, JSONObject limitJson, Integer val) throws ParseException {
		Integer time = (int) ((DateUtils.getDayNightTime().getTime() - System.currentTimeMillis()) / 60000);// 存的是分
		limitJson.put("currentTime", System.currentTimeMillis() / 1000);
		limitJson.put("times", val);
		TokenUtil.saveInfoToRedis(prefix + UserId, time, limitJson);
	}

}
