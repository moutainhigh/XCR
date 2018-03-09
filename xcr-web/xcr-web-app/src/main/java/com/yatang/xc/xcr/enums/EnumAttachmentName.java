package com.yatang.xc.xcr.enums;

/**
 * 
 * 任务申请门头照租金补贴附件名称枚举
 * @author : caotao
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  caotao 
 */
public enum EnumAttachmentName {
	ATTACHMENT_DOOR_HEADER_VIEW("DOOR_HEADER_VIEW", "门头全景"),
	ATTACHMENT_DOOR_HEADER_NUMBER("DOOR_HEADER_NUMBER", "门头编号特写"),
	ATTACHMENT_DOOR_HEADER_INVOICE("DOOR_HEADER_INVOICE", "门头制作发票"),
	ATTACHMENT_DOOR_HEADER_REPORT("DOOR_HEADER_REPORT", "门头验收单");
    

    private String code;

    private String message;


    EnumAttachmentName(String code,String message){
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

