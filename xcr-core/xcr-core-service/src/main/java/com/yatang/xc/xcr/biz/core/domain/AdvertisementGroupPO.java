package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;

import com.busi.common.datatable.PageBean;

public class AdvertisementGroupPO extends PageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7380271443273662499L;

	private Integer id;
	
	private String groupName;
	
	private String description;
	
	private Integer positionCode;
	
	private Integer isEnable;
	
	private String createTime;
	
	private String lastModifyTime;
	
	private Integer addrCode;
	

	public Integer getAddrCode() {
		return addrCode;
	}

	public void setAddrCode(Integer addrCode) {
		this.addrCode = addrCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(Integer positionCode) {
		this.positionCode = positionCode;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	@Override
	public String toString() {
		return "AdvertisementGroupPO [id=" + id + ", groupName=" + groupName
				+ ", description=" + description + ", positionCode="
				+ positionCode + ", isEnable=" + isEnable + ", createTime="
				+ createTime + ", lastModifyTime=" + lastModifyTime + "]";
	}
	
	
	

}
