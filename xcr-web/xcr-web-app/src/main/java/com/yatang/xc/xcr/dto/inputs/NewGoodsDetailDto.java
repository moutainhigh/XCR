package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月08日 14:43
 * @Summary : 商品详情v2.6
 */
@SuppressWarnings("serial")
public class NewGoodsDetailDto  implements Serializable{

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 门店编号
     */
    private String storeSerialNo;

    /**
     * TOKEN
     */
    private String token;
    
    /**
      * 商品条码
      */
    private String  goodsCode;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userId\":\"")
                .append(userId).append('\"');
        sb.append(",\"storeSerialNo\":\"")
                .append(storeSerialNo).append('\"');
        sb.append(",\"token\":\"")
                .append(token).append('\"');
        sb.append(",\"goodsCode\":\"")
                .append(goodsCode).append('\"');
        sb.append('}');
        return sb.toString();
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
}
