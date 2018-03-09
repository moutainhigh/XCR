package com.yatang.xc.xcr.util;

import com.alibaba.dubbo.common.URL;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月25日 15:42
 * @Summary :
 */
public class RequestHelper {

    public static final String PARAMS = "msg";
    private static final String PARAMS_EQ = "msg=";
    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

    /**
     * @param : [request]
     * @return ：  java.lang.String
     * @Summary :
     * @since ： 2.6.0
     */
    public static String getBodyAsString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }

        String body = sb.toString();
        if (StringUtils.isNotBlank(body)) logger.info(body);
        if ( ! StringUtils.isBlank(body)) {
            try {
                body = body.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B");
                body = URLDecoder.decode(body, "UTF-8");
            } catch (Exception e) {
                body = URL.decode(body);
            }
            if (body.contains(PARAMS_EQ)) {
                body = body.substring(PARAMS_EQ.length(), body.length());
            }
        }

        HttpServletRequest convert = convert(request);
        if (StringUtils.isBlank(body)) {
            body = urlParameter(convert);
            if ( ! StringUtils.isBlank(body)) {
                logger.info("The client requests parameter is URL Parameter ==> " + body);
            }
        }

        if (convert != null) {
            if ( ! StringUtils.isBlank(body)) {
                logger.info(Assert.append(convert.getRequestURI(),  " #HttpMethod=" , convert.getMethod() , "  #Http Payload Body ==> " , body));
            }
        }
        return body;
    }

    /**
     * @param : [request]
     * @return ：  javax.servlet.http.HttpServletRequest
     * @Summary :
     * @since ： 2.6.0
     */
    public static HttpServletRequest convert(ServletRequest request) {
        if (request instanceof HttpServletRequest) return (HttpServletRequest) request;
        return null;
    }

    /**
     * @param : [convert]
     * @return ：  java.lang.String
     * @Summary :
     * @since ： 2.6.0
     */
    private static String urlParameter(HttpServletRequest convert) {
        String msg = convert.getParameter(PARAMS);
        if ( ! StringUtils.isBlank(msg)) { return msg; }

        Map<String, String[]> parameterMap = convert.getParameterMap();
        if (parameterMap != null && parameterMap.size() > 0) {
            logger.info(JSON.toJSONString(parameterMap));
        }

        if (parameterMap == null || parameterMap.size() == 0) return null;
        Map<String, String[]> parameterMaps = new HashMap<>(parameterMap.size());
        Iterator<String> iterator = parameterMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if ( ! StringUtils.isBlank(key)) {
                String[] value = parameterMap.get(key);
                if (value != null && value.length == 0) {
                    parameterMaps.put(key, parameterMap.get(key));
                }
            }
        }
        if (parameterMaps.size() == 0) { return null; }
        return JSON.toJSONString(parameterMap);
    }


    ///<Summary>
    ///   获取根目录
    ///</Summary>
    public static String getURL(HttpServletRequest request) {
        String root = System.getProperty("webapp.root");
        String requestURI = request.getRequestURI();
        if (StringUtils.isBlank(root)) {
            root = request.getSession().getServletContext().getRealPath("/");
        }

        if (StringUtils.isNotBlank(requestURI)) {
            requestURI = requestURI.substring(1, requestURI.length());
            String webRoot = requestURI.substring(0, requestURI.indexOf("/"));
            if (root.contains(webRoot) && root.indexOf(webRoot) != -1) {
                root = root.substring((root.indexOf(webRoot)), root.length());
                root = root.substring(root.indexOf(webRoot), root.indexOf(webRoot) + webRoot.length()).replace(File.separator, "/");
            }
        }

        logger.info("project name {}", root);
        if (requestURI.contains(root) && requestURI.indexOf(root) != -1) {
            root = requestURI.substring(root.length(), requestURI.length());
        } else {
            root = "/" + requestURI;
        }

        if (!root.startsWith("/")) root = "/" + root;
        logger.info(" {} ==> {}", requestURI, root);
        return root;
    }

    /**
     * 设置payload参数
     */
    public static void hasPayload(String body, Map<String, Object> params) {
        Map<String, Object> map = null;
        String payload = null;
        boolean isCiphertext = false;
        try {
            map = JSON.parseObject(body, Map.class);
            payload = Assert.toString(map.get(RequestHelper.PARAMS));
            if (!StringUtils.isBlank(payload)) {
                Assert.transfor(JSON.parseObject(JSON.toJSONString(payload), Map.class), params);
            } else {
                Assert.transfor(JSON.parseObject(body, Map.class), params);
            }
        } catch (JSONException e) {
            isCiphertext = true;
            if (map != null && map.size() > 0) {
                payload = String.valueOf(map.get(RequestHelper.PARAMS));
            } else {
                payload = body;
            }
        }

        if (isCiphertext) {
            Object deviceId = params.get(ParamsArgumentResolver.DEVICE_ID);
            Object type = params.get(ParamsArgumentResolver.TYPE);
            if (deviceId != null && !"".equals(deviceId)) {
                String clientData = ParamsArgumentResolver.decryptReq(payload, deviceId.toString(), type.toString());
                Assert.transfor(JSON.parseObject(clientData, Map.class), params);
            }
        }
    }

}
