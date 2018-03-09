package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月07日 14:05
 * @Summary : 营业状态修改v2.0
 */
@SuppressWarnings("serial")
public class BusinessStateDto implements Serializable {

    /**
     *用户ID
     */
    private String userId;

    /**
     *店铺编号
     */
    private String storeSerialNo;

    /**
     *令牌
     */
    private String token;

    /**
     *营业状态
     * 0：暂停营业，1：正常营业
     */
    private Integer businessStatus;

    @Override
    public String toString() {
        return "BusinessStateDto{" +
                "userId='" + userId + '\'' +
                ", storeSerialNo='" + storeSerialNo + '\'' +
                ", token='" + token + '\'' +
                ", businessStatus=" + businessStatus +
                '}';
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

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }
}
