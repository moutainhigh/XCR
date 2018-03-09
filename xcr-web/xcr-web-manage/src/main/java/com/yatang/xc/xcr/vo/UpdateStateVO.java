package com.yatang.xc.xcr.vo;

import java.io.Serializable;

public class UpdateStateVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 279995465931207303L;

	/**
	 * 
	 */
	
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
