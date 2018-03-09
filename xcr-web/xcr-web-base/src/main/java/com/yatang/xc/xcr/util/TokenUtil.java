package com.yatang.xc.xcr.util;

import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.RedisKeyEnum;

/**
 * token的工具类
 *
 * @author caotao
 */
@Component
public class TokenUtil {

    private static RedisService<JSONObject> redisService;

    public void setRedisService(RedisService<JSONObject> redisService) {
        TokenUtil.redisService = redisService;
    }

    private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    private static Integer INTERVAL = 43000;// token过期时间间隔 单位：分钟
    private static Boolean IGNORE_TOKEN_VOLIDATION = false;
  


    /**
     * 生成一个token
     * @param userId 登录账号，格式：jms000001、0000001、jenkins5@A000073
     * @param jmsCode加盟商编号格式：jms_000001
     * @return
     */
    public static String generateToken(String jmsCode,String userId, boolean isFranchiser, boolean isF5Account) {
        String tokenData = UUID.randomUUID().toString();
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("Token", tokenData);
        tokenJson.put("currentTime", System.currentTimeMillis());
        tokenJson.put("jmsCode",jmsCode);
        tokenJson.put("isFranchiser",isFranchiser);
        tokenJson.put("isF5Account",isF5Account);
        String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
        tokenJson.put("sessionId",sessionId);
        synchronized (redisService) {
            redisService.saveObject(RedisKeyEnum.Key, userId, INTERVAL, tokenJson);
            logger.info("\n" + userId + "生成的token为:" + tokenJson.toString());
        }
        return tokenData;
    }

    /**
     * removeToken删除token
     *
     * @param id
     * @return
     */
    public static boolean removeToken(String userId) {
        synchronized (redisService) {
            redisService.removeByKey(RedisKeyEnum.Key, userId);
            logger.info(redisService.getKeyForObject(RedisKeyEnum.Key, userId,JSONObject.class) == null ? "\n已注销" : "\n注销失败");
        }
        return true;
    }

    /**
     * 验证Token是否过期
     *
     * @param tokenData
     * @param userId
     * @return
     */
    public static String volidateToken(String userId, String tokenData) {
    	if(IGNORE_TOKEN_VOLIDATION){
            logger.info("忽略验证Token是否过期");
    		return "M00";
    	}
        logger.info("验证Token是否过期 start ... userId:" + userId + "  tokenData:" + tokenData);
        long start = System.currentTimeMillis();
        String flag = "M00";
        long redisGetStart = System.currentTimeMillis();
        JSONObject tokenJson = getTokenFromRedis(userId);
        long redisGetEnd = System.currentTimeMillis();
        logger.info("redisService.getKeyForObject(RedisKeyEnum.Key, userId) -> userId:" + userId + " ->  消耗时间为：" + (redisGetEnd - redisGetStart)+"ms");
        logger.info("redisService.getKeyForObject(RedisKeyEnum.Key, userId) -> userId:" + userId + " ->  方法执行到此消耗总时间为：" + (redisGetEnd - start)+"ms");
        if (tokenJson != null && tokenJson.get("Token").equals(tokenData)) {
            long tokenTime = Long.valueOf(String.valueOf(tokenJson.get("currentTime")));
            // 目前现在的时间减去最近一次的操作时间 （将时间差的单位：毫秒改为分钟）
            int interval = (int) ((System.currentTimeMillis() - tokenTime) / 1000 / 60);
            //判断时间是否大于token默认失效时间，如果大于，则失效
            if(tokenJson.getString("jmsCode")==null){
            	flag = "M01";
            }else{
            	if (interval >= INTERVAL) {
                    long removeStart = System.currentTimeMillis();
                    redisService.removeByKey(RedisKeyEnum.Key, userId);
                    long removeEnd = System.currentTimeMillis();
                    logger.info("redisService.removeByKey(RedisKeyEnum.Key, userId) -> userId:" + userId + " 消耗时间为：" + (removeEnd - removeStart)+"ms");
                    logger.info("redisService.removeByKey(RedisKeyEnum.Key, userId) -> userId:" + userId + " 方法执行到此消耗总时间为：" + (removeEnd - start)+"ms");
                    logger.info("用户： " + userId + "  已过期");
                    flag = "M01";
                } else {
                    // 更新用户的最近一次操作时间
                    tokenJson.put("currentTime", System.currentTimeMillis());
                    long saveStart = System.currentTimeMillis();
                    redisService.saveObject(RedisKeyEnum.Key, userId, INTERVAL, tokenJson);
                    long saveEnd = System.currentTimeMillis();
                    logger.info("redisService.saveObject(RedisKeyEnum.Key, userId, INTERVAL, tokenJson) ->消耗时间为:"+(saveEnd - saveStart)+"ms");
                    logger.info("redisService.saveObject(RedisKeyEnum.Key, userId, INTERVAL, tokenJson) ->方法执行到此消耗总时间为:"+(saveEnd - start)+"ms");
                    flag = "M00";
                }
            }
        } else if (tokenJson != null && !tokenJson.get("Token").equals(tokenData)) {
            // 用户状态有误（即被迫下线的状态）
            flag = "M05";
        } else {
            // 用户信息已过期
            flag = "M01";
        }
        long end = System.currentTimeMillis();
        logger.info("验证Token是否过期 end ... 总耗时：" + (end - start)+"ms");
        return flag;
    }
    /**
     * 获得用户token信息
     * @param userId 登录账号，格式：jms000001、0000001、jenkins5@A000073
     * @return
     */
    public static JSONObject getTokenFromRedis(String userId) {
    	return redisService.getKeyForObject(RedisKeyEnum.Key, userId,JSONObject.class);
    }
    /**
     * 保存信息到Redis中
     * @param key
     * @param time
     * @param json
     */
    public static void saveInfoToRedis(String key,Integer time,JSONObject json) {
    	redisService.saveObject(RedisKeyEnum.Key, key, time, json);
    }
    
}