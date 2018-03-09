package com.yatang.xc.xcr.biz.mission.schedule.thread;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;

import java.util.Date;
import java.util.concurrent.Callable;

public interface IAuditMissionThread  extends Callable<String> {

    public Date getRunTime();


    public boolean isRuning();


	public void init(MissionExecuteInfoPO execute);
}
