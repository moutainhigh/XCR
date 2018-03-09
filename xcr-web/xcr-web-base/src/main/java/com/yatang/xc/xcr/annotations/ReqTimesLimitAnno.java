package com.yatang.xc.xcr.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gaodawei
 * @Date 2018年1月5日(星期五)
 * @version 1.0.0
 * @function 请求限制处理注解
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqTimesLimitAnno {

	String limitMethod() default "";

	int limitTimeLength() default 30;// 请求时间限长，单位：秒

	int limitTimes() default 1;// 限制请求次数

	String limit_Tip() default "超过请求次数限制";// 提示

	@Target({ ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@interface ReqTimesList {
		ReqTimesLimitAnno[] value();
	}

}
