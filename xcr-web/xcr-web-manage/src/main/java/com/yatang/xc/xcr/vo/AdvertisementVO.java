package com.yatang.xc.xcr.vo;

import java.io.Serializable;

public class AdvertisementVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1196204337167413385L;

	private Integer id;
	
	private String sore;

	private String picName;
	
	private String picUrl;
	
	private String activityUrl;
	
	private Integer state=0;
	
	private String createTime;
	
	//广告组名
	private String remaind;
	
	/**
	 * 组id
	 */
	private Integer groupId;
	
	/**
	 * 类型：1图片，2文字
	 */
	private Integer type;
	

	public String getSore() {
		return sore;
	}

	public void setSore(String sore) {
		this.sore = sore;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemaind() {
		return remaind;
	}

	public void setRemaind(String remaind) {
		this.remaind = remaind;
	}

	@Override
	public String toString() {
		return "AdvertisementVO [id=" + id + ", picName=" + picName
				+ ", picUrl=" + picUrl + ", activityUrl=" + activityUrl
				+ ", state=" + state + ", createTime=" + createTime
				+ ", remaind=" + remaind + "]";
	}
	

}
