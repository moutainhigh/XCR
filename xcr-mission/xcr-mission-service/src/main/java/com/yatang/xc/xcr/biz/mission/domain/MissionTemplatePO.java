package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;

public class MissionTemplatePO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 3691457899716532685L;


    @Override
    public String toString() {
        return "MissionTemplatePO [id=" + id + ", name=" + name + ", type=" + type + ", missionType=" + missionType + ", templateCode=" + templateCode
                + ", link=" + link + ", isManualAudit=" + isManualAudit + ", completeTips=" + completeTips + ", triggerInterfaceName=" + triggerInterfaceName
                + ", isSchedule=" + isSchedule + ", startHour=" + startHour + ", durationHours=" + durationHours + ", ruleTemplate=" + ruleTemplate
                + ", specialAwardRemark=" + specialAwardRemark + ", sort=" + sort + "]";
    }

    private Long id;

    private String name;

    private String type;

    private String missionType;

    private String templateCode;

    private String link;

    private boolean isManualAudit;

    private String completeTips;

    private String triggerInterfaceName;

    private boolean isSchedule;
    
    private boolean isDeleted;

    private Integer startHour;

    private Integer durationHours;

    private String ruleTemplate;

    private String specialAwardRemark;

    private Integer sort;


    public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public String getMissionType() {
        return missionType;
    }


    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }


    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
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


    public String getTemplateCode() {
        return templateCode;
    }


    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode == null ? null : templateCode.trim();
    }


    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }


    public boolean isManualAudit() {
        return isManualAudit;
    }


    public void setManualAudit(boolean isManualAudit) {
        this.isManualAudit = isManualAudit;
    }


    public String getCompleteTips() {
        return completeTips;
    }


    public void setCompleteTips(String completeTips) {
        this.completeTips = completeTips == null ? null : completeTips.trim();
    }


    public String getTriggerInterfaceName() {
        return triggerInterfaceName;
    }


    public void setTriggerInterfaceName(String triggerInterfaceName) {
        this.triggerInterfaceName = triggerInterfaceName == null ? null : triggerInterfaceName.trim();
    }


    public boolean isSchedule() {
        return isSchedule;
    }


    public void setSchedule(boolean isSchedule) {
        this.isSchedule = isSchedule;
    }


    public Integer getStartHour() {
        return startHour;
    }


    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }


    public Integer getDurationHours() {
        return durationHours;
    }


    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }


    public String getRuleTemplate() {
        return ruleTemplate;
    }


    public void setRuleTemplate(String ruleTemplate) {
        this.ruleTemplate = ruleTemplate == null ? null : ruleTemplate.trim();
    }
}