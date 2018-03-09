package com.yatang.xc.xcr.biz.mission.dubboservice;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;

public interface MissionClassroomDubboService {

    /**
     * 
     * @Description：小超课堂课程学习完成回调接口
     * @param merchantId
     * @param courseId
     * @param status
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    Response<Boolean> courseMissionCallback(String merchantId, String courseId, String status);

    /**
     * 
     * @Description：按门店编号和课程编号查询任务
     * @param merchantId
     * @param courseId
     * @return: 返回结果描述
     * @return Response<ViewMissionExecuteDto>: 返回值类型
     * @throws
     */
    Response<ViewMissionExecuteDto> queryMissionExecuteByMerchantIdAndCourseId(String merchantId, String courseId);

}
