package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;
import java.util.Date;

public class MissionExecuteQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -7353924228727635095L;


    @Override
    public String toString() {
        return "MissionExecuteQuery [id=" + id + ", status=" + status + ", type=" + type + ", merchantId=" + merchantId + ", startIndex=" + startIndex
                + ", endIndex=" + endIndex + ", orderBy=" + orderBy + "]";
    }

    private Long id;

    private String status;

    private String type;

    private String merchantId;

    private Integer startIndex;

    private Integer endIndex;

    private String orderBy;

    private Date now;


    public Date getNow() {
        return now;
    }


    public void setNow(Date now) {
        this.now = now;
    }


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
