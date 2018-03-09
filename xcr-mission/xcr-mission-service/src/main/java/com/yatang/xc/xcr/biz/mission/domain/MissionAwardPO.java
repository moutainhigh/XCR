package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;

public class MissionAwardPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -4736030047114224707L;


    @Override
    public String toString() {
        return "MissionAwardPO [id=" + id + ", missonInfoId=" + missonInfoId + ", awardType=" + awardType + ", grantStyle=" + grantStyle + ", grantNum="
                + grantNum + "]";
    }

    private Long id;

    private Long missonInfoId;

    private String awardType;

    private String grantStyle;

    private Double grantNum;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getMissonInfoId() {
        return missonInfoId;
    }


    public void setMissonInfoId(Long missonInfoId) {
        this.missonInfoId = missonInfoId;
    }


    public String getAwardType() {
        return awardType;
    }


    public void setAwardType(String awardType) {
        this.awardType = awardType == null ? null : awardType.trim();
    }


    public String getGrantStyle() {
        return grantStyle;
    }


    public void setGrantStyle(String grantStyle) {
        this.grantStyle = grantStyle == null ? null : grantStyle.trim();
    }


    public Double getGrantNum() {
        return grantNum;
    }


    public void setGrantNum(Double grantNum) {
        this.grantNum = grantNum;
    }
}