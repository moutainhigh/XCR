package com.yatang.xc.xcr.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class LogInterceptor implements MethodInterceptor {
	private static Logger	log			= Logger.getLogger(LogInterceptor.class);
	private int				logCost		= 500;

	private int				lenthLimit	= 2000;



	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object o = null;
		long now = System.currentTimeMillis();

		o = invocation.proceed();

		if (!log.isInfoEnabled()) {
			return o;
		}
		try {
			RequestMapping annotation = invocation.getMethod().getAnnotation(RequestMapping.class);
			if(annotation == null){
				return o;
			}
			long cost = System.currentTimeMillis() - now;

			StringBuilder sb = new StringBuilder();
			sb.append("【action日志】  : actionCost 【耗时】: ");
			sb.append(cost);
			if (cost > logCost) {
				sb.append("【action耗时太长 】:actionTooLong ");
			}
			sb.append(" 【URL】:");
			sb.append(JSONObject.toJSONString(annotation.value()));
			sb.append(" 【处理类】:");
			sb.append(invocation.getThis().getClass().getName());
			sb.append(" 【方法】:");
			sb.append(invocation.getMethod().getName());
			
			if (o != null) {
				String result = JSON.toJSONString(o);
				if (result.length() < lenthLimit) {
					sb.append(" 【返回值】:");
					sb.append(result);
				} else {
					sb.append(" 【返回值太长】:");
					sb.append(result.substring(0, lenthLimit / 2));
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