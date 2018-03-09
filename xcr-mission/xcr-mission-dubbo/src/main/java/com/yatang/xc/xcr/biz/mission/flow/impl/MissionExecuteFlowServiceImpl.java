package com.yatang.xc.xcr.biz.mission.flow.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.schedule.thread.IAuditMissionThread;
import com.yatang.xc.xcr.biz.mission.schedule.thread.ICreateMissionExecuteThread;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.exception.BusinessException;
import com.busi.common.resp.Response;
import com.busi.common.utils.JsonPathTools;
import com.yatang.xc.dc.biz.facade.dubboservice.DataCenterDobboService;
import com.yatang.xc.mbd.biz.bpm.dto.StartProcessInstanceDTO;
import com.yatang.xc.mbd.biz.bpm.dubboservice.AuditHandleDubboService;
import com.yatang.xc.mbd.biz.bpm.enums.ProcessTypeEnum;
import com.yatang.xc.mbd.biz.org.dto.FranchiseeDto;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.biz.message.dubboservice.JpushMsgDubboService;
import com.yatang.xc.xcr.biz.mission.bo.MissionInfoQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusAndBpmIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionOfflineExistsListPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.enums.EnumAttachmentType;
import com.yatang.xc.xcr.biz.mission.enums.EnumError;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumReason;
import com.yatang.xc.xcr.biz.mission.flow.MissionEngineService;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.service.MissionAwardService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;
import com.yatang.xc.xcr.biz.mission.service.RedisService;
import org.springframework.util.CollectionUtils;

/**
 * 任务dubbo服务
 *
 * @author yangqingsong
 */
@Service
@Transactional
public class MissionExecuteFlowServiceImpl extends ApplicationObjectSupport implements MissionExecuteFlowService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionExecuteService missionExecuteService;

    @Autowired
    private MissionAwardService missionAwardService;

    @Autowired
    private MissionEngineService missionEngineService;

    private AuditHandleDubboService auditHandleDubboService;

    private OrganizationService organizationService;

    private JpushMsgDubboService jpushMsgDubboService;

    private DataCenterDobboService dataCenterDobboService;

    private RedisService redisService;

    @Override
    public OrganizationService getOrganizationService() {
        if (organizationService == null) {
            organizationService = (OrganizationService) getApplicationContext().getBean("organizationDubboService");
        }
        return organizationService;
    }

    @Override
    public RedisService getRedisService() {
        if (redisService == null) {
            redisService = (RedisService) getApplicationContext().getBean("redisService");
        }
        return redisService;
    }

    @Override
    public ICreateMissionExecuteThread initCreateMissionExecuteThread(String merchantId) {
        ICreateMissionExecuteThread thread = null;
        thread = (ICreateMissionExecuteThread) getApplicationContext().getBean("createMissionExecuteThread");
        thread.init(merchantId);
        return thread;
    }

    /**
     * 使用主数据的接口以门店编号 获取一账通账号
     */
    @Override
    public String getLoginByMerchantId(String merchantId) {
        FranchiseeDto dto = getFranchiseeByMerchantId(merchantId);
        if (dto != null) {
            if (!StringUtils.isEmpty(dto.getYtAccount())) {
                return dto.getYtAccount();
            } else {
                log.error("can not get login by FranchiseeDto : " + JSONObject.toJSONString(dto));
            }
        }
        log.warn("can not get login by merchantId : " + merchantId);
        return null;
    }

    /**
     * 使用主数据的接口以门店编号 获取一账通账号
     */
    @Override
    public FranchiseeDto getFranchiseeByMerchantId(String merchantId) {
        try {
            if (!StringUtils.isEmpty(merchantId)) {
                Response<StoreDto> re = getOrganizationService().queryStoreById(merchantId);
                if (re.isSuccess() && re.getResultObject() != null) {
                    Response<FranchiseeDto> reDto = getOrganizationService()
                            .queryFranchiseeById(re.getResultObject().getFranchiseeId());
                    if (reDto.isSuccess() && reDto.getResultObject() != null) {
                        log.info("got FranchiseeDto by merchantId resutl: " + JSONObject.toJSONString(reDto));
                        return reDto.getResultObject();
                    }
                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        log.warn("can not get FranchiseeDto by merchantId : " + merchantId);
        return null;
    }

    /**
     * @param executeId
     * @return BaseResult<Boolean>: 返回值类型
     * @throws @Description：任务审核不通过
     * @return: 返回结果描述
     */
    @Override
    public boolean rejectMissionAudit(String executeId) {
        try {
            MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(executeId);
            MissionInfoPO info = missionService.findById(execute.getMissonInfoId());
            if (!info.isManualAudit()) {
                return false;
            }
            UpdateStatusQuery query = new UpdateStatusQuery();
            query.setReason(EnumReason.REASON_REJECT.getCode());
            query.setId(Long.valueOf(execute.getId()));
            query.setStatus(EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode());
            query.setOldStatus(new String[]{EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode()});
            missionExecuteService.updateMissionExecuteStatus(query);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return true;
    }

    @Override
    public boolean backMissionExecuteToHistory(Long executeId, List<MissionExecuteAwardPO> awards) {
        boolean success = missionExecuteService.backMissionExecuteToHistory(executeId);
        if (success) {
            missionAwardService.backMissionAwardToHistory(awards, executeId);
            missionExecuteService.backAttachmentToHistory(executeId,
                    EnumAttachmentType.ATTACHMENT_MISSION_EXECUTE.getCode());
        }
        return false;
    }

    @Override
    public ViewMissionExecuteDto buildViewExecute(MissionExecuteInfoPO execute) {
        MissionInfoPO info = missionService.findById(execute.getMissonInfoId());
        ViewMissionExecuteDto dto = new ViewMissionExecuteDto();
        if (info != null) {
            BeanUtils.copyProperties(info, dto);
        }
        BeanUtils.copyProperties(execute, dto);
        if (info != null) {
            dto.setTemplateCode(info.getMissonTemplateCode());
            dto.setCourseId(info.getCourseId());
        }
        List<MissionExecuteAwardPO> awards = missionAwardService.queryExecuteAwardByExecuteId(execute.getId());
        if (awards != null && !awards.isEmpty()) {
            List<AwardInfoDto> awardDtos = new ArrayList<AwardInfoDto>();
            for (MissionExecuteAwardPO award : awards) {
                AwardInfoDto awardDto = new AwardInfoDto();
                BeanUtils.copyProperties(award, awardDto);
                awardDtos.add(awardDto);
            }
            dto.setAwards(awardDtos);
        }

        List<MissionAttachmentPO> attachments = missionService.queryAttachmentByExecuteId(execute.getId());
        if (attachments != null && !attachments.isEmpty()) {
            List<AttachmentDto> attachmentDtos = new ArrayList<AttachmentDto>();
            for (MissionAttachmentPO a : attachments) {
                AttachmentDto aDto = new AttachmentDto();
                BeanUtils.copyProperties(a, aDto);
                attachmentDtos.add(aDto);
            }
            dto.setAttachments(attachmentDtos);
        }
        log.info("---> 任务返回列表 -> ViewMissionExecuteDto dto:" + dto);
        return dto;
    }

    @Override
    public boolean createMissionExecute(String merchantId) {
        log.info("createMissionExecute start...merchantId:" + merchantId);
        Date now = new Date();
        MissionInfoQuery query = new MissionInfoQuery();
        query.setStatus(EnumMissionStatus.STATUS_PUBLISH.getCode());

        List<MissionInfoPO> missionInofs = missionService.queryMissionInfo(query);
        List<MissionOfflineExistsListPO> offlineList = missionExecuteService.queryOfflineList(merchantId, null);
        HashSet<String> set = new HashSet<String>();

        if (offlineList != null) {
            for (MissionOfflineExistsListPO offline : offlineList) {
                set.add(offline.getTemplateCode());
            }
        }

        if (missionInofs != null) {
            for (MissionInfoPO missionInof : missionInofs) {

                if (now.after(missionInof.getDurationTimeEnd()) || now.before(missionInof.getDurationTimeStart())) {
                    continue;
                }

                MissionTemplatePO template = missionService.selectTemplateByCode(missionInof.getMissonTemplateCode());

                List<MissionExecuteInfoPO> executes = missionExecuteService
                        .queryExecuteMissionByMissionIdAndMerchantId(missionInof.getId(), merchantId);
                // missionExecuteService.queryExecuteMissionByMissionIdAndMerchantIdInHistory(missionInof.getId(),
                // merchantId);
                List<MissionAwardPO> awards = missionAwardService.queryAwardByMissionId(missionInof.getId());

                if (template.isSchedule()) {
                    if (executes == null || executes.isEmpty()) {
                        createMissionExecuteItem(awards, merchantId, missionInof, template, set);
                    } else {
                        boolean hasActiveExecute = false;
                        for (MissionExecuteInfoPO execute : executes) {
                            if (now.before(execute.getEndTime())) {
                                hasActiveExecute = true;
                                break;
                            }
                        }
                        if (!hasActiveExecute) {
                            createMissionExecuteItem(awards, merchantId, missionInof, template, set);
                        }
                    }
                } else {
                    if (executes == null || executes.isEmpty()) {

                        List<MissionExecuteInfoPO> historyExecutes = missionExecuteService
                                .queryExecuteMissionByMissionIdAndMerchantIdInHistory(missionInof.getId(), merchantId);
                        if (historyExecutes == null || historyExecutes.isEmpty()) {
                            createMissionExecuteItem(awards, merchantId, missionInof, template, set);
                        }
                    }
                }
            }
        }
        log.info("createMissionExecute end...merchantId:" + merchantId);
        return true;
    }

    @Override
    public boolean createDayMissionExecute(String merchantId) {
        log.info("自动创建日任务 ->  createDayMissionExecute start...merchantId:" + merchantId);
        Date now = new Date();
        MissionInfoQuery query = new MissionInfoQuery();
        query.setStatus(EnumMissionStatus.STATUS_DAY_PUBLISH.getCode());
        List<MissionInfoPO> missionInofs = missionService.queryMissionInfo(query);
        //任务黑名单
        List<MissionOfflineExistsListPO> offlineList = missionExecuteService.queryOfflineList(merchantId, null);
        HashSet<String> set = new HashSet<String>();
        if (offlineList != null) {
            for (MissionOfflineExistsListPO offline : offlineList) {
                set.add(offline.getTemplateCode());
            }
        }

        if (CollectionUtils.isEmpty(missionInofs)) {
            log.info("createDayMissionExecute end...merchantId:" + merchantId);
            return true;
        }

        for (MissionInfoPO missionInof : missionInofs) {
            log.info("需要被自动创建的日任务有：" + JSONObject.toJSONString(missionInof));
            log.info("自动创建日任务 -> 任务时间：missionInof.getDurationTimeStart() -> " + missionInof.getDurationTimeStart() + "  missionInof.getDurationTimeEnd():" + missionInof.getDurationTimeEnd());
            if (now.after(missionInof.getDurationTimeEnd()) || now.before(missionInof.getDurationTimeStart())) {
                continue;
            }
            //设置需要被创建的任务有效期和任务时间为当天
            missionInof.setValidTimeStart(DateUtil.getDateTimeByString(DateUtil.formatDate(new Date()) + " 00:00:00"));
            missionInof.setValidTimeEnd(DateUtil.getDateTimeByString(DateUtil.formatDate(new Date()) + " 23:59:59"));
            missionInof.setDurationTimeStart(DateUtil.getDateTimeByString(DateUtil.formatDate(new Date()) + " 00:00:00"));
            missionInof.setDurationTimeEnd(DateUtil.getDateTimeByString(DateUtil.formatDate(new Date()) + " 23:59:59"));

            MissionTemplatePO template = missionService.selectTemplateByCode(missionInof.getMissonTemplateCode());
            List<MissionExecuteInfoPO> executes = missionExecuteService.queryExecuteMissionByMissionIdAndMerchantId(missionInof.getId(), merchantId);
            List<MissionAwardPO> awards = missionAwardService.queryAwardByMissionId(missionInof.getId());

            //周期性任务判断任务是否已被创建
            log.info("自动创建日任务 -> template.isSchedule() -> " + template.isSchedule());
            if (CollectionUtils.isEmpty(executes)) {
                createMissionExecuteItem(awards, merchantId, missionInof, template, set);
            } else {
                boolean hasActiveExecute = false;
                for (MissionExecuteInfoPO execute : executes) {
                    if (now.before(execute.getEndTime())) {
                        hasActiveExecute = true;
                        break;
                    }
                }
                if (!hasActiveExecute) {
                    createMissionExecuteItem(awards, merchantId, missionInof, template, set);
                }
            }
        }
        return true;
    }

    private void createMissionExecuteItem(List<MissionAwardPO> awards, String merchantId, MissionInfoPO missionInof,
                                          MissionTemplatePO template, HashSet<String> offlineSet) {
        log.info("createMissionExecuteItem merchantId:" + merchantId + " missionInof:" + missionInof.getId());
        String status = EnumMissionExecuteStatus.STATUS_INIT.getCode();
        if (offlineSet.contains(template.getTemplateCode())) {
            status = EnumMissionExecuteStatus.STATUS_END.getCode();
        }
        MissionExecuteInfoPO execute = missionExecuteService.createMissionEexecute(merchantId, missionInof, template,
                status);
        if (execute != null && awards != null && !awards.isEmpty()) {
            for (MissionAwardPO award : awards) {
                MissionExecuteAwardPO eAward = new MissionExecuteAwardPO();
                BeanUtils.copyProperties(award, eAward);
                eAward.setMissonExecuteIdInfoId(execute.getId());
                eAward.setGrantTime(new Date());
                missionAwardService.createExecuteAward(eAward);
            }
        }
    }

    /**
     * 已经不再需求开启 bpm 流程， 暂时不使用
     */
    @Override
    public void startBpmProcess(Long executeId, String templateCode, String companyId, String merchantId,
                                String shopName) {

        StartProcessInstanceDTO processInstanceDTO = new StartProcessInstanceDTO();
        processInstanceDTO.setCompanyId(companyId);
        processInstanceDTO.setIntentionId(String.valueOf(executeId));
        processInstanceDTO.setFormId(merchantId);
        processInstanceDTO.setShopName(shopName);
        if (templateCode.equals("T004")) {
            processInstanceDTO.setProcessTypeEnum(ProcessTypeEnum.XCRMtouBt);
        } else if (templateCode.equals("T005")) {
            processInstanceDTO.setProcessTypeEnum(ProcessTypeEnum.XCRZjinBt);
        }
        processInstanceDTO.setUserId("-1");
        processInstanceDTO.setUserName("小超人");
        if (auditHandleDubboService == null) {
            auditHandleDubboService = (AuditHandleDubboService) getApplicationContext()
                    .getBean("auditHandleDubboService");
        }
        log.info("startBpmProcess processInstanceDTO: " + JSON.toJSONString(processInstanceDTO));
        Response<String> result = auditHandleDubboService.startProcessInstance(processInstanceDTO);
        log.info("startBpmProcess result: " + JSON.toJSONString(result));
        if (result == null || !result.isSuccess() || result.getResultObject().isEmpty()) {
            throw new BusinessException(EnumError.ERROR_BUSINESS_BPM_FAILE.getCode(),
                    EnumError.ERROR_BUSINESS_BPM_FAILE.getMessage() + ":" + result.getCode() + ":"
                            + result.getErrorMessage() + ":" + result.getResultObject());
        }
        UpdateStatusAndBpmIdQuery query = new UpdateStatusAndBpmIdQuery();
        query.setBpmId(result.getResultObject());
        query.setId(executeId);
        missionExecuteService.updateMissionExecuteStatusAndBpmId(query);

    }

    @Override
    public boolean checkOrderCountFromDataCenter(String merchantId) {
        if (dataCenterDobboService == null) {
            dataCenterDobboService = (DataCenterDobboService) getApplicationContext().getBean("dataCenterDobboService");
        }
        if (dataCenterDobboService != null) {
            // 调用曹韬 的接口查询门店用户完成了多少笔交易 判断交易数量是否大于1
            JSONObject reqJson = new JSONObject();
            reqJson.put("StoreSerialNo", merchantId);
            reqJson.put("PageIndex", 1);
            reqJson.put("PageSize", 1);
            log.info("reqJson ==" + reqJson.toString());
            String result = dataCenterDobboService.queryOneSmallTicketInfo(reqJson.toString());
            log.info("result ==" + result);
            List<Object> totals = JsonPathTools.getValues("hits.total", result);
            log.info("totals ==" + totals + " merchantId :" + merchantId);
            if (totals != null) {
                Object count = totals.get(0);
                if (count != null) {
                    if (count instanceof Integer) {
                        Integer total = (Integer) count;
                        if (total > 0) {
                            return true;
                        }
                    } else if (count instanceof Double) {
                        Double total = (Double) count;
                        if (total > 0d) {
                            return true;
                        }
                    }
                }
            }
        } else {
            log.error("can not find DataCenterDobboService bean from spring!");
        }
        return false;
    }

    @Override
    public boolean sendAppMessage(String merchantId, String content) {
        if (jpushMsgDubboService == null) {
            jpushMsgDubboService = (JpushMsgDubboService) getApplicationContext().getBean("jpushMsgDubboService");
        }
        List<String> regeSterIds = new ArrayList<String>();
        FranchiseeDto dto = getFranchiseeByMerchantId(merchantId);
        if (dto == null || StringUtils.isEmpty(dto.getRegistrationId())) {
            throw new RuntimeException("can not get Franchisee RegistrationId for merchantId :" + merchantId);
        }
        regeSterIds.add(dto.getRegistrationId());
        Map<String, String> extras = new HashMap<>();
        Response result = jpushMsgDubboService.sendPushByRegesterId(regeSterIds, null, content, extras);
        return result.isSuccess();
    }

    @Override
    public void updateExecuteToFinishedOrEnd(MissionExecuteInfoPO execute, String reason, String[] oldStatus) {
        if (execute == null) {
            return;
        }
        List<MissionExecuteAwardPO> awards = missionAwardService.queryExecuteAwardByExecuteId(execute.getId());
        String status = EnumMissionExecuteStatus.STATUS_FINISHED.getCode();
        boolean noAward = (awards == null || awards.isEmpty()) && StringUtils.isEmpty(execute.getSpecialAwardRemark());
        if (noAward) {
            status = EnumMissionExecuteStatus.STATUS_END.getCode();
        }
        UpdateStatusQuery query = new UpdateStatusQuery();
        query.setId(execute.getId());
        query.setReason(reason);
        query.setStatus(status);
        query.setOldStatus(oldStatus);
        missionExecuteService.updateMissionExecuteStatus(query);
        if (noAward) {
            backMissionExecuteToHistory(execute.getId(), awards);
        }

    }

    @Override
    public IAuditMissionThread initIAuditMissionThread(MissionExecuteInfoPO execute) {

        IAuditMissionThread thread = (IAuditMissionThread) getApplicationContext().getBean("auditMissionThread");
        thread.init(execute);
        return thread;
    }

    /**
     * @param execute
     * @param result
     * @return Response<Boolean>: 返回值类型
     * @throws BusinessException:    返回结果描述
     * @throws @Description：判断任务是否完成
     */
    @Override
    public boolean calculateMission(MissionExecuteInfoPO execute) {
        boolean result = false;
        try {
            if (execute == null || StringUtils.isEmpty(execute.getRule())) {
                return false;
            }
            if (!EnumMissionExecuteStatus.STATUS_INIT.getCode().equals(execute.getStatus())
                    && !EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode().equals(execute.getStatus())) {
                return false;
            }
            Date now = new Date();
            if (now.before(execute.getStartTime()) || now.after(execute.getEndTime())) {
                return false;
            }

            // 调用 drools
            RuleCalculateDto ruleCalculateDto = missionEngineService.calculateMission(execute);

            if (ruleCalculateDto == null || !ruleCalculateDto.hasSuccess()) {
                return false;
            }
            updateExecuteToFinishedOrEnd(execute, EnumReason.REASON_RULE_CALCULATE.getCode(),
                    new String[]{EnumMissionExecuteStatus.STATUS_INIT.getCode(),
                            EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode()});
            result = true;
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    public static void main(String[] args) {

        String row = "[{\n" +
                "\t\"EventName\": \"勿删-商品特价折扣活动【进行中】\",\n" +
                "\t\"RemainDays\": 15,\n" +
                "\t\"IsNewUserCanUse\": 0,\n" +
                "\t\"EventId\": \"20171101140701122620e7dacc448d8ac93aee57e29fc01\",\n" +
                "\t\"EndTime\": \"2017.11.16\",\n" +
                "\t\"EventType\": 2,\n" +
                "\t\"StartTime\": \"2017.11.01\"\n" +
                "},\n" +
                "{\n" +
                "\t\"EventName\": \"勿删-商家优惠券新活动【进行中】\",\n" +
                "\t\"RemainDays\": 15,\n" +
                "\t\"IsNewUserCanUse\": 1,\n" +
                "\t\"EventId\": \"2017110111250238efcbbeaca5f44e2a04d4240bf40d4d9\",\n" +
                "\t\"EndTime\": \"2017.11.16\",\n" +
                "\t\"EventType\": 1,\n" +
                "\t\"StartTime\": \"2017.11.01\"\n" +
                "}]";

        JSONArray jsonArray = JSONObject.parseArray(row);
        for (int i = 0; i < jsonArray.size(); i++) {
            Object object = jsonArray.get(i);
            JSONObject json = jsonArray.getJSONObject(i);
            System.out.println(JSONObject.toJSONString(object));
            System.out.println("----->  " + json.getString("EventId"));
        }

    }

}
