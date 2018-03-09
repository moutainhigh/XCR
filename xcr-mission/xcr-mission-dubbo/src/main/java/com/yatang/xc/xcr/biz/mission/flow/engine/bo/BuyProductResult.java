package com.yatang.xc.xcr.biz.mission.flow.engine.bo;

import java.io.Serializable;
import java.util.Map;

public class BuyProductResult implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -4243772261482580147L;

    private String sessionConfirmationNumber;

    private boolean result;

    private Map<String, Integer> productCount;

    private String errorMsg;


    public String getSessionConfirmationNumber() {
        return sessionConfirmationNumber;
    }


    public void setSessionConfirmationNumber(String sessionConfirmationNumber) {
        this.sessionConfirmationNumber = sessionConfirmationNumber;
    }


    public boolean isResult() {
        return result;
    }


    public void setResult(boolean result) {
        this.result = result;
    }


    public Map<String, Integer> getProductCount() {
        return productCount;
    }


    public void setProductCount(Map<String, Integer> productCount) {
        this.productCount = productCount;
    }


    public String getErrorMsg() {
        return errorMsg;
    }


    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
