package com.yatang.xc.xcr.web.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.util.StringUtils;

public class SignInMngActionMockData {
	
	public static JSONObject getSignMockData(JSONObject mapdata){
		Map<String, Object> subJson_Map = new HashMap<>();
		subJson_Map.put("SignResultMsg", "恭喜获得1元现金奖励");
		mapdata = StringUtils.replcNULLToStr(subJson_Map);
		return mapdata;
	}
	
	
	public static JSONObject getSignDetailData(JSONObject mapdata){
		Map<String, Object> subJson_Map = new HashMap<>();
		JSONArray jsonArr=new JSONArray();
		jsonArr.add(1);
		jsonArr.add(3);
		jsonArr.add(4);
		jsonArr.add(5);
		jsonArr.add(7);
		subJson_Map.put("CurrentDate", new Date().getTime());
		subJson_Map.put("ContinueSignDays", 5);
		subJson_Map.put("SignReward", 93);
		subJson_Map.put("RewardUnit", "元");
		subJson_Map.put("SignMsg", "给自己一个坚持的理由，像坚持运营小超一样，坚持连续签到，每天获得一份收获，每个阶段获得一份惊喜");
		subJson_Map.put("ContinueSignArrayDays", jsonArr);
		mapdata = StringUtils.replcNULLToStr(subJson_Map);
		return mapdata;
	}

}
