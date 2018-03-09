package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年8月9日 下午11:53:08 
* @version 1.0.0
* @function 
*/
public enum StateEnum {
	
	STATE_ERROR("100","程序出错"),
	STATE_OK("200","正常"),
	STATE_EXCE("300","异常"),
	STATE_NORMOL_ERR("500","正常错误返回");
	
	private String code;
	private String msg;
	/**
	 * @param code
	 * @param msg
	 */
	private StateEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
 