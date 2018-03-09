package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月07日 14:13
 * @Summary : 设置店铺简称v2.2
 */
@SuppressWarnings("serial")
public class ShopAbbreviationDto implements Serializable{

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
     * 店铺简称
     */
    private String storeAbbrevy;

    @Override
    public String toString() {
        return "ShopAbbreviationDto{" +
                "userId='" + userId + '\'' +
                ", storeSerialNo='" + storeSerialNo + '\'' +
                ", token='" + token + '\'' +
                ", storeAbbrevy='" + storeAbbrevy + '\'' +
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

    public String getStoreAbbrevy() {
        return storeAbbrevy;
    }

    public void setStoreAbbrevy(String storeAbbrevy) {
        this.storeAbbrevy = storeAbbrevy;
    }
}
