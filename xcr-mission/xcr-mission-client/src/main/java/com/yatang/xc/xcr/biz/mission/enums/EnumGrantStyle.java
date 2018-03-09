package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 发放方式枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumGrantStyle {
    GRANT_STYLE_REAL_TIME("REAL_TIME", "即时发放"),
    GRANT_STYLE_NONE("NONE", "无");
    

    private String code;

    private String message;


    EnumGrantStyle(String code,String message){
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
