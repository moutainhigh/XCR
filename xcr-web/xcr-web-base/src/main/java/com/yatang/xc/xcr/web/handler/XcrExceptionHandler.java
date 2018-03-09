package com.yatang.xc.xcr.web.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.exception.BusinessException;
import com.yatang.xc.xcr.exceptions.ErrorMessageResponseException;
import com.yatang.xc.xcr.exceptions.YTBusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年01月23日 下午2:16:39
 * @Summary :
 */
@SuppressWarnings("unchecked")
public class XcrExceptionHandler extends  ExceptionHandlerExceptionResolver {

	private @Value("${STATE_ERR:M02}") String STATE_ERR;
	private final static Logger logger = LoggerFactory.getLogger(XcrExceptionHandler.class);

	@Override
	protected @ResponseBody ModelAndView doResolveHandlerMethodException(
	        HttpServletRequest request,
            HttpServletResponse response,
            HandlerMethod handlerMethod,
            Exception exception) {
		if (handlerMethod == null) { return null; }
		Method method = handlerMethod.getMethod();
		if (method == null) { return null; }

        response.setStatus(500);
		ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		if (("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) || (responseBodyAnn != null)) {
			try {
			    if ((exception instanceof BusinessException)) {
                    BusinessException ex = (BusinessException) exception;
                    logger.error(ExceptionUtils.getStackTrace(ex));
                    resultMessage(ex.getErrorCode(), ex.getMessage(), modelAndView.getModel());
					return modelAndView;
				}

                if (exceptionAndView(exception, modelAndView)) { return modelAndView; }
				resultMessage(null, null, modelAndView.getModel());
                logger.error(ExceptionUtils.getStackTrace(exception));
				return modelAndView;
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
                resultMessage(null, null, modelAndView.getModel());
				return modelAndView;
			}
		}

        if (exceptionAndView(exception, modelAndView)) { return modelAndView; }
		resultMessage(null, null, modelAndView.getModel());
        logger.error(ExceptionUtils.getStackTrace(exception));
		return modelAndView;
	}

    private boolean exceptionAndView(Exception exception, ModelAndView modelAndView) {
        if(exception instanceof YTBusinessException){
            YTBusinessException e = (YTBusinessException) exception;
            logger.error(ExceptionUtils.getStackTrace(e));
            resultMessage(e.getCode(), e.getMessage(),modelAndView.getModel());
            return true;
        }
        if ( exception instanceof ErrorMessageResponseException){
            ErrorMessageResponseException view = (ErrorMessageResponseException)exception;
            printStackTrace(view);
            modelAndView.getModel().putAll(JSON.parseObject(view.getResponseBody().toStringEclipse(),Map.class));
            return true;
        }
        return false;
    }

    private void printStackTrace(Exception exception) {
		StackTraceElement stackTraceElement = exception.getStackTrace()[0];
		StringBuilder logMsg = new StringBuilder();
		logMsg.append(exception.toString());
		logMsg.append("Method=");
		logMsg.append(stackTraceElement.getClassName());
		logMsg.append(".");
		logMsg.append(stackTraceElement.getMethodName());
		logMsg.append(",FileName=");
		logMsg.append(stackTraceElement.getFileName());
		logMsg.append(",LineNumber=");
		logMsg.append(stackTraceElement.getLineNumber());
		logger.error(logMsg.toString());
	}

	private void resultMessage(String code, String message, Map<String, Object> model) {
		JSONObject stateJson = new JSONObject();
		if (StringUtils.isBlank(code)) {
			stateJson.put("State", STATE_ERR);
			stateJson.put("StateID", STATE_ERR.substring(1));
			stateJson.put("StateValue", STATE_ERR.substring(1));
		}
		else{
			stateJson.put("State", code);
			stateJson.put("StateID", code.substring(1));
			stateJson.put("StateValue", code.substring(1));
		}

		model.put("Status", stateJson);
		if(StringUtils.isBlank(message) || message.contains("Exception")){
            stateJson.put("StateDesc", "服务异常，请稍后重试");
        }
        else{
            stateJson.put("StateDesc", message);
        }
	}

}