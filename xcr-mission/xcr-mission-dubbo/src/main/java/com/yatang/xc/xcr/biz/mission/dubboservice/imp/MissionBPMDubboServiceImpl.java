package com.yatang.xc.xcr.biz.mission.dubboservice.imp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusAndBpmIdQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionBPMDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumAuditResult;
import com.yatang.xc.xcr.biz.mission.enums.EnumError;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumReason;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.service.MissionAwardService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

/**
 * 任务dubbo服务for BPM流程回调
 * 
 * @author yangqingsong
 *
 */
@Service("missionBPMDubboService")
@Transactional(propagation=Propagation.REQUIRED)
public class MissionBPMDubboServiceImpl implements MissionBPMDubboService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionAwardService missionAwardService;

    @Autowired
    private MissionExecuteService missionExecuteService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionExecuteFlowService executeFlowService;

    private String REJECT_MESSAGE = "亲爱的用户，您的任务 {0} 审核未通过，请尽快前往任务中心重新做任务！";


    /**
     * 
     * @Description：任务人工审核完成回调
     * @param executeId
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    @Override
    public Response<Boolean> manualAuditMissionCallback(String missonId, String bpmId, String returnCode, String msg) {
        log.info("manualAuditMissionCallback start : bpmId" + bpmId + " missonId:" + missonId + " returnCode:" + returnCode);
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(missonId);
            if(execute!=null){
                UpdateStatusAndBpmIdQuery query = new UpdateStatusAndBpmIdQuery();
                query.setId(Long.valueOf(execute.getId()));
                // query.setBpmId(bpmId);
                if (EnumAuditResult.AUDIT_RESULT_APPROVE.getCode().equals(returnCode)) {
                    log.info("manualAuditMissionCallback approve : bpmId" + bpmId);
                    missionExecuteService.updateMissionExecuteStatusAndBpmId(query);
                    executeFlowService.updateExecuteToFinishedOrEnd(execute,
                            EnumReason.REASON_BPM_CALLBACK.getCode(),
                            new String[] {EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode()});

                } else if (EnumAuditResult.AUDIT_RESULT_REJECT.getCode().equals(returnCode)) {
                    log.info("manualAuditMissionCallback reject : bpmId" + bpmId);
                    if (!StringUtils.isEmpty(msg) && msg.length() > 95) {
                        msg = msg.substring(0, 95);
                    }
                    query.setReason(EnumReason.REASON_BPM_CALLBACK.getCode() + ":" + msg);
                    query.setStatus(EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode());
                    query.setOldStatus(new String[] {EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode()});
                    missionExecuteService.updateMissionExecuteStatusAndBpmId(query);
                    MissionInfoPO info = missionService.findById(execute.getMissonInfoId());
                    String message = MessageFormat.format(REJECT_MESSAGE, info.getName());
                    executeFlowService.sendAppMessage(execute.getMerchantId(), message);
                }
                result.setResultObject(true);
                result.setSuccess(true);
                log.info("manualAuditMissionCallback end : bpmId" + bpmId);           	
            }
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }


    /**
     * 
     * @Description：奖励人工审核完成回调
     * @param executeId
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    @Override
    public Response<Boolean> manualAuditAwardCallback(String missonId, String bpmId, String returnCode, String msg) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        try {
            MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(missonId);
            List<MissionExecuteAwardPO> awards = missionAwardService.queryExecuteAwardByExecuteId(execute.getId());

            if (EnumAuditResult.AUDIT_RESULT_APPROVE.getCode().equals(returnCode)) {
                UpdateStatusAndBpmIdQuery query = new UpdateStatusAndBpmIdQuery();
                // query.setBpmId(bpmId);
                query.setId(Long.valueOf(execute.getId()));
                query.setStatus(EnumMissionExecuteStatus.STATUS_END.getCode());
                query.setOldStatus(new String[] {EnumMissionExecuteStatus.STATUS_AWARD_AUDIT.getCode()});
                query.setReason(EnumReason.REASON_BPM_CALLBACK.getCode());
                missionExecuteService.updateMissionExecuteStatusAndBpmId(query);
                executeFlowService.backMissionExecuteToHistory(execute.getId(), awards);
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
     * 
     * @Description：奖励人工审核完成回调
     * @param executeId
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    @Override
    public Response<List<AttachmentDto>> getAttachment(String missonExecuteId) {
        Response<List<AttachmentDto>> result = new Response<List<AttachmentDto>>();
        result.setSuccess(false);
        try {
            if (StringUtils.isEmpty(missonExecuteId) || !StringUtils.isNumeric(missonExecuteId)) {
                result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
                return result;
            }
            List<MissionAttachmentPO> attachments = missionService.queryAttachmentByExecuteId(Long.valueOf(missonExecuteId));
            if (attachments == null || attachments.isEmpty()) {
                attachments = missionService.queryAttachmentHistoryByExecuteId(Long.valueOf(missonExecuteId));
            }
            if (attachments != null && !attachments.isEmpty()) {
                List<AttachmentDto> attachmentDtos = new ArrayList<AttachmentDto>();
                for (MissionAttachmentPO a : attachments) {
                    AttachmentDto aDto = new AttachmentDto();
                    BeanUtils.copyProperties(a, aDto);
                    attachmentDtos.add(aDto);
                }
                result.setResultObject(attachmentDtos);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
            result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

}
