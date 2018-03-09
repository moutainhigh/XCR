package com.yatang.xc.xcr.enums;

/**
 * 
 * 任务申请租金补贴附件名称枚举
 * @author : caotao
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  caotao 
 */
public enum EnumRentName {
    ATTACHMENT_TV_ADVERT("TV_ADVERT", "开启电视机广告"),
    ATTACHMENT_LOGIN_CASHIER_VIEW("LOGIN_CASHIER_VIEW", "收银机登录收银系统"),
    ATTACHMENT_GOODS_PLACE_STYLE("GOODS_PLACE_STYLE", "货架商品陈列样式"),
    ATTACHMENT_JOIN_INVOICE("JOIN_INVOICE", "加盟验收单");
    

    private String code;

    private String message;


    EnumRentName(String code,String message){
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
