package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @since 2.5.1
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 11:26
 * @Summary :
 */
public class NewArrivalsDto implements Serializable{

	private static final long serialVersionUID = 8975024766916180997L;

	/**
     *用户ID
     */
    private String userId;

    /**
     *店铺编号
     */
    private String storeSerialNo;

    /**
     *TOKEN 令牌
     */
    private String token;

    /**
     *商品条形码
     */
    private String goodsCode;

    /**
     *推荐设置 0 不推荐，1推荐
     */
    private Integer arrivalsStatue;
    
    
    public Boolean isRecommend(){
    	if(this.arrivalsStatue == 1){
    		return Boolean.TRUE;
    	}
    	if(this.arrivalsStatue == 0){
    		return Boolean.FALSE; 
    	}
    	return null;
    }


    @Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo + "\",\"token\":\"" + token
				+ "\",\"goodsCode\":\"" + goodsCode + "\",\"arrivalsStatue\":\"" + arrivalsStatue + "\"}  ";
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Integer getArrivalsStatue() {
        return arrivalsStatue;
    }

    public void setArrivalsStatue(Integer arrivalsStatue) {
        this.arrivalsStatue = arrivalsStatue;
    }
}






































