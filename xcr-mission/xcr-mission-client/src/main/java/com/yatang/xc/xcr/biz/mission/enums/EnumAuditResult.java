package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 奖励类型枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumAuditResult {
    AUDIT_RESULT_APPROVE("APPROVE", "审核通过"),
    AUDIT_RESULT_REJECT("REJECT", "审核不通过");
    

    private String code;

    private String message;


    EnumAuditResult(String code,String message){
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
