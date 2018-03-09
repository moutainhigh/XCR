package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;
import java.util.Date;

public class MissionExecuteAwardPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 6039693223051554087L;


    @Override
    public String toString() {
        return "MissionExecuteAwardPO [id=" + id + ", missonExecuteIdInfoId=" + missonExecuteIdInfoId + ", awardType=" + awardType + ", grantStyle="
                + grantStyle + ", grantNum=" + grantNum + ", grantTime=" + grantTime + ", grantUser=" + grantUser + "]";
    }

    private Long id;

    private Long missonExecuteIdInfoId;

    private String awardType;

    private String grantStyle;

    private Double grantNum;

    private Date grantTime;

    private String grantUser;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getMissonExecuteIdInfoId() {
        return missonExecuteIdInfoId;
    }


    public void setMissonExecuteIdInfoId(Long missonExecuteIdInfoId) {
        this.missonExecuteIdInfoId = missonExecuteIdInfoId;
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


    public Date getGrantTime() {
        return grantTime;
    }


    public void setGrantTime(Date grantTime) {
        this.grantTime = grantTime;
    }


    public String getGrantUser() {
        return grantUser;
    }


    public void setGrantUser(String grantUser) {
        this.grantUser = grantUser == null ? null : grantUser.trim();
    }
}