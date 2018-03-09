package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;

public class PermissionPO implements Serializable{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8428356042007690060L;

	String permissionUrl;
	
	String permissionName;
	
	String permissionCode;
	
	String permissionId;

	public String getPermissionUrl() {
		return permissionUrl;
	}

	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
	
	

}
