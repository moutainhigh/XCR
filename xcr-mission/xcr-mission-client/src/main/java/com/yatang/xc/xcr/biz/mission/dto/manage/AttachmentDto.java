package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;

public class AttachmentDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -7168955151930736776L;

    private String name;

    private String description;

    private String type;

    private String missionInfoId;

    private String missionExecuteId;

    private String url;

    private Integer sort;


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


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getMissionInfoId() {
        return missionInfoId;
    }


    public void setMissionInfoId(String missionInfoId) {
        this.missionInfoId = missionInfoId;
    }


    public String getMissionExecuteId() {
        return missionExecuteId;
    }


    public void setMissionExecuteId(String missionExecuteId) {
        this.missionExecuteId = missionExecuteId;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    @Override
    public String toString() {
        return "AttachmentDto [name=" + name + ", description=" + description + ", type=" + type + ", missionInfoId=" + missionInfoId + ", missionExecuteId="
                + missionExecuteId + ", url=" + url + ", sort=" + sort + "]";
    }

}
