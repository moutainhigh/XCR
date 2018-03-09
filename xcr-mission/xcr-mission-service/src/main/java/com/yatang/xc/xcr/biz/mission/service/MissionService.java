package com.yatang.xc.xcr.biz.mission.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.yatang.xc.xcr.biz.mission.bo.MissionInfoQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionBuyListPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;
import com.yatang.xc.xcr.biz.mission.domain.MissionUserHistoryPO;
import com.yatang.xc.xcr.biz.mission.domain.UserSignPO;

@Transactional
public interface MissionService {

    /**
     * 获取任务对象
     *
     * @param missionId
     * @return
     */
    public MissionInfoPO findById(Long missionId);


    /**
     * 获取任务对象
     *
     * @param missionId
     * @return
     */
    public boolean createMissionInfo(MissionInfoPO missionInfo, List<MissionAwardPO> awards, List<MissionAttachmentPO> attachments);


    public MissionTemplatePO selectTemplateByCode(String code);


    public boolean updateMissionInfoStatus(UpdateStatusQuery query);


    public List<MissionTemplatePO> queryMissionTemplateByMissionType(String type);


    public boolean removeMissionById(Long id);


    public List<MissionInfoPO> queryMissionInfo(MissionInfoQuery query);


    public int queryMissionInfoCount(MissionInfoQuery query);


    public boolean updateMissionInfo(MissionInfoPO mission, List<MissionAwardPO> awards, List<MissionAttachmentPO> attachements);


    public boolean updateMissionSort(MissionInfoPO m1);


    public List<MissionAttachmentPO> queryAttachmentByMissionId(Long id);


    public List<MissionAttachmentPO> queryAttachmentByExecuteId(Long id);


    public List<MissionBuyListPO> queryBuyListBy(String login, Long missionId, String merchantId);


    public boolean updateBuyList(MissionBuyListPO buyList);


    public boolean createBuyList(MissionBuyListPO buyList);


    public MissionUserHistoryPO selectUserHistoryByMerchantId(String merchantId);


    public boolean insertUserHistory(MissionUserHistoryPO user);


    List<MissionInfoPO> queryMissionByCourseId(String courseId, String status);


    boolean updateUserHistory(MissionUserHistoryPO user, Date now);


    public Set<String> queryMerchantIdFromUserHistory();


    public boolean checkMissionName(String name);


    public List<MissionAttachmentPO> queryAttachmentHistoryByExecuteId(Long valueOf);

    /**
     * 校验用户连续签到任务是否完成
     *
     * @param shopCode
     * @param validStartDay
     * @param validEndDay
     * @return
     */
    int checkSignFinish(String shopCode, Date validStartDay, Date validEndDay);

    /**
     * 备份签到记录
     */
    void backupsSignHistory();

    /**
     * 获取签到门店编号
     *
     * @return
     */
    List<String> getSignShopCodeList();

    List<UserSignPO> getSignListByShopCode(String shopCode);

    /**
     * 根据Id更新连续签到次数
     * @param id
     */
    void updateContinueDayById(long id,int continueDay);
}
