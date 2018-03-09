package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;

public class ExecuteByMissionIdAndMerchantIdQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -8956260223173526586L;


    @Override
    public String toString() {
        return "ExecuteByMissionIdAndMerchantIdQuery [merchantId=" + merchantId + ", missionId=" + missionId + "]";
    }

    private String merchantId;

    private Long missionId;


    public String getMerchantId() {
        return merchantId;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }


    public Long getMissionId() {
        return missionId;
    }


    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

}
