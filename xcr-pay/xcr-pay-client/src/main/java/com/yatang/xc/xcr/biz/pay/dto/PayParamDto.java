package com.yatang.xc.xcr.biz.pay.dto;

import java.io.Serializable;

/**
 * 支付传入参数
 * Created by wangyang on 2017/10/26.
 */
public class PayParamDto implements Serializable {

    private static final long serialVersionUID = -1449289526518608295L;


    private String shopNo;//小超编号
    private String phone;//商户电话
    private int totalFee;//总金额
    private String mchCreateIp; //订单生成的机器 IP（发起支付APP的IP）

    public String getMchCreateIp() {
        return mchCreateIp;
    }

    public void setMchCreateIp(String mchCreateIp) {
        this.mchCreateIp = mchCreateIp;
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

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

}
