package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.domain.MissionAwardCollectPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;

public interface MissionAwardDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionAwardPO record);

    int insertSelective(MissionAwardPO record);

    MissionAwardPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionAwardPO record);

    //int updateByPrimaryKey(MissionAwardPO record);

    List<MissionAwardPO> queryByMissionId(Long missionId);

    MissionAwardCollectPO getAwardTotal(String shopCode);
}