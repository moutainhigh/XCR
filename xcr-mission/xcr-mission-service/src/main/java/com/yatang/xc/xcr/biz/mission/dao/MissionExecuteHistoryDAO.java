package com.yatang.xc.xcr.biz.mission.dao;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteHistoryPO;

public interface MissionExecuteHistoryDAO {
    int deleteByPrimaryKey(Long id);

    //int insert(MissionExecuteHistoryPO record);

    //int insertSelective(MissionExecuteHistoryPO record);

    MissionExecuteHistoryPO selectByPrimaryKey(Long id);

    //int updateByPrimaryKeySelective(MissionExecuteHistoryPO record);

    boolean backMissionExecuteToHistory(Long id);

    //int updateByPrimaryKey(MissionExecuteHistoryPO record);
}