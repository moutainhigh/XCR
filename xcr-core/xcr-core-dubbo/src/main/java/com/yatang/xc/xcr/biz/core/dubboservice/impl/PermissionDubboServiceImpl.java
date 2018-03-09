package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busi.common.resp.Response;
import com.busi.common.utils.BeanConvertUtils;
import com.yatang.xc.xcr.biz.core.domain.PermissionPO;
import com.yatang.xc.xcr.biz.core.dto.PermissionDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.PermissionDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.AdverEnum;
import com.yatang.xc.xcr.biz.core.service.PermissionService;

/**
 * 
* <权限配置>
*		
* @author: zhongrun
* @version: 1.0, 2017年12月19日
 */
@Service("permissionDubboService")
public class PermissionDubboServiceImpl implements PermissionDubboService{
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	PermissionService permissionService;

	/**
	 * 通过角色组获取权限
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.PermissionDubboService#findPermissionByRoles(java.util.List)
	 */
	@Override
	public Response<List<PermissionDTO>> findPermissionByRoles(List<String> roleList) {
		Response<List<PermissionDTO>> response = new Response<List<PermissionDTO>>();
		try {
			List<PermissionPO> permissionPOs = permissionService.findPermissionByRoles(roleList);
			List<PermissionDTO> permissionDTOs = BeanConvertUtils.convertList(permissionPOs, PermissionDTO.class);
			response.setResultObject(permissionDTOs);
			response.setSuccess(true);
			return response;
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			response.setErrorMessage(e.getMessage());
			response.setCode(AdverEnum.ERROR_CODE.getCode());
			response.setSuccess(false);
			return response;
		}
	
	}
	
}
