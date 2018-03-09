package com.yatang.xc.xcr.vo;

import java.io.Serializable;

public class XcAdverUpdateStateQueryVO implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3316376823800995847L;
	
	
	
	private Integer id;
	
	private Integer option;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOption() {
		return option;
	}

	public void setOption(Integer option) {
		this.option = option;
	}

	@Override
	public String toString() {
		return "XcAdverUpdateStateQueryVO [id=" + id + ", option=" + option + "]";
	}
	
	
	
	

}
