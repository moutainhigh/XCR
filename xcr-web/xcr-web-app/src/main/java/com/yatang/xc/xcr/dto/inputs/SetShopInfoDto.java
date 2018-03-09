package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @since 2.5.1
 * @Author : BobLee
 * @CreateTime : 2017年11月29日 上午11:30:47
 * @Summary : 设置配送信息
 */
@SuppressWarnings("serial")
public class SetShopInfoDto implements Serializable {
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
	 * 起送价格
	 */
	private String startPrice;

	/**
	 * 配送费
	 */
	private String deliveryFee;

	/**
	 * 是否面配送费 0： 否 1：是
	 */
	private Integer isFreeDelivery;

	/**
	 * 减免配送费额度(减免了多少配送费)
	 */
	private String freeDeliveryFee;

	/**
	 * 配送范围
	 */
	private String deliveryScope;

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

	public String getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(String startPrice) {
		this.startPrice = startPrice;
	}

	public String getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public Integer getIsFreeDelivery() {
		return isFreeDelivery;
	}

	public void setIsFreeDelivery(Integer isFreeDelivery) {
		this.isFreeDelivery = isFreeDelivery;
	}

	public String getFreeDeliveryFee() {
		return freeDeliveryFee;
	}

	public void setFreeDeliveryFee(String freeDeliveryFee) {
		this.freeDeliveryFee = freeDeliveryFee;
	}

	public String getDeliveryScope() {
		return deliveryScope;
	}

	public void setDeliveryScope(String deliveryScope) {
		this.deliveryScope = deliveryScope;
	}

	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo + "\",\"token\":\"" + token
				+ "\",\"startPrice\":\"" + startPrice + "\",\"deliveryFee\":\"" + deliveryFee
				+ "\",\"isFreeDelivery\":\"" + isFreeDelivery + "\",\"freeDeliveryFee\":\"" + freeDeliveryFee
				+ "\",\"deliveryScope\":\"" + deliveryScope + "\"}  ";
	}

}
