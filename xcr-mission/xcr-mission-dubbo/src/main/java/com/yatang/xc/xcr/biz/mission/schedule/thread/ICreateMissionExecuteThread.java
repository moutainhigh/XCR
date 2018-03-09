package com.yatang.xc.xcr.biz.mission.schedule.thread;

import java.util.Date;

public interface ICreateMissionExecuteThread extends Runnable {

    public void init(String merchantId);

    public void doRun() ;
    public Date getRunTime();
    
   
    public boolean isRuning();
}
