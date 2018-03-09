package com.yatang.xc.xcr.biz.message.redis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cache.Cache;
import org.springframework.stereotype.Component;

import com.busi.common.utils.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

@Component
public class MybatisRedisCache implements Cache {

	protected final Log			logger			= LogFactory.getLog(this.getClass());

	private static final String	KEY_PREFIX		= "xcr_mission:";

	/** The ReadWriteLock. */
	private final ReadWriteLock	readWriteLock	= new ReentrantReadWriteLock();

	private String				id;

	private static Pool<Jedis>	jedisPool;
	private static JedisCluster	jedisCluster;
	private static int			expireTime		= -1;
	private static boolean		isCluster		= true;



	public MybatisRedisCache() {
	}



	public static void setCluster(boolean isCluster) {
		MybatisRedisCache.isCluster = isCluster;
	}



	public static void setJedisCluster(JedisCluster jedisCluster) {
		MybatisRedisCache.jedisCluster = jedisCluster;
	}



	public static void setJedisPool(Pool<Jedis> jedisPool) {
		MybatisRedisCache.jedisPool = jedisPool;
	}



	public static void setExpireTime(Integer expireTime) {
		MybatisRedisCache.expireTime = expireTime;
	}



	public MybatisRedisCache(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		this.id = id;
	}



	@Override
	public String getId() {
		return this.id;
	}



	@Override
	public void putObject(Object key, Object value) {
		key = KEY_PREFIX + key;
		logger.debug("putObject : " + key + " value:" + value);
		Jedis jedis = null;
		try {
			if (isCluster) {
				if (expireTime > 0) {
					jedisCluster.setex(SerializeUtil.serialize(key), expireTime, SerializeUtil.serialize(value));
				} else {
					jedisCluster.set(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
				}
			} else {
				jedis = jedisPool.getResource();

				if (expireTime > 0) {
					jedis.setex(SerializeUtil.serialize(key.toString()), expireTime, SerializeUtil.serialize(value));
				} else {
					jedis.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
				}

			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}



	@Override
	public Object getObject(Object key) {
		key = KEY_PREFIX + key;
		logger.debug("getObject : " + key);
		Object value = null;
		Jedis jedis = null;
		try {
			if (isCluster) {
				byte[] result = jedisCluster.get(SerializeUtil.serialize(key));
				if (result == null) {
					return value;
				}
				value = SerializeUtil.unserialize(result);
			} else {
				jedis = jedisPool.getResource();

				byte[] result = jedis.get(SerializeUtil.serialize(key.toString()));
				if (result == null) {
					return value;
				}
				value = SerializeUtil.unserialize(result);
			}
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



	@Override
	public Object removeObject(Object key) {
		key = KEY_PREFIX + key;
		logger.debug("removeObject : " + key);
		Jedis jedis = null;
		try {
			if (isCluster) {
				return jedisCluster.expire(SerializeUtil.serialize(key), 0);
			} else {
				jedis = jedisPool.getResource();

				return jedis.expire(SerializeUtil.serialize(key.toString()), 0);

			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}



	@Override
	public void clear() {
		logger.debug("clear id: " + id);
		try {
			if (isCluster) {
				Set<byte[]> keys = new HashSet<byte[]>();

				for (String k : jedisCluster.getClusterNodes().keySet()) {
					JedisPool pool = jedisCluster.getClusterNodes().get(k);
					if (pool != null && !pool.isClosed()) {
						Jedis jedis = pool.getResource();
						if (jedis != null && jedis.isConnected()) {
							try {
								Set<byte[]> bytes = jedis.keys(("*" + id + "*").getBytes());
								for (byte[] item : bytes) {
									logger.debug("clear item: " +item);
								}
								keys.addAll(bytes);
							} catch (Exception e) {
								logger.error(ExceptionUtils.getFullStackTrace(e));
							} finally {
								jedis.close();// 用完一定要close这个链接！！！
							}
						}
					}
				}
				for (byte[] key : keys) {
					logger.debug("clear key: " + key);
					jedisCluster.del(key);
				}
			} else {
				del(jedisPool.getResource(), id);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
	}



	private void del(Jedis jedis, String id) {
		try {
			Set<byte[]> currentMapperKeys = jedis.keys(("*" + id + "*").getBytes());
			if (currentMapperKeys.size() > 0) {
				byte[][] padingRemove = new byte[currentMapperKeys.size()][];
				Iterator<byte[]> iterator = currentMapperKeys.iterator();
				for (int i = 0; i < currentMapperKeys.size(); i++) {
					padingRemove[i] = iterator.next();
				}
				jedis.del(padingRemove);
			}
			// jedis.flushDB();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			jedis.close();
		}
	}



	@Override
	public int getSize() {
		int count = 0;
		if (isCluster) {
			for (JedisPool pool : jedisCluster.getClusterNodes().values()) {
				Jedis jedis = pool.getResource();
				try {
					if (jedis != null) {
						count += jedis.dbSize().intValue();
					}
				} catch (Exception e) {
					logger.error(ExceptionUtils.getFullStackTrace(e));
				} finally {
					jedis.close();
				}
			}
		} else {
			Jedis jedis = jedisPool.getResource();
			try {
				count = Integer.valueOf(jedis.dbSize().toString());
			} catch (Exception e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
			} finally {
				jedis.close();
			}
		}
		return count;
	}



	@Override
	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}



	public void showInfo() {
		Map<String, JedisPool> map = jedisCluster.getClusterNodes();
		for (JedisPool pool : map.values()) {
			Jedis jedis = null;
			try {
				logger.info("pool :" + pool);
				jedis = pool.getResource();
				logger.info("jedis :" + jedis.info());
			} catch (Exception e) {
				logger.info(ExceptionUtils.getFullStackTrace(e));
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
			logger.info("count :" + map.size());
		}
	}

}
