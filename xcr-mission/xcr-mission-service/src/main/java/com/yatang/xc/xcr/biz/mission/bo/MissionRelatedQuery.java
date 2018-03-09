package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;

public class MissionRelatedQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -3649330730428889391L;


    @Override
    public String toString() {
        return "MissionRelatedQuery [id=" + id + ", type=" + type + ", status=" + status + ", startIndex=" + startIndex + ", endIndex=" + endIndex + "]";
    }

    private Long id;

    private String type;

    private String status;

    private Integer startIndex;

    private Integer endIndex;
	/**
	 * 排序
	 */
	private String	orderBy;
	

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
