package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;

import com.busi.common.datatable.PageBean;

public class StatePO  extends PageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8207997991603312364L;
	private Integer state;
	
	private Integer positionCode;



	public Integer getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(Integer positionCode) {
		this.positionCode = positionCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	

}
