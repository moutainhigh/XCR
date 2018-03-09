package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;

public interface MissionAttachmentDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionAttachmentPO record);

    //int insertSelective(MissionAttachmentPO record);

    MissionAttachmentPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionAttachmentPO record);

    int updateByPrimaryKey(MissionAttachmentPO record);

    List<MissionAttachmentPO> queryAttachmentByMissionId(Long id);

    List<MissionAttachmentPO> queryAttachmentByExecuteId(Long id);

    int insertToHistory(Long id);

    int backExpireAttachment(String status);
    
    int deleteExpireAttachment(String status);

    List<MissionAttachmentPO> queryAttachmentHistoryByExecuteId(Long id);
}