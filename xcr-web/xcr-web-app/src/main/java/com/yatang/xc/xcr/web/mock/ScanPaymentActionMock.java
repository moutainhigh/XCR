package com.yatang.xc.xcr.web.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.dao.support.DataAccessUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.StringUtils;

/** 
* @author gaodawei 
* @Date 2017年10月31日 上午10:58:08 
* @version 1.0.0
* @function 
*/
public class ScanPaymentActionMock {
	
	public static JSONObject getCollectTransactionDetialListMock(JSONObject jsonTemp){
		JSONObject json=new JSONObject();
		Random random=new Random();
		JSONObject mapdata=new JSONObject();
		mapdata.put("Count", "6");
		json.put("mapdata", mapdata);
		JSONObject listdata=new JSONObject();
		JSONArray rowsList = new JSONArray();
		for (int i = 0; i < 6; i++) {
			Map<String, Object> subMap = new HashMap<>();
			subMap.put("CollectTransactionDaily", random.nextDouble()*100);
			subMap.put("Date", DateUtils.dateSimpleFormat(DateUtils.getDateBefore(new Date(), i)));
			subMap.put("Time", "11:55:28");
			subMap.put("PayType", (random.nextInt(2)+1)+"");
			rowsList.add(StringUtils.replcNULLToStr(subMap));
		}
		listdata.put("rows", rowsList);
		listdata=CommonUtil.pagePackage(listdata, jsonTemp, 6, null);
		json.put("listdata", listdata);
		return json;
	}
	
	public static void main(String[] args) {
		JSONObject jsonTemp=new JSONObject();
		jsonTemp.put("PageIndex", 1);
		jsonTemp.put("PageSize", 20);
		System.err.println(getCollectTransactionDetialListMock(jsonTemp));
	}

}
 