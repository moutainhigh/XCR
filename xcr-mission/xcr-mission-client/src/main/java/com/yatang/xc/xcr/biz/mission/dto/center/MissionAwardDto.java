package com.yatang.xc.xcr.biz.mission.dto.center;

import java.io.Serializable;

/**
 * 任务奖励
 * Created by wangyang on 2017/7/15.
 */
public class MissionAwardDto implements Serializable{

    private static final long serialVersionUID = -92957824057672601L;
    private String award; //奖励(人民币/积分)
    private String awardUnit; //单位(元/积分)

    public MissionAwardDto() {
    }

    public MissionAwardDto(String award, String awardUnit) {
        this.award = award;
        this.awardUnit = awardUnit;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getAwardUnit() {
        return awardUnit;
    }

    public void setAwardUnit(String awardUnit) {
        this.awardUnit = awardUnit;
    }

    @Override
    public String toString() {
        return "MissionAwardDto{" +
                "award='" + award + '\'' +
                ", awardUnit='" + awardUnit + '\'' +
                '}';
    }
}
