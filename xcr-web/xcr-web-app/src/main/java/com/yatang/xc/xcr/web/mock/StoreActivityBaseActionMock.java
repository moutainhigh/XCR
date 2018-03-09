package com.yatang.xc.xcr.web.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/** 
* @author (优惠券活动)gaodawei 
* @Date 2017年10月17日 下午3:10:28 
* @version 1.0.0
* @function 店铺活动相关假数据封装
*/
public class StoreActivityBaseActionMock {
	//活动的数量
	private static final int ACTNUM=5;
	
	//获得活动的相关活动列表
	public static JSONObject getEventListMockData(JSONObject jsonTemp){
		JSONObject json=new JSONObject();
		Random rand =new Random(2);
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		for (int i = 0; i < ACTNUM; i++) {
			JSONObject subRow=new JSONObject();
			subRow.put("EventID", "no0000"+(i+1));
			subRow.put("EventType", rand.nextInt(2));
			subRow.put("EventName", "国庆大派送优惠券");
			subRow.put("StartTime", "2017-10-01");
			subRow.put("EndTime", "2017-10-27");
			subRow.put("RemainDays", rand.nextInt(5)+1);
			rowsList.add(subRow);
		}
		listdata.put("rows", rowsList);
		listdata.put("pageindex",1);		
		listdata.put("pagesize",1);		
		listdata.put("totalcount",ACTNUM);		
		listdata.put("totalpage",1);
		json.put("listdata", listdata);
		return json;
	}
	
	//优惠券活动详情假数据组装
	public static JSONObject getEventCouponDatailMockData(JSONObject jsonTemp){
		Map<String, String> map=new HashMap<>();
		map.put("Balance0", "50");
		map.put("Balance1", "100");
		map.put("Balance2", "200");
		map.put("Balance3", "300");
		map.put("Balance4", "400");
		map.put("Balance5", "500");
		map.put("Balance6", "600");
		map.put("Balance7", "700");
		map.put("Balance8", "800");
		map.put("Balance9", "900");
		
		JSONObject json=new JSONObject();
		Random rand =new Random(2);
		JSONObject listdata=new JSONObject();
		JSONObject mapdata=new JSONObject();
		mapdata.put("EventID", jsonTemp.getString("EventID"));
		mapdata.put("EventName", "国庆大派送优惠券");
		mapdata.put("EventStatus", rand.nextInt(3));
		mapdata.put("StartTime", "2017-10-01");
		mapdata.put("EndTime", "2017-10-27");
		mapdata.put("ReceivedCount", "90");
		mapdata.put("TotalCount", "100");
		mapdata.put("UsedUserCount", "28");
		mapdata.put("ReceivedUserCount", "90");
		
		JSONArray rowsList = new JSONArray();
		for (int i = 0; i < ACTNUM; i++) {
			JSONObject subRow=new JSONObject();
			String key="Balance"+i;
			subRow.put("CouponBalance", map.get(key));
			subRow.put("Duration", 3);
			subRow.put("ReceivedCount", 100);
			subRow.put("UsedCount", 25);
			subRow.put("UseCondition", 100);
			rowsList.add(subRow);
		}
		listdata.put("rows", rowsList);
		listdata.put("pageindex",1);		
		listdata.put("pagesize",1);		
		listdata.put("totalcount",ACTNUM);		
		listdata.put("totalpage",1);
		json.put("mapdata", mapdata);
		json.put("listdata", listdata);
		return json;
	}

}
 