/**
 * 
 */ 
package com.yatang.xc.xcr.biz.core.dubboservice;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.ActivityDto;

/** 
* @author gaodawei 
* @Date 2017年7月25日 下午2:12:38 
* @version 1.0.0
* @function 
*/
public interface ActivityDubboService {
	
	/**
	 * 年会报名登记
	 * @param po
	 * @return
	 */
	Response<?> enroll(ActivityDto po,int maxCount);

	/**
	 * 通过加盟商Id判断用户是否已经登记
	 * @param userId
	 * @return
	 */
	Response<?> getInfoByUserId(String userId,String type);

	/**
	 * @param type
	 * @return
	 */
	Response<?> getEnrollCount(String type);

}
 