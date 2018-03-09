package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangyang on 2017/7/14.
 */
public class MissionAwardCollectPO implements Serializable{

    private static final long serialVersionUID = 6938539026825775243L;
    private Integer id;
    private Double signScoreTotal;
    private Double messionScoreTotal;
    private Double signCashTotal;
    private Double messionCashTotal;
    private String shopCode;
    private Date createTime;
    private Date updateTime;

    public MissionAwardCollectPO() {
    }

    public MissionAwardCollectPO(Double signScoreTotal, Double messionScoreTotal, Double signCashTotal, Double messionCashTotal, String shopCode) {
        this.signScoreTotal = signScoreTotal;
        this.messionScoreTotal = messionScoreTotal;
        this.signCashTotal = signCashTotal;
        this.messionCashTotal = messionCashTotal;
        this.shopCode = shopCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getSignScoreTotal() {
        return signScoreTotal;
    }

    public void setSignScoreTotal(Double signScoreTotal) {
        this.signScoreTotal = signScoreTotal;
    }

    public Double getMessionScoreTotal() {
        return messionScoreTotal;
    }

    public void setMessionScoreTotal(Double messionScoreTotal) {
        this.messionScoreTotal = messionScoreTotal;
    }

    public Double getSignCashTotal() {
        return signCashTotal;
    }

    public void setSignCashTotal(Double signCashTotal) {
        this.signCashTotal = signCashTotal;
    }

    public Double getMessionCashTotal() {
        return messionCashTotal;
    }

    public void setMessionCashTotal(Double messionCashTotal) {
        this.messionCashTotal = messionCashTotal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MissionAwardCollectPO{" +
                "id=" + id +
                ", signScoreTotal=" + signScoreTotal +
                ", messionScoreTotal=" + messionScoreTotal +
                ", signCashTotal=" + signCashTotal +
                ", messionCashTotal=" + messionCashTotal +
                ", shopCode='" + shopCode + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
