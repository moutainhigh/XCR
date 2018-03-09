package com.yatang.xc.xcr.exportUtil;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <class description>XLS的Sheet描述注解
 * 
 * @author: zhoubaiyun
 * @version: 1.0, 2017年7月18日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XlsSheet {
	// sheet的名称
	String value();
}
