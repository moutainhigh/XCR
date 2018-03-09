package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @since 2.5.1
 * @Author : BobLee
 * @CreateTime : 2017年11月29日 上午11:25:57
 * @Summary : 设置店铺位置
 */
@SuppressWarnings("serial")
public class SetShopAddressDto implements Serializable {

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
	 * 维度
	 */
	private String latitude;

	/**
	 * 经度
	 */
	private String longitude;

	/**
	 * 定位位置
	 */
	private String locationAddress;

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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo + "\",\"token\":\"" + token
				+ "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\",\"locationAddress\":\""
				+ locationAddress + "\"}  ";
	}

}
