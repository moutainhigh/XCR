package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;

public class MissionAttachmentPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -7018725328352189123L;


    @Override
    public String toString() {
        return "MissionAttachmentPO [id=" + id + ", name=" + name + ", description=" + description + ", attachmentCode=" + attachmentCode + ", type=" + type
                + ", missionInfoId=" + missionInfoId + ", missionExecuteId=" + missionExecuteId + ", url=" + url + ", sort=" + sort + "]";
    }

    private Long id;

    private String name;

    private String description;

    private String attachmentCode;

    private String type;

    private String missionInfoId;

    private String missionExecuteId;

    private String url;

    private Integer sort;


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
        this.name = name == null ? null : name.trim();
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }


    public String getAttachmentCode() {
        return attachmentCode;
    }


    public void setAttachmentCode(String attachmentCode) {
        this.attachmentCode = attachmentCode == null ? null : attachmentCode.trim();
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }


    public String getMissionInfoId() {
        return missionInfoId;
    }


    public void setMissionInfoId(String missionInfoId) {
        this.missionInfoId = missionInfoId == null ? null : missionInfoId.trim();
    }


    public String getMissionExecuteId() {
        return missionExecuteId;
    }


    public void setMissionExecuteId(String missionExecuteId) {
        this.missionExecuteId = missionExecuteId == null ? null : missionExecuteId.trim();
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }


    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }
}