package com.yatang.xc.xcr.biz.mission.dto.creator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;

/**
 * 单个对象创建对象
 * 
 * @author yangqingsong
 *
 */
public class SingleItemRuleDto implements RuleDefinitionDto, RuleCalculateDto, Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 8595141907043068859L;


    @Override
    public String toString() {
        return "SingleItemRuleDto [type=" + type + ", identity=" + identity + ", status=" + status + ", awardNum=" + awardNum + "]";
    }

    // 实体类型
    private String type;

    // 实体标示
    private List<String> identity;

    // 当前状态
    private Integer status;

    // 奖励金额
    private Double awardNum;


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public List<String> getIdentity() {
        return identity;
    }


    public void setIdentity(List<String> identity) {
        this.identity = identity;
    }


    public void setIdentityGallery(String... identity) {
        this.identity = Arrays.asList(identity);
    }


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
