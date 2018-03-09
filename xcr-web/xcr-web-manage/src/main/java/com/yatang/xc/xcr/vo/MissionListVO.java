package com.yatang.xc.xcr.vo;

import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;

import java.util.Date;
import java.util.List;

/**
 * 普通任务列表VO
 * Created by wangyang on 2017/6/23.
 */
public class MissionListVO {


    private Long id;
    private String name; //任务名称
    private String related; //任务类型
    private String type; //任务分类
    private String awardType; //任务奖励
    private String needManualAudit; //审核类型
    private String status; //任务状态
    private String lastModifyTime; //最后更新时间
    private String usefulTime; //有效期

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
     * 模板名称
     */
    private String templateName;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 图标url
     */
    private String iconUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 奖励列表
     */
    private List<AwardInfoDto> awards;

    /**
     * 课程id
     */
    private String courseId;

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

    public int getFacePayNumber() {
        return facePayNumber;
    }

    public void setFacePayNumber(int facePayNumber) {
        this.facePayNumber = facePayNumber;
    }

    public double getFacePayAmount() {
        return facePayAmount;
    }

    public void setFacePayAmount(double facePayAmount) {
        this.facePayAmount = facePayAmount;
    }

    public double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public int getContinueDate() {
        return continueDate;
    }

    public void setContinueDate(int continueDate) {
        this.continueDate = continueDate;
    }

    public String getUsefulTime() {
        return usefulTime;
    }

    public void setUsefulTime(String usefulTime) {
        this.usefulTime = usefulTime;
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

    public List<AwardInfoDto> getAwards() {
        return awards;
    }

    public void setAwards(List<AwardInfoDto> awards) {
        this.awards = awards;
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

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAwardType() {
        return awardType;
    }

    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }

    public String getNeedManualAudit() {
        return needManualAudit;
    }

    public void setNeedManualAudit(String needManualAudit) {
        this.needManualAudit = needManualAudit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
