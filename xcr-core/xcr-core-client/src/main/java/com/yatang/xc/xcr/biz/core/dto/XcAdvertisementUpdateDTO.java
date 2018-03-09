package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;

public class XcAdvertisementUpdateDTO implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -586978679558588259L;
	
	private Integer id;
	
	private Integer state;

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
