package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 限速配置
 * 
 * @author : echo
 * @date : 2017年9月21日 下午6:22:03
 * @version : 2017年9月21日
 */
public class RateLimit implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO 变量名称
	 */
	private static final long serialVersionUID = 3462489397358380550L;

	private Integer id;

	// 名称
	private String name;

	// 描述
	private String discription;

	// 类型
	private String type;

	// 限速单位（秒）
	private Integer unitTime;

	// 限速数量
	private Integer countLimit;

	// 创建人
	private String creater;

	// 创建人id
	private Integer createrId;

	// 创建时间
	private Date gmtCreate;

	// 修改人
	private String modifier;

	// 修改人id
	private Integer modifierId;

	// 修改时间
	private Date gmtModify;

	// 是否删除
	private Boolean isDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription == null ? null : discription.trim();
	}

	public Integer getUnitTime() {
		return unitTime;
	}

	public void setUnitTime(Integer unitTime) {
		this.unitTime = unitTime;
	}

	public Integer getCountLimit() {
		return countLimit;
	}

	public void setCountLimit(Integer countLimit) {
		this.countLimit = countLimit;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater == null ? null : creater.trim();
	}

	public Integer getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier == null ? null : modifier.trim();
	}

	public Integer getModifierId() {
		return modifierId;
	}

	public void setModifierId(Integer modifierId) {
		this.modifierId = modifierId;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}