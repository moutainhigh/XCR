package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告信息
* <class description>
*		
* @author: zhongrun
* @version: 1.0, 2017年7月6日
 */
public class AdvertisementDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8481149391616190806L;

	private Integer id;
	
	/**
	 * 位置
	 */
	private String sore;

	/**
	 * 图片名称
	 */
	private String picName;
	
	/**
	 * 图片地址
	 */
	private String picUrl;
	
	/**
	 * 活动链接
	 */
	private String activityUrl;
	/**
	 * 广告状态
	 * 0：禁用 1：启用
	 */
	private Integer state;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 描述
	 */
	private String remaind;
	
	/**
	 * 组id
	 */
	private Integer groupId;
	
	/**
	 * 类型：1：轮播图片，2：app启动加载广告3：供应链商品推荐
	 */
	private Integer type;
	
	private Date lastModifyTime;
	

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getSore() {
		return sore;
	}

	public void setSore(String sore) {
		this.sore = sore;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

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

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemaind() {
		return remaind;
	}

	public void setRemaind(String remaind) {
		this.remaind = remaind;
	}

	@Override
	public String toString() {
		return "AdvertisementDTO [id=" + id + ", picName=" + picName
				+ ", picUrl=" + picUrl + ", activityUrl=" + activityUrl
				+ ", state=" + state + ", createTime=" + createTime
				+ ", remaind=" + remaind + ", groupId=" + groupId + ", type="
				+ type + "]";
	}


	
}
