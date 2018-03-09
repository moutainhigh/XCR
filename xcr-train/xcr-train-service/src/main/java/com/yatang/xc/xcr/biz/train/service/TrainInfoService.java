package com.yatang.xc.xcr.biz.train.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yatang.xc.xcr.biz.train.domain.TrainInfoPO;

/**
 * @描述: 课堂管理服务支撑接口.
 * @作者: huangjianjun
 * @创建时间: 2017-3-22,下午4:52:52 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface TrainInfoService {

	/**
	* @Description: 查询课堂列表
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:04
	 */
	PageInfo<TrainInfoPO> getTrainInfoPage(int pageNum, int pageSize, Integer status);

	/**
	* @Description: 保存课堂信息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	void saveTrainInfo(TrainInfoPO bean);

	/**
	* @Description: 根据主键查询课堂信息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	TrainInfoPO findByPrimaryKey(Long id);

	/**
	* @Description: 更新课堂信息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	void updateByPrimaryKey(TrainInfoPO bean);

	/**
	* @Description: 根据主键删除课堂信息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	void deleteByPrimaryKey(Long id);

	/**
	* @Description: 根据发布状态查询课堂列表
	* @author huangjianjun
	* @date 2017年3月23日 上午11:56:25
	 */
	List<TrainInfoPO> getListPublishedTrain(String status);

	/**
	* @Description: 保存修改
	* @author huangjianjun
	* @date 2017年3月30日 上午11:01:57
	 */
	void savaOrUpdate(TrainInfoPO po);

	/**
	* @Description: 获取最近发布课程数 
	* @author huangjianjun
	* @date 2017年4月10日 下午4:34:18
	 */
	Long getReleaseCount();

	/**
	* @Description: 下架发布课堂
	* @author huangjianjun
	* @date 2017年4月19日 上午9:16:03
	* @param id
	* @param status
	*/
	boolean downShelfOrReleases(Long id, String status);

	/**
	* @Description: 检索课堂名称是否存在
	* @author huangjianjun
	* @date 2017年5月3日 下午1:36:05
	 */
	boolean checkNameExist(String name);

	/**
	 * 查询最大课堂Id
	 * @return
	 */
	Long queryMaxClassReleaseTime();

}
