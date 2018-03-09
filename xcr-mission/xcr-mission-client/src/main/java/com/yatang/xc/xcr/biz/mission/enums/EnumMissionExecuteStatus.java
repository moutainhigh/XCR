package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 任务状态枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumMissionExecuteStatus {
    STATUS_INIT("INIT", "初始化"),
    STATUS_MISSION_AUDIT("MISSION_AUDIT", "任务审核中"),
    STATUS_UNFINISHED("MISSION_UNFINISHED", "未完成"),
    STATUS_FINISHED("MISSION_FINISHED", "完成"),
    STATUS_AWARD_AUDIT("AWARD_AUDIT", "奖励待审核"),
    STATUS_INVALID("INVALID", "失效"),
    STATUS_END("END", "结束");
    
    

    private String code;

    private String message;


    EnumMissionExecuteStatus(String code,String message){
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
