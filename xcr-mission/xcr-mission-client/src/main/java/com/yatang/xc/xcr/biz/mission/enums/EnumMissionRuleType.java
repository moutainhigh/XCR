package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 任务模板枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumMissionRuleType {
    TYPE_SIMPLE("simple", "简单类型规则"),
    TYPE_SIGN("sign", "签到类型规则"),
    TYPE_MONTH_FACE_PAY("month_face_pay","月度日均当面付"),
    TYPE_DAY_FACE_PAY("day_face_pay","日当面付笔数"),
    TYPE_MONTH_BUY_AMOUNT("month_buy_amount","月度供应链采购金额"),
    TYPE_COMPLEX("complex", "复杂类型规则");
    

    private String code;

    private String message;


    EnumMissionRuleType(String code,String message){
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
