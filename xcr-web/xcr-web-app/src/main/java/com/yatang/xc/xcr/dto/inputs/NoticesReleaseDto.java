package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月29日 下午2:08:13
 * @Summary : 店铺公告发布
 */
@SuppressWarnings("serial")
public class NoticesReleaseDto implements Serializable {

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
	 * 店铺公告内容
	 */
	private String noticesDetail;

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

	public String getNoticesDetail() {
		return noticesDetail;
	}

	public void setNoticesDetail(String noticesDetail) {
		this.noticesDetail = noticesDetail;
	}

	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo + "\",\"token\":\"" + token
				+ "\",\"noticesDetail\":\"" + noticesDetail + "\"}  ";
	}

}
