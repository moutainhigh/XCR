package com.yatang.xc.xcr.biz.core.enums;

/**
 * 功能使用统计枚举
 * Created by wangyang on 2017/7/3.
 */
public enum FunctionStatisticsEnum {


    MISSSIONlIST("/xcr/User/missionList.htm","查看任务列表");

    private String state;
    private String stateInfo;

    FunctionStatisticsEnum(String state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public String getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static FunctionStatisticsEnum stateOf(String index) {
        for (FunctionStatisticsEnum state : values()) {
            if (state.getState().equals(index)) {
                return state;
            }
        }
        return null;
    }

}
