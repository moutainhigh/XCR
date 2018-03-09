package com.yatang.xc.xcr.biz.mission.dao;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteHistoryAwardPO;

public interface MissionExecuteHistoryAwardDAO {
    int deleteByPrimaryKey(Long id);

    //int insert(MissionExecuteHistoryAwardPO record);

    //int insertSelective(MissionExecuteHistoryAwardPO record);

    MissionExecuteHistoryAwardPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionExecuteHistoryAwardPO record);

    int backHistoryAward(Long id);
    
    //int updateByPrimaryKey(MissionExecuteHistoryAwardPO record);
}