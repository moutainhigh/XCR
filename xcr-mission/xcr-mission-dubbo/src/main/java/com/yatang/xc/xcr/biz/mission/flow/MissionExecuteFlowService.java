package com.yatang.xc.xcr.biz.mission.flow;

import com.busi.common.exception.BusinessException;
import com.yatang.xc.mbd.biz.org.dto.FranchiseeDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.schedule.thread.IAuditMissionThread;
import com.yatang.xc.xcr.biz.mission.schedule.thread.ICreateMissionExecuteThread;
import com.yatang.xc.xcr.biz.mission.service.RedisService;

import java.util.List;

public interface MissionExecuteFlowService {

    boolean rejectMissionAudit(String executeId);


    ICreateMissionExecuteThread initCreateMissionExecuteThread(String merchantId);


    boolean backMissionExecuteToHistory(Long executeId, List<MissionExecuteAwardPO> awards);


    ViewMissionExecuteDto buildViewExecute(MissionExecuteInfoPO execute);


    void startBpmProcess(Long executeId, String templateCode, String companyId, String merchantId,String shopName);


    OrganizationService getOrganizationService();


    boolean createMissionExecute(String merchantId);

    boolean createDayMissionExecute(String merchantId);


    void updateExecuteToFinishedOrEnd(MissionExecuteInfoPO execute, String reason, String[] oldStatus);


    boolean sendAppMessage(String merchantId, String content);


    boolean checkOrderCountFromDataCenter(String merchantId);


    RedisService getRedisService();
    
    
	IAuditMissionThread initIAuditMissionThread(MissionExecuteInfoPO execute);


	boolean calculateMission(MissionExecuteInfoPO execute) throws BusinessException;


	String getLoginByMerchantId(String merchantId);


	FranchiseeDto getFranchiseeByMerchantId(String merchantId);


}
