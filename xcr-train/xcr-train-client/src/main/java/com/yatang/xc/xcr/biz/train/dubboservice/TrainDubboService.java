package com.yatang.xc.xcr.biz.train.dubboservice;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;

/**
 * @描述: TODO.课堂管理dubbo服务接口
 * @作者: huangjianjun
 * @创建时间: 2017年3月24日-下午6:02:46 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface TrainDubboService {

	/**
	* @Description: 编辑课堂信息
	* @author huangjianjun
	* @date 2017年4月1日 下午2:13:49
	* @param TrainInfoDTO
	 */
	@SuppressWarnings("rawtypes")
	Response editTrain(TrainInfoDTO dto);
	
	/**
	* @Description: 删除课堂
	* @author huangjianjun
	* @date 2017年4月1日 下午2:10:17
	* @param id
	 */
	@SuppressWarnings("rawtypes")
	Response deleteMsg(Long id);
	
	/**
	* @Description:设置为学习任务回调接口
	* @author huangjianjun
	* @date 2017年4月11日 下午4:08:37
	*  @param id 课程编号
	*/
	@SuppressWarnings("rawtypes")
	Response setUpTrainMission(Long id);
	
	/**
	* @Description: 发布下架
	* @author huangjianjun
	* @date 2017年4月19日 上午9:12:38
	 */
	@SuppressWarnings("rawtypes")
	Response downShelfOrReleases(Long id,String status);
}
