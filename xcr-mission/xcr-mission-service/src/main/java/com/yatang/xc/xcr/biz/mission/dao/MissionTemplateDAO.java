package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;

public interface MissionTemplateDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionTemplatePO record);

    //int insertSelective(MissionTemplatePO record);

    MissionTemplatePO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionTemplatePO record);

    //int updateByPrimaryKeyWithBLOBs(MissionTemplatePO record);

    //int updateByPrimaryKey(MissionTemplatePO record);

    List<MissionTemplatePO> queryMissionTemplateByMissionType(String missionType);

    MissionTemplatePO selectTemplateByCode(String code);
}