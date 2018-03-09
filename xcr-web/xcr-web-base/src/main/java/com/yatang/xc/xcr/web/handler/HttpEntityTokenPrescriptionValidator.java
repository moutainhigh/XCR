package com.yatang.xc.xcr.web.handler;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.enums.ErrorMessageList;
import com.yatang.xc.xcr.exceptions.ErrorMessageResponseException;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.util.Assert;
import com.yatang.xc.xcr.util.RequestHelper;
import com.yatang.xc.xcr.util.Validation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月25日 15:27
 * @Summary : Token 验证
 */
@SuppressWarnings("unchecked")
public class HttpEntityTokenPrescriptionValidator extends HttpServletRequestWrapper {

    private String body;
    private final static String USERID = "UserId";
    private final static Logger logger = LoggerFactory.getLogger(HttpEntityTokenPrescriptionValidator.class);

    public HttpEntityTokenPrescriptionValidator(HttpServletRequest request) {
        super(request);
        this.body = RequestHelper.getBodyAsString(request);
        if (StringUtils.isNotBlank(this.body) && body.startsWith("{") && body.endsWith("}")) {
            validateSession(this.body);
        }
    }

    public void validateSession(String body) {
        if (StringUtils.isNotBlank(body)) {
            Map<String, Object> bodyMap = JSON.parseObject(body, Map.class);
            if (bodyMap != null && bodyMap.size() != 0) {
                String token = Assert.toString(bodyMap.get(Validation.TOKEN_KEY));
                String userId = Assert.toString(bodyMap.get(USERID));
                String flag = Validation.token(userId, token);
                if (Validation.TOKEN_INVALID.equals(flag)) {
                    logger.warn(Assert.append("{UserId:", userId, ",Token:", token, ",Status:", Validation.TOKEN_INVALID, "}"));
                    throw new ErrorMessageResponseException(ResultMap.faill("M01", ErrorMessageList.INVALID));
                }

                if (Validation.TOKEN_STATE_ABNORMALITY.equals(flag)) {
                    logger.warn(Assert.append("{UserId:", userId, ",Token:", token, ",Status:", Validation.TOKEN_INVALID, "}"));
                    throw new ErrorMessageResponseException(ResultMap.faill("M05", ErrorMessageList.OFFLINE));
                }
                return;
            }
        }
        throw new ErrorMessageResponseException(ResultMap.faill(ErrorMessageList.SERVER));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        logger.info(body);
        final ByteArrayInputStream stream = new ByteArrayInputStream((StringUtils.isBlank(body) ? null : body.getBytes()));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

}