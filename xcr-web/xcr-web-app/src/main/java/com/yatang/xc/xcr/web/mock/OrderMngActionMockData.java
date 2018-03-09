package com.yatang.xc.xcr.web.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;

public class OrderMngActionMockData {
	
	public static JSONObject OrderListMockData(JSONObject jsonTemp1){
		JSONObject json=new JSONObject();
		//商品名称
		Map<String, String> map=new HashMap<>();
		map.put("name0", "https://img.alicdn.com/imgextra/i1/TB1UhGVRXXXXXc2apXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map.put("name1", "https://img.alicdn.com/imgextra/i4/T1ukYEFfNeXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map.put("name2", "https://img.alicdn.com/imgextra/i3/TB1ePrORXXXXXcPXpXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map.put("name3", "https://img.alicdn.com/imgextra/i3/TB1uVp2RXXXXXbDXXXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map.put("name4", "https://img.alicdn.com/imgextra/i3/TB1VkvpKFXXXXaTXpXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map.put("name5", "https://img.alicdn.com/imgextra/i3/TB1zmJ.RpXXXXX1XXXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map.put("name6", "https://img.alicdn.com/imgextra/i1/TB1yv52PpXXXXa5XFXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map.put("name7", "https://img.alicdn.com/imgextra/i2/TB1yMVkLpXXXXaxXVXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		//用户姓名
		Map<String, String> map1=new HashMap<>();
		map1.put("name0", "李先生");
		map1.put("name1", "陶女士");
		map1.put("name2", "何先生");
		map1.put("name3", "唐小姐");
		map1.put("name4", "赵先生");
		map1.put("name5", "黄先生");
		map1.put("name6", "邓女士");
		map1.put("name7", "卢小姐");
		
		//用户地址
		Map<String, String> map2=new HashMap<>();
		map2.put("name0", "北京市海淀区西土城路10号北京邮电大学");
		map2.put("name1", "中国上海市宝山区吴淞八棉新村23号103室");
		map2.put("name2", "南昌市 湾里区 江西中医学院 学生宿舍9栋103号");
		map2.put("name3", "河南省郑州市郑州航空工业管理学院东校区13栋211");
		map2.put("name4", "深圳市长成区星光街26号南厅大厦");
		map2.put("name5", "江西省.南昌市南昌市八一大道135号A座11楼1100");
		map2.put("name6", "西单商业街");
		map2.put("name7", "延安西路1号");
		
		Map<Integer, Integer> map4=new HashMap<>();
		map4.put(1, 2);
		map4.put(2, 3);
		map4.put(3, 31);
		map4.put(4, 4);
		map4.put(5, 5);
		map4.put(6, 6);
		map4.put(7, 10132);
		map4.put(8, 1017);
		map4.put(9, 10133);
		map4.put(10, 10332);
		map4.put(11, 10333);
		map4.put(0, 1037);
		
		Random rand =new Random(2);
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		for (int i = 0; i < 8; i++) {
			JSONObject jsonTemp=new JSONObject();
			
			if("201".equals(jsonTemp1.getString("RefusedType")) || "202".equals(jsonTemp1.getString("RefusedType")) ||
					"203".equals(jsonTemp1.getString("RefusedType")) ||
					"204".equals(jsonTemp1.getString("RefusedType")) ||
					"205".equals(jsonTemp1.getString("RefusedType"))){
				jsonTemp.put("CancelId", 0120+i);
			}else{
				jsonTemp.put("CancelId", "");
			}
			jsonTemp.put("SortNo", 0120+i);
			jsonTemp.put("OrderNo", 2017061200+i);
			if("0".equals(jsonTemp1.getString("OrderType"))){
				jsonTemp.put("OrderStatue", map4.get(rand.nextInt(12)));
				jsonTemp.put("OrderType", map4.get(rand.nextInt(12)));
			}else if(jsonTemp1.getString("OrderType")==null || "".equals(jsonTemp1.getString("OrderType"))){
				jsonTemp.put("OrderStatue", jsonTemp1.getString("RefusedType"));
				jsonTemp.put("OrderType", "");
			}else{
				jsonTemp.put("OrderStatue", jsonTemp1.getString("OrderType"));
				jsonTemp.put("OrderType", jsonTemp1.getString("OrderType"));
			}
			jsonTemp.put("GoodsNum", rand.nextInt(5));
			jsonTemp.put("CreateOrderTime", new Date().getTime());
			jsonTemp.put("PayType", rand.nextInt(2)+10);
			jsonTemp.put("OrderValue", "120.50");
			jsonTemp.put("AccountName", map1.get("name"+i));
			jsonTemp.put("Phone", "13885216953");
			jsonTemp.put("ResidualOrderTime", "5");
			jsonTemp.put("Address", map2.get("name"+i));
			jsonTemp.put("DeliveryType", rand.nextInt(3)+101);//101代表送货上门，102代表自取,103蜂鸟配送
			
			jsonTemp.put("DistributorName", "李太丽");
			jsonTemp.put("DistributorPhone", "13362547856");
			jsonTemp.put("DeliveryTime", "1502698523946");
			jsonTemp.put("SucTime", "1502698573264");
			
			if("201".equals(jsonTemp1.getString("RefusedType")) || "202".equals(jsonTemp1.getString("RefusedType")) ||
					"203".equals(jsonTemp1.getString("RefusedType")) ||
					"204".equals(jsonTemp1.getString("RefusedType")) ||
					"205".equals(jsonTemp1.getString("RefusedType"))){
				jsonTemp.put("ReturnOrderTime", "1502698573264");
				jsonTemp.put("ReturnOrderPrice", "56.20");
			}else{
				jsonTemp.put("ReturnOrderTime", "");
				jsonTemp.put("ReturnOrderPrice", "");
			}
			JSONArray picList = new JSONArray();
			for (int j = 0; j < 8; j++) {
				JSONObject jsonTemp_1=new JSONObject();
				jsonTemp_1.put("PicUrl", map.get("name"+j));
				picList.add(jsonTemp_1);
			}
			jsonTemp.put("PicList", picList);
			rowsList.add(jsonTemp);
		}
		listdata.put("rows", rowsList);
		listdata.put("pageindex",1);		
		listdata.put("pagesize",1);		
		listdata.put("totalcount",8);		
		listdata.put("totalpage",1);	
		
		
		JSONObject mapdata=new JSONObject();
		mapdata.put("NotRecivedNum", 5);
		mapdata.put("NotDeliveryNum", 8);
		mapdata.put("ShippedNum", 28);
		mapdata.put("ReturnedNum", 5);
		
		json.put("mapdata", mapdata);
		json.put("listdata", listdata);
		return json;
	}
	
	static Random mRandom = new Random();
	
	public static JSONObject OrderListDetailMockData(){
		System.err.println("=========================================");
		JSONObject json=new JSONObject();
		//商品名称
		Map<String, String> map=new HashMap<>();
		map.put("name0", "商品000001");
		map.put("name1", "商品000002");
		map.put("name2", "商品000003");
		map.put("name3", "商品000004");
		map.put("name4", "商品000005");
		map.put("name5", "商品000006");
		map.put("name6", "商品000007");
		map.put("name7", "商品000008");
		//用户姓名
		Map<String, String> map1=new HashMap<>();
		map1.put("name0", "李先生");
		map1.put("name1", "陶女士");
		map1.put("name2", "何先生");
		map1.put("name3", "唐小姐");
		map1.put("name4", "赵先生");
		map1.put("name5", "黄先生");
		map1.put("name6", "邓女士");
		map1.put("name7", "卢小姐");
		
		//用户地址
		Map<String, String> map2=new HashMap<>();
		map2.put("name0", "北京市海淀区西土城路10号北京邮电大学");
		map2.put("name1", "中国上海市宝山区吴淞八棉新村23号103室");
		map2.put("name2", "南昌市 湾里区 江西中医学院 学生宿舍9栋103号");
		map2.put("name3", "河南省郑州市郑州航空工业管理学院东校区13栋211");
		map2.put("name4", "深圳市长成区星光街26号南厅大厦");
		map2.put("name5", "江西省.南昌市南昌市八一大道135号A座11楼1100");
		map2.put("name6", "西单商业街");
		map2.put("name7", "延安西路1号");
		
		Map<String, String> map3=new HashMap<>();
		map3.put("name0", "https://img.alicdn.com/imgextra/i1/TB1UhGVRXXXXXc2apXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name1", "https://img.alicdn.com/imgextra/i4/T1ukYEFfNeXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name2", "https://img.alicdn.com/imgextra/i4/TB1vfixRXXXXXc.XFXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name3", "https://img.alicdn.com/imgextra/i3/TB1uVp2RXXXXXbDXXXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name4", "https://img.alicdn.com/imgextra/i3/TB1VkvpKFXXXXaTXpXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name5", "https://img.alicdn.com/imgextra/i3/TB1zmJ.RpXXXXX1XXXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name6", "https://img.alicdn.com/imgextra/i1/TB1yv52PpXXXXa5XFXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name7", "https://img.alicdn.com/imgextra/i2/TB1yMVkLpXXXXaxXVXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		
		Map<Integer, Integer> map4=new HashMap<>();
		map4.put(1, 2);
		map4.put(2, 3);
		map4.put(3, 31);
		map4.put(4, 4);
		map4.put(5, 5);
		map4.put(6, 6);
		map4.put(7, 10132);
		map4.put(8, 1017);
		map4.put(9, 10133);
		map4.put(10, 10332);
		map4.put(11, 10333);
		map4.put(0, 1037);
		
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		int num=0;
		double unitValue=0.00;
		double sum=0;
		for (int i = 0; i < 8; i++) {
			JSONObject jsonTemp=new JSONObject();
			jsonTemp.put("GoodName", map.get("name"+i));
			num=new Random(5).nextInt(5);
			unitValue=new Random(2).nextDouble()*1002f;
			jsonTemp.put("GoodNum", num);
			jsonTemp.put("GoodUnitValue", String.format("%.2f", unitValue-0.005));
			jsonTemp.put("GoodAllValue", String.format("%.2f", unitValue*num-0.005));
			jsonTemp.put("PicUrl", map3.get("name"+i));
			sum+=unitValue*num;
			rowsList.add(jsonTemp);
		}
		listdata.put("rows", rowsList);
		int testIndex= mRandom.nextInt(12);
		Log.info("\n***************testIndex的值为："+testIndex);
		JSONObject mapdata=new JSONObject();
		mapdata.put("DeliveryType", mRandom.nextInt(3)+101);//101代表送货上门，102代表自取,103蜂鸟配送
		mapdata.put("OrderStatue", map4.get(3));//map4.get(testIndex)
		mapdata.put("OrderStatus", map4.get(3));//map4.get(testIndex)
		mapdata.put("TotalNum", 8);
		mapdata.put("GoodAllValue", String.format("%.2f", sum-0.005));
		mapdata.put("ReceivableValue", String.format("%.2f", sum-0.005));
		mapdata.put("DeliveryFee", 0.00);
		mapdata.put("Discount", 0.00);
		mapdata.put("PaidUpValue", 0.00);
		mapdata.put("PayType", new Random(12).nextInt(2)+10);
		mapdata.put("AccountName", "卢先生");
		mapdata.put("Phone", "13865487526");
		mapdata.put("Address", "江西省.南昌市南昌市八一大道135号A座11楼1100");
		mapdata.put("SortNo", "no1402543");
		mapdata.put("OrderNo", "no1402543");
		mapdata.put("CreateOrderTime", new Date().getTime());
		mapdata.put("CancleOrderMsg", "配送时间太长，等不及了");
		
		mapdata.put("DistributorName", "李太丽");
		mapdata.put("DistributorPhone", "13362547856");
		mapdata.put("DeliveryTime", "1502698523946");
		mapdata.put("SucTime", "1502698573264");
		
		json.put("mapdata", mapdata);
		json.put("listdata", listdata);
		System.err.println("json:"+json);
		return json;
	}
	public static JSONObject OrderReturnDetailMockData(){
		System.err.println("=========================================");
		JSONObject json=new JSONObject();
		//商品名称
		Map<String, String> map=new HashMap<>();
		map.put("name0", "商品000001");
		map.put("name1", "商品000002");
		map.put("name2", "商品000003");
		map.put("name3", "商品000004");
		map.put("name4", "商品000005");
		map.put("name5", "商品000006");
		map.put("name6", "商品000007");
		map.put("name7", "商品000008");
		//用户姓名
		Map<String, String> map1=new HashMap<>();
		map1.put("name0", "李先生");
		map1.put("name1", "陶女士");
		map1.put("name2", "何先生");
		map1.put("name3", "唐小姐");
		map1.put("name4", "赵先生");
		map1.put("name5", "黄先生");
		map1.put("name6", "邓女士");
		map1.put("name7", "卢小姐");
		
		//用户地址
		Map<String, String> map2=new HashMap<>();
		map2.put("name0", "北京市海淀区西土城路10号北京邮电大学");
		map2.put("name1", "中国上海市宝山区吴淞八棉新村23号103室");
		map2.put("name2", "南昌市 湾里区 江西中医学院 学生宿舍9栋103号");
		map2.put("name3", "河南省郑州市郑州航空工业管理学院东校区13栋211");
		map2.put("name4", "深圳市长成区星光街26号南厅大厦");
		map2.put("name5", "江西省.南昌市南昌市八一大道135号A座11楼1100");
		map2.put("name6", "西单商业街");
		map2.put("name7", "延安西路1号");
		
		Map<String, String> map3=new HashMap<>();
		map3.put("name0", "https://img.alicdn.com/imgextra/i1/TB1UhGVRXXXXXc2apXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name1", "https://img.alicdn.com/imgextra/i4/T1ukYEFfNeXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name2", "https://img.alicdn.com/imgextra/i4/TB1vfixRXXXXXc.XFXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name3", "https://img.alicdn.com/imgextra/i3/TB1uVp2RXXXXXbDXXXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name4", "https://img.alicdn.com/imgextra/i3/TB1VkvpKFXXXXaTXpXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name5", "https://img.alicdn.com/imgextra/i3/TB1zmJ.RpXXXXX1XXXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name6", "https://img.alicdn.com/imgextra/i1/TB1yv52PpXXXXa5XFXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		map3.put("name7", "https://img.alicdn.com/imgextra/i2/TB1yMVkLpXXXXaxXVXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
		
		Map<Integer, Integer> map4=new HashMap<>();
		map4.put(1, 2);
		map4.put(2, 3);
		map4.put(3, 31);
		map4.put(4, 4);
		map4.put(5, 5);
		map4.put(6, 6);
		map4.put(7, 10132);
		map4.put(8, 1017);
		map4.put(9, 10133);
		map4.put(10, 10332);
		map4.put(11, 10333);
		map4.put(0, 1037);
		
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		int num=0;
		double unitValue=0.00;
		double sum=0;
		for (int i = 2; i < 8; i++) {
			JSONObject jsonTemp=new JSONObject();
			jsonTemp.put("GoodName", map.get("name"+i));
			num=new Random(5).nextInt(5);
			unitValue=new Random(2).nextDouble()*1002f;
			jsonTemp.put("GoodNum", num);
			jsonTemp.put("GoodUnitValue", String.format("%.2f", unitValue-0.005));
			jsonTemp.put("GoodAllValue", String.format("%.2f", unitValue*num-0.005));
			jsonTemp.put("PicUrl", map3.get("name"+i));
			sum+=unitValue*num;
			rowsList.add(jsonTemp);
		}
		listdata.put("rows", rowsList);
		int testIndex= mRandom.nextInt(12);
		Log.info("\n***************testIndex的值为："+testIndex);
		JSONObject mapdata=new JSONObject();
		mapdata.put("DeliveryType", mRandom.nextInt(3)+101);//101代表送货上门，102代表自取,103蜂鸟配送
		mapdata.put("OrderStatue", map4.get(3));//map4.get(testIndex)
		mapdata.put("ReturnNum", 8);
		mapdata.put("ReturnValue", String.format("%.2f", sum-0.005));
		mapdata.put("AccountName", "卢先生");
		mapdata.put("Phone", "13865487526");
		mapdata.put("CancelId", "mo00123456");
		mapdata.put("Address", "江西省.南昌市南昌市八一大道135号A座11楼1100");
		mapdata.put("SortNo", "no1402543");
		mapdata.put("OrderNo", "no1402543");
		mapdata.put("OrderReturnTime", new Date().getTime());
		mapdata.put("ReturnReason", "购买商品与所送商品不符");
		
		json.put("mapdata", mapdata);
		json.put("listdata", listdata);
		System.err.println("json:"+json);
		return json;
	}
	
	
	public static void main(String[] args) {
		JSONObject jsonTemp=new JSONObject();
		jsonTemp.put("RefusedType", "");
		jsonTemp.put("OrderType", "2");
//		JSONObject json=OrderListMockData(jsonTemp);
		JSONObject json=OrderListDetailMockData();
		System.err.print(json);
	}

}
