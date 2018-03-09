package com.yatang.xc.xcr.enums;

/**
 * 因为这几种条件基本都是固定的，固化删选条件
 * @author gaodawei
 *
 */
public enum StateEnum {
	STATE_0("M00","正常")
	,STATE_1("M01","账号过期，请重新登录")
	,STATE_2("M02","服务太忙了,请稍后再试")
	,STATE_3("M03","")
	,STATE_4("M04","无该商品")
	,STATE_5("M05","你已被迫下线，请确认是否密码泄露")
	,STATE_6("M06","")
	,STATE_7("M07","")
	,STATE_8("M08","")
	,STATE_9("M09","")
	,STATE_10("M10","")
	,STATE_11("M11","验证码错误")
	,STATE_12("M12","超出发送次数")
	,STATE_13("M13","加盟超时");


	private String state;
	private String desc;
	/**
	 * @param state
	 * @param desc
	 */
	private StateEnum(String state, String desc) {
		this.state = state;
		this.desc = desc;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
