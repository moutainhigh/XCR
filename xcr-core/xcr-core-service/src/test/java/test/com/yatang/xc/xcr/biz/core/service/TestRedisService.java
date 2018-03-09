/**
 * 
 */
package test.com.yatang.xc.xcr.biz.core.service;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.biz.core.service.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterInfoCache;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.JedisClusterCRC16;

/**
 * @author gaodawei
 * @Date 2017年7月25日 下午2:04:10
 * @version 1.0.0
 * @function
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
//@ActiveProfiles(value="dev")
public class TestRedisService {

	protected final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "jedisCluster")
	private JedisCluster jedisCluster;
	@Resource(name = "jedisPoolConfig")
	private JedisPoolConfig poolConfig;
	
	@Autowired
	private RedisService redisService;
	
	@Test
	public void testGetJedisFromClusterByKey() {
		JedisClusterInfoCache cache = new JedisClusterInfoCache(poolConfig, 2000, 2000, null);
		//getJedisFromClusterByKey("XXX" ,cache);
		redisService.getJedisFromClusterByKey("xxx");
		
		
	}
	@Test
	public void testGetClusterNodes() {

		JedisClusterInfoCache cache = new JedisClusterInfoCache(poolConfig, 2000, 2000, null);
		Map<String, JedisPool> map = jedisCluster.getClusterNodes();
		for(int i=0 ;i<1000;i++){
			String key = Math.floor(Math.random()*100)+"xxxa"+Math.floor(Math.random()*100);
			int slot = JedisClusterCRC16.getSlot(key);
			log.info("slot : " + slot);
			jedisCluster.setex(key,60, "xxx"+Math.floor(Math.random()*100));
			boolean hasCache = jedisCluster.exists(key);
			log.info("result : " + JSONObject.toJSONString(map.keySet()) + " hasCache:" + hasCache);
			long time = System.currentTimeMillis();
			cache.renewClusterSlots(jedisCluster.getClusterNodes().values().iterator().next().getResource());
			log.info("time : " + (System.currentTimeMillis() - time));
			JedisPool pool = cache.getSlotPool(slot);
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				boolean flag = jedis.exists(key);
				log.info("key : " + key + " hasCache:" + flag + " db:"+jedis.getDB());
				String value = jedis.get(key);
				log.info("slot : " + slot + " value:" + value);
			} catch (Exception e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}	
		}
	}
	
	
	public Jedis getJedisFromClusterByKey(String key,JedisClusterInfoCache cache) {
		int slot = JedisClusterCRC16.getSlot(key);
		log.debug("slot : " + slot);
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
