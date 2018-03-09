package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年10月20日 上午10:06:31 
* @version 1.0.0
* @function 
*/
public enum StoreActivityStateEnum {
	
	STORE_ACT_STATE_0(0,"全部"),STORE_ACT_STATE_1(1,"未开始"),STORE_ACT_STATE_2(2,"进行中"),STORE_ACT_STATE_3(3,"已结束"),STORE_ACT_STATE_4(4,"已作废");
	
	private Integer code;
	private String desc;
	/**
	 * @param code
	 * @param desc
	 */
	private StoreActivityStateEnum(Integer code, String desc) {
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
 