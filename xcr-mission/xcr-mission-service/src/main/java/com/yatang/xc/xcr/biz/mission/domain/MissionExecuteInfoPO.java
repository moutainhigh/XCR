package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;
import java.util.Date;

public class MissionExecuteInfoPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -1019951382286604288L;


    @Override
    public String toString() {
        return "MissionExecuteInfoPO{" +
                "id=" + id +
                ", missonInfoId=" + missonInfoId +
                ", merchantId='" + merchantId + '\'' +
                ", createTime=" + createTime +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", type='" + type + '\'' +
                ", isDeleted=" + isDeleted +
                ", lastModifyTime=" + lastModifyTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", triggerInterfaceName='" + triggerInterfaceName + '\'' +
                ", rule='" + rule + '\'' +
                ", specialAwardRemark='" + specialAwardRemark + '\'' +
                ", courseId='" + courseId + '\'' +
                ", bpmIdentity='" + bpmIdentity + '\'' +
                ", sort=" + sort +
                ", isBackup=" + isBackup +
                ", description='" + description + '\'' +
                ", validTimeStart=" + validTimeStart +
                ", validTimeEnd=" + validTimeEnd +
                '}';
    }

    private Long id;

    private Long missonInfoId;

    private String merchantId;

    private Date createTime;

    private String status;

    private String reason;

    private String type;

    private boolean isDeleted;

    private Date lastModifyTime;

    private Date startTime;

    private Date endTime;

    private String triggerInterfaceName;

    private String rule;

    private String specialAwardRemark;

    private String courseId;

    private String bpmIdentity;

    private Integer sort;

    private boolean isBackup;

    private String description; //任务描述，点击任务弹框查看

    /**
     * 生效开始时间
     */
    private Date validTimeStart;

    /**
     * 生效结束时间
     */
    private Date validTimeEnd;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isBackup() {
        return isBackup;
    }


    
    public void setBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }


    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public boolean isDeleted() {
        return isDeleted;
    }


    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public String getBpmIdentity() {
        return bpmIdentity;
    }


    public void setBpmIdentity(String bpmIdentity) {
        this.bpmIdentity = bpmIdentity;
    }


    public String getCourseId() {
        return courseId;
    }


    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public String getSpecialAwardRemark() {
        return specialAwardRemark;
    }


    public void setSpecialAwardRemark(String specialAwardRemark) {
        this.specialAwardRemark = specialAwardRemark;
    }


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


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
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


    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getEndTime() {
        return endTime;
    }


    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public String getTriggerInterfaceName() {
        return triggerInterfaceName;
    }


    public void setTriggerInterfaceName(String triggerInterfaceName) {
        this.triggerInterfaceName = triggerInterfaceName == null ? null : triggerInterfaceName.trim();
    }


    public String getRule() {
        return rule;
    }


    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }
}