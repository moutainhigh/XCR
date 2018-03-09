package com.yatang.xc.xcr.biz.mission.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;

public interface
MissionBPMDubboService {

    Response<Boolean> manualAuditMissionCallback(String missonId, String bpmId, String resultStr,String msg);

    Response<Boolean> manualAuditAwardCallback(String missonId, String bpmId, String resultStr,String msg);

    Response<List<AttachmentDto>> getAttachment(String missonExecuteId);

}
