package com.yatang.xc.xcr.biz.pay.enums;

/**
 * 商家状态枚举
 * Created by wangyang on 2017/11/8.
 */
public enum MerchantState {

    CASH_DEPOSIT("step_10", "代付保证金");

    private String state;
    private String stateInfo;

    MerchantState(String state, String stateInfo) {
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
