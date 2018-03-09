package com.yatang.xc.xcr.biz.pay.enums;

/**
 * 支付履历状态
 * Created by wangyang on 2017/11/6.
 */
public enum PayRecordState {

    PREPAID(0, "预支付"),
    PREPAID_SUCCESS(1, "预支付成功(待支付)"),
    PREPAID_FAIL(2, "预支付失败"),
    PAY_SUCCESS(3, "支付成功"),
    PAY_FAIL(4, "支付失败");

    private int state;
    private String stateInfo;

    PayRecordState(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static PayRecordState stateOf(int index) {
        for (PayRecordState state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }


}
