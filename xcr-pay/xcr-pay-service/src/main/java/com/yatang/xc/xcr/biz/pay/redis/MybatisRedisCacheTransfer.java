package com.yatang.xc.xcr.biz.pay.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.Pool;

public class MybatisRedisCacheTransfer {

    public void setJedisPool(Pool<Jedis> jedisPool) {
        MybatisRedisCache.setJedisPool(jedisPool);
    }

    public void setExpireTime(Integer expireTime) {
        MybatisRedisCache.setExpireTime(expireTime);
    }
    
    public void setJedisCluster(JedisCluster jedisCluster) {
    	MybatisRedisCache.setJedisCluster(jedisCluster);

    }
    
	public void setCluster(boolean isCluster) {
		MybatisRedisCache.setCluster(isCluster);
	}

}
