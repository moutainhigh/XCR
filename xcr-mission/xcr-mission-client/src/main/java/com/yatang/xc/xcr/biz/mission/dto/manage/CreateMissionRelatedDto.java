package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;
import java.util.List;

public class CreateMissionRelatedDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -1656592756004582844L;


   

    @Override
	public String toString() {
		return "CreateMissionRelatedDto [id=" + id + ", missonRelatedName=" + missonRelatedName
				+ ", missonRelatedDescription=" + missonRelatedDescription + ", isPublish=" + isPublish + ", publishMa="
				+ publishMa + ", details=" + details + "]";
	}


	/**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String missonRelatedName;

    /**
     * 描述
     */
    private String missonRelatedDescription;

    /**
     * 是否发布
     */
    private boolean isPublish;
    
    /**
     * 是否发布
     */
    private int publishMa;

    /**
     * 关联细节列表
     */
    private List<MissionRelatedDetailDto> details;


	public int getPublishMa() {
		return publishMa;
	}


	public void setPublishMa(int publishMa) {
		this.publishMa = publishMa;
	}


	public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public List<MissionRelatedDetailDto> getDetails() {
        return details;
    }


    public boolean isPublish() {
        return isPublish;
    }


    public void setPublish(boolean isPublish) {
        this.isPublish = isPublish;
    }


    public void setDetails(List<MissionRelatedDetailDto> details) {
        this.details = details;
    }


    public String getMissonRelatedName() {
        return missonRelatedName;
    }


    public void setMissonRelatedName(String missonRelatedName) {
        this.missonRelatedName = missonRelatedName;
    }


    public String getMissonRelatedDescription() {
        return missonRelatedDescription;
    }


    public void setMissonRelatedDescription(String missonRelatedDescription) {
        this.missonRelatedDescription = missonRelatedDescription;
    }

}
