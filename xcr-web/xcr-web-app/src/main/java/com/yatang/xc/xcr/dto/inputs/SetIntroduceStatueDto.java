package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月30日 上午11:33:56
 * @Summary : 设置介绍在店铺中显示
 */
public class SetIntroduceStatueDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8420809002805868268L;

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
	 * 显示状态 0: 否 1:是
	 */
	private String introduceStatue;

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

	public String getIntroduceStatue() {
		return introduceStatue;
	}

	public void setIntroduceStatue(String introduceStatue) {
		this.introduceStatue = introduceStatue;
	}

	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo + "\",\"token\":\"" + token
				+ "\",\"introduceStatue\":\"" + introduceStatue + "\"}  ";
	}

}
