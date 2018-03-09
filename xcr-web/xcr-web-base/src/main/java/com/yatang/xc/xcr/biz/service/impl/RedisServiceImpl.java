package com.yatang.xc.xcr.biz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatang.xc.oc.biz.adapter.RedisAdapterServie;
import com.yatang.xc.oc.biz.redis.dubboservice.RedisPlatform;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.RedisKeyEnum;

/**
 * 
 * 主要功能：保存对象到redis
 * 
 * @author liuli
 * @param <V
 *            extends Serializable>
 */	
@Service("redisService")
public class RedisServiceImpl<V> implements RedisService<V> {

	private Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	@Autowired
	private RedisAdapterServie<String, V> redisAdapter;

	/**
	 * 
	 * 功能说明： 保存对象到redis服务里去
	 * 
	 * @author liuli
	 * @creationDate 2015年3月31日 上午10:51:04
	 * @param prefixKey
	 *            对应redisKey的前缀
	 * @param suffixKey
	 *            对应redisKey的后缀
	 * @param timeout
	 *            多久过期（按分钟计算）
	 * @param value
	 *            对象值（保存的对象必须实现Serializable接口）
	 */
	public void saveObject(RedisKeyEnum prefixKey, String suffixKey, int timeout, V value) {
		try {
			String keyStr = getRedisKey(prefixKey, suffixKey);
			if (timeout < 0) {
				redisAdapter.set(RedisPlatform.common,keyStr, value);
				return;
			}
			redisAdapter.setex(RedisPlatform.common,keyStr, value, timeout * 60);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 
	 * 功能说明：根据key到redis里查询对应的值
	 * 
	 * @author liuli
	 * @creationDate 2015年3月31日 上午11:59:05
	 * @param prefixKey
	 *            对应redisKey的前缀
	 * @param suffixKey
	 *            对应redisKey的后缀
	 * @return V
	 */
	public V getKeyForObject(RedisKeyEnum prefixKey, String suffixKey, Class clazz) {
		try {
			String keyStr = getRedisKey(prefixKey, suffixKey);
			logger.info("get param:" + keyStr + " class:" + clazz);
			return redisAdapter.get(RedisPlatform.common,keyStr, clazz);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * 功能说明：根据key移除掉redis服务里的对象
	 * 
	 * @author liuli
	 * @creationDate 2015年4月1日 下午2:56:58
	 * @param prefixKey
	 *            对应redisKey的前缀
	 * @param suffixKey
	 *            对应redisKey的后缀
	 */
	public void removeByKey(RedisKeyEnum prefixKey, String suffixKey) {
		try {
			String keyStr = getRedisKey(prefixKey, suffixKey);
			redisAdapter.setex(RedisPlatform.common, keyStr, null, 1);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	// 获取redis对应的key
	private String getRedisKey(RedisKeyEnum prefixKey, String suffixKey) {
		return (prefixKey != null ? prefixKey.toString() : "") + "_"
				+ (StringUtils.isNotBlank(suffixKey) ? suffixKey : "");
	}

}
