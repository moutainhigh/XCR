package com.yatang.xc.xcr.biz.mission.dto;

import java.io.Serializable;

/**
 * 领取奖励实体
 * 
 * @author yangqingsong
 *
 */
public class GrantAwardDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 7292970165704707987L;


    @Override
    public String toString() {
        return "GrantAwardDto [missionExecuteId=" + missionExecuteId + ", grantUserId=" + grantUserId + "]";
    }

    // 任务执行id
    private String missionExecuteId;

    // 领取人
    private String grantUserId;


    public String getMissionExecuteId() {
        return missionExecuteId;
    }


    public void setMissionExecuteId(String missionExecuteId) {
        this.missionExecuteId = missionExecuteId;
    }


    public String getGrantUserId() {
        return grantUserId;
    }


    public void setGrantUserId(String grantUserId) {
        this.grantUserId = grantUserId;
    }

}
