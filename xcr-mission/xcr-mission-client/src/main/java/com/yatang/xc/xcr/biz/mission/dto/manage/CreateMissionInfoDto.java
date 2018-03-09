package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;
import java.util.List;

public class CreateMissionInfoDto implements Serializable {

    private static final long serialVersionUID = -5732303150928996832L;


    @Override
    public String toString() {
        return "CreateMissionInfoDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", link='" + link + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", awards=" + awards +
                ", attachments=" + attachments +
                ", publish=" + publish +
                ", pubStatus='" + pubStatus + '\'' +
                ", isRelated=" + isRelated +
                ", relStatus='" + relStatus + '\'' +
                ", awardType='" + awardType + '\'' +
                ", courseId='" + courseId + '\'' +
                ", durationTimeStart='" + durationTimeStart + '\'' +
                ", durationTimeEnd='" + durationTimeEnd + '\'' +
                ", validTimeStart='" + validTimeStart + '\'' +
                ", validTimeEnd='" + validTimeEnd + '\'' +
                ", continueDate=" + continueDate +
                ", facePayAmount=" + facePayAmount +
                ", facePayNumber=" + facePayNumber +
                ", buyAmount=" + buyAmount +
                '}';
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 连接
     */
    private String link;

    /**
     * 模板编号
     */
    private String templateCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 图标url
     */
    private String iconUrl;

    /**
     * 奖励列表
     */
    private List<AwardInfoDto> awards;

    /**
     * 奖励列表
     */
    private List<AttachmentDto> attachments;

    /**
     * 是否发布
     */
    private boolean publish;

    /**
     * 发布状态
     */
    private String pubStatus;

    /**
     * 是否关联任务
     */
    private boolean isRelated;

    /**
     * 关联状态
     */
    private String relStatus;

    /**
     * 奖励类型
     */
    private String awardType;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 有效开始时间
     */
    private String durationTimeStart;

    /**
     * 有效结束时间
     */
    private String durationTimeEnd;

    /**
     * 生效开始时间
     */
    private String validTimeStart;

    /**
     * 生效结束时间
     */
    private String validTimeEnd;

    /**
     * 签到连续时间
     */
    private int continueDate;

    /**
     * 当面付金额
     */
    private double facePayAmount;
    /**
     * 当面付笔数
     */
    private int facePayNumber;
    /**
     * 采购金额
     */
    private double buyAmount;

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

    public double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getDurationTimeStart() {
        return durationTimeStart + " 00:00:00";
    }

    public int getContinueDate() {
        return continueDate;
    }


    public void setContinueDate(int continueDate) {
        this.continueDate = continueDate;
    }


    public void setDurationTimeStart(String durationTimeStart) {
        this.durationTimeStart = durationTimeStart;
    }


    public String getDurationTimeEnd() {
        return durationTimeEnd + " 23:59:59";
    }


    public void setDurationTimeEnd(String durationTimeEnd) {
        this.durationTimeEnd = durationTimeEnd;
    }


    public String getValidTimeStart() {
        return validTimeStart + " 00:00:00";
    }


    public void setValidTimeStart(String validTimeStart) {
        this.validTimeStart = validTimeStart;
    }


    public String getValidTimeEnd() {
        return validTimeEnd + " 23:59:59";
    }


    public void setValidTimeEnd(String validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }


    public String getCourseId() {
        return courseId;
    }


    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public List<AttachmentDto> getAttachments() {
        return attachments;
    }


    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getAwardType() {
        return awardType;
    }


    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }


    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link;
    }


    public boolean isRelated() {
        return isRelated;
    }


    public void setRelated(boolean isRelated) {
        this.isRelated = isRelated;
    }


    public boolean isPublish() {
        return publish;
    }


    public void setPublish(boolean publish) {
        this.publish = publish;
    }


    public List<AwardInfoDto> getAwards() {
        return awards;
    }


    public String getIconUrl() {
        return iconUrl;
    }


    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }


    public void setAwards(List<AwardInfoDto> awards) {
        this.awards = awards;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getTemplateCode() {
        return templateCode;
    }


    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getPubStatus() {
        return pubStatus;
    }


    public void setPubStatus(String pubStatus) {
        this.pubStatus = pubStatus;
    }


    public String getRelStatus() {
        return relStatus;
    }


    public void setRelStatus(String relStatus) {
        this.relStatus = relStatus;
    }

}
