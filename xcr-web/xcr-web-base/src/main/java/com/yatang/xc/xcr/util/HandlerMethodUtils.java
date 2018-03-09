package com.yatang.xc.xcr.util;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author : BobLee
 * @CreateTime : 2018年01月04日 15:37
 * @Summary :
 */
public final class HandlerMethodUtils {

    private static final RequestMappingHandlerMapping requestMapping;
    private final static Map<RequestMappingInfo, org.springframework.web.method.HandlerMethod> handlerMethods;

    static {
        requestMapping = SpringContext.getBean(RequestMappingHandlerMapping.class);
        handlerMethods = requestMapping.getHandlerMethods();
    }

    /**
     * @param : []
     * @return ：  org.springframework.web.method.HandlerMethod
     * @Summary :
     * @since ： 2.7
     */
    public static HandlerMethod getHandlerMethod(String url) {
        if (handlerMethods != null) {
            Iterator<RequestMappingInfo> iterator = handlerMethods.keySet().iterator();
            while (iterator.hasNext()) {
                RequestMappingInfo keyModel = iterator.next();
                if (keyModel != null) {
                    PatternsRequestCondition patternsCondition = keyModel.getPatternsCondition();
                    if (patternsCondition != null) {
                        Set<String> patterns = patternsCondition.getPatterns();
                        if (patterns != null && patterns.size() > 0 && patterns.contains(url)) {
                            return handlerMethods.get(keyModel);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param : []
     * @return ：  org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
     * @Summary :
     * @since ： 2.7
     */
    public static RequestMappingHandlerMapping getRequestMapping() {
        return requestMapping;
    }

    /**
     * @param : []
     * @return ：  org.springframework.web.servlet.mvc.condition.PatternsRequestCondition
     * @Summary :
     * @since ： 2.7
     */
    public static PatternsRequestCondition getPatternsRequestCondition(String url) {
        if (handlerMethods != null) {
            Iterator<RequestMappingInfo> iterator = handlerMethods.keySet().iterator();
            while (iterator.hasNext()) {
                RequestMappingInfo keyModel = iterator.next();
                if (keyModel != null) {
                    PatternsRequestCondition patternsCondition = keyModel.getPatternsCondition();
                    if (patternsCondition != null) {
                        Set<String> patterns = patternsCondition.getPatterns();
                        if (patterns != null && patterns.size() > 0 && patterns.contains(url)) {
                            return patternsCondition;
                        }
                    }
                }
            }
        }
        return null;
    }


}
