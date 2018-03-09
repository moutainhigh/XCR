package com.yatang.xc.xcr.web.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.StringUtils;
import com.yatang.xc.oc.biz.adapter.RedisAdapterServie;
import com.yatang.xc.oc.biz.redis.dubboservice.RedisPlatform;

public class IdempotencyLockInterceptor implements MethodInterceptor {
	private static Logger						log	= Logger.getLogger(IdempotencyLockInterceptor.class);

	@Autowired
	private RedisAdapterServie<String, String>	redisDubboService;



	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String methodName = "";
		String idempotencyLockKey = null;
		Method currentMethod = null;
		try {
			try{
				methodName = invocation.getMethod().toString();
				currentMethod = invocation.getMethod();
				log.debug("调用----" + methodName);
				IdempotencyLock ideLock = currentMethod.getAnnotation(IdempotencyLock.class);
				idempotencyLockKey = getIdempotencyLockKey(ideLock, invocation.getArguments());
				// 给方法加幂等性锁
				if (!StringUtils.isEmpty(idempotencyLockKey)) {
					boolean aLong = beginIdempotencyLock(methodName, idempotencyLockKey, ideLock.value());
					if (!aLong) {
						log.info("===============幂等性锁成功============" + currentMethod.getReturnType().getName() + "===="
								+ Response.class.getName());
						if (currentMethod.getReturnType().getName().equals(Response.class.getName())) {
							if (StringUtils.isEmpty(ideLock.ideMessage())) {
								log.info("默认消息:手速不要太快");
								return new Response<>(false, "手速不要太快", "3003");
							} else {
								log.info("自定义锁消息:" + ideLock.ideMessage() + "==================");
								return new Response<>(false, ideLock.ideMessage(), "3004");
							}
						}
						return null;
					}
				}		
			}catch(Exception e){
				log.error(ExceptionUtils.getStackTrace(e));
			}
			Object returnValue;
			returnValue = invocation.proceed();
			log.debug("返回----" + JSON.toJSONString(returnValue) + "===" + (returnValue instanceof Response));
			return returnValue;
		} finally {
			if (!StringUtils.isEmpty(idempotencyLockKey)) {
				releaseIdempotencyLock(methodName, idempotencyLockKey);
			}
		}
	}



	/**
	 * 获取幂等性锁KEY @IdempotencyLock(idempotClsLock="UserQuery.userId#employeeLogin
	 * ")
	 */
	private String getIdempotencyLockKey(IdempotencyLock ideLock, Object[] args) {
		String idempotencyLockKey = "";
		if (ideLock != null) {
			if (!StringUtils.isEmpty(ideLock.idempotLock())) {
				idempotencyLockKey = ideLock.idempotLock();
			} else if (StringUtils.isEmpty(ideLock.idempotLock()) && !StringUtils.isEmpty(ideLock.idempotClsLock())) {
				// 访问目标方法的参数：
				if (args != null && args.length > 0) {
					for (Object arg : args) {
						if (arg != null && !arg.toString().equals("{}")) {
							// logger.info(ideLock.idempotClsLock()+"==="+ideLock.idempotClsLock().split("\\.")+"==="+ideLock.idempotClsLock().split("\\.").length);
							String argClsName = ideLock.idempotClsLock().split("\\.").length >= 2
									? ideLock.idempotClsLock().split("\\.")[0] : null;
							String[] filds = ideLock.idempotClsLock().split("\\.").length >= 2
									? ideLock.idempotClsLock().split("\\.")[1].split("#") : null;
							// logger.info(ideLock.idempotClsLock()+"==="+arg.getClass().getName()+"==="+arg.getClass().getName().endsWith(argClsName));
							if (filds != null && argClsName != null && arg.getClass().getName().endsWith(argClsName)) {
								idempotencyLockKey = ideLock.idempotClsLock();
								if (ideLock.isForm()) {
									idempotencyLockKey = "intentid";
								}
								for (String fild : filds) {
									String fildVal = getFildVal(arg, fild).toString();
									if (!StringUtils.isEmpty(fildVal)) {
										idempotencyLockKey += "_" + fildVal;
									}
								}
								break;
							}
						}
					}
				}

			} else {
				idempotencyLockKey = null;
			}

			return idempotencyLockKey;
		}

		return null;
	}



	/*
	 * 通过反射获取参数值
	 */
	private static Object getFildVal(Object bean, String fname) {
		if (bean instanceof JSONObject) {

			return ((JSONObject) bean).getString(fname) == null ? "" : ((JSONObject) bean).getString(fname);
		}

		Class<? extends Object> defineClass = (Class<? extends Object>) bean.getClass();
		Field[] fs = defineClass.getDeclaredFields();

		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			if (f.getName().equals(fname)) {
				try {
					Object val = f.get(bean);
					if (val != null) {
						// System.out.println("======"+val.toString());
						return val;
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					System.out.println("错误：" + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		return "";
	}



	private boolean beginIdempotencyLock(String method, String key, int expire) {
		if (StringUtils.isEmpty(key)) {
			key = "sessionException";
		}
		boolean result = redisDubboService.setnx(RedisPlatform.common, key, String.valueOf(System.currentTimeMillis()));
		if (result) {
			redisDubboService.setex(RedisPlatform.common, key, String.valueOf(System.currentTimeMillis()),
					Long.valueOf(expire));
		}
		log.info("idempotencyLockKey======" + key + "成功生效[" + expire + "]秒======方法[" + method + "]幂等性锁生效：result="
				+ result);
		return result;
	}



	public Long releaseIdempotencyLock(String method, String key) {
		// Jedis resource = jedisPool.getResource();
		Long del = redisDubboService.del(RedisPlatform.common, key); // resource.del(key);
		log.info("idempotencyLockKey======" + key + "删除释放======方法[" + method + "]幂等性锁释放：result=" + del);
		// resource.close();
		return del;
	}

}