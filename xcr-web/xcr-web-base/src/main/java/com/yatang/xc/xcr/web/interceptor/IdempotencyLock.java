/**
 * Create by zhangChenggang
 */
package com.yatang.xc.xcr.web.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 张成刚
 * 防止重复操作锁
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdempotencyLock {
	
	int  value() default 30;   //锁的过期时间  单位秒
	
	String idempotLock() default "";   //锁内容
	
	String idempotClsLock() default "";

	String ideMessage() default "";
	
	boolean isForm() default true;  //是否是表单锁
}
