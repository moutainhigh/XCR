package com.yatang.xc.xcr.biz.core.redis.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁管理器，支持对单个资源加锁解锁，或给一批资源的批量加锁及解锁
 * Created by wangyang on 2017/11/17.
 */
@Component
public class DistributedLockManger {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockManger.class);

    private static final int DEFAULT_SINGLE_EXPIRE_TIME = 3;

    @Autowired
    private JedisCluster jedisCluster;

    private static DistributedLockManger lockManger;

    public DistributedLockManger() {
    }

    @PostConstruct
    public void init() {
        lockManger = this;
        lockManger.jedisCluster = this.jedisCluster;
    }

    /**
     * 获取锁 如果锁可用   立即返回true，  否则立即返回false，作为非阻塞式锁使用
     *
     * @param key
     * @param value
     * @return
     */
    public boolean tryLock(String key, String value) {
        try {
            return tryLock(key, value, 0L, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false，作为阻塞式锁使用
     *
     * @param key                            锁键
     * @param value                          被谁锁定
     * @param timeout                        尝试获取锁时长，建议传递500,结合实践单位，则可表示500毫秒
     * @param unit，建议传递TimeUnit.MILLISECONDS
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String key, String value, long timeout, TimeUnit unit) throws InterruptedException {
        //纳秒
        log.info(jedisCluster.getClusterNodes().toString());
        long begin = System.nanoTime();
        do {
            //LOGGER.debug("{}尝试获得{}的锁.", value, key);
            Long i = lockManger.jedisCluster.setnx(key, value);
            if (i == 1) {
                lockManger.jedisCluster.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
                log.debug(value + "成功获取" + key + "的锁,设置锁过期时间为" + DEFAULT_SINGLE_EXPIRE_TIME + "秒");
                return true;
            }
            if (timeout == 0) {
                break;
            }
            //在其睡眠的期间，锁可能被解，也可能又被他人占用，但会尝试继续获取锁直到指定的时间
            Thread.sleep(100);
        }
        while ((System.nanoTime() - begin) < unit.toNanos(timeout));
        //因超时没有获得锁
        return false;
    }

    /**
     * 释放单个锁
     *
     * @param key   锁键
     * @param value 被谁释放
     */
    public void unLock(String key, String value) {
        try {
            lockManger.jedisCluster.del(key);
            log.debug("{}锁被{}释放 .", key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JedisCluster getJc() {
        return jedisCluster;
    }

    public void setJc(JedisCluster jc) {
        this.jedisCluster = jc;
    }

}
