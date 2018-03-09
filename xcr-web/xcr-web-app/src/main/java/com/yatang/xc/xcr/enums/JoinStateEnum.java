package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年11月3日 上午10:18:16 
* @version 1.0.0
* @function 
*/
public enum JoinStateEnum {
	
	joinState_ChooseBonds("waitChooseBonds","待选择档段","0"),
	joinState_sign_waiting("sign_waiting","您已选择保证金金额","1"),
	joinState_9("step_9","签约逾期","M13"),
	joinState_10("step_10","待付保证金","2"),
	joinState_11("step_11","支付逾期","M13"),
	joinState_12("step_12","(合同签订完成)完成加盟","M15"),
	joinState_13("step_13","(完成支付)完成加盟","M15");
	
	private String state;
	private String desc;
	private String appState;
	
	private JoinStateEnum(String state, String desc,String appState) {
		this.state = state;
		this.desc = desc;
		this.appState = appState;
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

	public String getAppState() {
		return appState;
	}

	public void setAppState(String appState) {
		this.appState = appState;
	}
	
}
 