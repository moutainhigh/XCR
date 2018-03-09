package com.yatang.xc.xcr.web.handler;

import com.yatang.xc.xcr.enums.ErrorMessageList;
import com.yatang.xc.xcr.model.ResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月06日 12:47
 * @Summary : return value printer
 */
@ControllerAdvice
public class ResultValueHandlerAdvice implements ResponseBodyAdvice<Object> {

    private final Logger logger = LoggerFactory.getLogger(ResultValueHandlerAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> converter, ServerHttpRequest request, ServerHttpResponse response) {
        if(body == null){ return ResultMap.failll(ErrorMessageList.SERVER); }
        logger.info("The controller/action method "+returnType.getMethod().getName() + "() reponse  ==> " + body);
        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        logger.info("The request URL = " +httpServletRequest.getRequestURI() +" # Http Method = " + httpServletRequest.getMethod() + "   execute end" );
        return body;
    }
}
