package com.yatang.xc.xcr.biz.mission.dto.center;

import java.io.Serializable;

public class MissionBuyDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 5938474449351647734L;


    @Override
    public String toString() {
        return "MissionBuyDto [login=" + login + ", missionExecuteId=" + missionExecuteId + ", orderId=" + orderId + ", productIds=" + productIds + "]";
    }

    private String login;

    private String missionExecuteId;

    private String orderId;

    private String productIds;


    public String getMissionExecuteId() {
        return missionExecuteId;
    }


    public void setMissionExecuteId(String missionExecuteId) {
        this.missionExecuteId = missionExecuteId;
    }


    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login == null ? null : login.trim();
    }


    public String getOrderId() {
        return orderId;
    }


    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }


    public String getProductIds() {
        return productIds;
    }


    public void setProductIds(String productIds) {
        this.productIds = productIds == null ? null : productIds.trim();
    }

}