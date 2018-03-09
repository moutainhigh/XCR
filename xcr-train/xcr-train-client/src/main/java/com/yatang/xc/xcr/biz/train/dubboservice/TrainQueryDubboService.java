package com.yatang.xc.xcr.biz.train.dubboservice;

import java.util.List;
import java.util.Map;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;

/**
 * @描述: 课堂管理只读服务接口
 * @作者: huangjianjun
 * @创建时间: 2017年3月24日-下午6:02:46 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface TrainQueryDubboService {
	/**
	* @Description: 查询单个课堂详情
	* @author huangjianjun
	* @date 2017年3月30日 下午3:49:26
	* @param Long id
	 */
	Response<TrainInfoDTO> findOneTrain(Long id);

	/**
	* @Description: 获取课堂列表分页信息
	* @author huangjianjun
	* @date 2017年3月30日 下午3:52:56
	* @param TrainInfoDTO param
	 */
	Response<Map<String, Object>> getTrainList(TrainInfoDTO param, int pageNum, int pageSize);

	/**
	* @Description: 获取已发布的课堂列表
	* @author huangjianjun
	* @date 2017年4月11日 下午3:05:19
	 */
	Response<List<Map<String, Object>>> getListPublishedTrain();

	/**
	* @Description: 获取app页面右上角新课堂记录数
	* @author huangjianjun
	* @date 2017年4月13日 下午8:12:31
	 */
	Response<Long> getTrainCount();

	/**
	* @Description: 获取app页面右上角新课堂记录数
	* @author huangjianjun
	* @date 2017年4月13日 下午8:12:31
	 */
	Response<Boolean> checkNameExist(String name);

	/**
	 * 
	 */
	Response<Long> queryMaxClassReleaseTime();

}
