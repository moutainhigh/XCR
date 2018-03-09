package com.yatang.xc.xcr.biz.core.domain;

import java.util.Date;

public class ActivityPO{

	private Long id;
	private String province;
	private String city;
	private String userId;
	private String storeNo;
	private String username;
	private String phone;
	private String branch_company;
	private String userPhoto;
	private String storePhoto;
	private String type;;
	private Date createTime;
	private Date updateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBranch_company() {
		return branch_company;
	}
	public void setBranch_company(String branch_company) {
		this.branch_company = branch_company;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getStorePhoto() {
		return storePhoto;
	}
	public void setStorePhoto(String storePhoto) {
		this.storePhoto = storePhoto;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "ActivityPO [id=" + id + ", province=" + province + ", city=" + city + ", userId=" + userId
				+ ", storeNo=" + storeNo + ", username=" + username + ", phone=" + phone + ", branch_company="
				+ branch_company + ", userPhoto=" + userPhoto + ", storePhoto=" + storePhoto + ", type=" + type
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}
