package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;

public class OfflineListQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 2377251142029089217L;


    @Override
    public String toString() {
        return "OfflineListQuery [merchantId=" + merchantId + ", templateCode=" + templateCode + "]";
    }

    private String merchantId;

    private String templateCode;


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }


    public String getTemplateCode() {
        return templateCode;
    }


    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

}
