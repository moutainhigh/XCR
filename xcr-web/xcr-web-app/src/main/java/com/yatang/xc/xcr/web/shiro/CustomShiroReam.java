package com.yatang.xc.xcr.web.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.StringUtils;
import com.yatang.fc.facade.dto.RoleDTO;
import com.yatang.xc.pos.cloud.dto.ResultDTO;
import com.yatang.xc.pos.cloud.dubboservice.XcrUserDubboService;
import com.yatang.xc.xcr.biz.core.dto.PermissionDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.PermissionDubboService;
import com.yatang.xc.xcr.util.TokenUtil;
import com.yatang.xc.xcr.web.ActivityEnrollAction;

public class CustomShiroReam extends AuthorizingRealm {

	private static final String			myRealmName	= "Realm";
	private static Logger log = LoggerFactory.getLogger(CustomShiroReam.class);
	
	@Autowired
	PermissionDubboService permissionDubboService;
	
	@Autowired
	XcrUserDubboService xcrUserDubboService;
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pPrincipals) {
		log.info("start doGetAuthorizationInfo");
		String loginName = (String) pPrincipals.getPrimaryPrincipal();
		if (!StringUtils.isEmpty(loginName)) {
			SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
			//Judge Is New User
			Boolean isF5Account = false;
			List<String> permis = new ArrayList<String>();
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(loginName);
            if (tokenJson != null) {
            	isF5Account = tokenJson.getBoolean("isF5Account");
            }
			if (!isF5Account) {
				permis.add("0");
			}else {
				Response<List<ResultDTO>> queryRoleByLoginAccountDubboService = xcrUserDubboService.queryRoleByLoginAccount(loginName);
				
				List<ResultDTO> res = queryRoleByLoginAccountDubboService.getResultObject();
				
				
				for (ResultDTO resultDTO : res) {
					switch (resultDTO.getRoleCode()) {
					case "jiamengshang":
						permis.add("0");
						break;
					case "franchisee":
						permis.add("1");
						break;
						
					case "shopowner":
						permis.add("2");
						break;
					case "cashier":
						permis.add("3");
						break;

					default:
						break;
					}
					
				}
			}
			
			
			
			Response<List<PermissionDTO>> per = permissionDubboService.findPermissionByRoles(permis);
			log.info("Call findPermissionByRole Request Data Is:"+com.alibaba.fastjson.JSONObject.toJSONString(permis)+"And Reponse Data Is"+com.alibaba.fastjson.JSONObject.toJSONString(per));
			Set<String> permission = new HashSet<String>();
			List<PermissionDTO> ers = per.getResultObject();
			for (PermissionDTO permissionDTO : ers) {
				permission.add(permissionDTO.getPermissionName());
			}
			authorizationInfo.setStringPermissions(permission);
			return authorizationInfo;
		}
		return null;
	}



	/*
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken pToken) throws AuthenticationException {
		log.info("start doGetAuthenticationInfo");
		String loginName = (String) pToken.getPrincipal();
		if (!StringUtils.isEmpty(loginName)) {
					AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(loginName,
							"123456", myRealmName);
					return authcInfo;
		}
		return null;
	}

}
