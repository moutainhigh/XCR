package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.bo.ExecuteByMerchantIdAndCourseIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.ExecuteByMissionIdAndMerchantIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.ExpireQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusAndBpmIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;

public interface MissionExecuteInfoDAO {

    int deleteByPrimaryKey(Long id);


    int insert(MissionExecuteInfoPO record);


    // int insertSelective(MissionExecuteInfoPO record);

    MissionExecuteInfoPO selectByPrimaryKey(Long id);


    int updateByPrimaryKeySelective(MissionExecuteInfoPO record);


    // int updateByPrimaryKey(MissionExecuteInfoPO record);

    List<MissionExecuteInfoPO> selectByMerchantId(String merchantId);


    List<MissionExecuteInfoPO> queryMissionExecute(MissionExecuteQuery query);


    int updateStatus(UpdateStatusQuery query);


    List<MissionExecuteInfoPO> queryExecuteMissionByMissionIdAndMerchantId(ExecuteByMissionIdAndMerchantIdQuery query);


    int queryExecuteMissionByMissionIdAndMerchantIdCount(MissionExecuteQuery query);


    List<MissionExecuteInfoPO> queryExecuteMissionByMerchantIdAndCourseId(ExecuteByMerchantIdAndCourseIdQuery query);


    int queryMissionExecuteCount(MissionExecuteQuery query);


    List<MissionExecuteInfoPO> queryExecuteMissionByMissionIdAndMerchantIdInHistory(ExecuteByMissionIdAndMerchantIdQuery query);


    int updateStatusAndBpmId(UpdateStatusAndBpmIdQuery query);


    int expireMissionExecutes(ExpireQuery query);

    int expireMissionDayExecutes(ExpireQuery query);

    void removeExpireMisiionExecute(String string);


    boolean backExpireMissionExecuteToHistory(String string);


    boolean removeByPrimaryKey(Long id);

    boolean removeByMissionInfoId(Long id);


    void deleteMissionExecuteByIsDeleted();


    List<MissionExecuteInfoPO> queryMissionExecuteInHistory(MissionExecuteQuery query);


    int queryMissionExecuteCountInHistory(MissionExecuteQuery query);


    List<MissionExecuteInfoPO> queryMissionExecuteOrderByRelated(MissionExecuteQuery query);


    int invalidExecuteMissionHasDeleted(String status);


    int expireNotExistMissionExecutes(ExpireQuery query);


    List<MissionExecuteInfoPO> queryExecuteMissionByMerchantIdAndCourseIdInHistory(
            ExecuteByMerchantIdAndCourseIdQuery query);

}