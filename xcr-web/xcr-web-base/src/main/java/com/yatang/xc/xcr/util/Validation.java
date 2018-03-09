package com.yatang.xc.xcr.util;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.ErrorMessageList;
import com.yatang.xc.xcr.enums.RedisKeyEnum;
import com.yatang.xc.xcr.exceptions.ErrorMessageResponseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 13:25
 * @Summary :
 */
public class Validation {

   /**Logger*/
    private static final Logger logger = LoggerFactory.getLogger(Validation.class);
    /**Token过期时间间隔 单位:分钟*/
    private static final int TOKEN_INTERVAL = 43000;
    /**成功*/
    public static final String TOKEN_SUCCESSFULLY="M00";
    /**Token信息失效*/
    public static final String TOKEN_INVALID="M01";
    /**登录状态异常*/
    public static final String TOKEN_STATE_ABNORMALITY="M05";
    /**Token key*/
    public static final String TOKEN_KEY="Token";
    private static Boolean IGNORE_TOKEN_VOLIDATION = false;

    /**
     * 验证Token是否过期
     * @param token
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
	public static String token(String userId, String token) {
    	if(IGNORE_TOKEN_VOLIDATION){
            logger.info("忽略验证Token是否过期");
    		return TOKEN_SUCCESSFULLY;
    	}
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(token)){
             throw new ErrorMessageResponseException(ErrorMessageList.VALIDATION);
        }

        RedisService<JSONObject> redisService;
        try {
            redisService = SpringContext.getBean(RedisService.class);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new ErrorMessageResponseException(ErrorMessageList.SERVER);
        }

        final JSONObject tokenJson = redisService.getKeyForObject(RedisKeyEnum.Key, userId,JSONObject.class);
        if  (tokenJson != null){
          if(token.equals(tokenJson.get(TOKEN_KEY))){
              long tokenTime = Long.valueOf(String.valueOf(tokenJson.get("currentTime")));
              
              /**目前现在的时间减去最近一次的操作时间 （将时间差的单位：毫秒改为分钟）*/
              int interval = (int) ((System.currentTimeMillis() - tokenTime) / 1000 / 60);
              
              /**判断时间是否大于token默认失效时间，如果大于，则失效*/
              if (interval >= TOKEN_INTERVAL) {
                  redisService.removeByKey(RedisKeyEnum.Key, userId);
                  return  TOKEN_INVALID;
              } else {
                  /**更新用户的最近一次操作时间*/
                  tokenJson.put("currentTime", System.currentTimeMillis());
                  redisService.saveObject(RedisKeyEnum.Key, userId, TOKEN_INTERVAL, tokenJson);
                  return  TOKEN_SUCCESSFULLY;
              }
          }

            /** 用户状态有误（即被迫下线的状态）*/
          if(  ! token.equals(tokenJson.get(TOKEN_KEY)) ){
              return  TOKEN_STATE_ABNORMALITY;
          }
            /**用户信息已过期*/
        }else{
            return  TOKEN_INVALID;
        }
        return TOKEN_SUCCESSFULLY;
    }


}
