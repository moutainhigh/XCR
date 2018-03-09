package com.yatang.xc.xcr.vo;

import java.io.Serializable;

public class AwardInfoDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 6956995755441649273L;


    @Override
    public String toString() {
        return "AwardInfoDto [awardType=" + awardType + ", grantStyle=" + grantStyle + ", grantNum=" + grantNum + "]";
    }

    /**
     * 奖励类型
     */
    private String awardType;

    /**
     * 发放方式
     */
    private String grantStyle;

    /**
     * 奖励数量
     */
    private Double grantNum;


    public String getAwardType() {
        return awardType;
    }


    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }


    public String getGrantStyle() {
        return grantStyle;
    }


    public void setGrantStyle(String grantStyle) {
        this.grantStyle = grantStyle;
    }


    public Double getGrantNum() {
        return grantNum;
    }


    public void setGrantNum(Double grantNum) {
        this.grantNum = grantNum;
    }

}
