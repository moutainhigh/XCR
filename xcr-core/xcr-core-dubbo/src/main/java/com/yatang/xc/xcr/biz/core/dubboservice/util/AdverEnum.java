package com.yatang.xc.xcr.biz.core.dubboservice.util;

/**
 * 广告枚举返回信息
* <class description>
*		
* @author: zhongrun
* @version: 1.0, 2017年7月17日
 */
public enum AdverEnum {
	
	ERROR_CODE("500","后台操作异常"),
	ADVER_EXSIST_CODE("300","广告已存在"),
	ADVER_GROUP_EXSIST_CODE("301","广告组已存在"),
	ADVER_GROUP_INCLUDE_CODE("301","广告组已存在"),
	ADVER_POSITION_INCLUDE_CODE("302","同一个位置不能启动多个组"),	
	ADVER_PARAM_ERROR("303","参数不对"),
	ADVER_REPEAT_ERROR("304","发布版本号不能相同")
	;
	
	private String code;
	
	private String msg;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private AdverEnum(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	public static void main(String[] args) {
		System.out.println(AdverEnum.ADVER_GROUP_EXSIST_CODE.getCode());
	}
}




