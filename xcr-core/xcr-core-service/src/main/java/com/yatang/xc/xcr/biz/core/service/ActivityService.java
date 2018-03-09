package com.yatang.xc.xcr.biz.core.service;

import com.yatang.xc.xcr.biz.core.domain.ActivityPO;

/**
 * @描述: 年会报名相关数据处理接口.
 * @作者: gaodawei
 * @创建时间: 2017年7月25日(星期二),13:39:52.
 * @版本: 1.0.
 */
public interface ActivityService {

	/**
	 * 年会报名登记
	 * @param po
	 * @return
	 */
	boolean enroll(ActivityPO po,int maxCount);

	/**
	 * 通过加盟商Id判断用户是否已经登记
	 * @param userId
	 * @return
	 */
	boolean getInfoByUserId(String userId,String type);

	/**
	 * 获得总共报名人数
	 * @return
	 */
	int getEnrollCount(String type);
	
}
