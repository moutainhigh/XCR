package com.yatang.xc.xcr.biz.core.service;

import com.yatang.xc.xcr.biz.core.domain.XcrWhiteListPO;

import java.util.List;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月11日 11:39
 * @Summary :
 */
public interface IF5WhiteListService{


    /**<Summary>
     * @Function Description : 查询一条记录
     *  </Summary>
     */
    XcrWhiteListPO selectOne(Map<String, Object> param);

}