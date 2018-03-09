package com.yatang.xc.xcr.biz.mission.flow.engine;

import com.yatang.xc.xcr.biz.mission.dto.GrantAwardDto;

/**
 * 任务奖励发放
 * @author yangqingsong
 *
 */
public interface MissionAwardGrant {

	/**
	 * 执行奖励发放
	 * @param grantAward
	 * @return
	 * @throws 
	 */
	public boolean grant(GrantAwardDto grantAward) ;
}
