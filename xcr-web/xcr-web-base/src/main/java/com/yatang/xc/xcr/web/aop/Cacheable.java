package com.yatang.xc.xcr.web.aop;

import com.yatang.xc.xcr.enums.DateEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解
 * Created by wangyang on 2017/10/10.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    String category() default "";
    String key();
    // 过期时间数值，默认-1为永久
    int expire() default -1;
    // 时间单位，默认为秒
    DateEnum dateType() default DateEnum.SECONDS;
}
