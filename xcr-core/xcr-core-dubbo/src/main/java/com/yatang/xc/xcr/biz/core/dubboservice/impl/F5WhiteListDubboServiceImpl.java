package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.domain.XcrWhiteListPO;
import com.yatang.xc.xcr.biz.core.dubboservice.F5WhiteListDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.Assert;
import com.yatang.xc.xcr.biz.core.service.IF5WhiteListService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月11日 11:41
 * @Summary : 登录白名单
 */
@Service("f5WhiteListDubboService")
public class F5WhiteListDubboServiceImpl implements F5WhiteListDubboService {

    private @Autowired
    IF5WhiteListService xcrWhiteListService;

    @Override
    public Response<Boolean> isExistence(String userAccount) {
        if (!StringUtils.isBlank(userAccount)) {

            /** 当用户输入纯数字时默认添加“jms”前缀*/
            if (Assert.isNumeric(userAccount)) {
                userAccount = "jms" + userAccount;
            }

            Map<String, Object> map = new HashMap<>(1);
            map.put("userAccount", userAccount);

            XcrWhiteListPO one = xcrWhiteListService.selectOne(map);
            if (one != null && !StringUtils.isBlank(one.getUserAccount())) {
                return new Response<Boolean>(Boolean.TRUE, Boolean.TRUE);
            } else {
                return new Response<Boolean>(Boolean.TRUE, Boolean.FALSE);
            }
        }

        Response<Boolean> booleanResponse = new Response<>(Boolean.FALSE, Boolean.FALSE);
        booleanResponse.setErrorMessage("UserAccount is empty");
        return booleanResponse;
    }


}
