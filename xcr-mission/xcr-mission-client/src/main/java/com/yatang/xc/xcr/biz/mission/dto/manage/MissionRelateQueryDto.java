package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;

public class MissionRelateQueryDto implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO 变量名称
	 */
	private static final long serialVersionUID = -4698615854980073834L;



	@Override
	public String toString() {
		return "MissionRelateQueryDto [type=" + type + ", status=" + status + ", id=" + id + ", startIndex="
				+ startIndex + ", endIndex=" + endIndex + "]";
	}

	/**
	 * 类型
	 */
	private String	type;

	/**
	 * 状态
	 */
	private String	status;

	/**
	 * 主键
	 */
	private Long	id;

	/**
	 * 排序
	 */
	private String	orderBy;

	/**
	 * 开始下标 分页用
	 */
	private Integer	startIndex;

	/**
	 * 结束下标 分页用
	 */
	private Integer	endIndex;



	public String getOrderBy() {
		return orderBy;
	}



	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Integer getStartIndex() {
		return startIndex;
	}



	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}



	public Integer getEndIndex() {
		return endIndex;
	}



	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

}
