package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.bo.MissionRelatedQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedDetailPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedPO;

public interface MissionRelatedDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionRelatedPO record);

    //int insertSelective(MissionRelatedPO record);

    MissionRelatedPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionRelatedPO record);

    //int updateByPrimaryKey(MissionRelatedPO record);

  

    int removeMissionRelatedById(Long id);

    List<MissionRelatedPO> queryMissionRelated(MissionRelatedQuery query);

    int queryMissionRelatedCount(MissionRelatedQuery query);

    int updateStatus(UpdateStatusQuery query);

    int countMissionByName(String name);
}