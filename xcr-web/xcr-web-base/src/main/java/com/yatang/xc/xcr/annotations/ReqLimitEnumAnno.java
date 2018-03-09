package com.yatang.xc.xcr.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yatang.xc.xcr.enums.LimitRoleEnum;

/**
 * @author gaodawei
 * @Date 2018年1月5日(星期五)
 * @version 1.0.0
 * @function 请求限制处理注解
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqLimitEnumAnno {

	LimitRoleEnum role() default LimitRoleEnum.SEC_LIMIT;

	@Target({ ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@interface LimitList {
		ReqLimitEnumAnno[] value();
	}

}
