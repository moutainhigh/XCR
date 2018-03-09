package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年8月7日 上午9:59:44 
* @version 1.0.0
* @function 
*/
public enum OrderStateEnum {
	ORDER_STATE_0("0","全部")
	,ORDER_STATE_1("1","待支付")
	,ORDER_STATE_2("2","待接单")
	,ORDER_STATE_3("3","待发货")
	,ORDER_STATE_30("30","已取消，包含(5，6，7，32，33)状态的数据")
	,ORDER_STATE_31("31","待收货（配送中）")
	,ORDER_STATE_32("32","已取消（买家取消）")
	,ORDER_STATE_33("33","已取消（用户拒收）")
	,ORDER_STATE_4("4","已完成")
	,ORDER_STATE_5("5","已取消（未支付）")
	,ORDER_STATE_6("6","已取消（商家拒单）")
	,ORDER_STATE_7("7","已取消（未接单）")
	,ORDER_STATE_101("101","退款中")
	,ORDER_STATE_102("102","退款失败")
	,ORDER_STATE_103("103","退款成功")
	,ORDER_STATE_201("201","待审核(用户发起退货请求)")
	,ORDER_STATE_202("202","待退货(退款状态同步到101)")
	,ORDER_STATE_203("203","已完成(退款状态同步到103)")
	,ORDER_STATE_204("204","已拒绝退货")
	,ORDER_STATE_205("205","已取消退货");
	
	private String state;
	private String desc;
	/**
	 * @param state
	 * @param desc
	 */
	private OrderStateEnum(String state, String desc) {
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
 