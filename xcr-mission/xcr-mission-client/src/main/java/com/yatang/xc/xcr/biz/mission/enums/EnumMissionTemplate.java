package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 任务模板
 * Created by wangyang on 2017/11/5.
 */
public enum EnumMissionTemplate {

    BUY_PRODUCT("T002", "购买收银机"),
    USER_SIGN("T007", "连续签到"),
    MONTH_FACE_PAY("T008", "月度日均当面付"),
    DAY_FACE_PAY("T009", "日当面付笔数"),
    MONTH_BUY_AMOUNT("T010", "月度供应链采购金额");

    private String code;

    private String message;


    EnumMissionTemplate(String code, String message) {
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
