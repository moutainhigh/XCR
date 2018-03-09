package com.yatang.xc.xcr.biz.core.service;

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

/**
 * 
* <广告服务>
*		
* @author: zhongrun
* @version: 1.0, 2017年9月21日
 */
public interface AdvertisementService {
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
	/**
	 * 新增广告组
	* <method description>
	*
	* @param advertisementGroupPO
	 */
	void insertAdvertisementGroup(AdvertisementGroupPO advertisementGroupPO);
	
	/**
	 * 修改广告组状态
	* <method description>
	*
	* @param updatePO
	 */
	void updateAdvertisementGroup(AdvertisementUpdatePO updatePO);
	
	/**
	 * 查询广告组
	* <method description>
	*
	* @param advertisementGroupPO
	* @return
	 */
	List<AdvertisementGroupPO> findAdvertisementGroup(AdvertisementGroupQueryPO advertisementGroupPO);
	
	/**
	 * 广告数量
	* <method description>
	*
	* @return
	 */
	Integer findAdvertisementTotal();
	
	/**
	 * 广告组数量
	* <method description>
	*
	* @return
	 */
	Integer findAdvertisementGroupTotal();
	
	/**
	 * 通过id获取广告详情
	* <method description>
	*
	* @param id
	* @return
	 */
	AdvertisementPO findAdvertisementById(Integer id);
	
	/**
	 * 更新广告
	* <method description>
	*
	* @param advertisementPO
	 */
	void updateAdertise(AdvertisementPO advertisementPO);
	
	/**
	 * 
	* <校验广告数量>
	*
	* @param id
	* @return
	 */
	Integer validateTotalAdver(Integer id);

	/**
	 * 
	* <通过id查询广告>
	*
	* @param id
	* @return
	 */
	AdvertisementGroupPO findAdverGroupById(Integer id);
	
	/**
	 * 
	* <通过组状态更新广告>
	*
	* @param updatePO
	 */
	void updateAdverByGroupState(AdvertisementUpdatePO updatePO);
	
	/**
	 * 
	* <当状态是删除的时候修改广告>
	*
	* @param staToPO
	 */
	void updateWhenStateISTo(AdverWhenStateToPO staToPO);
	
	/**
	 * 
	* <通过名字获取广告>
	*
	* @param name
	* @return
	 */
	AdvertisementPO findAdverByName(String name);
	
	/**
	 * 
	* <通过名字获取组>
	*
	* @param name
	* @return
	 */
	AdvertisementGroupPO findAdverGroupByName(String name);
	
	/**
	 * 
	* <通过组id获取广告数量>
	*
	* @param id
	* @return
	 */
	Integer findTotalByGroupId(Integer id);
	
	/**
	 * 
	* <通过位置获取广告（为app提供服务）>
	*
	* @param positionCode
	* @return
	 */
	Integer findAdverTotalByPosition(String positionCode);
	
	//------------------------供应链广告推荐接口------------------------------
	/**
	 * 	
	* <查询所有组>
	*
	* @param xcAdvertisementQueryPO
	* @return
	 */
	List<AdvertisementGroupPO> findGroup(XcAdvertisementQueryPO xcAdvertisementQueryPO);
		
		/**
		 * 
		* <通过组id获取子模块>
		*
		* @param id
		* @return
		 */
		List<AdvertisementPO> findAdverByGroupId(Integer id);
		
		/**
		 * 
		* <更新组信息>
		*
		* @param xcAdvertisementUpdatePO
		 */
		void updateGroupState(XcAdvertisementUpdatePO xcAdvertisementUpdatePO);
		
		/**
		 * 
		* <查询组详情>
		*
		* @param xcAdvertisementQueryPO
		* @return
		 */
		List<AdvertisementPO> findByGroupId(XcAdvertisementQueryPO xcAdvertisementQueryPO);
		
		/**
		 * 
		* <新增组>
		*
		* @param advertisementGroupPO
		 */
		void insertGroup(AdvertisementGroupPO advertisementGroupPO);
		
		/**
		 * 
		* <更新组>
		*
		* @param advertisementGroupPO
		 */
		void updateGroup(AdvertisementGroupPO advertisementGroupPO);
		
		/**
		 * 
		* <新增子广告>
		*
		* @param list
		 */
		void insertChain(List<AdvertisementPO> list);

		/**
		 * 
		* <更新组内子广告>
		*
		* @param list
		 */
		void updateChain(@Param("list") List<AdvertisementPO> list);
		
		/**
		 * 
		* <查询所有组数量>
		*
		* @param xcAdvertisementQueryPO
		* @return
		 */
		Integer findGroupTotal(XcAdvertisementQueryPO xcAdvertisementQueryPO);
		
		/**
		 * 
		* <查询最大排序code>
		*
		* @return
		 */
		Integer findMaxCode();
		
		/**
		 * 
		* <查询发布的广告图>
		*
		* @return
		 */
		List<SupplyAdvertisementGroupPO> findPublishAdver();
		
		String findGroupNameById(Integer id);
		//------------------------------首页启动广告推荐接口------------------------
		/**
		 * 
		* <新增广告>
		*
		* @param xcAdvertisementPO
		 */
		public void insertXcAdvertisement(XcAdvertisementPO xcAdvertisementPO);
		
		/**
		 * 
		* <更新广告图>
		*
		* @param xcAdvertisementPO
		 */
		public void updateXcAdvertisement(XcAdvertisementPO xcAdvertisementPO);
		
		/**
		 * 
		* <更新广告图状态>
		*
		* @param xcAdvertisementUpdatePO
		 */
		public void updateState(XcAdvertisementUpdatePO xcAdvertisementUpdatePO);
		
		/**
		 * 
		* <通过Id查询或者查询全部广告>
		*
		* @param xcAdvertisementQueryPO
		* @return
		 */
		public List<XcAdvertisementPO> findAllById(XcAdvertisementQueryPO xcAdvertisementQueryPO);
		
		/**
		 * 
		* <为App提供查询接口>
		*
		* @return
		 */
		public List<XcAdvertisementPO> findAllForApp();


}
