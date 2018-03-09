package com.yatang.xc.xcr.exceptions;

import com.yatang.xc.xcr.enums.ErrorMessageList;
import com.yatang.xc.xcr.model.ResultMap;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月26日 11:03
 * @Summary :  这个异常在特殊情况下 将异常信息当做客户端提示信息返回
 * 处理器： 只是利用其异常机制中断方法(线程)执行。无详细堆栈信息输出。
 */
public class ErrorMessageResponseException extends RuntimeException{

    private static final long serialVersionUID = 3264687668928470981L;

    private  ResultMap responseBody;

    public ErrorMessageResponseException(ResultMap responseBody) {
        super();
        this.responseBody = responseBody;
    }

    public ErrorMessageResponseException(String message) {
        super(message);
        this.responseBody = ResultMap.failll(message);
    }

    public ErrorMessageResponseException(String status, String message) {
        super(message);
        this.responseBody = ResultMap.faill(status, message);
    }

    public ErrorMessageResponseException(ErrorMessageList message) {
        super(message.getMessage());
        this.responseBody = ResultMap.failll(message.getMessage());
    }

    public ResultMap getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResultMap responseBody) {
        this.responseBody = responseBody;
    }
}
