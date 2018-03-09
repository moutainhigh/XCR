package com.yatang.xc.xcr.dto.outputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月08日 14:45
 * @Summary : 商品详情v2.6 结果集
 */
@SuppressWarnings("serial")
public class NewGoodsDetailResultSetDto implements Serializable{

    /**
      *商品Id
      */
    @JsonProperty("GoodsId")
    private String  goodsId;

    /**
     * 1：自建商品、2：非自建商品
     *商品类别
     */
    @JsonProperty("GoodsType")
    private Integer  goodsType;

    /**
     * 1：是，0：否
     *是否称重商品
     */
    @JsonProperty("IsWeighingGoods")
    private Integer  isWeighingGoods;

    /**
     *1：是，0：否
     * 是否允许退货
     */
    @JsonProperty("IsAllowReturn")
    private String  isAllowReturn;

    /**
     *商品名称
     */
    @JsonProperty("GoodsName")
    private String  goodsName;

    /**
     *商品分类Id
     */
    @JsonProperty("ClassifyId")
    private String  classifyId;

    /**
     *分类名称
     */
    @JsonProperty("ClassifyName")
    private String  classifyName;

    /**
     *零售价(含税)
     */
    @JsonProperty("GoodsPrice")
    private String  goodsPrice;

    /**
     *单位
     */
    @JsonProperty("UnitName")
    private String  unitName;

    /**
     *商品条码
     */
    @JsonProperty("GoodsCode")
    private String  goodsCode;

    /**
     *商品简码
     */
    @JsonProperty("GoodsSimpleCode")
    private String  goodsSimpleCode;

    /**
     *规格
     */
    @JsonProperty("Specificate")
    private String  specificate;

    /**
     *保质期
     */
    @JsonProperty("ShelfLife")
    private String  shelfLife;

    /**
     *品牌
     */
    @JsonProperty("Brand")
    private String  brand;

    /**
     *税率
     */
    @JsonProperty("TaxRate")
    private String  taxRate;

    /**
     *参考成本价
     */
    @JsonProperty("CostPrice")
    private String  costPrice;

    /**
     *参考毛利率
     */
    @JsonProperty("GrossRate")
    private String  grossRate;

    /**
     * 1：上架，0：下架
     *商品上下架状态
     */
    @JsonProperty("FrameType")
    private String  frameType;

    /**
     *备注
     */
    @JsonProperty("Remark")
    private String  remark;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"goodsId\":\"")
                .append(goodsId).append('\"');
        sb.append(",\"goodsType\":")
                .append(goodsType);
        sb.append(",\"isWeighingGoods\":")
                .append(isWeighingGoods);
        sb.append(",\"isAllowReturn\":\"")
                .append(isAllowReturn).append('\"');
        sb.append(",\"goodsName\":\"")
                .append(goodsName).append('\"');
        sb.append(",\"classifyId\":\"")
                .append(classifyId).append('\"');
        sb.append(",\"classifyName\":\"")
                .append(classifyName).append('\"');
        sb.append(",\"goodsPrice\":\"")
                .append(goodsPrice).append('\"');
        sb.append(",\"unitName\":\"")
                .append(unitName).append('\"');
        sb.append(",\"goodsCode\":\"")
                .append(goodsCode).append('\"');
        sb.append(",\"goodsSimpleCode\":\"")
                .append(goodsSimpleCode).append('\"');
        sb.append(",\"specificate\":\"")
                .append(specificate).append('\"');
        sb.append(",\"shelfLife\":\"")
                .append(shelfLife).append('\"');
        sb.append(",\"brand\":\"")
                .append(brand).append('\"');
        sb.append(",\"taxRate\":\"")
                .append(taxRate).append('\"');
        sb.append(",\"costPrice\":\"")
                .append(costPrice).append('\"');
        sb.append(",\"grossRate\":\"")
                .append(grossRate).append('\"');
        sb.append(",\"frameType\":\"")
                .append(frameType).append('\"');
        sb.append(",\"remark\":\"")
                .append(remark).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getIsWeighingGoods() {
        return isWeighingGoods;
    }

    public void setIsWeighingGoods(Integer isWeighingGoods) {
        this.isWeighingGoods = isWeighingGoods;
    }

    public String getIsAllowReturn() {
        return isAllowReturn;
    }

    public void setIsAllowReturn(String isAllowReturn) {
        this.isAllowReturn = isAllowReturn;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsSimpleCode() {
        return goodsSimpleCode;
    }

    public void setGoodsSimpleCode(String goodsSimpleCode) {
        this.goodsSimpleCode = goodsSimpleCode;
    }

    public String getSpecificate() {
        return specificate;
    }

    public void setSpecificate(String specificate) {
        this.specificate = specificate;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getGrossRate() {
        return grossRate;
    }

    public void setGrossRate(String grossRate) {
        this.grossRate = grossRate;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
