package com.yatang.xc.xcr.web.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * @author gaodawei
 * @Date   2017年5月17日(星期三)
 * @function 交易流水相关功能假数据管理
 */
public class TransactionFlowMockDataUtil {
	/**
	 * 小票
	 * @return
	 */
	public static JSONObject ticketListFakeData(){
		
		JSONObject fakeJson=new JSONObject();
		//mapdata
		JSONObject mapdata=new JSONObject();
		mapdata.put("TicketDate", "2017-05-17");
		
		//listdate
		Random rand =new Random(3);
		JSONObject listdate=new JSONObject();
		JSONArray rowsList = new JSONArray();
		double sum=0;
		for (int i = 0; i < 8; i++) {
			JSONObject json=new JSONObject();
			json.put("TicketNo", 0517+i);
			json.put("TicketType", rand.nextInt(2)+1);
			json.put("Time", 4*rand.nextInt(4)+":"+19*(rand.nextInt(3)+1)+":"+19*(rand.nextInt(3)+1));
			json.put("TicketAccount", rand.nextInt(500));
			sum+=json.getDoubleValue("TicketAccount");
			rowsList.add(json);
		}
		listdate.put("rows", rowsList);
		listdate.put("pageindex",1);		
		listdate.put("pagesize",1);		
		listdate.put("totalcount",8);		
		listdate.put("totalpage",1);	
		
		fakeJson.put("listdata", listdate);
		mapdata.put("TransactionAllValue", sum);
		fakeJson.put("mapdata", mapdata);
		
		return fakeJson;
	}
	
	/**
	 * 小票详情
	 * @return
	 */
	public static JSONObject ticketDetialFakeData(){
		
		JSONObject fakeJson=new JSONObject();
		//mapdata
		JSONObject mardata=new JSONObject();
		mardata.put("Time", "12:23:46");
		mardata.put("CashegisterNo", "MNO00020");
		mardata.put("CashierStaffNo", "YT00002");
		mardata.put("TicketAccount", 480.00);
		mardata.put("TicketType", 0);
		mardata.put("PostingSign", 1);
		mardata.put("GoodAllValue", 523.00);
		mardata.put("ReceivableValue", 480.00);
		mardata.put("PaidUpValue", 500.00);
		mardata.put("ChangeValue", 20.00);
		mardata.put("PayType", "电子现金");
		mardata.put("CouponValue", 0.00);
		mardata.put("YatangAssessedValue", 48.00);
		mardata.put("BusinessAssessedValue", 432.00);
		mardata.put("AllDiscountValue", 40.00);
		mardata.put("AllReducteValue", 3.00);
		mardata.put("ProfitLossValue", 0.00);
		
		//listdate
		Map<String, String> map=new HashMap<>();
		map.put("name0", "薯片");
		map.put("name1", "牛奶");
		map.put("name2", "中华烟");
		map.put("name3", "茅台酒");
		Map<String, Double> map2=new HashMap<>();
		map2.put("unit0", 5.00);
		map2.put("unit1", 60.00);
		map2.put("unit2", 40.00);
		map2.put("unit3", 200.00);
		
		Random rand =new Random(3);
		JSONObject listdate=new JSONObject();
		JSONArray rowsList = new JSONArray();
		double sum=0;
		for (int i = 0; i < 4; i++) {
			int num=rand.nextInt(5);
			JSONObject json=new JSONObject();
			json.put("GoodName", map.get("name"+i));
			json.put("GoodNum", num+1);
			json.put("GoodUnitValue", map2.get("unit"+1));
			json.put("GoodAllValue", (num+1)*map2.get("unit"+i));
			sum+=json.getDoubleValue("TicketAccount");
			rowsList.add(json);
		}
		listdate.put("rows", rowsList);
		listdate.put("pageindex",1);		
		listdate.put("pagesize",1);		
		listdate.put("totalcount",4);		
		listdate.put("totalpage",1);	
		
		fakeJson.put("listdata", listdate);
		fakeJson.put("mapdata", mardata);
		
		return fakeJson;
	}
	
	public static void main(String[] args) {
		JSONObject json=TransactionFlowMockDataUtil.ticketListFakeData();
		System.err.println(json);
	}

}
