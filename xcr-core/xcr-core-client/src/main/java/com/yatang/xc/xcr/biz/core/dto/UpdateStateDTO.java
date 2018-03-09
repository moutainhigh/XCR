package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;

public class UpdateStateDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4457451309791565278L;

	
	private Integer id;

	private Integer state;
	
	private Integer isDelate;

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

	public Integer getIsDelate() {
		return isDelate;
	}

	public void setIsDelate(Integer isDelate) {
		this.isDelate = isDelate;
	}
	
	

}
