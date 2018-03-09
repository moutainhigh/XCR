package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;

public class MissionRelatedDetailDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 8902474687132172399L;


    @Override
    public String toString() {
        return "MissionRelatedDetailDto [missonRelatedId=" + missonRelatedId + ", missonInfoId=" + missonInfoId + ", level=" + level + "]";
    }

    /**
     * 关联id
     */
    private Long missonRelatedId;

    /**
     * 任务信息id
     */
    private Long missonInfoId;

    /**
     * 级别 最小1级 ,越小级别的任务需越先完成
     */
    private Integer level;


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
