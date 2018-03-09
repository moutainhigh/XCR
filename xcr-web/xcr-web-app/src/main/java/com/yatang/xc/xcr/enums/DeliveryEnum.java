package com.yatang.xc.xcr.enums;

/**
 * 因为这几种条件基本都是固定的，固化删选条件
 * @author gaodawei
 *
 */
public enum DeliveryEnum {
	
	STYLE_101("101","商家配送"),STYLE_102("102","用户自提"),STYLE_103("103","蜂鸟配送"),STYLE_104("104","仓库配送");
	
	/**
	 * @param code
	 * @param desc
	 */
	private DeliveryEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	private String code;
	private String desc;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
