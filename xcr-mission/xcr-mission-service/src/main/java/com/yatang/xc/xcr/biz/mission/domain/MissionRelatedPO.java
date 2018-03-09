package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;
import java.util.Date;

public class MissionRelatedPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 5884018959962893953L;


    @Override
    public String toString() {
        return "MissionRelatedPO [id=" + id + ", missonRelatedName=" + missonRelatedName + ", missonRelatedDescription=" + missonRelatedDescription
                + ", createTime=" + createTime + ", status=" + status + ", reason=" + reason + ", type=" + type + ", lastModifyTime=" + lastModifyTime + "]";
    }

    private Long id;

    private String missonRelatedName;

    private String missonRelatedDescription;

    private Date createTime;

    private String status;

    private String reason;

    private String type;

    private Date lastModifyTime;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getMissonRelatedName() {
        return missonRelatedName;
    }


    public void setMissonRelatedName(String missonRelatedName) {
        this.missonRelatedName = missonRelatedName == null ? null : missonRelatedName.trim();
    }


    public String getMissonRelatedDescription() {
        return missonRelatedDescription;
    }


    public void setMissonRelatedDescription(String missonRelatedDescription) {
        this.missonRelatedDescription = missonRelatedDescription == null ? null : missonRelatedDescription.trim();
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }


    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }


    public Date getLastModifyTime() {
        return lastModifyTime;
    }


    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}