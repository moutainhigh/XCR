package com.yatang.xc.xcr.vo;

import java.io.Serializable;
import java.util.List;

import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;

public class CreateMissionInfoVO implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -3688159892725707588L;

	/**
     * 主键
     */
    private Long id;
    
    /**
     * 销售小票数
     */
    private String soldTicketNum;
    
    /**
     * 任务持续时间
     */
    private String lastingTime;
    
    /**
     * 任务结束时间
     */
    private String dateEnd;
    
    /**
     * 任务开始时间
     */
    private String dateStart;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSoldTicketNum() {
		return soldTicketNum;
	}

	public void setSoldTicketNum(String soldTicketNum) {
		this.soldTicketNum = soldTicketNum;
	}

	public String getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(String lastingTime) {
		this.lastingTime = lastingTime;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

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

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public String getPubStatus() {
		return pubStatus;
	}

	public void setPubStatus(String pubStatus) {
		this.pubStatus = pubStatus;
	}

	public boolean isRelated() {
		return isRelated;
	}

	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}

	public String getRelStatus() {
		return relStatus;
	}

	public void setRelStatus(String relStatus) {
		this.relStatus = relStatus;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "CreateMissionInfoVO [id=" + id + ", soldTicketNum="
				+ soldTicketNum + ", lastingTime=" + lastingTime + ", dateEnd="
				+ dateEnd + ", dateStart=" + dateStart + ", type=" + type
				+ ", link=" + link + ", templateCode=" + templateCode
				+ ", name=" + name + ", description=" + description
				+ ", iconUrl=" + iconUrl + ", awards=" + awards
				+ ", attachments=" + attachments + ", publish=" + publish
				+ ", pubStatus=" + pubStatus + ", isRelated=" + isRelated
				+ ", relStatus=" + relStatus + ", awardType=" + awardType
				+ ", courseId=" + courseId + "]";
	}

}
