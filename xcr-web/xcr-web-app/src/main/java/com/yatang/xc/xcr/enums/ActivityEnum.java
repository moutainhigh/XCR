package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年7月28日 下午2:07:56 
* @version 1.0.0
* @function 
*/
public enum ActivityEnum {
	ACTIVITY_1("1","年会"),ACTIVITY_2("2","爱心服务站"),ACTIVITY_3("3","保留");
	
	private String code;
	private String desc;
	/**
	 * @param code
	 * @param desc
	 */
	private ActivityEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
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
 