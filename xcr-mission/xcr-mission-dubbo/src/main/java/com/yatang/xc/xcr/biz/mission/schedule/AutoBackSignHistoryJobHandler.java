package com.yatang.xc.xcr.biz.mission.schedule;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 签到备份定时任务
 * 定时备份3个月前的签到信息到备份表
 * Created by wangyang on 2017/12/29.
 */
@Component
@JobHander(value = "autoBackSignHistoryJobHandler")
public class AutoBackSignHistoryJobHandler extends IJobHandler {

    @Autowired
    private MissionService missionService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        XxlJobLogger.log("AutoBackSignHistoryJobHandler  execute start..." + DateUtil.formatDateDefaule(new Date()));
        missionService.backupsSignHistory();
        XxlJobLogger.log("AutoBackSignHistoryJobHandler  execute end..." + DateUtil.formatDateDefaule(new Date()));
        return ReturnT.SUCCESS;
    }
}
