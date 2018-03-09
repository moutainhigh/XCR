package com.yatang.xc.xcr.biz.mission.schedule;

import com.busi.common.resp.Response;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 定时创建每日任务
 * Created by wangyang on 2017/12/29.
 */
@Component
@JobHander(value = "autoCreateMissionExecuteDayJobHandler")
public class AutoCreateMissionExecuteDayJobHandler extends IJobHandler {

    @Autowired
    private MissionExecuteFlowService missionExecuteFlowService;
    @Autowired
    private MissionService missionService;
    private static final int pageSize = 1000;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        XxlJobLogger.log("AutoCreateMissionExecuteDayJobHandler  execute start ..."+ DateUtil.formatDateDefaule(new Date()));
        try {
            Set<String> merchantIds = new HashSet<>(missionService.queryMerchantIdFromUserHistory());
            if (!CollectionUtils.isEmpty(merchantIds)) {
                XxlJobLogger.log("AutoCreateMissionExecuteDayJobHandler  execute 没有需要创建每日任务的门店");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("pageCount", "true");
            Response<Integer> result = missionExecuteFlowService.getOrganizationService().queryStoreCount(map);
            //得到门店数量
            Integer count = result.getResultObject();
            if (count != null && count > 0) {
                int pageCount = (int) (count / pageSize);
                for (int i = 0; i <= pageCount; i++) {
                    //查询门店信息列表
                    List<StoreDto> list = missionExecuteFlowService.getOrganizationService().queryPageStores(i + 1, pageSize, map).getResultObject();
                    if (list != null && !list.isEmpty()) {
                        //遍历门店，给每个门店创建任务
                        for (StoreDto dto : list) {
                            if (merchantIds.contains(dto.getId())) {
                                missionExecuteFlowService.createDayMissionExecute(dto.getId());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            XxlJobLogger.log("AutoCreateMissionExecuteDayJobHandler  execute 创建异常 ");
        }
        XxlJobLogger.log("AutoCreateMissionExecuteDayJobHandler  execute end ..." + DateUtil.formatDateDefaule(new Date()));
        return ReturnT.SUCCESS;
    }

}
