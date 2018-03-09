package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;
import java.util.Date;


public class XcAdvertisementDTO implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -9110524043279973503L;
	
	private Integer id;
	
	private String position;
	
	private Date lastModifyTime;
	
	private String activeUrl;
	
	private String imgUrl;
	
	private Integer state;
	
	private Integer type;
	

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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getActiveUrl() {
		return activeUrl;
	}

	public void setActiveUrl(String activeUrl) {
		this.activeUrl = activeUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "XcAdvertisementPO [id=" + id + ", position=" + position + ", lastModifyTime=" + lastModifyTime
				+ ", activeUrl=" + activeUrl + ", imgUrl=" + imgUrl + ", state=" + state + "]";
	}
	
	

}
