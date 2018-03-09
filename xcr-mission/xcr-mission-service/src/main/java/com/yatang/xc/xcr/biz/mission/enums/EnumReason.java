package com.yatang.xc.xcr.biz.mission.enums;


public enum EnumReason {
    REASON_SYS_CREATE("SYS_CREATE", "系统创建"),
    REASON_BPM_CALLBACK("BPM", "bpm流程回调"),
    REASON_CORES_CALLBACK("CORES_CALLBACK", "小超课堂课程学习回调"),
    REASON_MANUAL_AUDIT("MANUAL_AUDIT", "启动人工审核流程"),
    REASON_BPM_AUDIT_AWARD("BPM_AUDIT_AWARD", "启动任务奖励审核流程"),
    REASON_RULE_CALCULATE("RULE_CALCULATE", "规则引擎计算：通过"),
    REASON_REJECT("REJECT", "审核不通过"),
    
    
    
    
    REASON_SYS_NONE("NONE", "无");
    

    private String code;

    private String message;


    EnumReason(String code,String message){
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
