package com.yatang.xc.xcr.web.handler;

import com.yatang.xc.xcr.annotations.CookieValue;
import com.yatang.xc.xcr.model.RequestMap;
import com.yatang.xc.xcr.util.Assert;
import com.yatang.xc.xcr.util.InterfacePramsPrivate;
import com.yatang.xc.xcr.util.PakcageSanner;
import com.yatang.xc.xcr.util.RequestHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : Jun 8, 2017 6:07:47 AM
 * @Summary : java.io.StringReader
 */
public class RequestBodyParamsArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 若没配置需要取得的Cookie名字则按照注解来取 这里设置默认值一是区分用二是为了避免Spring启动报错
     */
    private static final String DEFAULT_COOKIE_NAME = "__NULL__";
    /**
     * 可通过配置文件spring.cookie.names配置，多个Cookie逗号相隔即可 建议使用对象(注解)的方式
     */
    private @Value("spring.cookie.names:" + DEFAULT_COOKIE_NAME)
    String cookieNames;
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RequestBodyParamsArgumentResolver.class);

    @Override
    public Object resolveArgument(MethodParameter param, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        HttpServletRequest req = request.getNativeRequest(HttpServletRequest.class);
        String body = RequestHelper.getBodyAsString(req);
        Class<?> className = param.getNestedParameterType();
        Assert.isNotEmpty(body, " 请输入正确的参数，当前参数为空");
        logger.info("The method parameter's type ==> " + className.getName());
        logger.info("The client request  parameter ==> " + body);

        // Text
        if (!body.startsWith("{") && !body.endsWith("}")) {
            Map<String, Object> params = new HashMap<>();
            params.put("msg", body);

            if (RequestMap.class.getName().equals(className.getName()) || RequestMap.class.isAssignableFrom(className)) {
                return new RequestMap(params);
            }

            if (PakcageSanner.fieldHasAnnotation(className, CookieValue.class)) {
                resolverCookieValue(className, request, params);
            }

            return InterfacePramsPrivate.convert(className, params);
        }

        // Object
        Map<String, Object> params = new HashMap<>();
        if (RequestMap.class.getName().equals(className.getName()) || RequestMap.class.isAssignableFrom(className)) {
            RequestHelper.hasPayload(body, params);
            return new RequestMap(params);
        }

        if (PakcageSanner.fieldHasAnnotation(className, CookieValue.class)) {
            resolverCookieValue(className, request, params);
        }

        if (!StringUtils.isBlank(body)) {
            RequestHelper.hasPayload(body, params);
        }
        return InterfacePramsPrivate.convert(className, params);
    }

    @Override
    public boolean supportsParameter(MethodParameter param) {
        return param.hasParameterAnnotation(com.yatang.xc.xcr.annotations.Payload.class);
    }

    /**
     * 读取Cookie Value
     */
    protected void resolverCookieValue(Class<?> className, NativeWebRequest request, Map<String, Object> params) {
        if (cookieNames.contains(DEFAULT_COOKIE_NAME)) {
            String cookieNamess = parameterResolver(className);
            if (!StringUtils.isBlank(cookieNamess)) {
                cookieNames = cookieNamess;
            } else return;
        }

        String[] names = cookieNames.split(",");
        if (names == null || names.length == 0) {
            return;
        }

        UrlPathHelper urlPathHelper = new UrlPathHelper();
        HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
        for (String cookieName : names) {
            Cookie cookieValue = WebUtils.getCookie(servletRequest, cookieName);
            if (Cookie.class.isAssignableFrom(className)) {
                params.put(cookieName, cookieValue);
            } else if (cookieValue != null) {
                String requestString = urlPathHelper.decodeRequestString(servletRequest, cookieValue.getValue());
                if (!StringUtils.isBlank(requestString)) {
                    params.put(cookieName, requestString);
                }
            }
        }
        logger.info("Cookie name & cookie value  mapping ==> " + params);
    }

    /**
     * 解析CookieValue 并获取到对应的Value
     */
    protected static String parameterResolver(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        StringBuilder cookies = new StringBuilder();
        for (Field field : fields) {
            com.yatang.xc.xcr.annotations.CookieValue ck = field.getAnnotation(com.yatang.xc.xcr.annotations.CookieValue.class);
            if (ck != null) {
                cookies.append(ck.value()).append(",");
            }
        }
        if (!StringUtils.isBlank(cookies.toString())) {
            return cookies.deleteCharAt(cookies.length() - 1).toString();
        }
        return null;
    }
}