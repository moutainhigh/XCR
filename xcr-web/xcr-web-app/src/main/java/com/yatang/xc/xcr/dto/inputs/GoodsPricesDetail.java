package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * 调价商品
 * Created by wangyang on 2017/12/21.
 */
public class GoodsPricesDetail implements Serializable {

    private static final long serialVersionUID = 5585904354744354722L;
    private String goodsId;
    private String newGoodsPrice;
    private String goodsCode;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getNewGoodsPrice() {
        return newGoodsPrice;
    }

    public void setNewGoodsPrice(String newGoodsPrice) {
        this.newGoodsPrice = newGoodsPrice;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"goodsId\":\"").append(goodsId).append('\"');
        sb.append(",\"newGoodsPrice\":\"").append(newGoodsPrice).append('\"');
        sb.append(",\"goodsCode\":\"").append(goodsCode).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
