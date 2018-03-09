package com.yatang.xc.xcr.biz.mission.dto.creator;

import java.io.Serializable;

import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;

/**
 * 简单规则创建对象
 * 
 * @author yangqingsong
 *
 */
public class SimpleRuleDto implements RuleDefinitionDto, RuleCalculateDto, Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -2016262546325238736L;


    @Override
    public String toString() {
        return "SimpleRuleDto [status=" + status + ", awardNum=" + awardNum + "]";
    }

    // 状态
    private Integer status;

    // 奖励金额
    private Double awardNum;


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public Double getAwardNum() {
        return awardNum;
    }


    public void setAwardNum(Double awardNum) {
        this.awardNum = awardNum;
    }


    @Override
    public boolean hasSuccess() {
        return null != this.awardNum;
    }
}
