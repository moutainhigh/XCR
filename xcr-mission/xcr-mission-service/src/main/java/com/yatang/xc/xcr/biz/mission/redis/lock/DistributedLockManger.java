package com.yatang.xc.xcr.biz.mission.redis.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁管理器，支持对单个资源加锁解锁，或给一批资源的批量加锁及解锁
 * Created by wangyang on 2017/8/8.
 */
public class DistributedLockManger {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockManger.class);

    private static final int DEFAULT_SINGLE_EXPIRE_TIME = 3;

    @Autowired
    JedisCluster jedisCluster;

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
        long begin = System.nanoTime();
        do {
            //LOGGER.debug("{}尝试获得{}的锁.", value, key);
            Long i = lockManger.jedisCluster.setnx(key, value);
            if (i == 1) {
                lockManger.jedisCluster.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
                log.debug(value + "成功获取" + key + "的锁,设置锁过期时间为" + DEFAULT_SINGLE_EXPIRE_TIME + "秒");
                return true;
            } else {
                // 存在锁 ，但可能获取不到，原因是获取的一刹那间
//                String desc = lockManger.jc.get(key);
//                LOGGER.error("{}正被{}锁定.", key, desc);
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

//    public void test() throws InterruptedException {
//        String productId = "18061249844";
//        String userId;
//        for (int i = 1; i <= 500; i++) {
//            //随机产生一个用户
//            userId = UUID.randomUUID().toString();
//            //该用户试图锁定(如果被锁，尝试等待300毫秒)，在处理一些事情后，再释放锁
//            testLock(productId, userId);
//            Thread.sleep(20);
//        }
//    }

//    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(150, 150, 30L, TimeUnit.SECONDS,
//            new LinkedBlockingQueue<Runnable>(), new BasicThreadFactory.Builder().daemon(true)
//            .namingPattern("mongo-oper-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());

//    private void testLock(final String key, final String value) {
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //获取锁，如果不能立即获取，尝试等待1000毫秒
//                    boolean isLock = DistributedLockManger.tryLock(key, value, 500, TimeUnit.MILLISECONDS);
//                    if (isLock) {
//                        //doSomething(占用锁20毫秒到4秒，模拟处理事务)
//                        long doSomeThingTime = RandomUtils.nextLong(20, 4000);
//                        log.debug(value + "将持有锁" + key + "时长" + doSomeThingTime + "毫秒.");
//                        Thread.sleep(doSomeThingTime);
//                        //然后释放锁
//                        DistributedLockManger.unLock(key, value);
//                    }
//                } catch (Throwable th) {
//                }
//            }
//        });
//    }

}
