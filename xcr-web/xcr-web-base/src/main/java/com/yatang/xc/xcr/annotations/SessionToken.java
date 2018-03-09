package com.yatang.xc.xcr.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 13:03
 * @Summary : 验证Token信息
 */
@Documented
@ResponseBody
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SessionToken {

    @AliasFor("value")
    boolean require() default  true;

    @AliasFor("require")
    boolean value() default  true;
}
