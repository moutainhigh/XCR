package com.yatang.xc.xcr.biz.mission.schedule.thread;

import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 
 * 多线程处理消息:使用适配器发送各渠道消息
 * 
 * @author : echo
 * @date : 2016年6月16日 下午3:16:33
 * @version : 2016年6月16日
 */
@Scope("prototype")
@Service("createMissionExecuteThread")
public class CreateMissionExecuteThread implements ICreateMissionExecuteThread {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private String merchantId;

	@Autowired
	private MissionExecuteFlowService service;

	public void init(String merchantId) {
		this.merchantId = merchantId;
	}

	private boolean isRuning = false;

	private Date runTime;

	@Override
	public void run() {
		try {
			isRuning = true;
			runTime = new Date();
			long time = System.currentTimeMillis();
			doRun();
			log.debug("run time=" + (System.currentTimeMillis() - time));
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			log.error(e.toString());
		} finally {
			isRuning = false;
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void doRun() {
		log.info(" schedule  therad start: " + merchantId);

		service.createMissionExecute(merchantId);
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

}
