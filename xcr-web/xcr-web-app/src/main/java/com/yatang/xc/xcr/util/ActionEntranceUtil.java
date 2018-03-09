package com.yatang.xc.xcr.util;

import com.alibaba.fastjson.JSONObject;
/**
 * @author gaodawei
 * @date   2017年3月20日(星期一)
 * @function ActionEntrance假数据的封装
 */
public class ActionEntranceUtil {

	/**
	 * GetPublicKey返回数据格式处理
	 * @param data
	 * @return
	 */
	public static JSONObject getResponseData(String data){
		JSONObject json=new JSONObject();
		JSONObject subJson02=new JSONObject();
		if(data!=null){
			json=CommonUtil.pageStatus(json,"M00", "正常");
			subJson02.put("PublicKey", data);
		}else{
			json=CommonUtil.pageStatus(json,"M02", "服务器异常");
			subJson02.put("PublicKey", null);
		}
		json.put("mapdata", subJson02);
		return json;
	}
	/**
	 * PostDesKey返回数据格式处理
	 * @param data
	 * @return
	 */
	public static JSONObject getPostDesKeyData(String data){
		JSONObject json=new JSONObject();
		if(data!=null){
			json=CommonUtil.pageStatus(json,"M00", "正常");
		}else{
			json=CommonUtil.pageStatus(json,"M02", "服务器异常");
		}
		return json;
	}
	

	/**
	 * 封装登录状态json
	 * 
	 * @param result
	 * @return
	 */
	public static JSONObject getLoginStateJson(JSONObject result,String info_ok) {
		JSONObject stateJson = new JSONObject();
		if(result!=null){
			if(info_ok.equals(result.getString("responseCode"))){
				stateJson=CommonUtil.pageStatus2("M00", "正常");
			}else{
				stateJson=CommonUtil.pageStatus2("M01", result.getString("errMsg"));
			}
		}else{
			stateJson=CommonUtil.pageStatus2("M02", "服务器异常");
		}
		return stateJson;
	}
}
