package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumError;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainQueryDubboService;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.web.interceptor.BuryingPoint;
/**
 * 小超课堂
 *
 */
@Controller
@RequestMapping("/User/")
public class XCClassAction {
	
	private static Logger log = LoggerFactory.getLogger(XCClassAction.class);

	@Value("${SYSTEM_CODE}")
	String SYSTEM_CODE;
	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	
	@Autowired
	private TrainQueryDubboService trainQueryDubboService;
	@Autowired
	private MissionClassroomDubboService missionClassroomDubboService;
	
	/**
	 * 小超课堂
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "ClassList", method = RequestMethod.POST)
	public void classList(@RequestBody String msg, HttpServletResponse response) throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ClassList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		/**
		 * flag为M00则向下调用service层接口，否则不调用直接响应
		 */
		JSONObject json = new JSONObject();
		if (stateJson.get("State").toString().equals(STATE_OK)) {
			// 调用service层接口
			JSONArray rowsList = new JSONArray();
			JSONObject listdata = new JSONObject();
			TrainInfoDTO trainInfoDTO = new TrainInfoDTO();
			// 已发布状态
			trainInfoDTO.setStatus(1);
			long trainStartTime=System.currentTimeMillis();
			log.info("\n*******于时间"+DateUtils.getLogDataTime(trainStartTime, null)+"开始调用trainQueryDubboService接口getTrainList的请求数据是：" + trainInfoDTO.toString());
			Response<Map<String, Object>> result = trainQueryDubboService.getTrainList(trainInfoDTO, 1, 2000);
			log.info("\n*******于时间"+DateUtils.getLogDataTime(trainStartTime, null)+"结束调用trainQueryDubboService接口getTrainList响应数据是：" 
					+"\n*******耗时为:"+CommonUtil.costTime(trainStartTime));
			Map<String, Object> map = result.getResultObject();
			if (result != null && result.isSuccess()) {
				for (TrainInfoDTO trainInfo : (List<TrainInfoDTO>) map.get("data")) {
					JSONObject listJson = new JSONObject();
					String merchantId = jsonTemp.get("StoreSerialNo").toString();
					long missionStartTime=System.currentTimeMillis();
					log.info("\n******于时间"+DateUtils.getLogDataTime(missionStartTime, null)+"开始调用任务服务接口queryMissionExecuteByMerchantIdAndCourseId的请求数据是："+ trainInfoDTO.toString());
					Response<ViewMissionExecuteDto> res = missionClassroomDubboService
							.queryMissionExecuteByMerchantIdAndCourseId(merchantId, trainInfo.getId().toString());
					log.info("\n******于时间"+DateUtils.getLogDataTime(missionStartTime, null)+"结束调用任务服务接口queryMissionExecuteByMerchantIdAndCourseId的响应数据是："
									+ JSONObject.toJSONString(res)
									+"\n*******耗时为:"+CommonUtil.costTime(missionStartTime));
					if (res.getResultObject() == null) {
						listJson.put("ClassType", "2");
					} else {
						if (res.getResultObject().getStatus()
								.equals(EnumMissionExecuteStatus.STATUS_AWARD_AUDIT.getCode())
								|| res.getResultObject().getStatus()
										.equals(EnumMissionExecuteStatus.STATUS_END.getCode())) {
							listJson.put("ClassType", "1");
							listJson.put("IsFinished", "1");
						} else {
							listJson.put("ClassType", "1");
							listJson.put("IsFinished", "0");
						}
					}
					listJson.put("ClassId", trainInfo.getId().toString());
					listJson.put("ClassName", trainInfo.getName());
					listJson.put("ClassTimes", trainInfo.getTrainLength().toString());// 建议时长
					listJson.put("ClassUrl", trainInfo.getFileUrl());
					listJson.put("Pic", trainInfo.getIcon());
					rowsList.add(listJson);
				}
			} else {
				json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
			}
			listdata.put("rows", rowsList);
			listdata.put("totalcount", rowsList.size());
			json.put("listdata", listdata);
		}
		json.put("Status", stateJson);
		log.info("\n**********于"+ DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！"
				//+"\n**********response to XCR_APP data is:  " + json 
				+"\n**********用时为："+ CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}
	
	/**
	 * 完成
	 * 
	 * @param msg
	 * @param response
	 * @throws IOException
	 */
	@BuryingPoint
	@RequestMapping(value = "FinishClass", method = RequestMethod.POST)
	public void finishClass(@RequestBody String msg, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "FinishClass");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.get("State").toString().equals(STATE_OK)) {
			// 调用service层接口
			String merchantId = jsonTemp.get("StoreSerialNo").toString();
			String courseId = jsonTemp.get("ClassId").toString();
			String status = "";
			log.info("\n**********************任务服务missionClassroomDubboService接口courseMissionCallback的请求数据是："
					+ merchantId + " " + courseId + " " + status);
			long dubboStart = System.currentTimeMillis();
			Response<Boolean> res = missionClassroomDubboService.courseMissionCallback(merchantId, courseId, status);
			long dubboEnd = System.currentTimeMillis();
			log.info("\n**********************任务服务missionClassroomDubboService接口courseMissionCallback的响应数据是："
					+ JSONObject.toJSONString(res));
			log.info("pro【任务完成耗时】 -> missionClassroomDubboService.courseMissionCallback 花费时间为："
					+ (dubboEnd - dubboStart));

			if (!res.isSuccess() && EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode().equals(res.getCode())) {
				json = CommonUtil.pageStatus(json, "M06", "任务已下架");
			} else if (res.isSuccess() && res.getResultObject() == true) {
				json.put("Status", stateJson);
			} else {
				json = CommonUtil.pageStatus(json, STATE_ERR, "服务器异常");
			}
		} else {
			json.put("Status", stateJson);
		}
		log.info("\n**********response to XCR_APP data is:  " + json + "\n**********于"
				+ DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！\n**********用时为："
				+ CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

}
