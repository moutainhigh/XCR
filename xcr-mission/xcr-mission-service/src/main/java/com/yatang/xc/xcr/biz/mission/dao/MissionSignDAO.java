package com.yatang.xc.xcr.biz.mission.dao;

import com.yatang.xc.xcr.biz.mission.domain.UserSignPO;

import java.util.Date;
import java.util.List;

public interface MissionSignDAO {

    /**
     * 获取指定时间段用户签到集合
     * <method description>
     *
     * @param shopCode
     * @param validStartDay
     * @param validEndDay
     * @return
     */
    List<UserSignPO> getSignPo(String shopCode, Date validStartDay, Date validEndDay);

}