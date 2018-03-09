package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionAwardDto;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionBuyDto;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionExecuteQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumAttachmentType;
import com.yatang.xc.xcr.biz.mission.enums.EnumError;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionOrderBy;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionType;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainQueryDubboService;
import com.yatang.xc.xcr.enums.EnumAttachmentName;
import com.yatang.xc.xcr.enums.EnumRentName;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.service.TaskMngService;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.TokenUtil;
import com.yatang.xc.xcr.web.interceptor.BuryingPoint;

@Controller
@RequestMapping("/User/")
public class TaskMngAction {

    private static Logger log = LoggerFactory.getLogger(TaskMngAction.class);

    @Value("${SYSTEM_CODE}")
    String SYSTEM_CODE;
    @Value("${STATE_OK}")
    String STATE_OK;
    @Value("${STATE_ERR}")
    String STATE_ERR;
    @Value("${INFO_OK}")
    String INFO_OK;
    // pos机的产品Id
    @Value("${POS_PRODUCT_ID}")
    String POS_PRODUCT_ID;
    // 图片Url前缀
    @Value("${URL_PREFIX}")
    String URL_PREFIX;
    @Value("${TOKEN_OUTTIME}")
    private Integer TOKEN_OUTTIME;

    @Autowired
    private RedisService<JSONObject> redisJsonService;
    @Autowired
    private TrainQueryDubboService trainQueryDubboService;
    @Autowired
    private MissionExecuteDubboService missionExecuteDubboService;
    @Autowired
    private TaskMngService taskMngService;
    @Autowired
    private MissionClassroomDubboService missionClassroomDubboService;
    @Autowired
    private MissionDubboService missionDubboService;
    @Autowired
    private OrganizationService organizationDubboService;


    /**
     * 任务列表
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @BuryingPoint
    @RequestMapping(value = "TaskList", method = RequestMethod.POST)
    public void taskList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "TaskList");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        JSONObject mapData = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            // 需要先做自动审核 动作
            try {
                String key = SYSTEM_CODE + tokenJson.getString("jmsCode");
                JSONObject datajson = CommonUtil.getRedisUserInfo(redisJsonService, key, organizationDubboService, tokenJson.getString("jmsCode"), SYSTEM_CODE, TOKEN_OUTTIME);
                long taskStartTime = System.currentTimeMillis();
                log.info("\n*********于时间：" + DateUtils.getLogDataTime(taskStartTime, null) + "任务模块接口autoAuditMissionByMerchantId："
                        + "\n*********请求参数是加盟商账号：" + jsonTemp.getString("StoreSerialNo") + "    金融账号:" + datajson.getString("ytAccount"));
                missionExecuteDubboService.autoAuditMissionByMerchantId(jsonTemp.getString("StoreSerialNo"));
                log.info("\n*********于时间:" + DateUtils.getLogDataTime(taskStartTime, null) + "调用任务模块autoAuditMissionByMerchantId接口   调用结束"
                        + "\n********所花费时间为：" + CommonUtil.costTime(taskStartTime));
            } catch (Exception e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
            // 调用service层接口
            MissionExecuteQueryDto queryDto = new MissionExecuteQueryDto();
            queryDto.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
            queryDto.setStartIndex(0);
            queryDto.setEndIndex(50000);
            long taskStartTime = System.currentTimeMillis();
            log.info("\n***********************于时间：" + DateUtils.getLogDataTime(taskStartTime, null) + "调用任务服务接口queryMissionExecuteOrderByRelated"
                    + "\n***********************请求数据是：" + queryDto.toString());
            Response<List<ViewMissionExecuteDto>> res = missionExecuteDubboService.queryMissionExecuteOrderByRelated(queryDto);
            JSONArray row = new JSONArray();
            JSONObject listdata = new JSONObject();
            if (res.isSuccess() && res.getCode().equals(INFO_OK)) {
                for (ViewMissionExecuteDto missionData : res.getResultObject()) {
                    // 判断任务的状态
                    if (missionData.getStatus().equals(EnumMissionExecuteStatus.STATUS_INIT.getCode())
                            || missionData.getStatus().equals(EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode())) {
                        JSONObject missionJson = getTaskListJson(missionData);
                        String reason = missionData.getReason();
                        // 取前四位
                        String reason1 = reason.substring(0, 4);
                        if ("BPM:".equals(reason1)) {
                            missionJson.put("TaskStatue", "3");// 审核失败的状态
                        } else {
                            missionJson.put("TaskStatue", "0");// 初始化状态
                        }
                        row.add(missionJson);
                    } else if (missionData.getStatus().equals(EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode())) {
                        JSONObject missionJson = getTaskListJson(missionData);
                        missionJson.put("TaskStatue", "1");// 任务审核中状态
                        row.add(missionJson);
                    } else if (missionData.getStatus().equals(EnumMissionExecuteStatus.STATUS_FINISHED.getCode())) {

                        String missionTemplateCode = missionData.getTemplateCode();
                        if (missionTemplateCode.equals("T008") || missionTemplateCode.equals("T009") || missionTemplateCode.equals("T010")) {
                            //执行领取奖励接口
                            taskMngService.missionReceiveReward(missionData.getMerchantId(), String.valueOf(missionData.getId()));
                            continue; //不将已自动领取奖励的任务放入任务列表
                        }
                        JSONObject missionJson = getTaskListJson(missionData);
                        missionJson.put("TaskStatue", "2");// 完成状态
                        row.add(missionJson);
                    }
                }
                listdata.put("rows", row);
                listdata.put("totalcount", row.size());
                Response<MissionAwardDto> missionAwardDtoResponse = missionDubboService.getAwardTotal(jsonTemp.get("StoreSerialNo").toString());
                if (missionAwardDtoResponse.isSuccess()) {
                    MissionAwardDto missionAwardDto = missionAwardDtoResponse.getResultObject();
                    if (missionAwardDto != null) {
                        mapData.put("Award", missionAwardDto.getAward());
                        mapData.put("AwardUnit", missionAwardDto.getAwardUnit());
                    } else {
                        mapData.put("Award", "0");
                        mapData.put("AwardUnit", "积分");
                    }
                }
                json.put("Status", stateJson);
                json.put("listdata", listdata);
                json.put("mapdata", mapData);
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 审核任务详情
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "CheckTaskClassDetial", method = RequestMethod.POST)
    public void checkTaskClassDetial(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "CheckTaskClassDetial");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        /**
         * flag为M00则向下调用service层接口，否则不调用直接响应
         */
        JSONObject json = new JSONObject();
        JSONObject mapJson = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            // 调用service层接口
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用任务服务接口queryMissionExecuteById的请求数据是：" + jsonTemp.getString("TaskId"));
            Response<ViewMissionExecuteDto> trainInfo = missionExecuteDubboService
                    .queryMissionExecuteById(jsonTemp.getString("TaskId"));
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "结束调用任务服务接口queryMissionExecuteById的响应数据是："
                    + JSONObject.toJSONString(trainInfo)
                    + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
            if (trainInfo.getCode().equals(INFO_OK)) {
                String reason = trainInfo.getResultObject().getReason();
                // 取前四位
                String reason1 = reason.substring(0, 4);
                // 去掉前四位
                String reason2 = reason.substring(4, reason.length());
                if ("BPM:".equals(reason1) && !"".equals(reason2)) {
                    mapJson.put("Reason", reason2);// 审核失败的状态
                } else {
                    mapJson.put("Reason", "");// 初始化状态
                }
                json.put("mapdata", mapJson);
            } else if (EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode().equals(trainInfo.getCode())) {
                json = CommonUtil.pageStatus(json, "M06", "任务已下架");
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 任务型课堂详情
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "TaskClassDetial", method = RequestMethod.POST)
    public void taskClassDetial(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "TaskClassDetial");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        /**
         * flag为M00则向下调用service层接口，否则不调用直接响应
         */
        JSONObject json = new JSONObject();
        if (stateJson.get("State").toString().equals(STATE_OK)) {
            // 调用service层接口
            Long id = jsonTemp.getLong("TaskId");
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用任务服务接口queryMissionExecuteByMerchantIdAndCourseId的请求数据是："
                    + jsonTemp.getString("StoreSerialNo") + "    任务Id：" + jsonTemp.getString("TaskId"));
            Response<ViewMissionExecuteDto> res = missionClassroomDubboService
                    .queryMissionExecuteByMerchantIdAndCourseId(jsonTemp.getString("StoreSerialNo"),
                            jsonTemp.getString("TaskId"));
            log.info(
                    "\n*******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "结束调用任务服务接口queryMissionExecuteByMerchantIdAndCourseId的响应数据是："
                            + JSONObject.toJSONString(res)
                            + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
            if (res.getResultObject() == null) {
                json = CommonUtil.pageStatus(json, "M06", "任务已下架");
            }
            long trainStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(trainStartTime, null) + "开始调用课堂服务trainQueryDubboService接口findOneTrain的请求数据是：" + id);
            Response<TrainInfoDTO> trainInfo = trainQueryDubboService.findOneTrain(id);
            log.info("\n******于时间" + DateUtils.getLogDataTime(trainStartTime, null) + "结束调用课堂接口findOneTrain的响应数据是："
                    + JSONObject.toJSONString(trainInfo)
                    + "\n*******耗时为:" + CommonUtil.costTime(trainStartTime));
            if (trainInfo.getCode().equals(INFO_OK)) {
                JSONObject mapJson = new JSONObject();
                mapJson.put("ClassId", trainInfo.getResultObject().getId());
                mapJson.put("ClassName", trainInfo.getResultObject().getName());
                mapJson.put("ClassTimes", trainInfo.getResultObject().getTrainLength().toString());// 建议时长
                mapJson.put("ClassUrl", trainInfo.getResultObject().getFileUrl());// 地址
                json.put("Status", stateJson);
                json.put("mapdata", mapJson);
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 任务完成记录接口
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "TaskCompleteList", method = RequestMethod.POST)
    public void taskCompleteList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "TaskCompleteList");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        JSONObject listdata = new JSONObject();
        JSONArray row = new JSONArray();
        if (stateJson.getString("State").equals(STATE_OK)) {
            // 获取奖励待审核的任务列表
            MissionExecuteQueryDto queryDto = new MissionExecuteQueryDto();
            queryDto.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
            // 最后修改时间排序（倒序）
            queryDto.setOrderBy(EnumMissionOrderBy.TYPE_TIME_DESC.getCode());
            long qmStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(qmStartTime, null) + "开始调用任务服务missionExecuteDubboService接口queryMissionExecuteOrderByRelated的请求数据是："
                    + queryDto.toString());
            Response<List<ViewMissionExecuteDto>> res = missionExecuteDubboService
                    .queryMissionExecuteOrderByRelated(queryDto);
            for (ViewMissionExecuteDto missionData : res.getResultObject()) {
                // 判断任务的状态，获取一种状态：奖励审核中 任务列表
                if (missionData.getStatus().equals(EnumMissionExecuteStatus.STATUS_AWARD_AUDIT.getCode())) {
                    JSONObject missionJson = new JSONObject();
                    missionJson.put("TaskId", missionData.getId());// 主键
                    missionJson.put("TaskName", missionData.getName()); // 任务名
                    missionJson.put("Pic", missionData.getIconUrl());// 图标
                    missionJson.put("TaskRewardMsg", missionData.getSpecialAwardRemark());// 特殊奖励标注
                    // 判断是否有奖励
                    if (missionData.getSpecialAwardRemark() == null && missionData.getAwards() == null) {
                        missionJson.put("IsTakeReward", "0"); // 不可领取奖励
                    } else {
                        missionJson.put("IsTakeReward", "1"); // 可领取奖励
                    }
                    row.add(missionJson);
                }
            }
            // 获取结束状态的任务列表
            MissionExecuteQueryDto missionExecuteQueryDto = new MissionExecuteQueryDto();
            missionExecuteQueryDto.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
            // 最后修改时间排序（倒序）
            missionExecuteQueryDto.setOrderBy(EnumMissionOrderBy.TYPE_TIME_DESC.getCode());
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用任务服务接口queryMissionExecuteInHistory的请求数据是："
                    + missionExecuteQueryDto.toString());
            Response<List<ViewMissionExecuteDto>> res2 = missionExecuteDubboService
                    .queryMissionExecuteInHistory(missionExecuteQueryDto);
            for (ViewMissionExecuteDto missionExecuteDto : res2.getResultObject()) {
                // 判断任务的状态,获取结束的列表
                if (missionExecuteDto.getStatus().equals(EnumMissionExecuteStatus.STATUS_END.getCode())) {
                    JSONObject missionJson = new JSONObject();
                    missionJson.put("TaskId", missionExecuteDto.getId());// 主键
                    missionJson.put("TaskName", missionExecuteDto.getName()); // 任务名
                    missionJson.put("Pic", missionExecuteDto.getIconUrl());// 图标
                    missionJson.put("TaskRewardMsg", missionExecuteDto.getSpecialAwardRemark());// 特殊奖励标注
                    // 判断是否有奖励
                    if (StringUtils.isEmpty(missionExecuteDto.getSpecialAwardRemark())
                            && (missionExecuteDto.getAwards() == null || missionExecuteDto.getAwards().isEmpty())) {
                        missionJson.put("IsTakeReward", "0"); // 不可领取奖励
                    } else {
                        missionJson.put("IsTakeReward", "1"); // 可领取奖励
                    }
                    row.add(missionJson);
                }
            }
            if (!res2.getCode().equals(INFO_OK)) {
                json = CommonUtil.pageStatus(json, STATE_ERR, "服务器异常");
            } else {
                listdata.put("rows", row);
                listdata.put("totalcount", row.size());
                json.put("listdata", listdata);
                json.put("Status", stateJson);
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 查询关联任务是否完成
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "CheckTask", method = RequestMethod.POST)
    public void checkTask(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "CheckTask");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.get("State").toString().equals(STATE_OK)) {
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用任务接口checkMissionRelatedAchieved的请求数据是："
                    + jsonTemp.getString("TaskId"));
            Response<Boolean> res = missionExecuteDubboService
                    .checkMissionRelatedAchieved(jsonTemp.getString("TaskId"));
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "结束调用任务接口checkMissionRelatedAchieved的响应数据是："
                    + JSONObject.toJSONString(res)
                    + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
            if (res.getResultObject() == false) {
                json = CommonUtil.pageStatus(json, "M03", "关联任务未完成");
            } else if (res.getResultObject() == true) {
                json.put("Status", stateJson);
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 本地提交单号
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "InputOrderNo", method = RequestMethod.POST)
    public void inputOrderNo(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "InputOrderNo");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        /**
         * flag为M00则向下调用service层接口，否则不调用直接响应
         */
        if (stateJson.get("State").toString().equals(STATE_OK)) {
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            // 调用service层接口
            String key = SYSTEM_CODE + tokenJson.getString("jmsCode");
            JSONObject datajson = CommonUtil.getRedisUserInfo(redisJsonService, key, organizationDubboService, tokenJson.getString("jmsCode"), SYSTEM_CODE, TOKEN_OUTTIME);
            MissionBuyDto missionBuyDto = new MissionBuyDto();
            missionBuyDto.setLogin(datajson.getString("ytAccount"));
            missionBuyDto.setMissionExecuteId(jsonTemp.getString("TaskId"));
            missionBuyDto.setOrderId(jsonTemp.getString("OrderNo"));
            missionBuyDto.setProductIds(POS_PRODUCT_ID);
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用接口buyProductConfirm的请求数据是："
                    + missionBuyDto.toString());
            Response<Boolean> res = missionExecuteDubboService.buyProductConfirm(missionBuyDto);
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "结束调用接口buyProductConfirm的响应数据是："
                    + JSONObject.toJSONString(res)
                    + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
            if (!res.isSuccess() && EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode().equals(res.getCode())) {
                json = CommonUtil.pageStatus(json, "M06", "任务已下架");
            } else if (res.isSuccess() && res.getResultObject() == true) {
//                String companyId = datajson.getString("subCompanyId");
                String executeId = jsonTemp.getString("TaskId");
//                String shopName = jsonTemp.getString("StoreName");
                long mStartTime = System.currentTimeMillis();
                log.info("\n******于时间" + DateUtils.getLogDataTime(mStartTime, null) + "开始调用接口executeMission的请求数据是：" + executeId);
                Response<Boolean> res2 = missionExecuteDubboService.executeMission(executeId);
                log.info("\n******于时间" + DateUtils.getLogDataTime(mStartTime, null) + "结束调用接口executeMission的响应数据是："
                        + JSONObject.toJSONString(res2)
                        + "\n*******耗时为:" + CommonUtil.costTime(mStartTime));
                if (res2.isSuccess() && res2.getResultObject() == true) {
                    json.put("Status", stateJson);
                } else if (res2.isSuccess() && res2.getResultObject() == false) {
                    json = CommonUtil.pageStatus(json, "M03", "审核不通过");
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, "该任务已完成");
                }
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 任务领取奖励接口
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "ReceiveReward", method = RequestMethod.POST)
    public void receiveReward(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ReceiveReward");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            String merchantId = jsonTemp.getString("StoreSerialNo");
            String taskId = jsonTemp.getString("TaskId");
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用任务接口missionReceiveReward的请求数据是：merchantId="
                    + merchantId + "     taskId=" + taskId);
            json = taskMngService.missionReceiveReward(merchantId, taskId);
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "结束调用接口missionReceiveReward的响应数据是："
                    + json + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 上传门头照接口
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "UploadDoorPic", method = RequestMethod.POST)
    public void uploadDoorPic(@RequestBody String msg, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "UploadDoorPic");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            Response<ViewMissionExecuteDto> res1 = missionExecuteDubboService
                    .queryMissionExecuteById(jsonTemp.getString("TaskId"));
            if (res1.getResultObject() == null) {
                json = CommonUtil.pageStatus(json, "M06", "任务已下架");
            }
            List<AttachmentDto> attachmentDtos = new ArrayList<AttachmentDto>();
            AttachmentDto attachmentDto1 = new AttachmentDto();
            attachmentDto1.setName(EnumAttachmentName.ATTACHMENT_DOOR_HEADER_VIEW.getCode());
            attachmentDto1.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto1.setUrl(jsonTemp.getString("PanoraPic").replace(URL_PREFIX, ""));
            attachmentDto1.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto1);
            AttachmentDto attachmentDto2 = new AttachmentDto();
            // 门头编号特写
            attachmentDto2.setName(EnumAttachmentName.ATTACHMENT_DOOR_HEADER_NUMBER.getCode());
            attachmentDto2.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto2.setUrl(jsonTemp.getString("NoPic").replace(URL_PREFIX, ""));
            attachmentDto2.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto2);
            AttachmentDto attachmentDto3 = new AttachmentDto();
            // 门头制作发票
            attachmentDto3.setName(EnumAttachmentName.ATTACHMENT_DOOR_HEADER_INVOICE.getCode());
            attachmentDto3.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto3.setUrl(jsonTemp.getString("InvoicePic").replace(URL_PREFIX, ""));
            attachmentDto3.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto3);
            AttachmentDto attachmentDto4 = new AttachmentDto();
            // 门头验收单
            attachmentDto4.setName(EnumAttachmentName.ATTACHMENT_DOOR_HEADER_REPORT.getCode());
            attachmentDto4.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto4.setUrl(jsonTemp.getString("AcceptPic").replace(URL_PREFIX, ""));
            attachmentDto4.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto4);
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用任务服务接口saveMissionAttachment的请求数据是："
                    + attachmentDtos.toString());
            Response<Boolean> res = missionExecuteDubboService.saveMissionAttachment(attachmentDtos);
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "结束调用任务服务接口saveMissionAttachment的响应数据是："
                    + JSONObject.toJSONString(res)
                    + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
            if (res.getResultObject() == true) {
                JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
                String key = SYSTEM_CODE + tokenJson.getString("jmsCode");
                JSONObject dataJson = CommonUtil.getRedisUserInfo(redisJsonService, key, organizationDubboService, tokenJson.getString("jmsCode"), SYSTEM_CODE, TOKEN_OUTTIME);
                String executeId = jsonTemp.getString("TaskId");
                // 目前还没有这个接口
                String companyId = dataJson.getString("subCompanyId");
                String shopName = jsonTemp.getString("StoreName");
                long me2StartTime = System.currentTimeMillis();
                log.info("\n******于时间" + DateUtils.getLogDataTime(me2StartTime, null) + "开始调用任务服务接口executeMission的请求数据是：executeId="
                        + executeId + "     ytAccount+" + dataJson.getString("ytAccount") + "     companyId=" + companyId + "     shopName=" + shopName);
                Response<Boolean> res2 = missionExecuteDubboService.executeMission(executeId);
                log.info("\n******于时间" + DateUtils.getLogDataTime(me2StartTime, null) + "结束调用任务服务接口saveMissionAttachment的响应数据是："
                        + JSONObject.toJSONString(res2)
                        + "\n*******耗时为:" + CommonUtil.costTime(me2StartTime));
                if (res2.getResultObject() == true) {
                    json.put("Status", stateJson);
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
                }
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 申请租金补贴接口
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "UploadRentPic", method = RequestMethod.POST)
    public void uploadRentPic(@RequestBody String msg, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "UploadRentPic");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.get("State").toString().equals(STATE_OK)) {
            long meStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "开始调用任务服务接口queryMissionExecuteById的请求数据是："
                    + jsonTemp.getString("TaskId"));
            Response<ViewMissionExecuteDto> res1 = missionExecuteDubboService
                    .queryMissionExecuteById(jsonTemp.getString("TaskId"));
            log.info("\n******于时间" + DateUtils.getLogDataTime(meStartTime, null) + "结束调用任务服务接口queryMissionExecuteById的响应数据是："
                    + JSONObject.toJSONString(res1)
                    + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
            if (res1.getResultObject() == null) {
                json = CommonUtil.pageStatus(json, "M06", "任务已下架");
            }
            List<AttachmentDto> attachmentDtos = new ArrayList<AttachmentDto>();
            AttachmentDto attachmentDto1 = new AttachmentDto();
            // 开启电视机广告
            attachmentDto1.setName(EnumRentName.ATTACHMENT_TV_ADVERT.getCode());
            attachmentDto1.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto1.setUrl(jsonTemp.getString("OpenTvPic").replace(URL_PREFIX, ""));
            attachmentDto1.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto1);
            AttachmentDto attachmentDto2 = new AttachmentDto();
            // 收银机登录收银系统
            attachmentDto2.setName(EnumRentName.ATTACHMENT_LOGIN_CASHIER_VIEW.getCode());
            attachmentDto2.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto2.setUrl(jsonTemp.getString("LoginCMPic").replace(URL_PREFIX, ""));
            attachmentDto2.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto2);
            AttachmentDto attachmentDto3 = new AttachmentDto();
            // 货架商品陈列样式
            attachmentDto3.setName(EnumRentName.ATTACHMENT_GOODS_PLACE_STYLE.getCode());
            attachmentDto3.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto3.setUrl(jsonTemp.getString("ShelfDisplayPic").replace(URL_PREFIX, ""));
            attachmentDto3.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto3);
            AttachmentDto attachmentDto4 = new AttachmentDto();
            // 加盟验收单
            attachmentDto4.setName(EnumRentName.ATTACHMENT_JOIN_INVOICE.getCode());
            attachmentDto4.setType(EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
            attachmentDto4.setUrl(jsonTemp.getString("AcceptPic").replace(URL_PREFIX, ""));
            attachmentDto4.setMissionExecuteId(jsonTemp.getString("TaskId"));
            attachmentDtos.add(attachmentDto4);
            long me2StartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(me2StartTime, null) + "开始调用任务服务接口saveMissionAttachment的请求数据是："
                    + attachmentDtos.toString());
            Response<Boolean> res = missionExecuteDubboService.saveMissionAttachment(attachmentDtos);
            log.info("\n******于时间" + DateUtils.getLogDataTime(me2StartTime, null) + "结束调用任务服务接口saveMissionAttachment的响应数据是："
                    + JSONObject.toJSONString(res)
                    + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
            if (res.getResultObject() == true) {
                JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
                String key = SYSTEM_CODE + tokenJson.getString("jmsCode");
                JSONObject dataJson = CommonUtil.getRedisUserInfo(redisJsonService, key, organizationDubboService, tokenJson.getString("jmsCode"), SYSTEM_CODE, TOKEN_OUTTIME);
                String executeId = jsonTemp.getString("TaskId");
                // 分公司Id
                String companyId = dataJson.getString("subCompanyId");
                String shopName = jsonTemp.getString("StoreName");
                long me3StartTime = System.currentTimeMillis();
                log.info("\n******于时间" + DateUtils.getLogDataTime(me3StartTime, null) + "开始调用任务服务接口executeMission的请求数据是：" + executeId
                        + " " + dataJson.getString("ytAccount") + " " + companyId + " " + shopName);
                Response<Boolean> res2 = missionExecuteDubboService.executeMission(executeId);
                log.info("\n******于时间" + DateUtils.getLogDataTime(me3StartTime, null) + "开始调用任务服务接口executeMission的响应数据是："
                        + JSONObject.toJSONString(res2)
                        + "\n*******耗时为:" + CommonUtil.costTime(meStartTime));
                if (res2.getResultObject() == true) {
                    json.put("Status", stateJson);
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
                }
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 封装任务列表json
     *
     * @param missionData
     * @return
     */
    public static JSONObject getTaskListJson(ViewMissionExecuteDto missionData) {
        JSONObject missionJson = new JSONObject();
        Map<String, Object> missionJson_Map = new HashMap<>();
        // 判断是否是关联任务
        if (missionData.isRelated()) {
            missionJson_Map.put("IsRelate", "1"); // 是关联任务
        } else {
            missionJson_Map.put("IsRelate", "0"); // 不是关联任务
        }
        missionJson_Map.put("TaskName", missionData.getName());
        // 图标
        missionJson_Map.put("Pic", missionData.getIconUrl());
        // 判断任务的分类
        if (missionData.getType().equals(EnumMissionType.MISSION_TYPE_DAILY.getCode())) {
            missionJson_Map.put("TaskClassfy", "1");// 日常任务
            missionJson_Map.put("TaskId", missionData.getId());// 主键
        } else if (missionData.getType().equals(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode())) {
            missionJson_Map.put("TaskClassfy", "2");// 推荐任务
            missionJson_Map.put("TaskId", missionData.getId());// 主键
        } else if (missionData.getType().equals(EnumMissionType.MISSION_TYPE_STUDY.getCode())) {
            missionJson_Map.put("TaskClassfy", "3");// 学习任务
            missionJson_Map.put("TaskId", missionData.getCourseId());// 主键
        }
        missionJson_Map.put("TaskRewardMsg", missionData.getSpecialAwardRemark());// 特殊奖励标注
        // 判断有无任务链接
        missionJson_Map.put("TaskUrl", missionData.getLink());// 任务链接
        // 判断任务类型
        switch (missionData.getTemplateCode().toString()) {
            case "T001":
                missionJson_Map.put("TaskType", "3");// 后台自动处理类型
                break;
            case "T002":
                missionJson_Map.put("TaskType", "4");// 本地提交单号类型
                break;
            case "T003":
                missionJson_Map.put("TaskType", "3");// 后台自动处理类型
                break;
            case "T004":
                missionJson_Map.put("TaskType", "1");// 本地任务拍门头照类型
                break;
            case "T005":
                missionJson_Map.put("TaskType", "5");// 申请租金补贴类型
                break;
            case "T006":
                missionJson_Map.put("TaskType", "2");// 本地任务拍门头照类型
                break;
            case "T007":
                missionJson_Map.put("TaskType", "6");// 签到
                break;
            case "T008":
                missionJson_Map.put("TaskType", "3");// 月度日均当面付
                break;
            case "T009":
                missionJson_Map.put("TaskType", "3");// 日当面付笔数
                break;
            case "T010":
                missionJson_Map.put("TaskType", "3");// 月度供应链采购金额
                break;
            default:
                break;
        }
        if (missionData.getAwards() != null) {
            missionJson_Map.put("IsTakeReward", "1"); // 不可领取奖励
        } else {
            missionJson_Map.put("IsTakeReward", "0"); // 可领取奖励
        }
        missionJson_Map.put("TaskExplain", missionData.getDescription());
        missionJson = com.yatang.xc.xcr.util.StringUtils.replcNULLToStr(missionJson_Map);
        return missionJson;
    }
}
