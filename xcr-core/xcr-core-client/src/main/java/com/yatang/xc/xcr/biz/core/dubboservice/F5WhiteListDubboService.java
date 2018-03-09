package com.yatang.xc.xcr.biz.core.dubboservice;

import com.busi.common.resp.Response;

/**
 * @since  2.6.0
 * @Author : BobLee
 * @CreateTime : 2017年12月11日 11:33
 * @Summary :  登录白名单
 */
public interface F5WhiteListDubboService {

    /**
     * @since ： 2.6.0
     * @param : [userAccount]
     * @return ：  com.busi.common.resp.Response<java.lang.Boolean>
     * @Summary :  查询该账户是否在白名单内
     */
    Response<Boolean> isExistence(String userAccount);

}
