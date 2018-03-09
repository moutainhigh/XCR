package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;
import java.util.Date;

public class MissionUserHistoryPO implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -6493396918064498878L;

    private Long id;

    /**
     * 一账通账号
     */
    private String login;

    /**
     * 门店编号
     */
    private String merchantId;

    /**
     * 小超账号
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

    /**
     * 手机app设备号
     */
    private String registrationId;

    /**
     * 真实姓名校验结果
     */
    private String realNameCheckResult;

    /**
     * 子公司id
     */
    private String companyId;

    /**
     * 小超名称
     */
    private String shopName;


    public String getShopName() {
        return shopName;
    }


    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public String getCompanyId() {
        return companyId;
    }


    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public String getRealNameCheckResult() {
        return realNameCheckResult;
    }


    public void setRealNameCheckResult(String realNameCheckResult) {
        this.realNameCheckResult = realNameCheckResult;
    }


    public String getRegistrationId() {
        return registrationId;
    }


    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }


    public Date getLastModifyTime() {
        return lastModifyTime;
    }


    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
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


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }


    public String getRealName() {
        return realName;
    }


    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

}