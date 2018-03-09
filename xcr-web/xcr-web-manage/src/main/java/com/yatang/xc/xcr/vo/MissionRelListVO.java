package com.yatang.xc.xcr.vo;

import com.yatang.xc.xcr.biz.mission.dto.manage.MissionRelatedDetailDto;

import java.util.Date;
import java.util.List;

/**
 * 关联任务页面列表
 * Created by wangyang on 2017/6/24.
 */
public class MissionRelListVO {


    private Long id; //主键
    private String missonRelatedName; //名称
    private String missonRelatedDescription; //描述
    private String type; //类型
    private String status; //状态
    private String lastModifyTime; //最后修改时间
    private List<MissionRelatedDetailDto> details; //关联细节列表

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

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public List<MissionRelatedDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<MissionRelatedDetailDto> details) {
        this.details = details;
    }
}
