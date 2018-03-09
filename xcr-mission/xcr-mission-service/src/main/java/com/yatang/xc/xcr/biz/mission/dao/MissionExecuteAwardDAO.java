package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.domain.MissionAwardCollectPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;

public interface MissionExecuteAwardDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionExecuteAwardPO record);

    //int insertSelective(MissionExecuteAwardPO record);

    MissionExecuteAwardPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionExecuteAwardPO record);

    //int updateByPrimaryKey(MissionExecuteAwardPO record);

    List<MissionExecuteAwardPO> queryExecuteAwardByExecuteId(Long id);

    void backExpireAward(String status);

    void deleteExpireAward(String status);

    void saveAward(MissionAwardCollectPO missionAwardCollectPO);

    MissionAwardCollectPO getAwardTotal(String shopCode);
}