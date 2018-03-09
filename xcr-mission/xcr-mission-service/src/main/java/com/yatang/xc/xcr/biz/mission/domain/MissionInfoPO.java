package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;
import java.util.Date;

public class MissionInfoPO implements Serializable {

    /**
     * @Fields serialVersionUID :
     */
    private static final long serialVersionUID = 31547024900913233L;


    @Override
    public String toString() {
        return "MissionInfoPO{" +
                "id=" + id +
                ", triggerInterfaceName='" + triggerInterfaceName + '\'' +
                ", bpmIdentity='" + bpmIdentity + '\'' +
                ", createTime=" + createTime +
                ", link='" + link + '\'' +
                ", isManualAudit=" + isManualAudit +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", type='" + type + '\'' +
                ", missonTemplateCode='" + missonTemplateCode + '\'' +
                ", isDeleted=" + isDeleted +
                ", lastModifyTime=" + lastModifyTime +
                ", completeTips='" + completeTips + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isRelated=" + isRelated +
                ", rule='" + rule + '\'' +
                ", courseId='" + courseId + '\'' +
                ", specialAwardRemark='" + specialAwardRemark + '\'' +
                ", sort=" + sort +
                ", durationTimeStart=" + durationTimeStart +
                ", durationTimeEnd=" + durationTimeEnd +
                ", validTimeStart=" + validTimeStart +
                ", validTimeEnd=" + validTimeEnd +
                '}';
    }

    private Long id;

    private String triggerInterfaceName;

    private String bpmIdentity;

    private Date createTime;

    private String link;

    private boolean isManualAudit;

    private String status;

    private String reason;

    private String type;

    private String missonTemplateCode;

    private boolean isDeleted;

    private Date lastModifyTime;

    private String completeTips;

    private String iconUrl;

    private String name;

    private String description;

    private boolean isRelated;

    private String rule;

    private String courseId;

    private String specialAwardRemark;

    private Integer sort;

    /**
     * 有效开始时间
     */
    private Date durationTimeStart;

    /**
     * 有效结束时间
     */
    private Date durationTimeEnd;

    /**
     * 生效开始时间
     */
    private Date validTimeStart;

    /**
     * 生效结束时间
     */
    private Date validTimeEnd;


    public Date getDurationTimeStart() {
        return durationTimeStart;
    }

    public void setDurationTimeStart(Date durationTimeStart) {
        this.durationTimeStart = durationTimeStart;
    }

    public Date getDurationTimeEnd() {
        return durationTimeEnd;
    }

    public void setDurationTimeEnd(Date durationTimeEnd) {
        this.durationTimeEnd = durationTimeEnd;
    }

    public Date getValidTimeStart() {
        return validTimeStart;
    }

    public void setValidTimeStart(Date validTimeStart) {
        this.validTimeStart = validTimeStart;
    }

    public Date getValidTimeEnd() {
        return validTimeEnd;
    }

    public void setValidTimeEnd(Date validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public boolean isManualAudit() {
        return isManualAudit;
    }


    public void setManualAudit(boolean isManualAudit) {
        this.isManualAudit = isManualAudit;
    }


    public boolean isDeleted() {
        return isDeleted;
    }


    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public boolean isRelated() {
        return isRelated;
    }


    public void setRelated(boolean isRelated) {
        this.isRelated = isRelated;
    }


    public String getSpecialAwardRemark() {
        return specialAwardRemark;
    }


    public void setSpecialAwardRemark(String specialAwardRemark) {
        this.specialAwardRemark = specialAwardRemark;
    }


    public String getCourseId() {
        return courseId;
    }


    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getTriggerInterfaceName() {
        return triggerInterfaceName;
    }


    public void setTriggerInterfaceName(String triggerInterfaceName) {
        this.triggerInterfaceName = triggerInterfaceName == null ? null : triggerInterfaceName.trim();
    }


    public String getBpmIdentity() {
        return bpmIdentity;
    }


    public void setBpmIdentity(String bpmIdentity) {
        this.bpmIdentity = bpmIdentity == null ? null : bpmIdentity.trim();
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
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


    public String getMissonTemplateCode() {
        return missonTemplateCode;
    }


    public void setMissonTemplateCode(String missonTemplateCode) {
        this.missonTemplateCode = missonTemplateCode == null ? null : missonTemplateCode.trim();
    }


    public Date getLastModifyTime() {
        return lastModifyTime;
    }


    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }


    public String getCompleteTips() {
        return completeTips;
    }


    public void setCompleteTips(String completeTips) {
        this.completeTips = completeTips == null ? null : completeTips.trim();
    }


    public String getIconUrl() {
        return iconUrl;
    }


    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }


    public String getRule() {
        return rule;
    }


    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }
}