package com.yatang.xc.xcr.web.filters;

import com.yatang.xc.xcr.annotations.SessionToken;
import com.yatang.xc.xcr.exceptions.ErrorMessageResponseException;
import com.yatang.xc.xcr.util.ControllerAnnotationUtils;
import com.yatang.xc.xcr.web.handler.HttpEntityTokenPrescriptionValidator;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月25日 15:35
 * @Summary :
 */
public class StreamResetFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            boolean hasSessionToken = ControllerAnnotationUtils.targetMethodHasAnnotation(SessionToken.class, request);
            if ( hasSessionToken ) {
                // 二进制信息暂时不处理 目前只处理文本信息
                String contentType = request.getContentType();
                if (MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType)
                        || MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType)
                        || MediaType.TEXT_HTML_VALUE.equalsIgnoreCase(contentType)
                        || MediaType.TEXT_PLAIN_VALUE.equalsIgnoreCase(contentType)
                        || MediaType.TEXT_XML_VALUE.equalsIgnoreCase(contentType)
                        || MediaType.APPLICATION_XML_VALUE.equalsIgnoreCase(contentType)
                        || MediaType.APPLICATION_XHTML_XML_VALUE.equalsIgnoreCase(contentType)
                        || MediaType.APPLICATION_RSS_XML_VALUE.equalsIgnoreCase(contentType)) {
                    request = new HttpEntityTokenPrescriptionValidator(request);
                }
            }
            filterChain.doFilter(request, servletResponse);
        } catch (Exception exception) {
            if (exception instanceof ErrorMessageResponseException) {
                ErrorMessageResponseException exception1 = (ErrorMessageResponseException) exception;
                try {
                    HttpServletResponse response = (HttpServletResponse) servletResponse;
                    response.getWriter().print(exception1.getResponseBody().toStringEclipse());
                } catch (Exception e) {
                    // pass
                }
            } else {
                throw exception;
            }
        }
    }

    @Override
    public void destroy() {}

}
