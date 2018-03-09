package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年11月2日 下午5:37:25 
* @version 1.0.0
* @function 
*/
public enum XCCheckEnum {
	
	CHECK_1("205","小超编号或加盟手机号码错误","M02"),//小超编号不存在
	CHECK_2004("2004","小超编号或加盟手机号码错误","M02"),//小超编号和手机号码不匹配
	CHECK_3("","验证码已发送，请稍后再试！","M02"),//ck_code:8001
	CHECK_4("","今日可发送次数已用完！","M12"),//ck_code:8001
	CHECK_207("207","短信发送失败","M02"),
	CHECK_215("215","校验出错","M02"),
	CHECK_2005("2005","加盟超时","M13"),
	CHECK_2006("2006","您的店铺已经加盟","M15"),
	CHECK_2007("2007","金融相关错误","M02"),
	CHECK_2009("2009","该店未绑定金融账号","M02"),
	CHECK_40001("40001","您的加盟流程正在进行中，无需电子签约入驻","M02"),;
	
	private String ck_code;
	private String tipMsg;
	private String appState;
	
	private XCCheckEnum(String ck_code,String tipMsg, String appState) {
		this.ck_code = ck_code;
		this.tipMsg = tipMsg;
		this.appState = appState;
	}
	public String getTipMsg() {
		return tipMsg;
	}
	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}
	public String getCk_code() {
		return ck_code;
	}
	public void setCk_code(String ck_code) {
		this.ck_code = ck_code;
	}
	public String getAppState() {
		return appState;
	}
	public void setAppState(String appState) {
		this.appState = appState;
	}
	
}
 