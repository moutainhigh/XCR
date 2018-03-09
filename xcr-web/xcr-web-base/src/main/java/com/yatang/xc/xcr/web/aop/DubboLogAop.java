package com.yatang.xc.xcr.web.aop;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 对dubbo服务调用做 日志打印
 * 
 * @author zhaokun
 *
 */
@Component
@Aspect
public class DubboLogAop {

	private static Logger log = LoggerFactory.getLogger(DubboLogAop.class);

	private int logCost = 500;

	private int lenthLimit = 2000;

	@Pointcut("execution(* *..*DubboService.*(..))")
	public void logPoint() {
	}

	@Pointcut("execution(* *..*DubboxService.*(..))")
	public void logPoint2() {
	}
	
	/**
	 * 对匹配 以DubboService 和 DubboxService结尾的 类的所有方法做日志打印
	 * 
	 * @param proceeding
	 * @return
	 */
	@Around("logPoint() or logPoint2()")
	public Object aroundLog(ProceedingJoinPoint proceeding) {

		Object o = null;
		long now = System.currentTimeMillis();
		try {
			o = proceeding.proceed(proceeding.getArgs());
		} catch (Throwable e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(!log.isInfoEnabled()){
			return o;
		}
		try {
			long cost = System.currentTimeMillis() - now;

			StringBuilder sb = new StringBuilder();
			sb.append("【dubbo日志】  : dubboCost 【耗时】: ");
			sb.append(cost);
			if (cost > logCost) {
				sb.append("【dubbo耗时太长 】:dubboTooLong ");
			}
			sb.append(" 【方法】:");
			sb.append(proceeding.toShortString());

			Object[] args = proceeding.getArgs();
			if (args != null && args.length > 0) {
				String argStr = JSON.toJSONString(args);
				if (argStr.length() < lenthLimit) {
					sb.append(" 【参数】:");
					sb.append(argStr);
				}else{
					sb.append(" 【参数太长】:");
					sb.append(argStr.substring(0,lenthLimit/2));
				}
				sb.append(" 【参数长度】:");
				sb.append(argStr.length());

			}
			if (o != null) {
				String result = JSON.toJSONString(o);
				if (result.length() < lenthLimit) {
					sb.append(" 【返回值】:");
					sb.append(result);
				}else{
					sb.append(" 【返回值太长】:");
					sb.append(result.substring(0,lenthLimit/2));
				}
				sb.append(" 【返回值长度】:");
				sb.append(result.length());
			}
			log.info(sb.toString());
		} catch (Throwable e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}

		return o;

	}

}
