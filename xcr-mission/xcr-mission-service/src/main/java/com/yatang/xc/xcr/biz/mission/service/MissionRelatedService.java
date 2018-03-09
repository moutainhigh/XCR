package com.yatang.xc.xcr.biz.mission.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yatang.xc.xcr.biz.mission.bo.MissionRelatedQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedDetailPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedPO;

@Transactional
public interface MissionRelatedService {


    public boolean createRelatedMission(MissionRelatedPO related, List<MissionRelatedDetailPO> details);

    public boolean updateRelatedStatus(UpdateStatusQuery query);

    public List<MissionRelatedDetailPO> findRelatedDetailsByRelatedId(Long relatedId);

    public MissionRelatedPO findById(Long id);

    public boolean removeMissionRelatedById(Long id);

    public List<MissionRelatedPO> queryMissionRelated(MissionRelatedQuery query);

    public List<MissionRelatedDetailPO> findRelatedDetailsByMissionId(Long id);

    public int queryMissionRelatedCount(MissionRelatedQuery query);

    public boolean updateRelatedMission(MissionRelatedPO related, List<MissionRelatedDetailPO> details);

    public boolean checkRelatedMissionName(String missonRelatedName);
}
