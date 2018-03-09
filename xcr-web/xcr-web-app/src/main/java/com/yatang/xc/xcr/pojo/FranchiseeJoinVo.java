package com.yatang.xc.xcr.pojo; 
/** 
* @author gaodawei 
* @Date 2017年10月26日 下午5:24:27 
* @version 1.0.0
* @function 加盟商加盟的用户信息类
*/
public class FranchiseeJoinVo {
	/**
	 * 加盟商姓名
	 */
	private String fName;
	/**
	 * 加盟商电话
	 */
	private String fPhone;
	/**
	 * 默认都传0
	 */
	private Integer joinType;
	/**
	 * 店铺详细地址
	 */
	private String detailAddress;
	/**
	 * 加盟商登记来源：1、代表从小超人商家版APP登记
	 */
	private Integer origin;
	/**
	 * 加盟商是否拥有超市：0、没有;1、有
	 */
	private Integer storeOpen;
	
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getfPhone() {
		return fPhone;
	}
	public void setfPhone(String fPhone) {
		this.fPhone = fPhone;
	}
	public Integer getJoinType() {
		return joinType;
	}
	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public Integer getOrigin() {
		return origin;
	}
	public void setOrigin(Integer origin) {
		this.origin = origin;
	}
	public Integer getStoreOpen() {
		return storeOpen;
	}
	public void setStoreOpen(Integer storeOpen) {
		this.storeOpen = storeOpen;
	}
	@Override
	public String toString() {
		return "FranchiseeJoinVo [fName=" + fName + ", fPhone=" + fPhone + ", joinType=" + joinType + ", detailAddress="
				+ detailAddress + ", origin=" + origin + ", storeOpen=" + storeOpen + "]";
	}
}
 