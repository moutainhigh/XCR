package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;

public class MissionBuyListPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 5938477839351647734L;


    @Override
    public String toString() {
        return "MissionBuyListPO [id=" + id + ", login=" + login + ", missionId=" + missionId + ", orderId=" + orderId + ", productIds=" + productIds
                + ", returnValue=" + returnValue + ", usedProductCount=" + usedProductCount + ", productCount=" + productCount + "]";
    }

    private Long id;

    private String login;

    private String missionId;

    private String executeId;

    private String orderId;

    private String productIds;

    private String returnValue;

    private Integer usedProductCount;

    private Integer productCount;


    public String getExecuteId() {
        return executeId;
    }


    public void setExecuteId(String executeId) {
        this.executeId = executeId;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login == null ? null : login.trim();
    }


    public String getMissionId() {
        return missionId;
    }


    public void setMissionId(String missionId) {
        this.missionId = missionId == null ? null : missionId.trim();
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


    public String getReturnValue() {
        return returnValue;
    }


    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue == null ? null : returnValue.trim();
    }


    public Integer getUsedProductCount() {
        return usedProductCount;
    }


    public void setUsedProductCount(Integer usedProductCount) {
        this.usedProductCount = usedProductCount;
    }


    public Integer getProductCount() {
        return productCount;
    }


    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
}