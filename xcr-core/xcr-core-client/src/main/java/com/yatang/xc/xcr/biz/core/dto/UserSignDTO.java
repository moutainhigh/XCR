package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;

/**
 * 用户签到
 * Created by wangyang on 2017/7/7.
 */
public class UserSignDTO implements Serializable {

    private static final long serialVersionUID = -7509923005187168758L;

    private Long id;
    private String userId;    //用户id(加盟商编号)
    private String shopCode;    //门店编号
    private int type;           //奖励类型(1:现金，2：积分)
    private String data;        //奖励数额

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
