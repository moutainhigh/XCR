package com.yatang.xc.xcr.biz.pay.enums;

/**
 * 威富通返回交易状态
 * Created by wangyang on 2017/11/7.
 */
public enum TradeState {

    SUCCESS("SUCCESS", "支付成功"),
    REFUND("REFUND", "转入退款"),
    NOTPAY("NOTPAY", "未支付"),
    CLOSED("CLOSED", "已关闭"),
    PAYERROR("PAYERROR", "支付失败");

    private String state;
    private String stateInfo;

    TradeState(String state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
}
