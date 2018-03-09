/**
 * 
 */ 
package com.yatang.xc.xcr.biz.core.service.impl;

import com.yatang.xc.xcr.biz.core.dao.ActivityDao;
import com.yatang.xc.xcr.biz.core.domain.ActivityPO;
import com.yatang.xc.xcr.biz.core.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/** 
* @author gaodawei 
* @Date 2017年7月25日 下午1:55:23 
* @version 1.0.0
* @function 年会报名信息登记
*/
@Service
public class ActivityServiceImpl implements ActivityService {
	
	@Autowired
    private ActivityDao activityDao;

    @Override
    public boolean enroll(ActivityPO po,int maxCount) {
    	synchronized (activityDao) {
    		int currentCount=getEnrollCount(po.getType());
    		if(maxCount<0 || (currentCount>0 && currentCount<maxCount)){
    			long id = activityDao.insert(po);
                return id > 0;
    		}else{
    			return false;
    		}
		}
    }
    
    @Override
    public boolean getInfoByUserId(String userId,String type){
    	Map<String, Object> map=new HashMap<>();
		map.put("userId", userId);
		map.put("type", type);
    	ActivityPO po=activityDao.getBy(map);
    	return po==null?false:true;
    }
    
    @Override
    public int getEnrollCount(String type){
    	int count=activityDao.getEnrollCount(type);
    	return count;
    }

}
 