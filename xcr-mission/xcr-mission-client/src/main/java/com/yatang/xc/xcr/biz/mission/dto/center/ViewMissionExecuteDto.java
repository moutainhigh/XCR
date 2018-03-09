package com.yatang.xc.xcr.biz.mission.dto.center;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;

public class ViewMissionExecuteDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -199033498401539343L;


    @Override
    public String toString() {
        return "ViewMissionExecuteDto [id=" + id + ", merchantId=" + merchantId + ", status=" + status + ", type=" + type + ", startTime=" + startTime
                + ", endTime=" + endTime + ", specialAwardRemark=" + specialAwardRemark + ", link=" + link + ", needManualAudit=" + needManualAudit
                + ", completeTips=" + completeTips + ", iconUrl=" + iconUrl + ", name=" + name + ", description=" + description + ", isRelated=" + isRelated
                + ", awards=" + awards + ", attachments=" + attachments + ", templateCode=" + templateCode + "]";
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 门店编号
     */
    private String merchantId;

    /**
     * 状态
     */
    private String status;

    /**
     * 原因
     */
    private String reason;

    /**
     * 类型
     */
    private String type;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 特殊奖励标注
     */
    private String specialAwardRemark;

    /**
     * 链接
     */
    private String link;

    /**
     * 是否人工审核
     */
    private Boolean needManualAudit;

    /**
     * 完成提示(预留)
     */
    private String completeTips;

    /**
     * 图标url
     */
    private String iconUrl;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否关联的任务
     */
    private boolean isRelated;

    /**
     * 课程编号
     */
    private String courseId;

    private Date validTimeStart;

    private Date validTimeEnd;

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

    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getCourseId() {
        return courseId;
    }


    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     * 奖励列表
     */
    private List<AwardInfoDto> awards;

    /**
     * 附件列表
     */
    private List<AttachmentDto> attachments;

    /**
     * 模板编码
     */
    private String templateCode;


    public String getTemplateCode() {
        return templateCode;
    }


    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
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


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
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


    public String getSpecialAwardRemark() {
        return specialAwardRemark;
    }


    public void setSpecialAwardRemark(String specialAwardRemark) {
        this.specialAwardRemark = specialAwardRemark;
    }


    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link;
    }


    public Boolean getNeedManualAudit() {
        return needManualAudit;
    }


    public void setNeedManualAudit(Boolean needManualAudit) {
        this.needManualAudit = needManualAudit;
    }


    public String getCompleteTips() {
        return completeTips;
    }


    public void setCompleteTips(String completeTips) {
        this.completeTips = completeTips;
    }


    public String getIconUrl() {
        return iconUrl;
    }


    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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


    public boolean isRelated() {
        return isRelated;
    }


    public void setRelated(boolean isRelated) {
        this.isRelated = isRelated;
    }


    public List<AwardInfoDto> getAwards() {
        return awards;
    }


    public void setAwards(List<AwardInfoDto> awards) {
        this.awards = awards;
    }
}
