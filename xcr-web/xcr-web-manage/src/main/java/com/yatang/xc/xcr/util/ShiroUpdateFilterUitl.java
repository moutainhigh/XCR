package com.yatang.xc.xcr.util;

import javax.annotation.PostConstruct;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ShiroUpdateFilterUitl {
	private static Logger log = LoggerFactory.getLogger(ShiroUpdateFilterUitl.class);

	@Autowired
	private ShiroFilterFactoryBean shiroFilterFactoryBean;

	private boolean updateShiroFilter;

	private String filterChainDefinitions;

	@PostConstruct
	public void init() {
		if (updateShiroFilter) {
			shiroFilterFactoryBean.setFilterChainDefinitions(filterChainDefinitions);
		}
	}

	public String getFilterChainDefinitions() {
		return filterChainDefinitions;
	}

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public boolean isUpdateShiroFilter() {
		return updateShiroFilter;
	}

	public void setUpdateShiroFilter(boolean updateShiroFilter) {
		this.updateShiroFilter = updateShiroFilter;
	}

}
