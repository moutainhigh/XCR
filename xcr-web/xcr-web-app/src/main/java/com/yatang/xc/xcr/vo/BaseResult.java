package com.yatang.xc.xcr.vo;

public class BaseResult {
    protected String responseCode = "200";

    protected String errMsg;

    public BaseResult(){};

    public BaseResult(String code, String msg){
        this.responseCode = code;
        this.errMsg = msg;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}