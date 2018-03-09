package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ViewMissionInfoDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -2676189401784913747L;


    @Override
    public String toString() {
        return "ViewMissionInfoDto{" +
                "templateName='" + templateName + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", sort=" + sort +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", awards=" + awards +
                ", attachments=" + attachments +
                ", needManualAudit=" + needManualAudit +
                ", status='" + status + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                ", specialAwardRemark='" + specialAwardRemark + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", awardType='" + awardType + '\'' +
                ", isRelated=" + isRelated +
                ", description='" + description + '\'' +
                ", courseId='" + courseId + '\'' +
                ", durationTimeStart=" + durationTimeStart +
                ", durationTimeEnd=" + durationTimeEnd +
                ", validTimeStart=" + validTimeStart +
                ", validTimeEnd=" + validTimeEnd +
                ", continueDate=" + continueDate +
                ", facePayNumber=" + facePayNumber +
                ", facePayAmount=" + facePayAmount +
                ", buyAmount=" + buyAmount +
                '}';
    }

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 排序（现在不使用）
     */
    private Integer sort;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 奖励列表
     */
    private List<AwardInfoDto> awards;

    /**
     * 附件列表
     */
    private List<AttachmentDto> attachments;

    /**
     * 是否人工审核
     */
    private Boolean needManualAudit;

    /**
     * 状态
     */
    private String status;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

    /**
     * 特殊奖励标注
     */
    private String specialAwardRemark;

    /**
     * 图标url
     */
    private String iconUrl;

    /**
     * 奖励类型
     */
    private String awardType;

    /**
     * 是否关联属性
     */
    private boolean isRelated;

    /**
     * 描述
     */
    private String description;

    /**
     * 课程id
     */
    private String courseId;

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

    /**
     * 连续签到天数
     */
    private int continueDate;

    /**
     * 日当面付笔数
     */
    private int facePayNumber;

    /**
     * 月当面付金额
     */
    private double facePayAmount;

    /**
     * 月供应链采购金额
     */
    private double buyAmount;


    public double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public double getFacePayAmount() {
        return facePayAmount;
    }

    public void setFacePayAmount(double facePayAmount) {
        this.facePayAmount = facePayAmount;
    }

    public int getFacePayNumber() {
        return facePayNumber;
    }

    public void setFacePayNumber(int facePayNumber) {
        this.facePayNumber = facePayNumber;
    }

    public int getContinueDate() {
        return continueDate;
    }

    public void setContinueDate(int continueDate) {
        this.continueDate = continueDate;
    }

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

    public String getCourseId() {
        return courseId;
    }


    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public String getTemplateName() {
        return templateName;
    }


    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


    public String getTemplateCode() {
        return templateCode;
    }


    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public String getIconUrl() {
        return iconUrl;
    }


    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }


    public String getAwardType() {
        return awardType;
    }


    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }


    public boolean isRelated() {
        return isRelated;
    }


    public void setRelated(boolean isRelated) {
        this.isRelated = isRelated;
    }


    public List<AttachmentDto> getAttachments() {
        return attachments;
    }


    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public List<AwardInfoDto> getAwards() {
        return awards;
    }


    public void setAwards(List<AwardInfoDto> awards) {
        this.awards = awards;
    }


    public Boolean getNeedManualAudit() {
        return needManualAudit;
    }


    public void setNeedManualAudit(Boolean needManualAudit) {
        this.needManualAudit = needManualAudit;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public Date getLastModifyTime() {
        return lastModifyTime;
    }


    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

}
