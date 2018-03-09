package com.yatang.xc.xcr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月23日 下午5:06:02
 * @Summary : 如若想在参数上使用
 * @see org.springframework.web.bind.annotation.CookieValue
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CookieValue {
	
	/**
	 * The name of the cookie to bind to.
	 * @since 4.2
	 */
	String value() ;

}
