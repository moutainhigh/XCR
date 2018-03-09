package com.yatang.xc.xcr.biz.mission.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusAndBpmIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionOfflineExistsListPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;

/**
 * 任务查找器
 *
 * @author yangqingsong
 */
@Transactional
public interface MissionExecuteService {


    /**
     * 查找指定门店的任务
     *
     * @param param
     * @return
     */
    public List<MissionExecuteInfoPO> findExecutableMissions(String merchantId);


    /**
     * 根据id查找
     *
     * @param id
     * @return
     * @throws
     */
    public MissionExecuteInfoPO findExecuteMissionById(String id);


    public List<MissionExecuteInfoPO> queryMissionExecute(MissionExecuteQuery query);


    public boolean updateMissionExecuteStatus(UpdateStatusQuery query);


    public List<MissionExecuteInfoPO> queryExecuteMissionByMissionIdAndMerchantId(Long missonInfoId, String merchantId);


    public List<MissionExecuteInfoPO> queryExecuteMissionByMissionIdAndMerchantIdInHistory(Long missonInfoId, String merchantId);


    public int queryMissionExecuteCount(MissionExecuteQuery query);


    public boolean deleteExecuteById(Long id);


    public MissionExecuteInfoPO findExecuteMissionByMerchantIdAndCourseId(String merchantId, String courseId, String status);


    MissionExecuteInfoPO createMissionEexecute(String merchantId, MissionInfoPO missionInof, MissionTemplatePO template, String initStatus);


    public boolean backMissionExecuteToHistory(Long executeId);

    public boolean removeMissionExecuteByMissionId(Long missionInfoId);

    public boolean saveAttachment(MissionAttachmentPO a);


    public boolean backAttachmentToHistory(Long executeId, String type);


    public List<MissionOfflineExistsListPO> queryOfflineList(String merchantId, String templateCode);


    public boolean updateMissionExecuteStatusAndBpmId(UpdateStatusAndBpmIdQuery query);


    public boolean expireMissionExecutes(String status);

    public boolean expireMissionExecutesForDayMission(String status);


    public List<MissionExecuteInfoPO> queryMissionExecuteInHistory(MissionExecuteQuery query);


    public int queryMissionExecuteCountInHistory(MissionExecuteQuery query);


    public List<MissionExecuteInfoPO> queryMissionExecuteOrderByRelated(MissionExecuteQuery query);


    boolean removeMisiionExecuteByStatus(String status);


    public int invalidExecuteMissionHasDelted(String code);


    MissionExecuteInfoPO findExecuteMissionByMerchantIdAndCourseIdInHistory(String merchantId, String courseId,
                                                                            String status);


}
