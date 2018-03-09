package com.yatang.xc.xcr.enums;

/** 
* @author gaodawei 
* @Date 2018年1月3日 上午11:57:00 
* @version 1.0.0
* @function 
*/
public enum LimitRoleEnum {
	
	SEC_LIMIT("SEC_LIMIT",30,1,"验证请求需间隔30秒，请稍后再试"),
	MNS_LIMIT("TEN_LIMIT",600,3,"十分钟之内限请求三次"),
	DAY_LIMIT("DAY_LIMIT",86400,30,"一天最多可验证30次，请明天再试吧");
	
	private String limitRole;
	private Integer limitTimeLength;// 请求时间限长，单位：秒
	private Integer limitTimes;// 限制请求次数
	private String limitTip;// 提示

	private LimitRoleEnum(String limitRole, Integer limitTimeLength, Integer limitTimes, String limitTip) {
		this.limitRole = limitRole;
		this.limitTimeLength = limitTimeLength;
		this.limitTimes = limitTimes;
		this.limitTip = limitTip;
	}
	public String getLimitRole() {
		return limitRole;
	}
	public void setLimitRole(String limitRole) {
		this.limitRole = limitRole;
	}
	public Integer getLimitTimeLength() {
		return limitTimeLength;
	}
	public void setLimitTimeLength(Integer limitTimeLength) {
		this.limitTimeLength = limitTimeLength;
	}
	public Integer getLimitTimes() {
		return limitTimes;
	}
	public void setLimitTimes(Integer limitTimes) {
		this.limitTimes = limitTimes;
	}
	public String getLimitTip() {
		return limitTip;
	}
	public void setLimitTip(String limitTip) {
		this.limitTip = limitTip;
	}



}
 