package com.yatang.xc.xcr.model;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月12日 11:56
 * @Summary :
 */
@SuppressWarnings("serial")
public class RequestBaseModel implements Serializable{

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 门店编号
     */
    private String storeSerialNo;

    /**
     * TOKEN
     */
    private String token;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userId\":\"").append(userId).append('\"');
        sb.append(",\"storeSerialNo\":\"").append(storeSerialNo).append('\"');
        sb.append(",\"token\":\"").append(token).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreSerialNo() {
        return storeSerialNo;
    }

    public void setStoreSerialNo(String storeSerialNo) {
        this.storeSerialNo = storeSerialNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
