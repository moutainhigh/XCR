package com.yatang.xc.xcr.biz.mission.service;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.domain.MissionAwardCollectPO;
import org.springframework.transaction.annotation.Transactional;

import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;

@Transactional
public interface MissionAwardService {
    /**
     * 查找指定任务的奖励
     * @param param
     * @return
     */
    public List<MissionAwardPO> queryAwardByMissionId(Long missionId);

    public List<MissionExecuteAwardPO> queryExecuteAwardByExecuteId(Long executeId);

    public boolean deleteExecuteAwardByExecuteId(Long id);

    public boolean backMissionAwardToHistory(List<MissionExecuteAwardPO> awards, Long executeHistoryId);

    public boolean deleteExecuteAwardById(Long id);

    public boolean createExecuteAward(MissionExecuteAwardPO eAward);

    /**
     * 任务奖励入库
     * @param missionAwardCollectPO
     * @return
     */
    boolean saveAward(MissionAwardCollectPO missionAwardCollectPO);

    /**
     * 获取奖励统计
     * @param shopCode
     * @return
     */
    MissionAwardCollectPO getAwardTotal(String shopCode);
}
