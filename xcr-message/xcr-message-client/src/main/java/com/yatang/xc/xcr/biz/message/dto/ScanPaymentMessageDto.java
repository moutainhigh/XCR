package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @描述: 扫码支付消息
 * @作者: echo
 * @创建时间: 2017年9月25日-下午3:31:51 .
 */
public class ScanPaymentMessageDto implements Serializable {

    private static final long serialVersionUID = -8278870669702427835L;

    private String orderId;

    private String resultCode;

    private String merchantId;

    private String remark;

    private double totalAmount;

    private Date payTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }


}
