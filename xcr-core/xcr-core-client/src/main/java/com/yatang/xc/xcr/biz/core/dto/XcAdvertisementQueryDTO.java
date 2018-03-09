package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;

import com.busi.common.datatable.PageBean;

public class XcAdvertisementQueryDTO extends PageBean implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -285449641229484277L;
	
	private Integer id;
	
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
	
	

}
