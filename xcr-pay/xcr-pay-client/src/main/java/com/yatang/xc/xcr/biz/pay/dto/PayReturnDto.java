package com.yatang.xc.xcr.biz.pay.dto;

import java.io.Serializable;

/**
 * 支付返回参数
 * Created by wangyang on 2017/10/26.
 */
public class PayReturnDto implements Serializable {

    private static final long serialVersionUID = -6313370093020947812L;

    private String shopNo;//小超编号
    private String phone;//商户电话
    private String mchId; //商户号
    private String tokenId;//授权码
    private String services;//支持支付方式
    private String outTradeNo;//商户订单号

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }
}
