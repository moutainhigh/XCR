package com.yatang.xc.xcr.vo;

import com.yatang.xc.xcr.biz.mission.dto.manage.*;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;

import java.util.List;

/**
 * 任务详情
 * Created by wangyang on 2017/12/4.
 */
public class MissionDetailVO {


    private String missionId; //任务id
    private String missionName; //任务名
    private String missionType; //任务分类
    private String missionAwardType; //任务奖励类型
    private String needManualAudit; //审核类型
    private String missionStatus; //任务状态
    private String iconUrl; //图标url
    private String templateCode; //任务类型
    private String templateName;
    private String isRelated;
    private String description;
    private String courseId;
    private String durationTimeStart;
    private String durationTimeEnd;
    private String validTimeStart;
    private String validTimeEnd;
    private String continueDate;
    private String facePayNumber;
    private String facePayAmount;
    private String buyAmount;

    /**
     * 奖励列表
     */
    private List<AwardInfoDto> awards;

    /**
     * 奖励列表
     */
    private List<AttachmentDto> attachments;


    public List<AwardInfoDto> getAwards() {
        return awards;
    }

    public void setAwards(List<AwardInfoDto> awards) {
        this.awards = awards;
    }

    public List<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }

    public String getMissionAwardType() {
        return missionAwardType;
    }

    public void setMissionAwardType(String missionAwardType) {
        this.missionAwardType = missionAwardType;
    }

    public String getNeedManualAudit() {
        return needManualAudit;
    }

    public void setNeedManualAudit(String needManualAudit) {
        this.needManualAudit = needManualAudit;
    }

    public String getMissionStatus() {
        return missionStatus;
    }

    public void setMissionStatus(String missionStatus) {
        this.missionStatus = missionStatus;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getIsRelated() {
        return isRelated;
    }

    public void setIsRelated(String isRelated) {
        this.isRelated = isRelated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDurationTimeStart() {
        return durationTimeStart;
    }

    public void setDurationTimeStart(String durationTimeStart) {
        this.durationTimeStart = durationTimeStart;
    }

    public String getDurationTimeEnd() {
        return durationTimeEnd;
    }

    public void setDurationTimeEnd(String durationTimeEnd) {
        this.durationTimeEnd = durationTimeEnd;
    }

    public String getValidTimeStart() {
        return validTimeStart;
    }

    public void setValidTimeStart(String validTimeStart) {
        this.validTimeStart = validTimeStart;
    }

    public String getValidTimeEnd() {
        return validTimeEnd;
    }

    public void setValidTimeEnd(String validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

    public String getContinueDate() {
        return continueDate;
    }

    public void setContinueDate(String continueDate) {
        this.continueDate = continueDate;
    }

    public String getFacePayNumber() {
        return facePayNumber;
    }

    public void setFacePayNumber(String facePayNumber) {
        this.facePayNumber = facePayNumber;
    }

    public String getFacePayAmount() {
        return facePayAmount;
    }

    public void setFacePayAmount(String facePayAmount) {
        this.facePayAmount = facePayAmount;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }
}
