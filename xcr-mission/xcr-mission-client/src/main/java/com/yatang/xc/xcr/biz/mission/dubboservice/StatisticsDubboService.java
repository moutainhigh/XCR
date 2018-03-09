package com.yatang.xc.xcr.biz.mission.dubboservice;

import java.util.Date;
import java.util.Map;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.statistics.MissionStatisticsDto;
import com.yatang.xc.xcr.biz.mission.dto.statistics.StatisticsDto;

public interface StatisticsDubboService {

	/**
	 * 
	 * @Description：展示数据-操作统计 @return: 返回统计结果 @return
	 * Response<Map<String,StatisticsDto>>: 返回值类型 @throws
	 */
	Response<Map<String, StatisticsDto>> showStatisticsMap();
	/**
	 * 
	 * @Description：展示数据-操作统计 @return: 返回统计结果 @return
	 * @param startTime 开始时间 , endTime 结束时间
	 * Response<Map<String,StatisticsDto>>: 返回值类型 @throws
	 */
	Response<Map<String, StatisticsDto>> showStatisticsMap(Date startTime, Date endTime);



	/**
	 * 
	 * @Description：展示数据-任务完成情况统计 @return: 返回统计结果 @return
	 * Response<Map<String,MissionStatisticsDto>>: 返回值类型 @throws
	 */
	Response<Map<String, MissionStatisticsDto>> showMissionStatisticsMap();
}
