package com.yatang.xc.xcr.pojo;

import java.io.Serializable;

/** 
* @author gaodawei 
* @Date 2017年7月28日 下午5:32:36 
* @version 1.0.0
* @function 
*/
public class UserInfoVo implements Serializable{
	private static final long serialVersionUID = 4316859204403331206L;
	private String userId;
	private String userName;
	private String loginId;
	private String identityCard;
	private Integer status;
	private Integer delFlag;
	private String ytAccount;
	private String bankAccount;
	private String subCompanyId;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	public String getYtAccount() {
		return ytAccount;
	}
	public void setYtAccount(String ytAccount) {
		this.ytAccount = ytAccount;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getSubCompanyId() {
		return subCompanyId;
	}
	public void setSubCompanyId(String subCompanyId) {
		this.subCompanyId = subCompanyId;
	}
	@Override
	public String toString() {
		return "UserInfoVo [userId=" + userId + ", userName=" + userName + ", loginId=" + loginId + ", identityCard="
				+ identityCard + ", status=" + status + ", delFlag=" + delFlag + ", ytAccount=" + ytAccount
				+ ", bankAccount=" + bankAccount + ", subCompanyId=" + subCompanyId + "]";
	}
	
}
 