package com.yatang.xc.xcr.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;

/**
 * 封装数据json的类
 * 
 * @author caotao
 * @date 2017-04-27
 */
public class ActionUserUtil {

	/**
	 * 封装状态json
	 * 
	 * @param jsonTemp
	 * @return
	 */
	public static JSONObject getStateJson(JSONObject jsonTemp) {
		JSONObject stateJson = new JSONObject();
		if (jsonTemp != null) {
			if (jsonTemp.getString("flag").equals("M00")) {
				stateJson=CommonUtil.pageStatus2("M00", "正常");
			} else if (jsonTemp.getString("flag").equals("M01")) {
				stateJson=CommonUtil.pageStatus2("M01", "账号过期，请重新登录");
			} else {
				stateJson=CommonUtil.pageStatus2("M05", "你已被迫下线，请确认是否密码泄露");
			}
		} else {
			stateJson=CommonUtil.pageStatus2("M02", "请重新填写");
		}
		return stateJson;
	}

	/**
	 * 封装领取奖励json
	 * 
	 * @param jsonTemp
	 * @return
	 */
	public static JSONObject getReceiveRewardStateJson(Response<List<AwardInfoDto>> res) {
		JSONObject stateJson = new JSONObject();
		if (res == null) {
			stateJson=CommonUtil.pageStatus2("M02", "服务器异常");
		}else{
			if (res.isSuccess()) {
				stateJson=CommonUtil.pageStatus2("M00", "正常");
			} else {
				stateJson=CommonUtil.pageStatus2("M02", res.getErrorMessage());
			}
		}
		return stateJson;
	}
}
