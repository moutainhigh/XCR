package com.yatang.xc.xcr.biz.core.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.PermissionDTO;

/**
 * 
* <权限>
*		
* @author: zhongrun
* @version: 1.0, 2017年12月19日
 */
public interface PermissionDubboService {
	
	/**
	 * 
	* <通过角色组获取权限>
	*
	* @param roleList
	* @return
	 */
	Response<List<PermissionDTO>> findPermissionByRoles(List<String> roleList);

}
