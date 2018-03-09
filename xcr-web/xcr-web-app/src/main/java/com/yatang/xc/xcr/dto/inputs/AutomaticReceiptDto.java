package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月07日 14:10
 * @Summary : 设置自动接单v2.2
 */
@SuppressWarnings("serial")
public class AutomaticReceiptDto implements Serializable {

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
     * 自动接单
     *0：手动接单，1：自动接单
     */
    private Integer reciveStatus;

    @Override
    public String toString() {
        return "AutomaticReceiptDto{" +
                "userId='" + userId + '\'' +
                ", storeSerialNo='" + storeSerialNo + '\'' +
                ", token='" + token + '\'' +
                ", reciveStatus=" + reciveStatus +
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

    public Integer getReciveStatus() {
        return reciveStatus;
    }

    public void setReciveStatus(Integer reciveStatus) {
        this.reciveStatus = reciveStatus;
    }
}
