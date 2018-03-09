package com.yatang.xc.xcr.dto.outputs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 下午5:28:23
 * @Summary : @JsonProperty("listdata") private String listdata;
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodsOutListResultDto implements Serializable {

	private static final long serialVersionUID = 8737301586046356764L;
	/**
	 * 商品名称
	 */
	@JsonProperty("GoodsName")
	private String goodsName;

	/**
	 * 售价
	 */
	@JsonProperty("GoodsPrice")
	private String goodsPrice;

	/**
	 * 条码
	 */
	@JsonProperty("GoodsCode")
	private String goodsCode;

	/**
	 * 单位
	 */
	@JsonProperty("UnitName")
	private String unitName;

	/**
	 * 商品图片
	 */
	@JsonProperty("GoodsPic")
	private String goodsPic;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	@Override
	public String toString() {
		return "GoodsOutListResultDto{" +
				"goodsName='" + goodsName + '\'' +
				", goodsPrice='" + goodsPrice + '\'' +
				", goodsCode='" + goodsCode + '\'' +
				", unitName='" + unitName + '\'' +
				", goodsPic='" + goodsPic + '\'' +
				'}';
	}
}
