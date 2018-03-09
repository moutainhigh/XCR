package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ViewMissionRelatedDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -1147561766260229784L;


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
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;
    
    
    /**
     * 关联细节列表
     */
    private List<MissionRelatedDetailDto> details;


    
    public List<MissionRelatedDetailDto> getDetails() {
        return details;
    }


    
    public void setDetails(List<MissionRelatedDetailDto> details) {
        this.details = details;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
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
