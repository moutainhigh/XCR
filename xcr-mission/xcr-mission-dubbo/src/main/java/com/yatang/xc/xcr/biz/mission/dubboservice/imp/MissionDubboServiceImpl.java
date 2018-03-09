package com.yatang.xc.xcr.biz.mission.dubboservice.imp;

import com.alibaba.fastjson.JSON;
import com.busi.common.exception.BusinessException;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.bo.MissionInfoQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionRelatedQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.*;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionAwardDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.*;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionDubboService;
import com.yatang.xc.xcr.biz.mission.enums.*;
import com.yatang.xc.xcr.biz.mission.flow.MissionEngineService;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.RuleCalculate;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.DayFacePayRuleCreator;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.MonthBuyAmountRuleCreator;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.MonthFacePayRuleCreator;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SignRuleCreator;
import com.yatang.xc.xcr.biz.mission.service.MissionAwardService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionRelatedService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务dubbo服务
 *
 * @author yangqingsong
 */
@Service("missionDubboService")
@Transactional(propagation = Propagation.REQUIRED)
public class MissionDubboServiceImpl implements MissionDubboService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionEngineService missionEngineService;

    @Autowired
    private MissionAwardService missionAwardService;

    @Autowired
    private MissionRelatedService missionRelatedService;

    @Autowired
    private RuleCalculate ruleCalculate;

    @Autowired
    private MissionExecuteService missionExecuteService;

    /**
     * @param CreateMissionInfoDto
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：新建任务信息
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> createMissionInfo(CreateMissionInfoDto dto) {

        log.info("新建任务信息 -> CreateMissionInfoDto:" + dto);

        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        if (!checkCreateMission(dto)) {
            result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
            result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
            return result;
        }
        if (dto.isPublish() && dto.isRelated()) {
            result.setCode(EnumError.ERROR_BUSINESS_RELATED_PUBLISH.getCode());
            result.setErrorMessage(EnumError.ERROR_BUSINESS_RELATED_PUBLISH.getMessage());
            return result;
        }
        try {
            boolean checkName = missionService.checkMissionName(dto.getName());
            if (!checkName) {
                result.setCode(EnumError.ERROR_BUSINESS_NAME_CHECK_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_NAME_CHECK_ERROR.getMessage());
                return result;
            }
            MissionTemplatePO template = missionService.selectTemplateByCode(dto.getTemplateCode());
            if (template == null) {
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_TEMPLATE_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_TEMPLATE_ERROR.getMessage());
                return result;
            }
            if (dto.isRelated() && template.isSchedule()) {
                result.setCode(EnumError.ERROR_BUSINESS_RELATED_CREATE.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_RELATED_CREATE.getMessage());
                return result;
            }
            if (!StringUtils.isEmpty(dto.getCourseId())) {
                List<MissionInfoPO> list = missionService.queryMissionByCourseId(dto.getCourseId(), null);
                if (list != null) {
                    for (MissionInfoPO info : list) {
                        if (!info.isDeleted()) {
                            result.setCode(EnumError.ERROR_BUSINESS_COURSEID_DUPLICATE.getCode());
                            result.setErrorMessage(EnumError.ERROR_BUSINESS_COURSEID_DUPLICATE.getMessage());
                            return result;
                        }
                    }
                }
            }
            MissionInfoPO mission = createMissionInfoPO(template, dto);
            String durationTimeStart = dto.getDurationTimeStart();
            String durationTimeEnd = dto.getDurationTimeEnd();
            String validTimeStart = dto.getValidTimeStart();
            String validTimeEnd = dto.getValidTimeEnd();

            log.info("durationTimeStart:" + durationTimeStart + "  durationTimeEnd:" + durationTimeEnd + "  validTimeStart:" + validTimeStart + "  validTimeEnd:" + validTimeEnd);

            mission.setDurationTimeStart(DateUtil.getDateTimeByString(durationTimeStart));
            mission.setDurationTimeEnd(DateUtil.getDateTimeByString(durationTimeEnd));
            mission.setValidTimeStart(DateUtil.getDateTimeByString(validTimeStart));
            mission.setValidTimeEnd(DateUtil.getDateTimeByString(validTimeEnd));

            mission.setSort(((Long) new Date().getTime()).intValue());
            List<MissionAwardPO> awards = createAwardInfoDto(dto);
            List<MissionAttachmentPO> attachements = createAattachementDto(dto);
            mission.setStatus(EnumMissionStatus.STATUS_INIT.getCode());
            if (dto.isPublish()) {
                if (dto.getTemplateCode().equals(EnumMissionTemplate.DAY_FACE_PAY.getCode())) {
                    mission.setStatus(EnumMissionStatus.STATUS_DAY_PUBLISH.getCode());
                } else {
                    mission.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
                }
            }

            log.info("创建任务 -> missionService.createMissionInfo(mission, awards,attachements) -> mission:" + mission);
            log.info("创建任务 -> missionService.createMissionInfo(mission, awards,attachements) -> awards:" + awards);
            log.info("创建任务 -> missionService.createMissionInfo(mission, awards,attachements) -> attachements:" + attachements);
            missionService.createMissionInfo(mission, awards, attachements);
            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }

    private List<MissionAttachmentPO> createAattachementDto(CreateMissionInfoDto dto) {
        List<AttachmentDto> list = dto.getAttachments();
        List<MissionAttachmentPO> attachments = new ArrayList<MissionAttachmentPO>();
        if (list != null && !list.isEmpty()) {
            for (AttachmentDto a : list) {
                MissionAttachmentPO po = new MissionAttachmentPO();
                BeanUtils.copyProperties(a, po);
                po.setAttachmentCode(a.getType() + ":" + a.getMissionInfoId() + ":" + a.getName());
                attachments.add(po);
            }
        }
        return attachments;
    }

    /**
     * @param template
     * @param dto
     * @return MissionInfoPO: 返回值类型
     * @throws BusinessException: 返回结果描述
     * @throws
     * @Description：组装需任务数据
     */
    private MissionInfoPO createMissionInfoPO(MissionTemplatePO template, CreateMissionInfoDto dto) {

        log.info("组装需任务数据 -> createMissionInfoPO -> MissionTemplatePO template:" + template);
        log.info("组装需任务数据 -> createMissionInfoPO -> CreateMissionInfoDto dto:" + dto);

        MissionInfoPO mission = new MissionInfoPO();
        BeanUtils.copyProperties(template, mission, "id");
        BeanUtils.copyProperties(dto, mission, "id");
        if (StringUtils.isEmpty(mission.getLink())) {
            mission.setLink(template.getLink());
        }
        mission.setMissonTemplateCode(template.getTemplateCode());
        BuildMissionRuleDto ruleDto = new BuildMissionRuleDto();
        ruleDto.setRuleTemplate(template.getRuleTemplate());
        ruleDto.setRuleType(template.getType());
        ruleDto.setRuleTemplate(template.getRuleTemplate());
        ruleDto.setDto(dto);
        log.info("组装需任务数据 -> BuildMissionRuleDto ruleDto :" + ruleDto);
        String rule = missionEngineService.buildMissionRule(ruleDto);

        mission.setRule(rule);
        mission.setStatus(EnumMissionStatus.STATUS_INIT.getCode());
        mission.setDeleted(false);

        if (dto.isPublish() && !dto.isRelated()) {
            mission.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
        }
        log.info("组装需任务数据 -> 返回的MissionInfoPO mission：" + mission);
        return mission;
    }

    /**
     * @param dto
     * @return List<MissionAwardPO>: 返回值类型
     * @throws
     * @Description：组装奖励数据
     * @return: 返回结果描述
     */
    private List<MissionAwardPO> createAwardInfoDto(CreateMissionInfoDto dto) {
        List<AwardInfoDto> list = dto.getAwards();
        List<MissionAwardPO> awards = new ArrayList<MissionAwardPO>();
        if (list != null && !list.isEmpty()) {
            for (AwardInfoDto awardDto : list) {
                MissionAwardPO ward = new MissionAwardPO();
                BeanUtils.copyProperties(awardDto, ward);
                awards.add(ward);
            }
        }
        return awards;
    }

    /**
     * @param CreateMissionInfoDto
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：更新任务信息
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> updateMissionInfo(CreateMissionInfoDto dto) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        /**
         * dto: {
         "awardType": "SCORE",
         "awards": [{
         "awardType": "SCORE",
         "grantNum": 100,
         "grantStyle": ""
         }],
         "description": "连续签到10次，得100积分!!!!!!!!!!!",
         "durationTimeEnd": "2017-07-31",
         "durationTimeStart": "2017-07-01",
         "iconUrl": "http://sit.image.com/group1/M00/00/EF/rB4KPFlkRUqASpp4AACcHG0EWfU483.jpg",
         "id": 625,
         "name": "连续签到10次可以吗",
         "publish": false,
         "related": false,
         "templateCode": "T007",
         "type": "DAILY",
         "validTimeEnd": "2017-07-20",
         "validTimeStart": "2017-07-10"
         }
         */
        if (!checkCreateMission(dto, true)) {
            result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
            result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
            return result;
        }
        if (dto.isPublish() && dto.isRelated()) {
            result.setCode(EnumError.ERROR_BUSINESS_RELATED_PUBLISH.getCode());
            result.setErrorMessage(EnumError.ERROR_BUSINESS_RELATED_PUBLISH.getMessage());
            return result;
        }
        try {
            MissionTemplatePO template = missionService.selectTemplateByCode(dto.getTemplateCode());
            if (template == null) {
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_TEMPLATE_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_TEMPLATE_ERROR.getMessage());
                return result;
            }
            if (dto.isRelated() && template.isSchedule()) {
                result.setCode(EnumError.ERROR_BUSINESS_RELATED_CREATE.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_RELATED_CREATE.getMessage());
                return result;
            }
            MissionInfoPO mission = createMissionInfoPO(template, dto);
            if (dto.isPublish()) {
                mission.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
            }
            mission.setSort(null);
            mission.setId(dto.getId());
            List<MissionAwardPO> awards = createAwardInfoDto(dto);
            List<MissionAttachmentPO> attachements = createAattachementDto(dto);


            String durationTimeStart = dto.getDurationTimeStart();
            String durationTimeEnd = dto.getDurationTimeEnd();
            String validTimeStart = dto.getValidTimeStart();
            String validTimeEnd = dto.getValidTimeEnd();

            log.info("durationTimeStart:" + durationTimeStart + "  durationTimeEnd:" + durationTimeEnd + "  validTimeStart:" + validTimeStart + "  validTimeEnd:" + validTimeEnd);

            mission.setDurationTimeStart(DateUtil.getDateTimeByString(durationTimeStart));
            mission.setDurationTimeEnd(DateUtil.getDateTimeByString(durationTimeEnd));
            mission.setValidTimeStart(DateUtil.getDateTimeByString(validTimeStart));
            mission.setValidTimeEnd(DateUtil.getDateTimeByString(validTimeEnd));

            log.info("更新任务传入参数 -> mission：" + mission);
            log.info("更新任务传入参数 -> awards：" + awards);
            log.info("更新任务传入参数 -> attachements：" + attachements);


            mission.setSpecialAwardRemark(awards.get(0).getGrantNum().intValue() + "积分");
            missionService.updateMissionInfo(mission, awards, attachements);
            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }

    private boolean checkCreateMission(CreateMissionInfoDto dto) {
        return checkCreateMission(dto, false);
    }

    private boolean checkCreateMission(CreateMissionInfoDto dto, boolean checkId) {
        if (dto == null) {
            return false;
        }
        if (StringUtils.isEmpty(dto.getAwardType())) {
            return false;
        }
        if (StringUtils.isEmpty(dto.getName())) {
            return false;
        }
        if (StringUtils.isEmpty(dto.getTemplateCode())) {
            return false;
        }
        if (StringUtils.isEmpty(dto.getType())) {
            return false;
        }
        if (checkId && dto.getId() == null) {
            return false;
        }
        return true;
    }

    /**
     * @param missionId
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：任务发布
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> publishMissionInfo(String missionId) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            if (!checkLongId(missionId)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionInfoPO mission = missionService.findById(Long.valueOf(missionId));
            if (mission == null) {
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
                return result;
            }
            if (mission.isRelated()) {
                result.setCode(EnumError.ERROR_BUSINESS_RELATED_PUBLISH.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_RELATED_PUBLISH.getMessage());
                return result;
            }

            UpdateStatusQuery query = new UpdateStatusQuery();
            query.setId(Long.valueOf(missionId));
            String missonTemplateCode = mission.getMissonTemplateCode();
            if (StringUtils.isEmpty(missonTemplateCode)) {
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_TEMPLATE_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_TEMPLATE_ERROR.getMessage());
                return result;
            }
            query.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
            if (missonTemplateCode.equals(EnumMissionTemplate.DAY_FACE_PAY.getCode())) {
                query.setStatus(EnumMissionStatus.STATUS_DAY_PUBLISH.getCode());
            }
            query.setOldStatus(new String[]{EnumMissionStatus.STATUS_INIT.getCode(), EnumMissionStatus.STATUS_CANCEL.getCode()});
            missionService.updateMissionInfoStatus(query);

            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }


    private boolean checkLongId(String missionId) {
        if (StringUtils.isEmpty(missionId)) {
            return false;
        }
        if (!StringUtils.isNumeric(missionId)) {
            return false;
        }
        return true;
    }

//    /**
//     * 
//     * @Description：任务下架/取消
//     * @param missionIds
//     * @return: 返回结果描述
//     * @return Response<Boolean>: 返回值类型
//     * @throws
//     */
////    @Override
//    public Response<Boolean> cancelMissionInfo(String[] missionIds) {
//        Response<Boolean> result = new Response<Boolean>();
//        result.setSuccess(false);
//        try {
//            for (String missionId : missionIds) {
//                UpdateStatusQuery query = new UpdateStatusQuery();
//                query.setId(Long.valueOf(missionId));
//                query.setStatus(EnumMissionStatus.STATUS_CANCEL.getCode());
//                query.setOldStatus(new String[] {EnumMissionStatus.STATUS_PUBLISH.getCode()});
//                missionService.updateMissionInfoStatus(query);
//            }
//            result.setResultObject(true);
//            result.setSuccess(true);
//        } catch (Exception e) {
//            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
//            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
//            log.error(ExceptionUtils.getFullStackTrace(e));
//        }
//        return result;
//    }


    /**
     * @param type
     * @return Response<List<MissionTemplateDto>>: 返回值类型
     * @throws
     * @Description：按类型查询任务模板
     * @return: 返回结果描述
     */
    @Override
    public Response<List<MissionTemplateDto>> queryMissionTemplateByMissionType(String missionType) {
        Response<List<MissionTemplateDto>> result = new Response<List<MissionTemplateDto>>();
        result.setSuccess(false);
        try {
            List<MissionTemplatePO> list = missionService.queryMissionTemplateByMissionType(missionType);
            List<MissionTemplateDto> dtos = new ArrayList<MissionTemplateDto>();
            if (list != null && !list.isEmpty()) {
                for (MissionTemplatePO template : list) {
                    log.info("queryMissionTemplateByMissionType -> 要查询的任务类型：missionType" + missionType);
                    log.info("queryMissionTemplateByMissionType -> 查询到的任务模板有：template" + template);
                    MissionTemplateDto dto = new MissionTemplateDto();
                    BeanUtils.copyProperties(template, dto);
                    dtos.add(dto);
                }
            }
            result.setResultObject(dtos);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }


    /**
     * @param
     * @return Response<List<ViewMissionInfoDto>>: 返回值类型
     * @throws
     * @Description：条件查询任务信息
     * @return: 返回结果描述
     */
    @Override
    public Response<List<ViewMissionInfoDto>> queryMissionInof(MissionInfoQueryDto queryDto) {
        Response<List<ViewMissionInfoDto>> result = new Response<List<ViewMissionInfoDto>>();
        result.setSuccess(false);
        try {
            if (!checkMissionQuery(queryDto)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionInfoQuery query = new MissionInfoQuery();
            BeanUtils.copyProperties(queryDto, query);
            log.info("查询任务列表参数 -> Response<List<ViewMissionInfoDto>> queryMissionInof(MissionInfoQueryDto queryDto) query:" + query);
            List<MissionInfoPO> list = missionService.queryMissionInfo(query);

            List<ViewMissionInfoDto> dtos = new ArrayList<ViewMissionInfoDto>();
            if (list != null && !list.isEmpty()) {
                for (MissionInfoPO po : list) {
                    log.info("查询任务列表返回结果 -> MissionInfoPO po:" + po);

                    //规则引擎中获取存入数据,目前只有签到任务使用了该引擎
                    String templateCode = po.getMissonTemplateCode();

                    MissionTemplatePO template = missionService.selectTemplateByCode(po.getMissonTemplateCode());
                    ViewMissionInfoDto dto = new ViewMissionInfoDto();
                    if (EnumMissionRuleType.TYPE_SIGN.getCode().equals(template.getType())) {
                        String ruleStr = po.getRule();
                        RuleDefinitionDto ruleDto = this.ruleCalculate.calcDefinition(ruleStr);
                        //1：签到任务
                        if (ruleDto instanceof SignRuleCreator.RuleDefinition) {
                            SignRuleCreator.RuleDefinition signruleDto = (SignRuleCreator.RuleDefinition) ruleDto;
                            int continueDate = signruleDto.getContinueDate();
                            dto.setContinueDate(continueDate);
                            log.info("签到任务 -> 解析rule -> ruleDto:" + JSON.toJSON(ruleDto));
                            log.info("签到任务 -> 解析rule -> signruleDto:" + JSON.toJSON(signruleDto));
                        }
                    }
                    if (EnumMissionRuleType.TYPE_DAY_FACE_PAY.getCode().equals(template.getType())) {
                        String ruleStr = po.getRule();
                        RuleDefinitionDto ruleDto = this.ruleCalculate.calcDefinition(ruleStr);
                        //2:日当面付任务
                        if (ruleDto instanceof DayFacePayRuleCreator.RuleDefinition) {
                            DayFacePayRuleCreator.RuleDefinition dayFacePayDto = (DayFacePayRuleCreator.RuleDefinition) ruleDto;
                            int facePayNumber = dayFacePayDto.getFacePayNumber();
                            dto.setFacePayNumber(facePayNumber);
                            log.info("日当面付任务 -> 解析rule -> ruleDto:" + JSON.toJSON(ruleDto));
                            log.info("日当面付任务 -> 解析rule -> signruleDto:" + JSON.toJSON(dayFacePayDto));
                        }
                    }
                    if (EnumMissionRuleType.TYPE_MONTH_FACE_PAY.getCode().equals(template.getType())) {
                        String ruleStr = po.getRule();
                        RuleDefinitionDto ruleDto = this.ruleCalculate.calcDefinition(ruleStr);
                        //3:月当面付金额任务
                        if (ruleDto instanceof MonthFacePayRuleCreator.RuleDefinition) {
                            MonthFacePayRuleCreator.RuleDefinition monthFacePayDto = (MonthFacePayRuleCreator.RuleDefinition) ruleDto;
                            double facePayAmount = monthFacePayDto.getFacePayAmount();
                            dto.setFacePayAmount(facePayAmount);
                            log.info("月当面付金额任务 -> 解析rule -> ruleDto:" + JSON.toJSON(ruleDto));
                            log.info("月当面付金额任务 -> 解析rule -> signruleDto:" + JSON.toJSON(monthFacePayDto));
                        }
                    }
                    if (EnumMissionRuleType.TYPE_MONTH_BUY_AMOUNT.getCode().equals(template.getType())) {
                        String ruleStr = po.getRule();
                        RuleDefinitionDto ruleDto = this.ruleCalculate.calcDefinition(ruleStr);
                        //4:月供应链采购金额任务
                        if (ruleDto instanceof MonthBuyAmountRuleCreator.RuleDefinition) {
                            MonthBuyAmountRuleCreator.RuleDefinition monthBuyAmountDto = (MonthBuyAmountRuleCreator.RuleDefinition) ruleDto;
                            double buyAmount = monthBuyAmountDto.getBuyAmount();
                            dto.setBuyAmount(buyAmount);
                            log.info("月供应链采购金额任务 -> 解析rule -> ruleDto:" + JSON.toJSON(ruleDto));
                            log.info("月供应链采购金额任务 -> 解析rule -> signruleDto:" + JSON.toJSON(monthBuyAmountDto));
                        }
                    }
                    BeanUtils.copyProperties(po, dto);
                    dto.setNeedManualAudit(template.isManualAudit());
                    dto.setTemplateCode(templateCode);
                    dto.setTemplateName(template.getName());

                    List<MissionAwardPO> awards = missionAwardService.queryAwardByMissionId(po.getId());
                    String awardType = EnumAwardType.AWARD_TYPE_NONE.getCode();
                    List<String> awardTypes = new ArrayList<String>();
                    if (awards != null && !awards.isEmpty()) {
                        List<AwardInfoDto> awardDtos = new ArrayList<AwardInfoDto>();
                        for (MissionAwardPO award : awards) {
                            awardType = award.getAwardType();
                            awardTypes.add(award.getAwardType());
                            AwardInfoDto awrdDto = new AwardInfoDto();
                            BeanUtils.copyProperties(award, awrdDto);
                            awardDtos.add(awrdDto);
                        }
                        if (awardTypes.contains(EnumAwardType.AWARD_TYPE_CASH.getCode())
                                && awardTypes.contains(EnumAwardType.AWARD_TYPE_SCORE.getCode())) {
                            awardType = EnumAwardType.AWARD_TYPE_CASH_AND_SCORE.getCode();
                        }
                        dto.setAwards(awardDtos);
                    }
                    dto.setAwardType(awardType);
                    if (!queryDto.isIgnoreDetail()) {
                        List<MissionAttachmentPO> attachments = missionService.queryAttachmentByMissionId(po.getId());
                        if (attachments != null && !attachments.isEmpty()) {
                            List<AttachmentDto> attachmentDtos = new ArrayList<AttachmentDto>();
                            for (MissionAttachmentPO a : attachments) {
                                AttachmentDto aDto = new AttachmentDto();
                                BeanUtils.copyProperties(a, aDto);
                                attachmentDtos.add(aDto);
                            }
                            dto.setAttachments(attachmentDtos);
                        }
                    }
                    dtos.add(dto);
                }
            }
            result.setSuccess(true);
            result.setResultObject(dtos);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    private boolean checkMissionQuery(MissionInfoQueryDto queryDto) {
        if (queryDto == null) {
            return false;
        }
        if (queryDto.getOrderBy() != null && !EnumMissionOrderBy.toMap().containsKey(queryDto.getOrderBy())) {
            return false;
        }

        return true;
    }

    /**
     * @param
     * @return Response<Integer>: 返回值类型
     * @throws
     * @Description：条件查询任务信息数目
     * @return: 返回结果描述
     */
    @Override
    public Response<Integer> queryMissionInofCount(MissionInfoQueryDto queryDto) {
        Response<Integer> result = new Response<Integer>();
        result.setSuccess(false);
        try {
            if (!checkMissionQuery(queryDto)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionInfoQuery query = new MissionInfoQuery();
            BeanUtils.copyProperties(queryDto, query);
            int count = missionService.queryMissionInfoCount(query);

            result.setResultObject(count);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    /**
     * @param missionId
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：任务删除
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> removeMissionInfo(String missionId) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            if (!checkLongId(missionId)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionInfoPO mission = missionService.findById(Long.valueOf(missionId));
            if (mission == null) {
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
                return result;
            }
            missionService.removeMissionById(Long.valueOf(missionId));
            missionExecuteService.removeMissionExecuteByMissionId(Long.valueOf(missionId));
            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    /**
     * @param missionIds
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：创建关联任务
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> createRelatedMission(CreateMissionRelatedDto dto) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            if (!checkCreateRelate(dto)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            boolean checkName = missionRelatedService.checkRelatedMissionName(dto.getMissonRelatedName());
            if (!checkName) {
                result.setCode(EnumError.ERROR_BUSINESS_NAME_CHECK_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_NAME_CHECK_ERROR.getMessage());
                return result;
            }
            MissionRelatedPO related = new MissionRelatedPO();
            BeanUtils.copyProperties(dto, related, "id");
            List<MissionRelatedDetailPO> details = createMissionRelatedDetail(dto);

            if (details == null) {
                result.setCode(EnumError.ERROR_BUSINESS_NOT_RELATED.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_NOT_RELATED.getMessage());
                return result;
            }
            //想办法消除环形任务 下面这个不行。。。
//            for(MissionRelatedDetailPO detail:details){
//                if(detail.getLevel()>1){
//                    List<MissionRelatedDetailPO> findDetails = missionRelatedService.findRelatedDetailsByMissionId(detail.getMissonInfoId());
//                    if(findDetails.size()>1){
//                        result.setCode(EnumError.ERROR_BUSINESS_NOT_RELATED.getCode());
//                        result.setErrorMessage(EnumError.ERROR_BUSINESS_NOT_RELATED.getMessage());                
//                        return result;                        
//                    }
//                }
//            }
            related.setStatus(EnumMissionRelatedStatus.STATUS_INIT.getCode());
            if (dto.isPublish()) {
                related.setStatus(EnumMissionRelatedStatus.STATUS_PUBLISH.getCode());
            }
            missionRelatedService.createRelatedMission(related, details);
            if (dto.isPublish()) {
                for (MissionRelatedDetailPO detail : details) {
                    UpdateStatusQuery query = new UpdateStatusQuery();
                    query.setId(Long.valueOf(detail.getMissonInfoId()));
                    query.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
                    query.setOldStatus(new String[]{EnumMissionStatus.STATUS_INIT.getCode(), EnumMissionStatus.STATUS_CANCEL.getCode()});
                    missionService.updateMissionInfoStatus(query);
                }
            }
            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    private boolean checkCreateRelate(CreateMissionRelatedDto dto) {
        return checkCreateRelate(dto, false);
    }

    private boolean checkCreateRelate(CreateMissionRelatedDto dto, boolean checkId) {
        if (dto == null) {
            return false;
        }
        if (StringUtils.isEmpty(dto.getMissonRelatedName())) {
            return false;
        }
        if (checkId && dto.getId() == null) {
            return false;
        }
        return true;
    }

    /**
     * @param dto
     * @return List<MissionRelatedDetailPO>: 返回值类型
     * @throws
     * @Description：组装关联明细
     * @return: 返回结果描述
     */
    private List<MissionRelatedDetailPO> createMissionRelatedDetail(CreateMissionRelatedDto dto) {
        List<MissionRelatedDetailPO> details = new ArrayList<MissionRelatedDetailPO>();
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            for (MissionRelatedDetailDto detailDto : dto.getDetails()) {
                MissionRelatedDetailPO detail = new MissionRelatedDetailPO();
                MissionInfoPO mission = missionService.findById(detailDto.getMissonInfoId());
                if (!mission.isRelated() || EnumMissionStatus.STATUS_PUBLISH.getCode().equals(mission.getStatus())) {
                    return null;
                }
                BeanUtils.copyProperties(detailDto, detail);
                details.add(detail);
            }
        }
        return details;

    }

    /**
     * @param missionIds
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：更新关联任务
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> updateRelatedMission(CreateMissionRelatedDto dto) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            if (!checkCreateRelate(dto, true)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionRelatedPO related = new MissionRelatedPO();
            BeanUtils.copyProperties(dto, related);

            List<MissionRelatedDetailPO> details = createMissionRelatedDetail(dto);
            if (details == null) {
                result.setCode(EnumError.ERROR_BUSINESS_NOT_RELATED.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_NOT_RELATED.getMessage());
                return result;
            }
            if (dto.isPublish()) {
                related.setStatus(EnumMissionRelatedStatus.STATUS_PUBLISH.getCode());
            }
            missionRelatedService.updateRelatedMission(related, details);
            if (EnumMissionRelatedStatus.STATUS_PUBLISH.getCode().equals(related.getStatus())) {
                for (MissionRelatedDetailPO detail : details) {
                    UpdateStatusQuery query = new UpdateStatusQuery();
                    query.setId(Long.valueOf(detail.getMissonInfoId()));
                    query.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
                    query.setOldStatus(new String[]{EnumMissionStatus.STATUS_INIT.getCode(), EnumMissionStatus.STATUS_CANCEL.getCode()});
                    missionService.updateMissionInfoStatus(query);
                }
            }
            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    /**
     * @param missionIds
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：发布关联任务
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> publishRelatedMission(String relatedId) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            if (!checkLongId(relatedId)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            UpdateStatusQuery query = new UpdateStatusQuery();
            query.setId(Long.valueOf(relatedId));
            query.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
            query.setOldStatus(new String[]{EnumMissionStatus.STATUS_INIT.getCode()});

            missionRelatedService.updateRelatedStatus(query);
            List<MissionRelatedDetailPO> details = missionRelatedService.findRelatedDetailsByRelatedId(Long.valueOf(relatedId));
            if (details != null && !details.isEmpty()) {
                for (MissionRelatedDetailPO detail : details) {
                    UpdateStatusQuery detailQuery = new UpdateStatusQuery();
                    detailQuery.setId(Long.valueOf(detail.getMissonInfoId()));
                    detailQuery.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());
                    detailQuery.setOldStatus(new String[]{EnumMissionStatus.STATUS_INIT.getCode()});
                    missionService.updateMissionInfoStatus(detailQuery);
                }
            }


            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    /**
     * @param relatedId
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：任务关联删除
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> removeMissionRelate(String relatedId) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            if (!checkLongId(relatedId)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionRelatedPO related = missionRelatedService.findById(Long.valueOf(relatedId));
            if (related == null) {
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
                return result;
            }
            missionRelatedService.removeMissionRelatedById(Long.valueOf(relatedId));
            result.setResultObject(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    /**
     * @param
     * @return Response<List<ViewMissionInfoDto>>: 返回值类型
     * @throws
     * @Description：条件查询任务关联信息
     * @return: 返回结果描述
     */
    @Override
    public Response<List<ViewMissionRelatedDto>> queryMissionRelate(MissionRelateQueryDto queryDto) {
        Response<List<ViewMissionRelatedDto>> result = new Response<List<ViewMissionRelatedDto>>();
        result.setSuccess(false);
        try {
            if (!checkRelateQuery(queryDto)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionRelatedQuery query = new MissionRelatedQuery();
            BeanUtils.copyProperties(queryDto, query);
            List<MissionRelatedPO> list = missionRelatedService.queryMissionRelated(query);

            List<ViewMissionRelatedDto> dtos = new ArrayList<ViewMissionRelatedDto>();

            if (list != null && !list.isEmpty()) {
                for (MissionRelatedPO po : list) {
                    ViewMissionRelatedDto dto = new ViewMissionRelatedDto();
                    BeanUtils.copyProperties(po, dto);
                    List<MissionRelatedDetailPO> details = missionRelatedService.findRelatedDetailsByRelatedId(po.getId());
                    if (details != null && !details.isEmpty()) {
                        List<MissionRelatedDetailDto> detailDtos = new ArrayList<MissionRelatedDetailDto>();
                        for (MissionRelatedDetailPO detailPo : details) {
                            MissionRelatedDetailDto detailDto = new MissionRelatedDetailDto();
                            BeanUtils.copyProperties(detailPo, detailDto);
                            detailDtos.add(detailDto);
                        }
                        dto.setDetails(detailDtos);
                    }
                    dtos.add(dto);
                }
            }
            result.setSuccess(true);
            result.setResultObject(dtos);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    private boolean checkRelateQuery(MissionRelateQueryDto queryDto) {
        if (queryDto == null) {
            return false;
        }
        if (queryDto.getOrderBy() != null && !EnumMissionOrderBy.toMap().containsKey(queryDto.getOrderBy())) {
            return false;
        }
        return true;
    }

    /**
     * @param
     * @return Response<Integer>: 返回值类型
     * @throws
     * @Description：条件查询任务关联信息数目
     * @return: 返回结果描述
     */
    @Override
    public Response<Integer> queryMissionRelateCount(MissionRelateQueryDto queryDto) {
        Response<Integer> result = new Response<Integer>();
        result.setSuccess(false);
        try {
            if (!checkRelateQuery(queryDto)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            MissionRelatedQuery query = new MissionRelatedQuery();
            BeanUtils.copyProperties(queryDto, query);
            int count = missionRelatedService.queryMissionRelatedCount(query);
            result.setSuccess(true);
            result.setResultObject(count);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    /**
     * @param
     * @return Response<Integer>: 返回值类型
     * @throws
     * @Description：交换missionIfno的 排序位置
     * @return: 返回结果描述
     */
    @Override
    public Response<Boolean> updateMissionSort(Long id1, Long id2) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            MissionInfoPO m1 = missionService.findById(id1);
            MissionInfoPO m2 = missionService.findById(id2);
            if (m1 == null || m2 == null) {
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
                return result;
            }
            int temp = m1.getSort();
            m1.setSort(m2.getSort());
            m2.setSort(temp);
            missionService.updateMissionSort(m1);
            missionService.updateMissionSort(m2);
            result.setSuccess(true);
            result.setResultObject(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public Response<MissionAwardDto> getAwardTotal(String shopCode) {
        log.info("获取任务奖励总数 -> 传入参数 -> shopCode:" + shopCode);
        MissionAwardCollectPO missionAwardCollectPO = missionAwardService.getAwardTotal(shopCode);
        log.info("获取任务奖励总数 -> missionAwardService.getAwardTotal(shopCode); -> missionAwardCollectPO：" + missionAwardCollectPO);
        if (missionAwardCollectPO == null) {
            return new Response<>(true, new MissionAwardDto("0", "积分"));
        }
        String awardUnit = EnumAwardType.AWARD_TYPE_SCORE.getMessage();
        String award = "0";
        Double scoreTotal = missionAwardCollectPO.getMessionScoreTotal();
        if (scoreTotal != null) {
            awardUnit = EnumAwardType.AWARD_TYPE_SCORE.getMessage();
        }
        if (awardUnit.equals(EnumAwardType.AWARD_TYPE_CASH.getMessage())) {
            Double cashTotal = missionAwardCollectPO.getMessionCashTotal();
            DecimalFormat df = new DecimalFormat("#.00");
            award = df.format(cashTotal);
            return new Response<>(true, new MissionAwardDto(award, awardUnit));
        }
        award = String.valueOf(scoreTotal.intValue());
        return new Response<>(true, new MissionAwardDto(award, awardUnit));
    }
}
