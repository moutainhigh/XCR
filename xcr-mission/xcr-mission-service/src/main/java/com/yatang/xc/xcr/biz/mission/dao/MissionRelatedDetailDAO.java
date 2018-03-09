package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedDetailPO;

public interface MissionRelatedDetailDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionRelatedDetailPO record);

    //int insertSelective(MissionRelatedDetailPO record);

    MissionRelatedDetailPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionRelatedDetailPO record);

    //int updateByPrimaryKey(MissionRelatedDetailPO record);

    List<MissionRelatedDetailPO> findRelatedDetailsByMissionId(Long id);
    
    List<MissionRelatedDetailPO> findRelatedDetailsByRelatedId(Long relatedId);
}