package com.yatang.xc.xcr.web.mock;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.StringUtils;

/**
 * 数据统计假数据包
 * @author gaodawei
 * 2017年6月30日(星期五)
 */
public class DataStatisticsActionMockData {

	//listdate
	private static final Map<String, String> map=new HashMap<>();
	static{
		map.put("name0", "薯片");
		map.put("name1", "牛奶");
		map.put("name2", "中华烟");
		map.put("name3", "茅台酒");
		map.put("name4", "可乐");
		map.put("name5", "苹果");
		map.put("name6", "香蕉");
		map.put("name7", "手套");
		map.put("name8", "铅笔");
		map.put("name9", "毛笔");
		map.put("name10", "墨水");
		map.put("name11", "篮球");
		map.put("name12", "果冻");
		map.put("name13", "灯笼");
		map.put("name14", "薯片");
		map.put("name15", "毛巾");
		map.put("name16", "红酒");
		map.put("name17", "被子");
		map.put("name18", "杯子");
		map.put("name19", "小灯");
		map.put("name20", "玩具枪");
		map.put("name21", "抹布");
	}
	/**
	 * 统计列表v2.0假数据
	 * @param jsonTemp
	 * @return
	 */
	public static JSONObject getStatisticsListMockData(JSONObject jsonTemp){
		JSONObject mapdata=new JSONObject();
		JSONArray rowsList01 = new JSONArray();
		JSONArray rowsList02 = new JSONArray();
		Map<String, Object> MerchandiseSales_Map=new HashMap<>();
		Map<String, Object> MerchandiseProfits_Map=new HashMap<>();
		JSONObject subJson = new JSONObject();
		for (int i = 0; i < 5; i++) {
			MerchandiseSales_Map.put("GoodsName", map.get("name"+i));
			MerchandiseSales_Map.put("GoodsCode", 21070701+""+i);
			MerchandiseSales_Map.put("GoodsSaleVaule", (50/(i+1))*80);
			subJson=StringUtils.replcNULLToStr(MerchandiseSales_Map);
			rowsList01.add(subJson);
		}
		for (int i = 5; i < 10; i++) {
			MerchandiseProfits_Map.put("GoodsName", map.get("name"+i));
			MerchandiseProfits_Map.put("GoodsCode", 21070701+""+i);
			MerchandiseProfits_Map.put("GoodsProfitsVaule", (10/(i+1))*500+".00");
			subJson=StringUtils.replcNULLToStr(MerchandiseProfits_Map);
			rowsList02.add(subJson);
		}
		mapdata.put("MerchandiseSales", rowsList01);
		mapdata.put("MerchandiseProfits", rowsList02);
		
		JSONObject json = new JSONObject();
		json.put("mapdata", mapdata);
		return json;
	}
	/**
	 * 统计详情列表v2.0假数据
	 * @param jsonTemp
	 * @return
	 */
	public static JSONObject getStatisticsDetialListMockData(JSONObject jsonTemp){
		JSONObject listdata=new JSONObject();
		JSONArray rowsList01 = new JSONArray();
		Map<String, Object> Map=new HashMap<>();
		JSONObject subJson = new JSONObject();
		int start=(jsonTemp.getIntValue("PageIndex")-1)*jsonTemp.getIntValue("PageSize");
		int end=jsonTemp.getIntValue("PageIndex")*jsonTemp.getIntValue("PageSize");
		int tempVal=0;
		tempVal=(map.size()>end)?end:map.size();
		for (int i = start; i < tempVal; i++) {
			Map.put("GoodsName", map.get("name"+i));
			Map.put("GoodsCode", 21070701+""+i);
			if(jsonTemp.getIntValue("Sort")==0){
				Map.put("GoodsVaule", (100/(i+1))*50);
			}else if(jsonTemp.getIntValue("Sort")==1){
				Map.put("GoodsVaule", (i+1)*50);
			}
			subJson=StringUtils.replcNULLToStr(Map);
			rowsList01.add(subJson);
		}
		listdata.put("rows", rowsList01);
		listdata=CommonUtil.pagePackage(listdata, jsonTemp, map.size(), null);
		JSONObject json = new JSONObject();
		json.put("listdata", listdata);
		return json;
	}

	public static void main(String[] args) {
		JSONObject jsonTemp=new JSONObject();
		System.err.println(getStatisticsDetialListMockData(jsonTemp));
	}
}
