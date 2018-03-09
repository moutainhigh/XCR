package com.yatang.xc.xcr.vo;

import java.io.Serializable;

import com.busi.common.datatable.PageBean;

public class StateQueryVO extends PageBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7759128096535511804L;
	private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
