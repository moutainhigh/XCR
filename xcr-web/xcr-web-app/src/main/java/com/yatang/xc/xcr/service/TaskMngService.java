package com.yatang.xc.xcr.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 任务webService服务
 * Created by wangyang on 2017/7/14.
 */
public interface TaskMngService {

    /**
     * 任务完成操作
     * @param merchantId 门店编号
     * @param taskId 任务执行ID
     * @return
     */
    JSONObject missionReceiveReward(String merchantId, String taskId);




}
