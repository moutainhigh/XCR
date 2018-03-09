package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 签到详情
 * Created by wangyang on 2017/7/10.
 */
public class UserSignInfoDTO implements Serializable{

    private static final long serialVersionUID = 7676606411753063180L;
    private String CurrentDate; //今日时间
    private String ContinueSignDays; //连续签到天数
    private String SignReward; //签到奖励
    private String RewardUtil; //奖励单位
    private String SignMsg; //活动说明
    private int IsCurrentDateSign; //今日是否已签到，1：签到，0：未签
    private Integer[] ContinueSignArrayDays; //当月签到日期


    public int getIsCurrentDateSign() {
        return IsCurrentDateSign;
    }

    public void setIsCurrentDateSign(int isCurrentDateSign) {
        IsCurrentDateSign = isCurrentDateSign;
    }

    public String getSignMsg() {
        return SignMsg;
    }

    public void setSignMsg(String signMsg) {
        SignMsg = signMsg;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getContinueSignDays() {
        return ContinueSignDays;
    }

    public void setContinueSignDays(String continueSignDays) {
        ContinueSignDays = continueSignDays;
    }

    public String getSignReward() {
        return SignReward;
    }

    public void setSignReward(String signReward) {
        SignReward = signReward;
    }

    public String getRewardUtil() {
        return RewardUtil;
    }

    public void setRewardUtil(String rewardUtil) {
        RewardUtil = rewardUtil;
    }

    public Integer[] getContinueSignArrayDays() {
        return ContinueSignArrayDays;
    }

    public void setContinueSignArrayDays(Integer[] continueSignArrayDays) {
        ContinueSignArrayDays = continueSignArrayDays;
    }

    @Override
    public String toString() {
        return "UserSignInfoDTO{" +
                "CurrentDate='" + CurrentDate + '\'' +
                ", ContinueSignDays='" + ContinueSignDays + '\'' +
                ", SignReward='" + SignReward + '\'' +
                ", RewardUtil='" + RewardUtil + '\'' +
                ", SignMsg='" + SignMsg + '\'' +
                ", ContinueSignArrayDays=" + Arrays.toString(ContinueSignArrayDays) +
                '}';
    }
}
