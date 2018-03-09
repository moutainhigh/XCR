package com.yatang.xc.xcr.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.TypeReference;
import com.yatang.xc.xcr.biz.mission.dto.manage.*;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionTemplate;
import com.yatang.xc.xcr.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.DateUtils;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionOrderBy;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionType;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainQueryDubboService;
import com.yatang.xc.xcr.util.CommonUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * 任务管理：任务信息后台管理功能的web服务接口
 *
 * @author gaodawei
 */
@Controller
@RequestMapping(value = "xcr/mission")
public class MissionManageAction {

    private static Logger log = LoggerFactory.getLogger(MissionManageAction.class);

    @Autowired
    private MissionDubboService missionDubboService;
    @Autowired
    private TrainQueryDubboService trainQueryDubboService;

    @Autowired
    private MissionClassroomDubboService missionClassroomDubboService;

    /**
     * 新建任务信息
     *
     * @param missionInfo
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "create", method = {RequestMethod.POST})
    public ReturnClass createMissionInfo(@RequestBody CreateMissionInfoDto missionInfo) throws IOException {

        log.info("... in xcr/mission/create ... ");
        log.info("missionInfo:" + JSON.toJSONString(missionInfo));
        ReturnClass returnClass = new ReturnClass();
        if (missionInfo.getType().equals("STUDY")) {
            //加上前台传过来的课程id
            missionInfo.setCourseId(missionInfo.getCourseId());
            missionInfo.setTemplateCode("T006");
        }

        Response<Boolean> result = missionDubboService.createMissionInfo(missionInfo);
        if (result.isSuccess()) {
            returnClass.setFlag(true);
        } else {
            returnClass.setFlag(false);
            returnClass.setErrorMessage(result.getErrorMessage());
        }
        return returnClass;
    }

    /**
     * 任务详情展示
     *
     * @return
     */
    @RequestMapping(value = "{id}/missionDetail.htm", method = RequestMethod.GET)
    public ModelAndView getMissionDetail(@PathVariable Long id, ModelMap map) {
        System.err.println("id:" + id);
        MissionInfoQueryDto queryDto = new MissionInfoQueryDto();
        queryDto.setId(id);
        Response<List<ViewMissionInfoDto>> result = missionDubboService.queryMissionInof(queryDto);
        List<ViewMissionInfoDto> list = result.getResultObject();
        if (!CollectionUtils.isEmpty(list)) {
            ViewMissionInfoDto dto = list.get(0);
            log.info("ViewMissionInfoDto:" + JSONObject.toJSONString(dto));
            map.put("missionDetail", dto);
            if (dto != null) {
                List<AwardInfoDto> awardInfoDtos = dto.getAwards();
                if (!CollectionUtils.isEmpty(awardInfoDtos)) {
                    AwardInfoDto awardInfoDto = awardInfoDtos.get(0);
                    map.put("grantNum", awardInfoDto.getGrantNum().intValue());
                }
            }
        }
        return new ModelAndView("screen/mission/normalMissionDetail", map);
//        return "screen/mission/normalMissionDetail";
    }


    /**
     * @param missionId
     * @throws IOException
     * @Description：任务发布
     */
    @ResponseBody
    @RequestMapping(value = "publish", method = {RequestMethod.POST})
    public Boolean publishMissionInfo(String missionId) throws IOException {
        Response<Boolean> result = missionDubboService.publishMissionInfo(missionId);
        return result.isSuccess();
    }

    /**
     * @param missionType
     * @param response
     * @throws IOException
     * @Description：按类型查询任务模板
     */
    @RequestMapping(value = "queryByMisType", method = {RequestMethod.POST})
    public void queryMissionTemplateByMissionType(String missionType, HttpServletResponse response) throws IOException {
        log.info("queryByMisType -> missionType:" + missionType);
        if (missionType.equals(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode())
                || missionType.equals(EnumMissionType.MISSION_TYPE_DAILY.getCode())) {
            Response<List<MissionTemplateDto>> result = missionDubboService
                    .queryMissionTemplateByMissionType(missionType);
            response.getWriter().println(JSONObject.toJSONString(result.getResultObject()));
            return;
        }
        if (missionType.equals(EnumMissionType.MISSION_TYPE_STUDY.getCode())) {
            Response<List<Map<String, Object>>> result = trainQueryDubboService.getListPublishedTrain();
            List<Map<String, Object>> listCopy = new ArrayList<>();
            List<Map<String, Object>> list = result.getResultObject();
            for (Map<String, Object> map : list) {
                Response<ViewMissionExecuteDto> response2 = missionClassroomDubboService
                        .queryMissionExecuteByMerchantIdAndCourseId(null, map.get("id").toString());
                if (response2.getResultObject() == null) {
                    listCopy.add(map);
                }
            }
            response.getWriter().print(JSONObject.toJSONString(listCopy));
            return;
        }
    }

    /**
     * @param queryDto
     * @throws IOException
     * @Description：条件查询任务信息
     */
    @ResponseBody
    @RequestMapping(value = "queryBy", method = {RequestMethod.POST})
    public PageResultModel<MissionListVO> queryMissionInof(MissionInfoQueryDto queryDto, @RequestBody Map map)
            throws IOException {

        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        log.info("queryBy-> pageIndex:" + pageIndex + "   pageSize:" + pageSize);
        pageIndex = pageIndex == null ? 0 : pageIndex;
        pageSize = pageSize == null ? 10 : pageSize;

        queryDto.setStartIndex(pageIndex);
        queryDto.setEndIndex(pageSize);
        queryDto.setOrderBy(EnumMissionOrderBy.TYPE_TIME_DESC.getCode());
        queryDto.setIgnoreDetail(true);
        Response<List<ViewMissionInfoDto>> result = missionDubboService.queryMissionInof(queryDto);

        PageResultModel<MissionListVO> pageResult = new PageResultModel<>();

        List<ViewMissionInfoDto> responseData = result.getResultObject();
        List<MissionListVO> missionListVOList = InitMissionListVO(responseData);

        int total = missionDubboService.queryMissionInofCount(queryDto).getResultObject();
        pageResult.setData(missionListVOList);
        pageResult.setRows(total);
        pageResult.setTotal(total);
        return pageResult;
    }

    /**
     * 根据Id获取任务详情
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMissionById", method = RequestMethod.POST)
    public MissionListVO getMissionById(MissionInfoQueryDto queryDto) {
        log.info("in getMissionById ... queryDto:" + queryDto);
        if (queryDto == null || queryDto.getId() == null) {
            log.error("getMissionById -> queryDto == null or queryDto.getId() == null !!!");
            return null;
        }
        Response<List<ViewMissionInfoDto>> result = missionDubboService.queryMissionInof(queryDto);
        if (result == null) {
            log.error("getMissionById -> missionDubboService.queryMissionInof(queryDto) ->result == null !!!");
            return null;
        }
        List<ViewMissionInfoDto> viewMissionInfoDtos = result.getResultObject();
        if (CollectionUtils.isEmpty(viewMissionInfoDtos)) {
            return null;
        }
        ViewMissionInfoDto dto = viewMissionInfoDtos.get(0);
        log.info("ViewMissionInfoDto dto:" + dto);
        MissionListVO vo = new MissionListVO();
        vo.setDescription(dto.getDescription());
        vo.setContinueDate(dto.getContinueDate());
        vo.setBuyAmount(dto.getBuyAmount());
        vo.setFacePayNumber(dto.getFacePayNumber());
        vo.setFacePayAmount(dto.getFacePayAmount());
        return vo;
    }

    /**
     * @param queryDto
     * @param response
     * @throws IOException
     * @Description：条件查询任务信息
     */
    @ResponseBody
    @RequestMapping(value = "getRelMis", method = {RequestMethod.POST})
    public void queryMissionInof(MissionInfoQueryDto queryDto, HttpServletResponse response) throws IOException {
        if (queryDto != null) {
            queryDto.setIgnoreDetail(true);
        }
        Response<List<ViewMissionInfoDto>> result = missionDubboService.queryMissionInof(queryDto);
        response.getWriter().print(JSONObject.toJSONString(result.getResultObject()));
    }

    /**
     * @param missionId
     * @param response
     * @throws IOException
     * @Description：删除任务
     */
    @ResponseBody
    @RequestMapping(value = "remove", method = {RequestMethod.POST, RequestMethod.GET})
    public void removeMissionInfo(Integer missionId, HttpServletResponse response) throws IOException {

        System.out.println("missionId:" + missionId);
        if (missionId == null || missionId == 0) {
            response.getWriter().print(0);
        }

        Response<Boolean> result = missionDubboService.removeMissionInfo(String.valueOf(missionId));
        if (result.isSuccess()) {
            response.getWriter().print(1);
        } else {
            response.getWriter().print(0);
        }
    }

    /**
     * 创建任务关联
     *
     * @param dto
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "createRel", method = {RequestMethod.POST})
    public void createRelatedMission(@RequestBody CreateMissionRelatedDto dto, HttpServletResponse response)
            throws IOException {
        System.err.println("接收到的参数为：    " + dto.toString());
        Response<Boolean> result = missionDubboService.createRelatedMission(dto);
        if (result.isSuccess()) {
            response.getWriter().print(1);
        } else if (result.getCode().equals("BUSERROR017")) {
            response.getWriter().print(2);
        } else {
            response.getWriter().print(0);
        }
    }

    /**
     * @param relatedId
     * @param response
     * @throws IOException
     * @Description： 发布关联任务
     */
    @RequestMapping(value = "publicRel", method = {RequestMethod.POST})
    public void publishRelatedMission(String relatedId, HttpServletResponse response) throws IOException {
        Response<Boolean> result = missionDubboService.publishRelatedMission(relatedId);
        if (result.isSuccess()) {
            response.getWriter().println(1);
        }
    }

    /**
     * @param relatedId
     * @param response
     * @throws IOException
     * @Description：未发布关联任务删除，已发布关联任务不能删除
     */
    @RequestMapping(value = "removeRel", method = {RequestMethod.POST})
    public void removeMissionRelate(String relatedId, HttpServletResponse response) throws IOException {
        Response<Boolean> result = missionDubboService.removeMissionRelate(relatedId);
        if (result.isSuccess()) {
            response.getWriter().println(1);
        } else {
            response.getWriter().print(0);
        }
    }

    /**
     * @param queryDto
     * @throws IOException
     * @Description：条件查询任务关联信息
     */
    @ResponseBody
    @RequestMapping(value = "queryRelBy", method = {RequestMethod.POST})
    public PageResultModel<MissionRelListVO> queryMissionRelate(MissionRelateQueryDto queryDto, @RequestBody Map map) throws IOException {

        log.info("in queryMissionRelate ... ");
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        pageIndex = pageIndex == null ? 0 : pageIndex;
        pageSize = pageSize == null ? 10 : pageSize;
        log.info("queryRelBy-> pageIndex:" + pageIndex + "   pageSize:" + pageSize);

        queryDto.setStartIndex(pageIndex);
        queryDto.setEndIndex(pageSize);
        queryDto.setOrderBy(EnumMissionOrderBy.TYPE_TIME_DESC.getCode());
        Response<List<ViewMissionRelatedDto>> result = missionDubboService.queryMissionRelate(queryDto);
        if (result == null) {
            log.error("missionDubboService.queryMissionRelate(queryDto) -> result == null");
            return new PageResultModel<>();
        }
        PageResultModel<MissionRelListVO> pageResultModel = new PageResultModel<>();
        List<ViewMissionRelatedDto> dtoList = result.getResultObject();
        List<MissionRelListVO> voList = InitMissionRelListVO(dtoList);

        pageResultModel.setData(voList);
        int total = missionDubboService.queryMissionRelateCount(queryDto).getResultObject();
        pageResultModel.setTotal(total);
        pageResultModel.setRows(total);
        return pageResultModel;
    }

    /**
     * @param queryDto
     * @param response
     * @throws IOException
     * @Description：条件查询任务信息数目
     */
    @RequestMapping(value = "queryCount", method = {RequestMethod.POST})
    public void queryMissionInofCount(@RequestBody MissionInfoQueryDto queryDto, HttpServletResponse response)
            throws IOException {
        Response<Integer> result = missionDubboService.queryMissionInofCount(queryDto);
        response.getWriter().println(result.getResultObject());
    }

    /**
     * 条件查询任务关联信息数目
     *
     * @param queryDto
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "queryRelCount", method = {RequestMethod.POST})
    public void queryMissionRelateCount(@RequestBody MissionInfoQueryDto queryDto, HttpServletResponse response)
            throws IOException {
        Response<Integer> result = missionDubboService.queryMissionInofCount(queryDto);
        response.getWriter().println(result.getResultObject());
    }

    /**
     * @param dto
     * @throws IOException
     * @description 更新任务信息
     */
    @ResponseBody
    @RequestMapping(value = "update", method = {RequestMethod.POST})
    public ReturnClass updateMissionInfo(@RequestBody CreateMissionInfoDto dto)
            throws IOException {
        log.info("dto:" + JSON.toJSONString(dto));
        Response<Boolean> result = missionDubboService.updateMissionInfo(dto);
        ReturnClass returnClass = new ReturnClass();
        returnClass.setFlag(result.isSuccess());
        returnClass.setErrorMessage(result.getErrorMessage());
        return returnClass;
    }

    /**
     * @param dto
     * @param response
     * @throws IOException
     * @description 更新关联任务信息
     */
    @RequestMapping(value = "updateRel", method = {RequestMethod.POST})
    public void updateRelatedMission(@RequestBody CreateMissionRelatedDto dto, HttpServletResponse response)
            throws IOException {
        Response<Boolean> result = missionDubboService.updateRelatedMission(dto);
        response.getWriter().println(result.getResultObject());
    }

    /**
     * 组装页面表格数据(普通任务列表)
     *
     * @param responseData
     * @return
     */
    private List<MissionListVO> InitMissionListVO(List<ViewMissionInfoDto> responseData) {
        if (CollectionUtils.isEmpty(responseData)) {
            return new ArrayList<>();
        }
        List<MissionListVO> missionListVOList = new ArrayList<>();
        for (ViewMissionInfoDto dto : responseData) {
            MissionListVO vo = new MissionListVO();

            //取值校验
            boolean related = dto.isRelated();
            String type = dto.getType(); //任务分类
            type = type == null ? "" : type;
            String awardType = dto.getAwardType();  //任务奖励
            List<com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto> awardInfoDtoList = dto.getAwards();
            double GrantNum = 0;
            if (!CollectionUtils.isEmpty(awardInfoDtoList)) {
                GrantNum = awardInfoDtoList.get(0).getGrantNum();
            }
            awardType = awardType == null ? "" : awardType;
            Boolean needManuaAudit = dto.getNeedManualAudit(); //审核类型
            needManuaAudit = needManuaAudit == null ? false : needManuaAudit;
            String status = dto.getStatus(); //任务状态
            status = status == null ? "" : status;
            Date lastTime = dto.getLastModifyTime(); //最后更新时间
            Date durationTimeStart = dto.getValidTimeStart();
            Date durationTimeEnd = dto.getValidTimeEnd();
            Date validTimeStart = dto.getDurationTimeStart();
            Date validTimeEnd = dto.getDurationTimeEnd();
            String durationTimeStartStr, durationTimeEndStr, validTimeStartStr, validTimeEndStr, usefulTime;

            //赋值给vo
            vo.setTemplateCode(dto.getTemplateCode());
            vo.setTemplateName(dto.getTemplateName());
            vo.setDescription(dto.getDescription());
            vo.setIconUrl(dto.getIconUrl());
            vo.setAwards(dto.getAwards());
            vo.setCourseId(dto.getCourseId());
            vo.setId(dto.getId());
            vo.setName(dto.getName());
            vo.setRelated(related ? "关联任务" : "普通任务");
            vo.setType(type.equals("DAILY") ? "日常任务" : type.equals("RECOMMEND") ? "推荐任务" : type.equals("STUDY") ? "学习任务" : "");
            vo.setAwardType(awardType.equals("CASH") ? "现金奖励" : awardType.equals("SCORE") ? String.valueOf((int) GrantNum) + "积分" : awardType.equals("CASH_AND_SCORE") ? "现金及积分" : "无奖励");
            vo.setNeedManualAudit(needManuaAudit ? "人工审核" : "系统审核");
            vo.setStatus(status.equals("INIT") ? "未发布" : status.equals("PUBLISH") ? "已发布" : status.equals("CANCEL") ? "下架/取消" : status.equals("DAY_PUBLISH") ? "每日任务-已发布" : "-");
            vo.setLastModifyTime(CommonUtil.getDateString(lastTime));

            if (durationTimeStart != null && durationTimeEnd != null) {
                durationTimeStartStr = DateUtils.dateSimpleFormat(durationTimeStart);
                durationTimeEndStr = DateUtils.dateSimpleFormat(durationTimeEnd);
                vo.setDurationTimeStart(durationTimeStartStr);
                vo.setDurationTimeEnd(durationTimeEndStr);
            }
            if (validTimeStart != null && validTimeEnd != null) {
                validTimeStartStr = DateUtils.dateSimpleFormat(validTimeStart);
                validTimeEndStr = DateUtils.dateSimpleFormat(validTimeEnd);
                usefulTime = validTimeStartStr + "~" + validTimeEndStr;
                vo.setValidTimeStart(validTimeStartStr);
                vo.setValidTimeEnd(validTimeEndStr);
                vo.setUsefulTime(usefulTime);
                if (dto.getTemplateCode().equals(EnumMissionTemplate.DAY_FACE_PAY.getCode())) {
                    vo.setUsefulTime("永久");
                }
            } else {
                vo.setUsefulTime("没有设置有效期");
            }
            missionListVOList.add(vo);
        }
        return missionListVOList;
    }

    /**
     * 组装页面表格数据(关联任务列表)
     *
     * @param dtoList
     * @return
     */
    private List<MissionRelListVO> InitMissionRelListVO(List<ViewMissionRelatedDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        List<MissionRelListVO> missionRelVOList = new ArrayList<>();
        for (ViewMissionRelatedDto dto : dtoList) {

            MissionRelListVO vo = new MissionRelListVO();
            String status = dto.getStatus();
            status = status == null ? "" : status;
            Date lastModifyTime = dto.getLastModifyTime();

            vo.setId(dto.getId());
            vo.setMissonRelatedName(dto.getMissonRelatedName());
            vo.setMissonRelatedDescription(dto.getMissonRelatedDescription());
            vo.setStatus(status.equals("INIT") ? "未发布" : status.equals("PUBLISH") ? "已发布" : status.equals("CANCEL") ? "下架/取消" : "-");
            vo.setDetails(dto.getDetails());
            vo.setLastModifyTime(CommonUtil.getDateString(lastModifyTime));
            missionRelVOList.add(vo);
        }
        return missionRelVOList;
    }
}


