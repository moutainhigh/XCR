package com.yatang.xc.xcr.dto.outputs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月08日 15:19
 * @Summary :商品列表结果集
 */
@SuppressWarnings("serial")
public class NewGoodsListResultSetDto implements Serializable{

    /**
      * 商品Id
      */
    @JsonProperty("GoodsId")
    private String  goodsId;

    /**
     *分类Id
     */
    @JsonProperty("ClassifyId")
    private String  classifyId;

    /**
     *分类名称
     */
    @JsonProperty("ClassifyName")
    private String  classifyName;

    /**
     *商品名称
     */
    @JsonProperty("GoodsName")
    private String  goodsName;

    /**
     *商品条码
     */
    @JsonProperty("GoodsCode")
    private String  goodsCode;

    /**
     *售价
     */
    @JsonProperty("GoodsPrice")
    private String  goodsPrice;

    /**
     *单位
     */
    @JsonProperty("UnitName")
    private String  unitName;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"goodsId\":\"").append(goodsId).append('\"');
        sb.append(",\"classifyId\":\"").append(classifyId).append('\"');
        sb.append(",\"classifyName\":\"").append(classifyName).append('\"');
        sb.append(",\"goodsName\":\"").append(goodsName).append('\"');
        sb.append(",\"goodsCode\":\"").append(goodsCode).append('\"');
        sb.append(",\"goodsPrice\":\"").append(goodsPrice).append('\"');
        sb.append(",\"unitName\":\"").append(unitName).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) { this.goodsId = goodsId; }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) { this.goodsCode = goodsCode; }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
