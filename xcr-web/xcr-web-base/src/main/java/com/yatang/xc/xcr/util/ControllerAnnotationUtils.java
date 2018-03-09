package com.yatang.xc.xcr.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @Author : BobLee
 * @CreateTime : 2018年01月05日 11:34
 * @Summary : Controller 注解校验
 */
public final class ControllerAnnotationUtils {

    /**
     * @since ： 2.7
     * @param : [ann]
     * @return ：  boolean
     * @Summary :  检查Controller方法是否存在该注解
     */
    public static boolean targetMethodHasAnnotation(Class<? extends Annotation> ann, HttpServletRequest request) {
        Assert.isNotEmpty(ann);
        String requestURL = RequestHelper.getURL(request);
        if (StringUtils.isNotBlank(requestURL)) {
            HandlerMethod handlerMethod = HandlerMethodUtils.getHandlerMethod(requestURL);
            if (handlerMethod != null) {
                if (handlerMethod.getMethodAnnotation(ann) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @since ： 2.7
     * @param : [ann]
     * @return ：  boolean
     * @Summary :  检查Controller方法参数是否存在该注解
     */
    public static boolean targetParametersAnnotation(Class<? extends Annotation> ann, HttpServletRequest request) {
        Assert.isNotEmpty(ann);
        String requestURL = RequestHelper.getURL(request);
        if (StringUtils.isNotBlank(requestURL)) {
            HandlerMethod handlerMethod = HandlerMethodUtils.getHandlerMethod(requestURL);
            if (handlerMethod != null) {
                MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
                if (methodParameters != null && methodParameters.length != 0) {
                    for (MethodParameter methodParameter : methodParameters) {
                        if (methodParameter.getParameterAnnotation(ann) != null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
