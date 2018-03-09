package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 奖励类型枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumAwardType {
    AWARD_TYPE_CASH("CASH", "现金"),
    AWARD_TYPE_SCORE("SCORE", "积分"),
    AWARD_TYPE_CASH_AND_SCORE("CASH_AND_SCORE", "现金和积分"),
    AWARD_TYPE_NONE("NONE", "无");
    

    private String code;

    private String message;


    EnumAwardType(String code,String message){
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
