package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;

/**
 *  可执行任务的查询对象
 * 
 * @author yangqingsong
 *
 */
public class BuyListByQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -4455350146390111072L;

    private String missionId;

    private String login;

    private String executeId;


    public String getMissionId() {
        return missionId;
    }


    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }


    public String getExecuteId() {
        return executeId;
    }


    public void setExecuteId(String executeId) {
        this.executeId = executeId;
    }


    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    @Override
    public String toString() {
        return "buyListByQuery [missionId=" + missionId + ", login=" + login + "]";
    }

}
