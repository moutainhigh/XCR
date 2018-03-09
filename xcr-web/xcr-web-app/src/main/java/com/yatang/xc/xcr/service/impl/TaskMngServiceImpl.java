package com.yatang.xc.xcr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionExecuteQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.service.TaskMngService;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务服务实现
 * Created by wangyang on 2017/7/14.
 */
@Service("taskMngService")
public class TaskMngServiceImpl implements TaskMngService {

    private static Logger log = Logger.getLogger(TaskMngServiceImpl.class);

    @Autowired
    private MissionExecuteDubboService missionExecuteDubboService;
    @Autowired
    private MissionClassroomDubboService missionClassroomDubboService;

    @Override
    public JSONObject missionReceiveReward(String merchantId, String taskId) {
        JSONObject json = new JSONObject();
        log.info("任务领取奖励 -> missionReceiveReward -> merchantId:" + merchantId + "  taskId:" + taskId);
        log.info("\n**********************任务服务missionClassroomDubboService接口queryMissionExecuteByMerchantIdAndCourseId的请求数据是："
                + merchantId + "    任务Id：" + taskId);
        Response<ViewMissionExecuteDto> res = missionClassroomDubboService
                .queryMissionExecuteByMerchantIdAndCourseId(merchantId,
                        taskId);
        log.info("\n**********************任务服务missionClassroomDubboService接口queryMissionExecuteByMerchantIdAndCourseId的响应数据是："
                + JSONObject.toJSONString(res));

        JSONObject mapdata = new JSONObject();
        Map<String, Object> mapdata_map = new HashMap<>();
        JSONObject stateJson1 = null;
        Long startTime = System.currentTimeMillis();
        if (res.getResultObject() != null) {
            log.info("\n**********************" + DateUtils.getLogDataTime(startTime, null) + "One:goAndGetAward:请求参数为：" + res.getResultObject().getId());
            Response<List<AwardInfoDto>> res2 = missionExecuteDubboService
                    .goAndGetAward(res.getResultObject().getId().toString(), merchantId);
            log.info("\n*****************于时间：" + DateUtils.getLogDataTime(startTime, null) + "One:goAndGetAward:响应数据为：" + JSONObject.toJSONString(res2));
            stateJson1 = ActionUserUtil.getReceiveRewardStateJson(res2);
            if (stateJson1.getString("State").equals("M00")) {
                json = getSucMsg(res2.getResultObject());
            }
            json.put("Status", stateJson1);
        } else {
            log.info("\n**********************" + DateUtils.getLogDataTime(startTime, null) + "Two:goAndGetAward:请求参数为：" + taskId);
            Response<List<AwardInfoDto>> res2 = missionExecuteDubboService.goAndGetAward(taskId, merchantId);
            log.info("\n*****************于时间：" + DateUtils.getLogDataTime(startTime, null) + "Two:goAndGetAward:响应数据为：" + JSONObject.toJSONString(res2));
            stateJson1 = ActionUserUtil.getReceiveRewardStateJson(res2);
            if (stateJson1.getString("State").equals("M00")) {
                json = getSucMsg(res2.getResultObject());
            }
            json.put("Status", stateJson1);
        }
        return json;
    }

    /**
     * 判断是否已经领取了奖励
     *
     * @param merchantId
     * @param taskId
     * @return
     */
    private boolean haveReceiveReward(String merchantId, String taskId) {
        MissionExecuteQueryDto queryDto = new MissionExecuteQueryDto();
        queryDto.setId(Long.valueOf(taskId));
        queryDto.setMerchantId(merchantId);
        Response<List<ViewMissionExecuteDto>> listResponse = missionExecuteDubboService.queryMissionExecute(queryDto);
        if (listResponse != null && listResponse.isSuccess()) {
            List<ViewMissionExecuteDto> viewMissionExecuteDtoList = listResponse.getResultObject();
            if (CollectionUtils.isEmpty(viewMissionExecuteDtoList)) {
                return true;
            }
            ViewMissionExecuteDto dto = viewMissionExecuteDtoList.get(0);
            if (dto != null) {
                String status = dto.getStatus();
                if (com.busi.common.utils.StringUtils.isNotEmpty(status)) {
                    if (status.equals("END")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 组装提示
     *
     * @param awardInfoDtoList
     * @return
     */
    private JSONObject getSucMsg(List<AwardInfoDto> awardInfoDtoList) {
        JSONObject json = new JSONObject();
        JSONObject mapdata = new JSONObject();
        Map<String, Object> mapdata_map = new HashMap<>();
        StringBuffer param = new StringBuffer();
        param.append("恭喜你！获得奖励:");
        for (AwardInfoDto dto : awardInfoDtoList) {
            String type = "";
            String awardtype = dto.getAwardType();
            if ("SCORE".equals(awardtype)) {
                type = "积分";
                Double grantNum = dto.getGrantNum();
                if (grantNum == null) {
                    grantNum = 0.0;
                }
                param.append(grantNum.intValue()).append(type).append(" ");
            }
        }
        mapdata_map.put("SucMsg", param.toString());
        mapdata = com.yatang.xc.xcr.util.StringUtils.replcNULLToStr(mapdata_map);
        json.put("mapdata", mapdata);
        return json;
    }


}
