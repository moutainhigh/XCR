package com.yatang.xc.xcr.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Author : BobLee
 * @CreateTime : 2017年5月18日 上午9:43:45
 * @Summary :  
 */
public class RequestContextHolder {

	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static HttpServletResponse getResponse() {
		ServletWebRequest servletWebRequest = new ServletWebRequest(getRequest());
		return servletWebRequest.getResponse();
	}

}