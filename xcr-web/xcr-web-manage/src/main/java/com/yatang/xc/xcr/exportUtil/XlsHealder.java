package com.yatang.xc.xcr.exportUtil;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <class description>XLS的header描述注解
 * 
 * @author: zhoubaiyun
 * @version: 1.0, 2017年7月18日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XlsHealder {
	// 列名称
	String value();



	// 列长度，单位为字节数，一个汉字占三个字节，比如："hello，雅堂"占了12字节，不包含引号
	int width() default -1;

}
