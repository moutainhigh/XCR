package com.yatang.xc.xcr.biz.mission.schedule;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动标识过期的可执行任务
 * Created by wangyang on 2017/12/29.
 */
@Component
@JobHander(value = "autoExpireMissionExecuteJobHandler")
public class AutoExpireMissionExecuteJobHandler extends IJobHandler {

    @Autowired
    private MissionExecuteService missionExecuteService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        XxlJobLogger.log("AutoExpireMissionExecuteJobHandler execute start ... " + DateUtil.formatDateDefaule(new Date()));
        boolean success = missionExecuteService.expireMissionExecutes(EnumMissionExecuteStatus.STATUS_INVALID.getCode());
        XxlJobLogger.log("AutoExpireMissionExecuteJobHandler  execute 自动标识过期的可执行任务 结果 success:" + success);
        XxlJobLogger.log("AutoExpireMissionExecuteJobHandler execute end ... " + DateUtil.formatDateDefaule(new Date()));
        return ReturnT.SUCCESS;
    }
}
