package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;

public class MissionRelatedDetailPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -3034216414209921973L;


    @Override
    public String toString() {
        return "MissionRelatedDetailPO [id=" + id + ", missonRelatedId=" + missonRelatedId + ", missonInfoId=" + missonInfoId + ", level=" + level + "]";
    }

    private Long id;

    private Long missonRelatedId;

    private Long missonInfoId;

    private Integer level;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getMissonRelatedId() {
        return missonRelatedId;
    }


    public void setMissonRelatedId(Long missonRelatedId) {
        this.missonRelatedId = missonRelatedId;
    }


    public Long getMissonInfoId() {
        return missonInfoId;
    }


    public void setMissonInfoId(Long missonInfoId) {
        this.missonInfoId = missonInfoId;
    }


    public Integer getLevel() {
        return level;
    }


    public void setLevel(Integer level) {
        this.level = level;
    }
}