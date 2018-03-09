package com.yatang.xc.xcr.web.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.PermissionDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.PermissionDubboService;

public class ShiroPermissionFactory extends ShiroFilterFactoryBean {
	
	@Autowired
	PermissionDubboService permissionDubboService;
	
	private static Logger log = LoggerFactory.getLogger(ShiroPermissionFactory.class);

	
	private String				definition	= "";

	@Value("${IS_ENABLE}")
	private  Boolean IS_ENABLE=false;

	
	@Override
	public void setFilterChainDefinitions(String definitions) {
		this.definition = definitions;// 记录配置的静态过滤链
		Map<String, String> chains = new HashMap<String, String>();
		if(IS_ENABLE){
			permissionTransfer(chains);
		}
		// 加载配置默认的过滤链
		Ini ini = new Ini();
		ini.load(definition);
		Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}
		// 加上数据库中过滤链
		section.putAll(chains);
		setFilterChainDefinitionMap(section);
	}


	/**
	 * 获取拦截权限
	 * @param chains
	 */
	private void permissionTransfer(Map<String, String> chains) {
		List<String> allCode = new ArrayList<String>();
		allCode.add("0");
		Response<List<PermissionDTO>> findPermissionDubboResult = permissionDubboService.findPermissionByRoles(allCode);
		log.info("Call findPermissionByRoles Request Data Is:"+JSONObject.toJSONString(allCode)+"And Response Data Is:"+JSONObject.toJSONString(findPermissionDubboResult));
		if (findPermissionDubboResult==null||!findPermissionDubboResult.isSuccess()) {
			log.error("Call findPermissionByRoles Return False Or Null");
		}else {
			List<PermissionDTO> permissionDTOs = findPermissionDubboResult.getResultObject();
			for (PermissionDTO permissionDTO : permissionDTOs) {
				chains.put(permissionDTO.getPermissionUrl(), "perms["+permissionDTO.getPermissionName()+"]");
			}
		}
	}

}
