/**
 * 
 */ 
package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.domain.ActivityPO;
import com.yatang.xc.xcr.biz.core.dto.ActivityDto;
import com.yatang.xc.xcr.biz.core.dubboservice.ActivityDubboService;
import com.yatang.xc.xcr.biz.core.service.ActivityService;

/** 
* @author gaodawei 
* @Date 2017年7月25日 下午2:15:07 
* @version 1.0.0
* @function 
*/
@Service("activityDubboService")
@Transactional(propagation=Propagation.REQUIRED)
public class ActivityDubboServiceImpl implements ActivityDubboService {

	 protected final Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private ActivityService activityService;
	
	@Override
	public Response<?> enroll(ActivityDto dto,int maxCount) {
		if(dto==null){
			return new Response<>(false, "ActivityDto->传入参数为空", "100");
		}else{
			ActivityPO po=JSONObject.toJavaObject((JSON)JSON.toJSON(dto), ActivityPO.class);
			try {
				boolean flag=activityService.enroll(po,maxCount);
				return new Response<>(true,flag);
			} catch (Exception e) {
				log.error("\n*********activityService.enrolld请求参数ActivityPO,maxCount对应"+po.toString()+","+maxCount+"   \n******报错信息："+e.getMessage());
				return new Response<>(false, "服务器正忙，需要小憇一下", "100");
			}
		}
	}

	@Override
	public Response<?> getInfoByUserId(String userId,String type) {
		if(userId==null){
			return new Response<>(false, "userId->传入参数为空", "100");
		}else{
			try {
				boolean flag=activityService.getInfoByUserId(userId,type);
				return new Response<>(true,flag);
			} catch (Exception e) {
				log.error("\n*********activityService.getInfoByUserId请求参数userId="+userId+"   \n******报错信息："+e.getMessage());
				return new Response<>(false, "服务器正忙，需要小憇一下", "100");
			}
		}
	}
	
	@Override
	public Response<?> getEnrollCount(String type){
		if(type==null){
			return new Response<>(false, "type->传入参数为空", "100");
		}else{
			try {
				int count=activityService.getEnrollCount(type);
				return new Response<>(true,count);
			} catch (Exception e) {
				log.error("\n*********activityService.getEnrollCount请求参数type="+type+"   \n******报错信息："+e.getMessage());
				return new Response<>(false, "服务器正忙，需要小憇一下", "100");
			}
		}
	}
}
 