package com.yatang.xc.xcr.dto.outputs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 下午4:57:34
 * @Summary : resultJson.put("GoodsOutStatue", xcrProductDTO.getProductType());
 *          resultJson.put("GoodsName", xcrProductDTO.getItemDesc());
 *          resultJson.put("ClassifyFirstName", classifyFirstName);
 *          resultJson.put("ClassifySecName", classifySecName);
 *          resultJson.put("ClassifyThirdName", classifyThirdName);
 *          resultJson.put("GoodsPrice", xcrProductDTO.getAdjustCost());
 *          resultJson.put("UnitName", xcrProductDTO.getUnit());
 *          resultJson.put("GoodsCode", xcrProductDTO.getItemNum());
 *          resultJson.put("FrameType", xcrProductDTO.getO2oOnShelves());
 *          resultJson.put("GoodsCostPrice", xcrProductDTO.getCostPrice());
 */
@JsonSerialize
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GoodsOutDetialResultDto implements Serializable {

	private static final long serialVersionUID = 4352797675107895965L;

	/**
	 * 商品类别 1 常规商品 2 打包商品 3 附属商品 4 称重商品 5 加工称重商品 6 加工非称重商品
	 */
	@JsonProperty("GoodsOutStatue")
	private String goodsOutStatue;

	/**
	 * 商品名称
	 */
	@JsonProperty("GoodsName")
	private String goodsName;

	/**
	 * 商品一级分类
	 */
	@JsonProperty("ClassifyFirstName")
	private String classifyFirstName;

	/**
	 * 商品二级分类
	 */
	@JsonProperty("ClassifySecName")
	private String classifySecName;

	/**
	 * 商品三级分类
	 */
	@JsonProperty("ClassifyThirdName")
	private String classifyThirdName;

	/**
	 * 售价
	 */
	@JsonProperty("GoodsPrice")
	private String goodsPrice;

	/**
	 * 单位
	 */
	@JsonProperty("UnitName")
	private String unitName;

	/**
	 * 商品条码
	 */
	@JsonProperty("GoodsCode")
	private String goodsCode;

	/**
	 * 类别名称
	 */
	@JsonProperty("ClassifyName")
	private String classifyName;

	/**
	 * 商品状态 1 上架 0 下架
	 */
	@JsonProperty("FrameType")
	private String frameType;

	/**
	 * 成本价
	 */
	@JsonProperty("GoodsCostPrice")
	private String goodsCostPrice;

	/**
	 * @since 2.5.1
	 * @value 0 不是 1 是
	 */
	@JsonProperty("IsNewArrivals")
	private Integer isNewArrivals;

	/**
	 * @since 2.5.1
	 * @value 0 不是 1 是（仅直营店显示）
	 */
	@JsonProperty("IsSuperVip")
	private Integer isSuperVip;

	public Integer getIsNewArrivals() {
		return isNewArrivals;
	}

	public void setIsNewArrivals(Integer isNewArrivals) {
		this.isNewArrivals = isNewArrivals;
	}

	public Integer getIsSuperVip() {
		return isSuperVip;
	}

	public void setIsSuperVip(Integer isSuperVip) {
		this.isSuperVip = isSuperVip;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getGoodsOutStatue() {
		return goodsOutStatue;
	}

	public void setGoodsOutStatue(String goodsOutStatue) {
		this.goodsOutStatue = goodsOutStatue;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getClassifyFirstName() {
		return classifyFirstName;
	}

	public void setClassifyFirstName(String classifyFirstName) {
		this.classifyFirstName = classifyFirstName;
	}

	public String getClassifySecName() {
		return classifySecName;
	}

	public void setClassifySecName(String classifySecName) {
		this.classifySecName = classifySecName;
	}

	public String getClassifyThirdName() {
		return classifyThirdName;
	}

	public void setClassifyThirdName(String classifyThirdName) {
		this.classifyThirdName = classifyThirdName;
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

	public String getFrameType() {
		return frameType;
	}

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}

	public String getGoodsCostPrice() {
		return goodsCostPrice;
	}

	public void setGoodsCostPrice(String goodsCostPrice) {
		this.goodsCostPrice = goodsCostPrice;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	@Override
	public String toString() {
		return "{\"goodsOutStatue\":\"" + goodsOutStatue + "\",\"goodsName\":\"" + goodsName
				+ "\",\"classifyFirstName\":\"" + classifyFirstName + "\",\"classifySecName\":\"" + classifySecName
				+ "\",\"classifyThirdName\":\"" + classifyThirdName + "\",\"goodsPrice\":\"" + goodsPrice
				+ "\",\"unitName\":\"" + unitName + "\",\"goodsCode\":\"" + goodsCode + "\",\"classifyName\":\""
				+ classifyName + "\",\"frameType\":\"" + frameType + "\",\"goodsCostPrice\":\"" + goodsCostPrice
				+ "\",\"isNewArrivals\":\"" + isNewArrivals + "\",\"isSuperVip\":\"" + isSuperVip + "\"}  ";
	}

}
