package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年10月20日 上午10:06:31 
* @version 1.0.0
* @function 
*/
public enum StoreActivityEnum {
	
	STORE_ACTIVITY_1(1,"领券活动"),STORE_ACTIVITY_2(2,"折扣特价活动");
	
	private Integer code;
	private String desc;
	/**
	 * @param code
	 * @param desc
	 */
	private StoreActivityEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
 