package com.yatang.xc.xcr.biz.mission.dto.statistics;

import java.io.Serializable;

public class StatisticsDto implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO 变量名称
	 */
	private static final long	serialVersionUID	= 7761059931511367167L;
	/**
	 * 统计项目名称
	 */
	private String				name;
	/**
	 * 统计项目code 对应 枚举 EnumStatisticsItem 的 code
	 */
	private String				itemCode;
	/**
	 * 当前计数
	 */
	private Long				count;
	/**
	 * 计数增长率
	 */
	private Float				increase;



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getItemCode() {
		return itemCode;
	}



	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}



	public Long getCount() {
		return count;
	}



	public void setCount(Long count) {
		this.count = count;
	}



	public Float getIncrease() {
		return increase;
	}



	public void setIncrease(Float increase) {
		this.increase = increase;
	}

}
