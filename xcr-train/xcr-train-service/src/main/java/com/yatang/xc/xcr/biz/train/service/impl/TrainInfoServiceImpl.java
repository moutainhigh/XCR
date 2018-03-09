package com.yatang.xc.xcr.biz.train.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yatang.xc.xcr.biz.train.dao.TrainInfoDao;
import com.yatang.xc.xcr.biz.train.domain.TrainInfoPO;
import com.yatang.xc.xcr.biz.train.enums.PublicEnum;
import com.yatang.xc.xcr.biz.train.service.TrainInfoService;

/**
 * @描述: 课堂管理服务实现.
 * @作者: huangjianjun
 * @创建时间: 2017年3月22日-下午3:00:02 .
 * @版本: 1.0 .
 * @param <T>
 */
@Service("trainInfoService")
public class TrainInfoServiceImpl implements TrainInfoService {
	@Autowired
	private TrainInfoDao trainInfoDao;

	@Override
	public PageInfo<TrainInfoPO> getTrainInfoPage(int pageNum, int pageSize, Integer status) {
		Map<String, Object> paramMap = new HashMap<String, Object>(); // 业务条件查询参数
		paramMap.put("status", status); // 状态
		PageHelper.startPage(pageNum, pageSize);
		List<TrainInfoPO> list = trainInfoDao.listBy(paramMap);
		PageInfo<TrainInfoPO> pageInfo = new PageInfo<TrainInfoPO>(list);
		return pageInfo;
	}

	@Override
	public void saveTrainInfo(TrainInfoPO bean) {
		trainInfoDao.insert(bean);
	}

	@Override
	public TrainInfoPO findByPrimaryKey(Long id) {
		return trainInfoDao.getById(id);
	}

	@Override
	public void updateByPrimaryKey(TrainInfoPO bean) {
		trainInfoDao.update(bean);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		trainInfoDao.deleteById(id);
	}

	@Override
	public List<TrainInfoPO> getListPublishedTrain(String status) {
		Map<String, Object> paramMap = new HashMap<String, Object>(); // 业务条件查询参数
		paramMap.put("status", status); // 状态
		paramMap.put("isMission", PublicEnum.ZERO.getCode()); // 是否学习任务
		return trainInfoDao.listBy(paramMap);
	}

	@Override
	public void savaOrUpdate(TrainInfoPO po) {
		if (po != null) {
			if (po.getId() != null && po.getId() != 0) {
				trainInfoDao.update(po);
			} else {
				trainInfoDao.insert(po);
			}
		}
	}

	@Override
	public Long getReleaseCount() {
		return trainInfoDao.getReleaseCount();
	}

	@Override
	public boolean downShelfOrReleases(Long id, String status) {
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("id", id);
		pMap.put("status", status);
		return trainInfoDao.downShelfOrReleases(pMap) >= 1;
	}

	@Override
	public boolean checkNameExist(String name) {
		return trainInfoDao.checkNameExist(name) >= 1;
	}

	@Override
	public Long queryMaxClassReleaseTime() {
		return trainInfoDao.queryMaxClassReleaseTime();
	}

}
