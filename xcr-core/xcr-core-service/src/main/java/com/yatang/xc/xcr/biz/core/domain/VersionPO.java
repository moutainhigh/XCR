package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;
import java.util.Date;

public class VersionPO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 809641076639827765L;
	
	private Integer id;

	private Integer type;
	
	private String versionCode;
	
	private Date publishTime;
	
	private Integer state;
	
	private Integer isLiveUp;
	
	private String description;
	
	private String apkUrl;
	
	private Integer isDelate;
	
	private Integer code;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getIsDelate() {
		return isDelate;
	}

	public void setIsDelate(Integer isDelate) {
		this.isDelate = isDelate;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsLiveUp() {
		return isLiveUp;
	}

	public void setIsLiveUp(Integer isLiveUp) {
		this.isLiveUp = isLiveUp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "VersionPO [id=" + id + ", type=" + type + ", versionCode="
				+ versionCode + ", publishTime=" + publishTime + ", state="
				+ state + ", isLiveUp=" + isLiveUp + ", description="
				+ description + ", apkUrl=" + apkUrl + "]";
	}


	
	

}
