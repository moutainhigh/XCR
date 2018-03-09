package com.yatang.xc.xcr.biz.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busi.common.utils.SerializeUtil;
import com.yatang.xc.xcr.biz.core.dto.RateLimit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterInfoCache;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.util.JedisClusterCRC16;

/**
 * 
 * Redis的service类 负责与redis的交互操作
 * 
 * @author : echo
 * @date : 2017年9月16日 下午4:34:17
 * @version : 2017年9月16日
 */
@Service("RedisService")
public class RedisService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "jedisPoolConfig")
	private JedisPoolConfig poolConfig;
	@Resource(name = "jedisCluster")
	private JedisCluster jedisCluster;

	private static final String KEY_PREFIX = "xcr_core:";

	private int expireTime = -1;

	private Map<String, Date> overLimitTimeMap = new ConcurrentHashMap<String, Date>();

	private JedisClusterInfoCache cache = null;

	@PostConstruct
	public JedisClusterInfoCache initCache() {
		if (cache == null) {
			cache = new JedisClusterInfoCache(poolConfig, 2000, 2000, null);
		}
		return cache;
	}

	public void putObject(Object key, Object value) {
		key = KEY_PREFIX + key;
		logger.debug("putObject : " + key + " value:" + value);
		Jedis jedis = null;
		try {
			if (expireTime > 0) {
				jedisCluster.setex(SerializeUtil.serialize(key), expireTime, SerializeUtil.serialize(value));
			} else {
				jedisCluster.set(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Object getObject(Object key) {
		key = KEY_PREFIX + key;
		logger.debug("getObject : " + key);
		Object value = null;
		Jedis jedis = null;
		try {
			byte[] result = jedisCluster.get(SerializeUtil.serialize(key));
			if (result == null) {
				return value;
			}
			value = SerializeUtil.unserialize(result);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		logger.debug("getObject return : " + value);
		return value;
	}

	public Object removeObject(Object key) {
		key = KEY_PREFIX + key;
		logger.debug("removeObject : " + key);
		Jedis jedis = null;
		try {
			return jedisCluster.expire(SerializeUtil.serialize(key), 0);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/**
	 * 
	 * @Description：检查是否满足限速条件 且累加计数器:满足限速条件返回-1，不满足限速条件返回计数器剩余时间（毫秒）
	 * @param limit
	 * @return: 返回结果描述
	 * @return boolean: 返回值类型
	 * 
	 */
	public synchronized long checkSpeedBylimiter(RateLimit limit) {
		if (limit == null) {
			return -1;
		}
		String key = "speedLimiter_" + limit.getType();
		Jedis jedis = null;

		try {
			Date now = new Date();
			if (overLimitTimeMap.get(key) != null && now.before(overLimitTimeMap.get(key))) {
				return overLimitTimeMap.get(key).getTime() - now.getTime();
			}
			jedis = getJedisFromClusterByKey(key);
			if (jedis == null) {
				logger.error("can not find jedis for :" + key);
				return -1;
			}
			jedis.watch(key);
			long remainderTime = jedis.pttl(key);
			Transaction t = jedis.multi();
			if (remainderTime < 50) {
				t.setex(key, limit.getUnitTime(), "1");
				List<Object> exec = t.exec();
				if (exec != null) {
					return -100;
				} else {
					return 1;
				}
			}
			Response<Long> currentCountResponse = t.incr(key);
			List<Object> exec = t.exec();
			if (exec == null) {
				return 1;
			}
			long currentCount = currentCountResponse.get();
			if (currentCount > limit.getCountLimit()) {
				overLimitTimeMap.put(key, new Date(new Date().getTime() + remainderTime));
				return remainderTime;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return -1;
	}

	public Jedis getJedisFromClusterByKey(String key) {
		int slot = JedisClusterCRC16.getSlot(key);
		logger.debug("slot : " + slot);
		if (cache == null) {
			cache = initCache();
		}
		JedisPool pool = cache.getSlotPool(slot);
		if (pool == null) {
			Map<String, JedisPool> map = jedisCluster.getClusterNodes();
			if (!map.isEmpty()) {
				Jedis firstJedis = map.values().iterator().next().getResource();
				cache.renewClusterSlots(firstJedis);
			}
			pool = cache.getSlotPool(slot);
		}
		if (pool != null) {
			Jedis jedis = pool.getResource();
			return jedis;
		}
		return null;
	}

}
