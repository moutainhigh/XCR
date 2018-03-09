package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;
import java.util.Date;


public class AdvertisementPO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5575766776272256414L;
	
	private Integer id;
	
	private String sore;

	private String picName;
	
	private String picUrl;
	
	private String activityUrl;
	
	private Integer state;
	
	private String createTime;
	
	private String remaind;
	
	private Integer groupId;
	
	private Integer type;
	
	private Date lastModifyTime;
	
	

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getSore() {
		return sore;
	}

	public void setSore(String sore) {
		this.sore = sore;
	}

	@Override
	public String toString() {
		return "AdvertisementPO [id=" + id + ", picName=" + picName
				+ ", picUrl=" + picUrl + ", activityUrl=" + activityUrl
				+ ", state=" + state + ", createTime=" + createTime
				+ ", remaind=" + remaind + "]";
	}

	public Integer getGroupId() {
		return groupId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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
	
	
	

}
