package com.yatang.xc.xcr.biz.mission.service.imp;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatang.xc.xcr.biz.mission.service.RedisService;

import redis.clients.jedis.JedisCluster;

/**
 * 
 * Redis的service类 负责与redis的交互操作
 * 
 * @author : echo
 * @date : 2017年4月26日 下午4:34:17
 * @version : 2017年4月26日
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

	private final Logger	log	= LoggerFactory.getLogger(getClass());

	@Autowired
	private JedisCluster	jedisCluster;



	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}



	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}



	@Override
	public String getItem(String key) {
		try {
			return jedisCluster.get(key);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return null;
	}



	@Override
	public void saveItem(String key, Integer time, String value) {
		try {
			if (time != null && time > 0) {
				jedisCluster.setex(key, time, value);
			} else {
				jedisCluster.set(key, value);
			}
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

}
