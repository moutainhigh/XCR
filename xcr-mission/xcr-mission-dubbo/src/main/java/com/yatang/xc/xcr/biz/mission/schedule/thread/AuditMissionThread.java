package com.yatang.xc.xcr.biz.mission.schedule.thread;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 多线程计算任务
 *
 * @author : echo
 * @version : 2016年6月16日
 * @date : 2016年6月16日 下午3:16:33
 */
@Scope("prototype")
@Service("auditMissionThread")
public class AuditMissionThread implements IAuditMissionThread {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private boolean isRuning = false;

    private Date runTime;

    private MissionExecuteInfoPO execute;

    @Autowired
    private MissionExecuteFlowService executeFlowService;

    @Override
    public void init(MissionExecuteInfoPO execute) {
        this.execute = execute;
    }

    @Override
    public String call() throws Exception {
        String result = null;
        try {
            isRuning = true;
            runTime = new Date();
            long time = System.currentTimeMillis();
            result = run();
            log.info("------AuditMissionThread run time:" + (System.currentTimeMillis() - time) + " execute:" + execute.getId());
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        } finally {
            isRuning = false;
        }
        return result;
    }

    private String run() {
        if (executeFlowService.calculateMission(execute)) {
            return String.valueOf(execute.getId());
        }
        return null;
    }

    public boolean isRuning() {
        return isRuning;
    }

    public void setRuning(boolean isRuning) {
        this.isRuning = isRuning;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public MissionExecuteInfoPO getExecute() {
        return execute;
    }

    public void setExecute(MissionExecuteInfoPO execute) {
        this.execute = execute;
    }

}
