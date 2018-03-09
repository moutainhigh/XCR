package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 任务状态枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumMissionRelatedStatus {
    STATUS_INIT("INIT", "未发布"),
    STATUS_PUBLISH("PUBLISH", "已发布");
    
    

    private String code;

    private String message;


    EnumMissionRelatedStatus(String code,String message){
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
