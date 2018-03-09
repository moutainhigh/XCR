package com.yatang.xc.xcr.biz.mission.schedule;

import com.busi.common.resp.Response;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.bo.MissionInfoQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionStatus;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.schedule.thread.ICreateMissionExecuteThread;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 定时给用户创建任务
 * Created by wangyang on 2017/12/29.
 */
@Component
@JobHander(value = "autoCreateMissionExecuteJobHandler")
public class AutoCreateMissionExecuteJobHandler extends IJobHandler {

    @Resource(name = "createMissionExecuteTaskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private MissionExecuteFlowService missionExecuteFlowService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionExecuteService missionExecuteService;

    private static final int pageSize = 1000;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        XxlJobLogger.log("AutoCreateMissionExecuteJobHandler  execute start ..." + DateUtil.formatDateDefaule(new Date()));
        missionExecuteService.removeMisiionExecuteByStatus(EnumMissionExecuteStatus.STATUS_END.getCode());
        missionExecuteService.invalidExecuteMissionHasDelted(EnumMissionExecuteStatus.STATUS_INVALID.getCode());
        try {
            MissionInfoQuery query = new MissionInfoQuery();
            query.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
            List<MissionInfoPO> missionInofs = missionService.queryMissionInfo(query);
            if (missionInofs == null || missionInofs.isEmpty()) {
                XxlJobLogger.log("AutoCreateMissionExecuteJobHandler  execute missionInofs == null || missionInofs.isEmpty() !!!");
                return ReturnT.SUCCESS;
            }
            Set<String> merchantIds = new HashSet<>(missionService.queryMerchantIdFromUserHistory());

            Map<String, Object> map = new HashMap<>();
            map.put("pageCount", "true");
            Response<Integer> result = missionExecuteFlowService.getOrganizationService().queryStoreCount(map);
            Integer count = result.getResultObject();
            if (count != null && count > 0) {
                int pageCount = count / pageSize;
                for (int i = 0; i <= pageCount; i++) {
                    List<StoreDto> list = missionExecuteFlowService.getOrganizationService()
                            .queryPageStores(i + 1, pageSize, map).getResultObject();
                    if (list != null && !list.isEmpty()) {
                        for (StoreDto dto : list) {
                            if (merchantIds.contains(dto.getId())) {
                                //创建普通任务
                                createMissionByMerchant(dto.getId());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            XxlJobLogger.log("AutoCreateMissionExecuteJobHandler  execute error:" + e.getMessage());
        }
        XxlJobLogger.log("AutoCreateMissionExecuteJobHandler  execute end ..." + DateUtil.formatDateDefaule(new Date()));
        return ReturnT.SUCCESS;
    }

    private void createMissionByMerchant(String merchantId) {
        if (taskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity() <= 0) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                XxlJobLogger.log("AutoCreateMissionExecuteJobHandler createMissionByMerchant errorMessage:" + e.getMessage());
            }
        }
        ICreateMissionExecuteThread thread = missionExecuteFlowService.initCreateMissionExecuteThread(merchantId);
        taskExecutor.execute(thread);
    }
}
