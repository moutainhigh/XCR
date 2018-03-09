package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;
import java.util.List;

/**
 * @since 2.5.1
 * @Author : BobLee
 * @CreateTime : 2017年11月29日 上午11:36:12
 * @Summary : 店铺介绍发布
 */
@SuppressWarnings("serial")
public class IntroduceReleaseDto implements Serializable {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 店铺编号
	 */
	private String storeSerialNo;

	/**
	 * 令牌
	 */
	private String token;

	/**
	 * 店铺发布图片列表
	 */
	private List<String> introducePicUrls;

	/**
	 * 图片链接
	 */
	private String picUrl;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStoreSerialNo() {
		return storeSerialNo;
	}

	public void setStoreSerialNo(String storeSerialNo) {
		this.storeSerialNo = storeSerialNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getIntroducePicUrls() {
		return introducePicUrls;
	}

	public void setIntroducePicUrls(List<String> introducePicUrls) {
		this.introducePicUrls = introducePicUrls;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo + "\",\"token\":\"" + token
				+ "\",\"introducePicUrls\":\"" + introducePicUrls + "\",\"picUrl\":\"" + picUrl + "\"}  ";
	}

}
