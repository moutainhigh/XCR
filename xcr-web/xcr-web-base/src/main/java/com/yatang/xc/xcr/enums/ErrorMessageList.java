package com.yatang.xc.xcr.enums;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月26日 11:36
 * @Summary : 异常，提示信息
 */
public enum ErrorMessageList {

    SERVER("服务异常，请稍后重试"),

    OFFLINE("您已被迫下线，请确认是否密码泄露"),

    INVALID("账号过期，请重新登录"),

    TIMEOUT("请求超时,请稍后重试"),

    NETWORKS("网络异常,请稍后重试"),

    INTERNAL_SERVER_ERROR("内部服务器错误"),

    /**
     * 通常指 Token校验失败
     */
    VALIDATION("信息校验失败，请重试"),

    /**
     * XXX不能为空 : 使用这个 需要调用 #placeholder(String) 替换占位符
     * <pre>
     * System.out.println(ErrorMessageList.R_PARAM_NULL.placeholder("UserId"));
     * Console ==> UserId不能为空
     * </per>
     */
    R_PARAM_NULL("%s不能为空");

    private String message;

    ErrorMessageList(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String placeholder(String replaceMessage) {
        return String.format(this.getMessage(), replaceMessage);
    }

}
