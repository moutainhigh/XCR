package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;
import java.util.Date;

public class UpdateStatePO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -708452681027466946L;
	
	private Integer id;

	private Integer state;
	
	private Integer isDelate;
	
	private Date publishTime;

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
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

	public Integer getIsDelate() {
		return isDelate;
	}

	public void setIsDelate(Integer isDelate) {
		this.isDelate = isDelate;
	}
	
	

}
