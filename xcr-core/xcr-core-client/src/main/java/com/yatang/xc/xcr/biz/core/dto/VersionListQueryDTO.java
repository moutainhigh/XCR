package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;
import java.util.Date;


public class VersionListQueryDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7014039242688057263L;
	
	private Integer id;

	private Integer type;
	
	private String versionCode;
	
	private String startDay;
	
	private Date startTime;
	
	private Date endTime;
	
	private String endDay;
	
	private Integer state;
	
	private Integer isLiveUp;
	
	private Integer pageIndex;
	private Integer pageSize;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
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

	@Override
	public String toString() {
		return "versionListQueryVO [type=" + type + ", versionCode="
				+ versionCode + ", startDay=" + startDay + ", endDay=" + endDay
				+ ", state=" + state + ", isLiveUp=" + isLiveUp + "]";
	}
	
	

}
