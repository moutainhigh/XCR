package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;
import java.util.List;

public class SupplyAdvertisementGroupPO implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3972638070615982664L;
	
	
	private Integer id;
	
	private String groupName;
	
	private String lastModifyTime;
	
	private Integer addrCode;
	
	private List<AdvertisementPO> advertisementPOs;

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

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Integer getAddrCode() {
		return addrCode;
	}

	public void setAddrCode(Integer addrCode) {
		this.addrCode = addrCode;
	}

	public List<AdvertisementPO> getAdvertisementPOs() {
		return advertisementPOs;
	}

	public void setAdvertisementPOs(List<AdvertisementPO> advertisementPOs) {
		this.advertisementPOs = advertisementPOs;
	}
	
	

}
