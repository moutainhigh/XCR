package com.yatang.xc.xcr.biz.mission.schedule;

import com.busi.common.resp.Response;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionExecuteQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionTemplate;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 任务自动完成
 * Created by wangyang on 2017/12/29.
 */
@Component
@JobHander(value = "autoMissionCompletionJobHandler")
public class AutoMissionCompletionJobHandler extends IJobHandler {

    private static final int pageSize = 1000;

    @Autowired
    private MissionExecuteFlowService missionExecuteFlowService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private MissionExecuteDubboService missionExecuteDubboService;
    @Autowired
    private MissionExecuteService missionExecuteService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        XxlJobLogger.log("AutoMissionCompletionJobHandler execute start ... " + DateUtil.formatDateDefaule(new Date()));
        try {
            Set<String> merchantIds = new HashSet<>(missionService.queryMerchantIdFromUserHistory());
            Map<String, Object> map = new HashMap<>();
            map.put("pageCount", "true");
            Response<Integer> result = missionExecuteFlowService.getOrganizationService().queryStoreCount(map);
            //得到门店数量
            Integer count = result.getResultObject();
            if (count != null && count > 0) {
                int pageCount = (count / pageSize);
                for (int i = 0; i <= pageCount; i++) {
                    //查询门店信息列表
                    List<StoreDto> list = missionExecuteFlowService.getOrganizationService().queryPageStores(i + 1, pageSize, map).getResultObject();
                    if (list != null && !list.isEmpty()) {
                        //遍历门店，每个门店判断是否完成,如果完成直接领取奖励,并修改任务状态
                        for (StoreDto dto : list) {
                            if (merchantIds.contains(dto.getId())) {
                                missionCompletionByMerchant(dto.getId());
                            }
                        }
                    }
                }
            }
            //失效超时任务
            missionExecuteService.expireMissionExecutesForDayMission(EnumMissionExecuteStatus.STATUS_INVALID.getCode());
        } catch (Exception e) {
            XxlJobLogger.log("AutoMissionCompletionJobHandler execute error:" + e.getMessage());
        }
        XxlJobLogger.log("AutoMissionCompletionJobHandler execute end ... " + DateUtil.formatDateDefaule(new Date()));
        return ReturnT.SUCCESS;
    }

    /**
     * 任务自动完成
     *
     * @param id
     */
    private void missionCompletionByMerchant(String id) {
        XxlJobLogger.log("AutoMissionCompletionJobHandler missionCompletionByMerchant() id" + id);
        MissionExecuteQueryDto queryDto = new MissionExecuteQueryDto();
        queryDto.setMerchantId(id);
        missionExecuteDubboService.autoAuditMissionByMerchantIdWithoutCache(id);
        Response<List<ViewMissionExecuteDto>> res = missionExecuteDubboService.queryMissionExecute(queryDto);
        if (res != null && res.isSuccess()) {
            for (ViewMissionExecuteDto missionData : res.getResultObject()) {
                String missionTemplateCode = missionData.getTemplateCode();
                //如果任务完成，直接领取奖励
                if (missionData.getStatus().equals(EnumMissionExecuteStatus.STATUS_FINISHED.getCode())) {
                    if (missionTemplateCode.equals(EnumMissionTemplate.MONTH_FACE_PAY.getCode())
                            || missionTemplateCode.equals(EnumMissionTemplate.DAY_FACE_PAY.getCode())
                            || missionTemplateCode.equals(EnumMissionTemplate.MONTH_BUY_AMOUNT.getCode())) {
                        //领取奖励
                        missionReceiveReward(missionData.getMerchantId(), String.valueOf(missionData.getId()));
                        XxlJobLogger.log("AutoMissionCompletionJobHandler missionCompletionByMerchant() missionReceiveReward() merchantId" + missionData.getMerchantId() + "  taskId:" + missionData.getId());
                    }
                }
            }
        }
    }

    /**
     * 自动领取奖励
     *
     * @param merchantId
     * @param taskId
     */
    private boolean missionReceiveReward(String merchantId, String taskId) {
        Response<List<AwardInfoDto>> listResponse = missionExecuteDubboService.goAndGetAward(taskId, merchantId);
        return listResponse != null && listResponse.isSuccess();
    }
}
