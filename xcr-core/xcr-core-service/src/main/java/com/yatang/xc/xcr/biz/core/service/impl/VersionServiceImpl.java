package com.yatang.xc.xcr.biz.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatang.xc.xcr.biz.core.dao.VersionDao;
import com.yatang.xc.xcr.biz.core.domain.UpdateStatePO;
import com.yatang.xc.xcr.biz.core.domain.VersionAppQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionListQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionPO;
import com.yatang.xc.xcr.biz.core.service.VersionService;

@Service
public class VersionServiceImpl implements VersionService{
	@Autowired
	private VersionDao versionDao;

	@Override
	public List<VersionPO> findVersionList(VersionListQueryPO versionListQueryPO) {
		return versionDao.findVersionList(versionListQueryPO);
	}

	@Override
	public Integer findTotalByCondetion(VersionListQueryPO versionListQueryPO) {
		return versionDao.findTotalByCondetion(versionListQueryPO);
	}

	@Override
	public List<VersionPO> findByTypeAndVersion(VersionAppQueryPO versionAppQueryPO) {
		return versionDao.findByTypeAndVersion(versionAppQueryPO);
	}

	@Override
	public void updateStateAndPublish(UpdateStatePO updateStatePO) {
		versionDao.updateStateAndPublish(updateStatePO);
	}

	@Override
	public void insertVersion(VersionPO versionPO) {
		versionDao.insertVersion(versionPO);
		
	}

	@Override
	public void updateVersion(VersionPO versionPO) {
		versionDao.updateVersion(versionPO);
		
	}

	@Override
	public List<VersionPO> validateVersion(VersionPO versionPO) {	
		return versionDao.validateVersion(versionPO);
	}

	@Override
	public List<VersionPO> findGongYingLianVersion(
			VersionAppQueryPO versionAppQueryPO) {

		return versionDao.findGongYingLianVersion(versionAppQueryPO);
	}

	@Override
	public Integer findMaxVersion(VersionPO versionPO) {
		return versionDao.findMaxVersion(versionPO);
	}

}
