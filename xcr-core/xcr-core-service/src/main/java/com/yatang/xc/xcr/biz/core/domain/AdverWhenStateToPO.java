package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;

public class AdverWhenStateToPO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7130074272309267935L;

	private String remaind="请选择";
	
	private Integer id;

	public String getRemaind() {
		return remaind;
	}

	public void setRemaind(String remaind) {
		this.remaind = remaind;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

}
