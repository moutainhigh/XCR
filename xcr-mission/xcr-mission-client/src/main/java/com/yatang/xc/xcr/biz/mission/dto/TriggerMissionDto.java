package com.yatang.xc.xcr.biz.mission.dto;

import java.io.Serializable;

/**
 * 被动触发任务传入参数
 * 
 * @author yangqingsong
 *
 */
public class TriggerMissionDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 4576355082963561659L;


    @Override
    public String toString() {
        return "TriggerMissionDto [triggerInterface=" + triggerInterface + ", merchantId=" + merchantId + ", calculateParam=" + calculateParam + "]";
    }

    // 触发接口
    private String triggerInterface;

    // 店铺信息
    private String merchantId;

    // 计算对象
    private RuleCalculateDto calculateParam;


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


    public RuleCalculateDto getCalculateParam() {
        return calculateParam;
    }


    public void setCalculateParam(RuleCalculateDto calculateParam) {
        this.calculateParam = calculateParam;
    }

}
