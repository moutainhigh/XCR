package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;

public class AdvertisementUpdateDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4026620716415101248L;

	private Integer id;
	
	private Integer state;
	
	private String positionCode;
	
	private String lastModifyTime;

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	

}
