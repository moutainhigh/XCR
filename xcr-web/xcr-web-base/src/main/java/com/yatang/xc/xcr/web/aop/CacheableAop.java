package com.yatang.xc.xcr.web.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.yatang.xc.oc.biz.adapter.RedisAdapterServie;
import com.yatang.xc.oc.biz.redis.dubboservice.RedisPlatform;
import com.yatang.xc.xcr.enums.DateEnum;
import com.yatang.xc.xcr.util.AopUtils;
import com.yatang.xc.xcr.util.SpringExpressionUtils;

/**
 * Created by wangyang on 2017/10/11.
 */
@Aspect
@Component
public class CacheableAop {

    private static Logger log = LoggerFactory.getLogger(CacheableAop.class);
    @Autowired
    private RedisAdapterServie redisAdapter;

    @Around("@annotation(cache)")
    public Object cached(final ProceedingJoinPoint pjp, Cacheable cache) throws Throwable {
        log.info("in aop cache .......................................................");
        Object result = null;
        Method method = AopUtils.getMethod(pjp);

        String key = cache.key();
        // 用SpEL解释key值
        String keyVal = SpringExpressionUtils.parseKey(key, method, pjp.getArgs());
        if (!StringUtils.isEmpty(cache.category())) {
            keyVal = cache.category() + "_" + keyVal;
        } else {
            // 如果cacheable的注解中category为空取 类名+方法名
            keyVal = pjp.getTarget().getClass().getSimpleName() + "_"
                    + method.getName() + "_" + keyVal;
        }
        Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();

        // 从redis读取keyVal，并且转换成returnType的类型
        result = redisAdapter.get(RedisPlatform.common,keyVal, returnType);

        if (result == null) {
            try {
                // 如果redis没有数据则执行拦截的方法体
                result = pjp.proceed();
                int expireSeconds = 0;
                // 如果Cacheable注解中的expire为默认(默认值为-1)
                if (cache.expire() == -1) {
                    expireSeconds = 3600;
                } else {
                    expireSeconds = getExpireSeconds(cache);
                }
                // 把拦截的方法体得到的数据设置进redis，过期时间为计算出来的expireSeconds
                redisAdapter.setex(RedisPlatform.common,keyVal, result, expireSeconds);
                log.info("已缓存缓存:key=" + keyVal);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return result;
        }
        log.info("========从缓存中读取");
        log.info("=======:key   = " + key);
        log.info("=======:keyVal= " + keyVal);
        log.info("=======:val   = " + result);
        return result;
    }

    /**
     * 计算根据Cacheable注解的expire和DateUnit计算要缓存的秒数
     *
     * @param cacheable
     * @return
     */
    private int getExpireSeconds(Cacheable cacheable) {
        int expire = cacheable.expire();
        DateEnum unit = cacheable.dateType();
        if (expire <= 0) {
            return 0;
        }
        if (unit == DateEnum.MINUTES) {
            return expire * 60;
        } else if (unit == DateEnum.HOURS) {
            return expire * 60 * 60;
        } else if (unit == DateEnum.DAYS) {
            return expire * 60 * 60 * 24;
        } else if (unit == DateEnum.MONTHS) {
            return expire * 60 * 60 * 24 * 30;
        } else if (unit == DateEnum.YEARS) {
            return expire * 60 * 60 * 24 * 365;
        }
        return expire;
    }
}
