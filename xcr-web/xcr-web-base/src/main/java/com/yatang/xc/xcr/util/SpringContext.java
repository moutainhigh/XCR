package com.yatang.xc.xcr.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月23日 下午4:04:41
 * @Summary : Hold住Spring容器上下文 以便从容器中获取对应的Bean
 */
public class SpringContext implements ApplicationContextAware {

	private static  ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContext.context = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return SpringContext.context;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T) getApplicationContext().getBean(name);
	}
	
	public static  <T> T getBean(Class<T> clazz) throws BeansException {
		return getApplicationContext().getBean(clazz);
	}

}
