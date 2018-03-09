package com.yatang.xc.xcr.biz.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatang.xc.xcr.biz.core.dao.AdvertisementDao;
import com.yatang.xc.xcr.biz.core.domain.AdverWhenStateToPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementGroupPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementGroupQueryPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementUpdatePO;
import com.yatang.xc.xcr.biz.core.domain.StatePO;
import com.yatang.xc.xcr.biz.core.domain.SupplyAdvertisementGroupPO;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementPO;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementQueryPO;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementUpdatePO;
import com.yatang.xc.xcr.biz.core.service.AdvertisementService;
/**
 * 
* 广告后台配置
*		
* @author: zhongrun
* @version: 1.0, 2017年7月6日
 */
@Service("advertisementServiceImpl")
public class AdvertisementServiceImpl implements AdvertisementService{
	@Autowired
	AdvertisementDao advertisementDao;

	@Override
	public List<AdvertisementPO> findAdvertisementByState(StatePO state) {
		
		return advertisementDao.findAdvertisementByState(state);
	}

	@Override
	public void insertAdvertisement(AdvertisementPO advertisementPO) {
		advertisementDao.insertAdvertisement(advertisementPO);
		
	}

	@Override
	public void updateAdvertisement(AdvertisementUpdatePO updatePO) {
		advertisementDao.updateAdvertisement(updatePO);
		
	}

	@Override
	public void insertAdvertisementGroup(
			AdvertisementGroupPO advertisementGroupPO) {
		advertisementDao.insertAdvertisementGroup(advertisementGroupPO);
		
	}

	@Override
	public void updateAdvertisementGroup(AdvertisementUpdatePO updatePO) {
		advertisementDao.updateAdvertisementGroup(updatePO);
		
	}

	@Override
	public List<AdvertisementGroupPO> findAdvertisementGroup(
			AdvertisementGroupQueryPO advertisementGroupPO) {
		return advertisementDao.findAdvertisementGroup(advertisementGroupPO);
	}

	@Override
	public Integer findAdvertisementTotal() {
		return advertisementDao.findAdvertisementTotal();
	}

	@Override
	public Integer findAdvertisementGroupTotal() {
		return advertisementDao.findAdvertisementGroupTotal();
	}

	@Override
	public AdvertisementPO findAdvertisementById(Integer id) {
		
		return advertisementDao.findAdvertisementById(id);
	}

	@Override
	public void updateAdertise(AdvertisementPO advertisementPO) {
		advertisementDao.updateAdertise(advertisementPO);
		
	}

	@Override
	public Integer validateTotalAdver(Integer id) {
		return advertisementDao.validateTotalAdver(id);
	}

	@Override
	public AdvertisementGroupPO findAdverGroupById(Integer id) {
		return advertisementDao.findAdverGroupById(id);
	}

	@Override
	public void updateAdverByGroupState(AdvertisementUpdatePO updatePO) {
		advertisementDao.updateAdverByGroupState(updatePO);
		
	}

	@Override
	public void updateWhenStateISTo(AdverWhenStateToPO staToPO) {
		advertisementDao.updateWhenStateISTo(staToPO);
		
	}

	@Override
	public AdvertisementPO findAdverByName(String name) {
		return advertisementDao.findAdverByName(name);
	}

	@Override
	public AdvertisementGroupPO findAdverGroupByName(String name) {
		return advertisementDao.findAdverGroupByName(name);
	}

	@Override
	public Integer findTotalByGroupId(Integer id) {
		return advertisementDao.findTotalByGroupId(id);
	}

	@Override
	public Integer findAdverTotalByPosition(String positionCode) {
		return advertisementDao.findAdverTotalByPosition(positionCode);
	}

	@Override
	public List<AdvertisementGroupPO> findGroup(XcAdvertisementQueryPO xcAdvertisementQueryPO) {
		return advertisementDao.findGroup(xcAdvertisementQueryPO);
	}

	@Override
	public List<AdvertisementPO> findAdverByGroupId(Integer id) {
		return advertisementDao.findAdverByGroupId(id);
	}

	@Override
	public void updateGroupState(XcAdvertisementUpdatePO xcAdvertisementUpdatePO) {
		advertisementDao.updateGroupState(xcAdvertisementUpdatePO);
		
	}

	@Override
	public List<AdvertisementPO> findByGroupId(XcAdvertisementQueryPO xcAdvertisementQueryPO) {
		return advertisementDao.findByGroupId(xcAdvertisementQueryPO);
	}

	@Override
	public void insertGroup(AdvertisementGroupPO advertisementGroupPO) {
		advertisementDao.insertGroup(advertisementGroupPO);
		
	}

	@Override
	public void updateGroup(AdvertisementGroupPO advertisementGroupPO) {
		advertisementDao.updateGroup(advertisementGroupPO);
		
	}

	@Override
	public void insertChain(List<AdvertisementPO> list) {
		advertisementDao.insertChain(list);
		
	}

	@Override
	public void updateChain(List<AdvertisementPO> list) {
		advertisementDao.updateChain(list);
		
	}

	@Override
	public Integer findGroupTotal(XcAdvertisementQueryPO xcAdvertisementQueryPO) {
		return advertisementDao.findGroupTotal(xcAdvertisementQueryPO);
	}

	@Override
	public Integer findMaxCode() {
		return advertisementDao.findMaxCode();
	}

	@Override
	public List<SupplyAdvertisementGroupPO> findPublishAdver() {
		return advertisementDao.findPublishAdver();
	}
	
	
	@Override
	public void insertXcAdvertisement(XcAdvertisementPO xcAdvertisementPO) {
		advertisementDao.insertXcAdvertisement(xcAdvertisementPO);
	}

	@Override
	public void updateXcAdvertisement(XcAdvertisementPO xcAdvertisementPO) {
		advertisementDao.updateXcAdvertisement(xcAdvertisementPO);
	}

	@Override
	public void updateState(XcAdvertisementUpdatePO xcAdvertisementUpdatePO) {
		advertisementDao.updateState(xcAdvertisementUpdatePO);
	}

	@Override
	public List<XcAdvertisementPO> findAllById(XcAdvertisementQueryPO xcAdvertisementQueryPO) {
		return advertisementDao.findAllById(xcAdvertisementQueryPO);
	}

	@Override
	public List<XcAdvertisementPO> findAllForApp() {
		return advertisementDao.findAllForApp();
	}

	@Override
	public String findGroupNameById(Integer id) {
		return advertisementDao.findGroupNameById(id);
	}


	

}
