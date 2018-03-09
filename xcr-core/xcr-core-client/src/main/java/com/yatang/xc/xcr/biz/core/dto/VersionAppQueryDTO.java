package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;

public class VersionAppQueryDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1042173401339163618L;

	private String currentVersion;
	
	private Integer type;
	
	private Integer code;
	

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "VersionAppQueryPO [currentVersion=" + currentVersion
				+ ", type=" + type + "]";
	}
	
	

}
