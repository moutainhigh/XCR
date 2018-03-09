package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;

/**
 *  可执行任务的查询对象
 * 
 * @author yangqingsong
 *
 */
public class ExecutableMissionQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -4455350146390193072L;


    @Override
    public String toString() {
        return "ExecutableMissionQuery [triggerInterface=" + triggerInterface + ", merchantId=" + merchantId + "]";
    }

    private String triggerInterface;

    private String merchantId;


    public String getTriggerInterface() {
        return triggerInterface;
    }


    public void setTriggerInterface(String triggerInterface) {
        this.triggerInterface = triggerInterface;
    }


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
