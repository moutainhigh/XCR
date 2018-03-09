package com.yatang.xc.xcr.biz.mission.dto.center;

import java.io.Serializable;

public class MissionExecuteQueryDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -737847501150432269L;


    @Override
    public String toString() {
        return "MissionExecuteQueryDto [id=" + id + ", status=" + status + ", type=" + type + ", merchantId=" + merchantId + ", startIndex=" + startIndex
                + ", endIndex=" + endIndex + ", orderBy=" + orderBy + "]";
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 状态
     */
    private String status;

    /**
     * 类型
     */
    private String type;

    /**
     * 门店编号
     */
    private String merchantId;

    /**
     * 开始下标 分页用
     */
    private Integer startIndex;

    /**
     * 结束下标 分页用
     */
    private Integer endIndex;

    /**
     * 排序
     */
    private String orderBy;


    public String getOrderBy() {
        return orderBy;
    }


    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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
