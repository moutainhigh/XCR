package com.yatang.xc.xcr.biz.core.enums;

/**
 * 签到奖励类型枚举
 * Created by wangyang on 2017/7/7.
 */
public enum UserSignTypeEnum {

    CASH(1, "现金奖励"),
    SCORE(2, "积分奖励");


    private int state;
    private String stateInfo;

    UserSignTypeEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static UserSignTypeEnum stateOf(int index) {
        for (UserSignTypeEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
