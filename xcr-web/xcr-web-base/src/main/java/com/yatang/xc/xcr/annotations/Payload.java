package com.yatang.xc.xcr.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月24日 下午12:35:01
 * @Summary :  payload 标记注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Payload {}
