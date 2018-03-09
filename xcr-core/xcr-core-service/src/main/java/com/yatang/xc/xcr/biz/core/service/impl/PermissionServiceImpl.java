package com.yatang.xc.xcr.biz.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatang.xc.xcr.biz.core.dao.PermissionDAO;
import com.yatang.xc.xcr.biz.core.domain.PermissionPO;
import com.yatang.xc.xcr.biz.core.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService{
	
	@Autowired
	PermissionDAO permissionDAO;

	@Override
	public List<PermissionPO> findPermissionByRoles(List<String> list) {
		return permissionDAO.findPermissionByRoles(list);
	}

}
