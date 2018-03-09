package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;

public class UserDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 1621151562001846230L;

    /**
     * 小超账号
     */
    private String UserName;

    /**
     * 一账通账号
     */
    private String login;

    /**
     * 门店编号
     */
    private String merchantId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 小超人app手机设备号
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


    public String getRealName() {
        return realName;
    }


    public void setRealName(String realName) {
        this.realName = realName;
    }


    public String getUserName() {
        return UserName;
    }


    public void setUserName(String userName) {
        UserName = userName;
    }


    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }


    public String getRegistrationId() {
        return registrationId;
    }


    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

}
