package com.yatang.xc.xcr.service;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.model.ResultMap;

/** 
* @author gaodawei 
* @Date 2018年1月5日 下午4:33:07 
* @version 1.0.0
* @function 
*/
public interface BankCardService {
	
	public JSONObject setBankCard(JSONObject jsonTemp,JSONObject stateJson) throws Exception;

}
 