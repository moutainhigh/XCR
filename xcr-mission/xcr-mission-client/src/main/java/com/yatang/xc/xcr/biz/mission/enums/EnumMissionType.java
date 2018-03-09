package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 任务类型枚举
 *
 * @author : zhaokun
 * @version : 2017年3月20日  zhaokun
 * @date : 2017年3月20日 下午5:39:59
 */
public enum EnumMissionType {
    MISSION_TYPE_DAILY("DAILY", "日常任务"),
    MISSION_TYPE_RECOMMEND("RECOMMEND", "推荐任务"),
    MISSION_TYPE_STUDY("STUDY", "学习任务"),
    MISSION_TYPE_AUTO_COMPLETE("AUTO_COMPLETE", "自动奖励发放任务");


    private String code;

    private String message;


    EnumMissionType(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

}
