package com.yatang.xc.xcr.biz.train.enums;


/**
 * @描述: 课堂公共枚举类
 * @作者: huangjianjun
 * @创建时间: 2017年4月11日-下午3:32:35 .
 * @版本: 1.0 .
 * @param <T>
 */
public enum PublicEnum {
	ZERO("0"),
	ONE("1"),
	TWO("2"),
	THREE("3"),
	
	ID("id"),
	NAME("name"),
	
	ERROR_CODE("200"),
	SUCCESS_CODE("100"),
	ERROR_MSG("后台操作异常")
	;
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private PublicEnum(String code) {
		this.code = code;
	}
}
