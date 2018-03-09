package com.yatang.xc.xcr.biz.core.service;

import java.util.List;

import com.yatang.xc.xcr.biz.core.domain.UpdateStatePO;
import com.yatang.xc.xcr.biz.core.domain.VersionAppQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionListQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionPO;

/**
 * 
* <版本服务>
*		
* @author: zhongrun
* @version: 1.0, 2017年9月22日
 */
public interface VersionService {
	
	/**
	 * 
	* <查询版本列表>
	*
	* @param versionListQueryPO
	* @return
	 */
	List<VersionPO> findVersionList(VersionListQueryPO versionListQueryPO); 
	
	/**
	 * 
	* <通过条件查询版本条数>
	*
	* @param versionListQueryPO
	* @return
	 */
	Integer findTotalByCondetion(VersionListQueryPO versionListQueryPO);
	
	/**
	 * 
	* <通过类型和序列号号查询大的所有版本>
	*
	* @param versionAppQueryPO
	* @return
	 */
	List<VersionPO> findByTypeAndVersion(VersionAppQueryPO versionAppQueryPO);
	
	/**
	 * 
	* <修改版本状态>
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
	
	/**
	 * 
	* <更新版本>
	*
	* @param versionPO
	 */
	void updateVersion(VersionPO versionPO);

	/**
	 * 
	* <校验版本是否重复>
	*
	* @param versionPO
	* @return
	 */
	List<VersionPO> validateVersion(VersionPO versionPO);

	/**
	 * 
	* <查询供应链版本>
	*
	* @param versionAppQueryPO
	* @return
	 */
	List<VersionPO> findGongYingLianVersion(VersionAppQueryPO versionAppQueryPO);
	
	public Integer findMaxVersion(VersionPO versionPO);
}
