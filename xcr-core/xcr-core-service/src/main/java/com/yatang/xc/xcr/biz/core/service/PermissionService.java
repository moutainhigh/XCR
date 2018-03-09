package com.yatang.xc.xcr.biz.core.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yatang.xc.xcr.biz.core.domain.PermissionPO;

public interface PermissionService {
	
	/**
	 * 
	* <根据角色code列表查询权限>
	*
	* @param list
	* @return
	 */
	List<PermissionPO> findPermissionByRoles(@Param("list") List<String> list);

}
