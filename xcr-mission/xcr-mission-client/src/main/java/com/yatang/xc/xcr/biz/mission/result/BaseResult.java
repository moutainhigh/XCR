package com.yatang.xc.xcr.biz.mission.result;

import java.io.Serializable;

/**
 * 
 * 基础返回类用于service接口的返回值
 * @author : zhaokun
 * @date : 2017年3月20日 下午7:23:33  
 * @version : 2017年3月20日  zhaokun
 */
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 6821913805128204057L;

    private T resultObject;

    private boolean isSuccess;

    private String message;

    private String errorCode;


    public BaseResult() {
    }


    public BaseResult(T resultObject) {
        this.resultObject = resultObject;
    }


    public T getResultObject() {
        return resultObject;
    }


    public void setResultObject(T resultObject) {
        this.resultObject = resultObject;
    }


    public boolean isSuccess() {
        return isSuccess;
    }


    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
