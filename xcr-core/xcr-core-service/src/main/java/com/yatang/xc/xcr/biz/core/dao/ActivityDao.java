package com.yatang.xc.xcr.biz.core.dao;

import com.yatang.xc.xcr.biz.core.domain.ActivityPO;

import java.util.Map;

/**
 * 登记参加年会信息
 * @author gaodawei
 * @Date 2017年7月25日(星期二)
 */
public interface ActivityDao{
	
	int getEnrollCount(String type);

	long insert(ActivityPO entity);

	ActivityPO getBy(Map<String, Object> paramMap);
	
}
