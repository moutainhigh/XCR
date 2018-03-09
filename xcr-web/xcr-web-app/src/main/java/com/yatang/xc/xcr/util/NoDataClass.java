package com.yatang.xc.xcr.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 当查到的数据为空时，可以直接调用addKeyValue方法返回。
 * @author dongshengde
 *
 */
public class NoDataClass {
	public static JSONObject addKeyValue() {
		JSONObject jsResult = new JSONObject();
		JSONObject jsState = new JSONObject();
		JSONObject jsListData = new JSONObject();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		jsState.put("State", "M00");
		jsState.put("StateID", "00");
		jsState.put("StateValue", "00");
		jsState.put("StateDesc", "成功");

		jsResult.put("Status", jsState);
		jsResult.put("listdata", jsListData);

		jsListData.put("pageindex", 0);
		jsListData.put("pagesize", 0);
		jsListData.put("totalpage", 0);
		jsListData.put("totalcount", 0);
		jsListData.put("rows", jsonObjects);

		return jsResult;
	}

	public static JSONObject addKeyValue(Integer pageIndex,Integer pageSize) {
		JSONObject jsResult = new JSONObject();
		JSONObject jsState = new JSONObject();
		JSONObject jsListData = new JSONObject();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		jsState.put("State", "M00");
		jsState.put("StateID", "00");
		jsState.put("StateValue", "00");
		jsState.put("StateDesc", "成功");

		jsResult.put("Status", jsState);
		jsResult.put("listdata", jsListData);

		jsListData.put("pageindex", pageIndex);
		jsListData.put("pagesize", pageSize);
		jsListData.put("totalpage", 0);
		jsListData.put("totalcount", 0);
		jsListData.put("rows", jsonObjects);

		return jsResult;
	}
}
