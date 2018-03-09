package com.yatang.xc.xcr.biz.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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

public interface AdvertisementDao {
	
	/**
	 * 通过状态获取广告
	* <method description>
	*
	* @param state
	* @return
	 */
	List<AdvertisementPO> findAdvertisementByState(StatePO state);
	
	
	
	/**
	 * 新增广告
	* <method description>
	*
	* @param advertisementPO
	 */
	void insertAdvertisement(AdvertisementPO advertisementPO);
	
	/**
	 * 设置广告状态
	* <method description>
	*
	* @param id
	 */
	void updateAdvertisement(AdvertisementUpdatePO updatePO);
	
	
	void insertAdvertisementGroup(AdvertisementGroupPO advertisementGroupPO);
	
	void updateAdvertisementGroup(AdvertisementUpdatePO updatePO);
	
	List<AdvertisementGroupPO> findAdvertisementGroup(AdvertisementGroupQueryPO advertisementGroupQueryPO);
	
	Integer findAdvertisementTotal();
	
	Integer findAdvertisementGroupTotal();
	
	AdvertisementPO findAdvertisementById(Integer id);
	
	void updateAdertise(AdvertisementPO advertisementPO);
	
	Integer validateTotalAdver(Integer id);
	
	AdvertisementGroupPO findAdverGroupById(Integer id);
	
	void updateAdverByGroupState(AdvertisementUpdatePO updatePO);
	
	void updateWhenStateISTo(AdverWhenStateToPO staToPO);
	
	
	AdvertisementPO findAdverByName(String name);
	
	AdvertisementGroupPO findAdverGroupByName(String name);
	
	Integer findTotalByGroupId(Integer id);
	
	Integer findAdverTotalByPosition(String positionCode);
	
	//------------------------供应链广告推荐接口------------------------------
	List<AdvertisementGroupPO> findGroup(XcAdvertisementQueryPO xcAdvertisementQueryPO);
	
	List<AdvertisementPO> findAdverByGroupId(Integer id);
	
	void updateGroupState(XcAdvertisementUpdatePO xcAdvertisementUpdatePO);
	
	List<AdvertisementPO> findByGroupId(XcAdvertisementQueryPO xcAdvertisementQueryPO);
	
	void insertGroup(AdvertisementGroupPO advertisementGroupPO);
	
	void updateGroup(AdvertisementGroupPO advertisementGroupPO);
	
	void insertChain(List<AdvertisementPO> list);

	void updateChain(@Param("list") List<AdvertisementPO> list);
	
	Integer findGroupTotal(XcAdvertisementQueryPO xcAdvertisementQueryPO);
	
	Integer findMaxCode();
	
	List<SupplyAdvertisementGroupPO> findPublishAdver();
	
	String findGroupNameById(Integer id);
	//------------------------------首页启动广告推荐接口------------------------
	public void insertXcAdvertisement(XcAdvertisementPO xcAdvertisementPO);
	
	public void updateXcAdvertisement(XcAdvertisementPO xcAdvertisementPO);
	
	public void updateState(XcAdvertisementUpdatePO xcAdvertisementUpdatePO);
	
	public List<XcAdvertisementPO> findAllById(XcAdvertisementQueryPO xcAdvertisementQueryPO);
	
	public List<XcAdvertisementPO> findAllForApp();
	
	
	
}
