package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年8月8日 上午11:33:09 
* @version 1.0.0
* @function 
*/
public enum OrderCancelEnum {
	CANCEL_ORDER_STATE_201("201","待审核")
	,CANCEL_ORDER_STATE_202("202","待退款")
	,CANCEL_ORDER_STATE_203("203","已完成")
	,CANCEL_ORDER_STATE_204("204","商家拒绝退货")
	,CANCEL_ORDER_STATE_205("205","用户取消退货");
	
	private String state;
	private String desc;
	/**
	 * @param state
	 * @param desc
	 */
	private OrderCancelEnum(String state, String desc) {
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
 