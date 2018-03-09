package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 任务附件类型枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumAttachmentType {
    ATTACHMENT_MISSION_INFO("INFO", "任务信息附件"),
    ATTACHMENT_MISSION_EXECUTE("EXECUTE", "任务过程附件");
    

    private String code;

    private String message;


    EnumAttachmentType(String code,String message){
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
