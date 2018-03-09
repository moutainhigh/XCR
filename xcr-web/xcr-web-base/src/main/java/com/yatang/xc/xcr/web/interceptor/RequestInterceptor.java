package com.yatang.xc.xcr.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

/**
 * 对每次访问耗时 做自动日志打印
 * @author zhaokun
 *
 */
public class RequestInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {

	private static Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

	private int lengthLimit = 2000;
	private int costLimit = 3000;
	private static final String TIME_FLAG = "interceptorTime"; 
	
	public void init() {

	}

	/**
	 * 记录访问初始时间
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute(TIME_FLAG, System.currentTimeMillis());

		return true;
	}

	/**
	 * 打印访问日志
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if(!log.isInfoEnabled()){
			return;
		}
		if (handler instanceof HandlerMethod) {
			// 做注解校验，如果没有注解不做记录
			HandlerMethod h = (HandlerMethod) handler;
			// 获取请求信息
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("【访问日志】:");
				sb.append("【URI】:").append(request.getRequestURI());
				Object time = request.getAttribute(TIME_FLAG);
				if (time != null && time instanceof Long) {
					long cost = System.currentTimeMillis() - (Long) time;
					sb.append("【访问耗时 】requestCost:").append(cost);
					if (cost > costLimit) {
						sb.append("【访问耗时太长 】:costTooLong ");
					}
				}
				sb.append("【处理类】:").append(h.getBean().getClass().getName());
				sb.append("【方法】:").append(h.getMethod().getName());
				if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
					String param = JSONObject.toJSON(request.getParameterMap()).toString();
					if (param.length() < lengthLimit) {
						sb.append("【参数】:").append(param);
					}else{
						sb.append("【参数太长】:").append(param.substring(0,lengthLimit/2));
					}
					sb.append("【参数长度】:").append(param.length());
				}
				sb.append("【状态】:").append(response.getStatus());
				Cookie[] cookies = request.getCookies();
				if (cookies != null && cookies.length != 0) {
					for (Cookie cookie : cookies) {
						if ("Type".equals(cookie.getName())) {
							sb.append("【Type】:").append(cookie.getValue());
						}
						if ("DeviceId".equals(cookie.getName())) {
							sb.append("【DeviceId】:").append(cookie.getValue());
						}
					}
				}
				log.info(sb.toString());
			} catch (Exception e) {
				ExceptionUtils.getFullStackTrace(e);
			}

		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}

	public int getLengthLimit() {
		return lengthLimit;
	}

	public void setLengthLimit(int lengthLimit) {
		this.lengthLimit = lengthLimit;
	}

	public int getCostLimit() {
		return costLimit;
	}

	public void setCostLimit(int costLimit) {
		this.costLimit = costLimit;
	}

	
}
