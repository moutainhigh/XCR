package com.yatang.xc.xcr.annotations;

import java.lang.annotation.*;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月21日 15:19
 * @Summary : 参数泛型注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface GenericParadigmType {
    Class<?> value() ;
}
