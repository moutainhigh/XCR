package com.yatang.xc.xcr.biz.mission.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionAwardDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.CreateMissionInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.CreateMissionRelatedDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.MissionInfoQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.MissionRelateQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.MissionTemplateDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.ViewMissionInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.ViewMissionRelatedDto;

/**
 * 任务引擎：任务信息后台管理功能的dubbo服务接口
 *
 * @author yangqingsong
 */
public interface MissionDubboService {

    /**
     * @param CreateMissionInfoDto
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：新建任务信息
     * @return: 返回结果描述
     */
    public Response<Boolean> createMissionInfo(CreateMissionInfoDto dto);


    /**
     * @param missionId
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：任务发布
     * @return: 返回结果描述
     */
    public Response<Boolean> publishMissionInfo(String missionId);


    /**
     *
     * @Description：任务下架/取消
     * @param missionIds
     * @return: 返回结果描述
     * @return Response<Boolean>: 返回值类型
     * @throws
     */
//    public Response<Boolean> cancelMissionInfo(String[] missionIds);

    /**
     * @param type
     * @return Response<List<MissionTemplateDto>>: 返回值类型
     * @throws
     * @Description：按类型查询任务模板
     * @return: 返回结果描述
     */
    public Response<List<MissionTemplateDto>> queryMissionTemplateByMissionType(String missionType);


    /**
     * @param
     * @return Response<List<ViewMissionInfoDto>>: 返回值类型
     * @throws
     * @Description：条件查询任务信息
     * @return: 返回结果描述
     */
    public Response<List<ViewMissionInfoDto>> queryMissionInof(MissionInfoQueryDto queryDto);


    /**
     * @param missionId
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：任务删除
     * @return: 返回结果描述
     */
    public Response<Boolean> removeMissionInfo(String missionId);


    /**
     * @param missionIds
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：创建关联任务
     * @return: 返回结果描述
     */
    public Response<Boolean> createRelatedMission(CreateMissionRelatedDto dto);


    /**
     * @param missionIds
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：发布关联任务
     * @return: 返回结果描述
     */
    public Response<Boolean> publishRelatedMission(String relatedId);


    /**
     * @param relatedId
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：任务关联删除
     * @return: 返回结果描述
     */
    public Response<Boolean> removeMissionRelate(String relatedId);


    /**
     * @param
     * @return Response<List<ViewMissionInfoDto>>: 返回值类型
     * @throws
     * @Description：条件查询任务关联信息
     * @return: 返回结果描述
     */
    public Response<List<ViewMissionRelatedDto>> queryMissionRelate(MissionRelateQueryDto queryDto);


    /**
     * @param
     * @return Response<Integer>: 返回值类型
     * @throws
     * @Description：条件查询任务信息数目
     * @return: 返回结果描述
     */
    public Response<Integer> queryMissionInofCount(MissionInfoQueryDto queryDto);


    /**
     * @param
     * @return Response<Integer>: 返回值类型
     * @throws
     * @Description：条件查询任务关联信息数目
     * @return: 返回结果描述
     */
    public Response<Integer> queryMissionRelateCount(MissionRelateQueryDto queryDto);


    /**
     * @param CreateMissionInfoDto
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：更新任务信息
     * @return: 返回结果描述
     */
    public Response<Boolean> updateMissionInfo(CreateMissionInfoDto dto);


    /**
     * @param missionIds
     * @return Response<Boolean>: 返回值类型
     * @throws
     * @Description：更新关联任务
     * @return: 返回结果描述
     */
    public Response<Boolean> updateRelatedMission(CreateMissionRelatedDto dto);


    public Response<Boolean> updateMissionSort(Long id1, Long id2);

    /**
     * 获取总任务奖励
     *
     * @param shopCode
     * @return
     */
    Response<MissionAwardDto> getAwardTotal(String shopCode);
}
