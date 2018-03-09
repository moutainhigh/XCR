package com.yatang.xc.xcr.biz.mission.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionBuyDto;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionExecuteQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.UserDto;

/**
 * 任务引擎：app 任务中心功能的dubbo服务接口
 * 
 * @author yangqingsong
 *
 */
public interface MissionExecuteDubboService {

    /**
     * 
     * @Description：条件查询可执行任务
     * @param queryDto
     * @return: 返回结果描述
     * @return Response<List<ViewMissionExecuteDto>>: 返回值类型
     * @throws
     */
    Response<List<ViewMissionExecuteDto>> queryMissionExecute(MissionExecuteQueryDto queryDto);


    /**
     * 
     * @Description：条件查询可执行任务数目
     * @param queryDto
     * @return: 返回结果描述
     * @return Response<Integer>: 返回值类型
     * @throws
     */
    Response<Integer> queryMissionExecuteCount(MissionExecuteQueryDto queryDto);


    /**
     * 
     * @Description：按门店编号和课程编号查询任务(在历史表中查询)
     * @param MissionExecuteQueryDto
     * @param courseId
     * @return: 返回结果描述
     * @return Response<ViewMissionExecuteDto>: 返回值类型
     * @throws
     */
    Response<List<ViewMissionExecuteDto>> queryMissionExecuteInHistory(MissionExecuteQueryDto queryDto);

    /**
     * 
     * @Description：按门店编号和课程编号查询任务数量(在历史表中查询)
     * @param MissionExecuteQueryDto
     * @param courseId
     * @return: 返回结果描述
     * @return Response<ViewMissionExecuteDto>: 返回值类型
     * @throws
     */
    Response<Integer> queryMissionExecuteCountInHistory(MissionExecuteQueryDto queryDto);

    /**
     * 
     * @Description：执行任务流程,提交任务审核申请（人工审核和系统审核）
     * @param executeId
     * @return: 系统审核不通过的任务返回 false ， 系统审核通过返回true， 人工审核都返回true
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    Response<Boolean> executeMission(String executeId);


//    /**
//     * 
//     * @Description：自动审核（系统审核） 任务是否完成，按任务id计算单个任务，并修改完成任务的状态（不再提供此接口）
//     * @param executeId
//     * @return: 返回结果描述 
//     * @return Response<Boolean>: 返回值类型
//     * @throws
//     */
//    Response<Boolean> autoAuditMission(String executeId, String login);
//

    /**
     * 
     * @Description：领取任务奖励
     * @param executeId
     * @return: 实时发放的奖励返回 true，需要走bpm审核的返回 false
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    Response<List<AwardInfoDto>> goAndGetAward(String executeId,String merchantId);


    /**
     * 
     * @Description：自动审核（系统审核） 任务是否完成，按任务商户id计算多个任务，并修改完成任务的状态
     * @param merchantId
     * @return: 返回系统审核通过的 任务id 列表
     * @return Response<List<String>>: 返回值类型
     * @throws
     */
    Response<List<String>> autoAuditMissionByMerchantId(String merchantId);

    /**
     * 自动审核（无缓存）
     * @param merchantId
     * @param login
     * @return
     */
    Response<List<String>> autoAuditMissionByMerchantIdWithoutCache(String merchantId);


    /**
     * 
     * @Description：保存任务附件
     * @param as
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    Response<Boolean> saveMissionAttachment(List<AttachmentDto> as);


    /**
     * 
     * @Description：购买指定商品确认
     * @param dto
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    Response<Boolean> buyProductConfirm(MissionBuyDto dto);


    /**
     * 
     * @Description：初始化门店的可执行任务
     * @param UserDto
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    Response<Boolean> initMissionExecuteByMerchantId(UserDto userDto);


    /**
     * 
     * @Description：按可执行任务id查询 任务
     * @param missonId
     * @return: 返回结果描述
     * @return Response<ViewMissionExecuteDto>: 返回值类型
     * @throws
     */
    Response<ViewMissionExecuteDto> queryMissionExecuteById(String missonId);

    

    /**
     * 
     * @Description：条件查询可执行任务：先按时间排序,再按关联任务级别排序
     * @param queryDto
     * @return: 返回结果描述
     * @return Response<List<ViewMissionExecuteDto>>: 返回值类型
     * @throws
     */
    Response<List<ViewMissionExecuteDto>> queryMissionExecuteOrderByRelated(MissionExecuteQueryDto queryDto);



    /**
     * 
     * @Description：查询任务是否满足关联任务要求
     * @param executeId
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
    Response<Boolean> checkMissionRelatedAchieved(String executeId);

    /**
     * 将任务奖励存库
     * @param merchantId
     * @param awardInfoDtos
     * @return
     */
    Response<Boolean> saveAward(String merchantId, List<AwardInfoDto> awardInfoDtos);

    /**
     * 更新奖励信息
     * @param merchantId
     * @param taskId
     * @return
     */
    Response<Boolean> updateMissionExecute(String merchantId, String taskId);
}
