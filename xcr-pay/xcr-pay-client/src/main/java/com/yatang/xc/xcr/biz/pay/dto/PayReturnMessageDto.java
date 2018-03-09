package com.yatang.xc.xcr.biz.pay.dto;

import com.yatang.xc.xcr.biz.pay.enums.PayRecordState;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付回调参数
 * Created by wangyang on 2017/10/27.
 */
public class PayReturnMessageDto implements Serializable {

    private static final long serialVersionUID = 3271954072985277315L;

    private String mchId; //商户号
    private String outTradeNo; //订单编号（商户订单号）
    private PayRecordState payState; //支付状态
    private Date timeEnd; //支付完成时间

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public PayRecordState getPayState() {
        return payState;
    }

    public void setPayState(PayRecordState payState) {
        this.payState = payState;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}
