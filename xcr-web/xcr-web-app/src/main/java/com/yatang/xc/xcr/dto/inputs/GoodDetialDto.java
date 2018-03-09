package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 11:08
 * @Summary :
 *
 * {"Token":"aaa","UserId":"111111","StoreSerialNo":"A000305","GoodsCode":"6901028120692"}
 */
@SuppressWarnings("serial")
public class GoodDetialDto implements Serializable{

	private static final long serialVersionUID = -4669940095685179355L;

	/**
     * TOKEN
     */
    private String token;

    /**
     * 用户ID之类的
     */
    private String userId;

    /**
     *店铺编号
     */
    private String storeSerialNo;

    /**
     * 商品条形码
     */
    private String goodsCode;


    @Override
	public String toString() {
		return "{\"token\":\"" + token + "\",\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo
				+ "\",\"goodsCode\":\"" + goodsCode + "\"}  ";
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

}
