package com.yatang.xc.xcr.biz.mission.dao;

import java.util.Date;
import java.util.List;

import com.yatang.xc.xcr.biz.mission.bo.MissionByCourseIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionInfoQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.UserSignPO;

public interface MissionInfoDAO {

    //int deleteByPrimaryKey(Long id);


    int insert(MissionInfoPO record);


    // int insertSelective(MissionInfoPO record);

    MissionInfoPO selectByPrimaryKey(Long id);


    int updateByPrimaryKeySelective(MissionInfoPO record);

    // int updateByPrimaryKeyWithBLOBs(MissionInfoPO record);


    // int updateByPrimaryKey(MissionInfoPO record);

    List<MissionInfoPO> queryMissionInfo(MissionInfoQuery query);


    int removeMissionById(Long id);


    int queryMissionInfoCount(MissionInfoQuery query);


    int updateStatus(UpdateStatusQuery query);


    boolean updateSort(MissionInfoPO m);


    List<MissionInfoPO> queryMissionByCourseId(MissionByCourseIdQuery query);


    int countMissionByName(String name);

    /**
     * 获取有效期内连续签到天数
     *
     * @param shopCode
     * @param validStartDay
     * @param validEndDay
     * @return
     */
    Integer getSignCount(String shopCode, Date validStartDay, Date validEndDay);

    /**
     * 备份签到数据
     */
    Integer backupsSignHistory();

    /**
     * 删除已备份数据
     */
    void deleteSignHistoryBackups();

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