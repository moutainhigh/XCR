package com.yatang.xc.xcr.biz.core.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.core.domain.UpdateStatePO;
import com.yatang.xc.xcr.biz.core.domain.VersionAppQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionListQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionPO;

public interface VersionDao {
	
	/**
	 * 
	* <查询所有版本>
	*
	* @param versionListQueryPO
	* @return
	 */
	List<VersionPO> findVersionList(VersionListQueryPO versionListQueryPO); 
	
	/**
	 * 
	* <根据条件查询版本数量>
	*
	* @param versionListQueryPO
	* @return
	 */
	Integer findTotalByCondetion(VersionListQueryPO versionListQueryPO);	
	
	/**
	 * 
	* <通过类型和序列号查询所有>
	*
	* @param versionAppQueryPO
	* @return
	 */
	List<VersionPO> findByTypeAndVersion(VersionAppQueryPO versionAppQueryPO);
	
	/**
	 * 
	* <更新版本状态>
	*
	* @param updateStatePO
	 */
	void updateStateAndPublish(UpdateStatePO updateStatePO);
	
	/**
	 * 
	* <新增版本>
	*
	* @param versionPO
	 */
	void insertVersion(VersionPO versionPO);
	
	void updateVersion(VersionPO versionPO);
	
	List<VersionPO> validateVersion(VersionPO versionPO);
	
	List<VersionPO> findGongYingLianVersion(VersionAppQueryPO versionAppQueryPO);
	
	public Integer findMaxVersion(VersionPO versionPO);
	

}
