package com.yatang.xc.xcr.biz.mission.dubboservice.imp;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionUserHistoryPO;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumError;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumReason;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

/**
 * 任务dubbo服务 for 小超课堂 回调
 * 
 * @author yangqingsong
 *
 */
@Service("missionClassroomDubboService")
@Transactional(propagation=Propagation.REQUIRED)
public class MissionClassroomDubboServiceImpl implements MissionClassroomDubboService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionExecuteService missionExecuteService;

    @Autowired
    private MissionService missionService;
    
    @Autowired
    private MissionExecuteFlowService executeFlowService;
    /**
     * 
     * @Description：课程学习完成回调
     * @param executeId
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    @Override
    public Response<Boolean> courseMissionCallback(String merchantId, String courseId, String coureseStatus) {
        Response<Boolean> result = new Response<Boolean>();
        result.setSuccess(false);
        if(StringUtils.isEmpty(courseId)||StringUtils.isEmpty(merchantId)){
            result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
            result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage()); 
            return result;
        }
        try {
            MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionByMerchantIdAndCourseId(merchantId, courseId,null);
            if(execute==null||execute.isDeleted()||execute.getStatus().equals(EnumMissionExecuteStatus.STATUS_INVALID.getCode())){
                result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
                result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
                return result;
            }
            
            executeFlowService.updateExecuteToFinishedOrEnd(execute, EnumReason.REASON_CORES_CALLBACK.getCode(), 
                    new String[] {EnumMissionExecuteStatus.STATUS_INIT.getCode()});
            
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
     * @Description：按门店和课程id获取可执行任务的信息
     * @param executeId
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    @Override
    public Response<ViewMissionExecuteDto> queryMissionExecuteByMerchantIdAndCourseId(String merchantId,String courseId) {
        Response<ViewMissionExecuteDto> result = new Response<ViewMissionExecuteDto>();
        result.setSuccess(false);
        if(StringUtils.isEmpty(courseId)){
            result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
            result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage()); 
            return result;
        }
        try {
            List<MissionInfoPO> list = missionService.queryMissionByCourseId(courseId,null);

            if(StringUtils.isEmpty(merchantId)){
                if(list !=null && !list.isEmpty()){
                	MissionInfoPO existInfo = null;
                	for(MissionInfoPO info: list){
                		if(!info.isDeleted()){
                			existInfo = info;
                			break;
                		}
                	}
                    if(existInfo == null){
                        MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionByMerchantIdAndCourseId(merchantId, courseId,null);
                        if(execute != null){
                            ViewMissionExecuteDto dto = new ViewMissionExecuteDto();
                            result.setResultObject(dto);
                        }            
                    }else{
                        ViewMissionExecuteDto dto = new ViewMissionExecuteDto();
                        result.setResultObject(dto);     
                    }
                }
            }else{
                if(list !=null && !list.isEmpty()){
                	MissionInfoPO existInfo = null;
                	for(MissionInfoPO info: list){
                		if(!info.isDeleted()){
                			existInfo = info;
                			break;
                		}
                	}
                	if(existInfo!=null){
                        MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionByMerchantIdAndCourseId(merchantId, courseId,null);
                        ViewMissionExecuteDto dto = new ViewMissionExecuteDto();
                        if(execute!=null){
                            dto = executeFlowService.buildViewExecute(execute);
                            result.setResultObject(dto);
                        }else{
                        	if(existInfo.getStatus().equals(EnumMissionStatus.STATUS_PUBLISH.getCode())){
                                MissionExecuteInfoPO executeHistory = missionExecuteService.findExecuteMissionByMerchantIdAndCourseIdInHistory(merchantId, courseId,null);
                                if(executeHistory!=null){
                                	dto.setStatus(EnumMissionExecuteStatus.STATUS_END.getCode());
                                    result.setResultObject(dto);
                                }
                        	}
                        }
                	}else{
                		ViewMissionExecuteDto dto = new ViewMissionExecuteDto();
                        MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionByMerchantIdAndCourseId(merchantId, courseId,null);
                        if(execute!=null){
                            dto = executeFlowService.buildViewExecute(execute);
                            result.setResultObject(dto);
                        }
                	}
                }
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
