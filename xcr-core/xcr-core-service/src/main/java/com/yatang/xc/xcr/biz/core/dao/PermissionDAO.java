package com.yatang.xc.xcr.biz.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yatang.xc.xcr.biz.core.domain.PermissionPO;

/**
 * 
* <权限一期小超人接口>
*		
* @author: zhongrun
* @version: 1.0, 2017年12月19日
 */
public interface PermissionDAO {
	
	/**
	 * 
	* <根据角色code列表查询权限>
	*
	* @param list
	* @return
	 */
	List<PermissionPO> findPermissionByRoles(@Param("list") List<String> list);
	
}
