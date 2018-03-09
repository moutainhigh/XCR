package com.yatang.xc.xcr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.dto.inputs.LogisticsInfoListDto;
import com.yatang.xc.xcr.dto.outputs.LogisticsInfoListResultSetDto;
import com.yatang.xc.xcr.enums.ErrorMessageList;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IExpressQueryService;
import com.yatang.xc.xcr.util.Assert;
import com.yatang.xcsm.common.response.Response;
import com.yatang.xcsm.remote.api.dto.OutLogisticsDTO;
import com.yatang.xcsm.remote.api.dubboxservice.PushOrderDubboxService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月29日 14:39
 * @Summary :
 */
@Service
public class ExpressQueryServiceImpl implements IExpressQueryService {

    private @Autowired
    PushOrderDubboxService pushOrderDubboxService;


    private final static String EX_NAME_SUFFIX  = "快递";
    private final Logger logger = LoggerFactory.getLogger(ExpressQueryServiceImpl.class);

    /**
     * @param : [model]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary :
     * @since ： 2.7
     */
    @Override
    public ResultMap logisticsInfoList(LogisticsInfoListDto model) {
        if (model  == null){
            throw new IllegalArgumentException("参数为空 ==> "  + model.getClass().getSimpleName());
        }

        Response<List<OutLogisticsDTO>> response = pushOrderDubboxService.queryKuaidiInfo(model.getOrderNo());
        if (response == null || response.getResultObject() == null || !response.isSuccess()) {
            logger.error("The remote method PushOrderDubboxService.queryKuaidiInfo execute fail, prams ==> {}", model.getOrderNo());
            logger.error("The remote method PushOrderDubboxService.queryKuaidiInfo execute fail, result ==>{} ", JSON.toJSONString(response));
            if (response != null && !StringUtils.isBlank(response.getErrorMessage())) {
                return ResultMap.failll(response.getErrorMessage());
            }
            return ResultMap.faill(ErrorMessageList.SERVER);
        }

        List<OutLogisticsDTO> records = response.getResultObject();
        if (records == null || records.size() == 0) {
            logger.info("The remote method PushOrderDubboxService.queryKuaidiInfo execute success, prams ==>{} ", JSON.toJSONString(model.getStoreSerialNo()));
            logger.info("The remote method PushOrderDubboxService.queryKuaidiInfo execute success, result ==> {}", JSON.toJSONString(response));
            return ResultMap.successu().listData();
        }

        List<LogisticsInfoListResultSetDto> array = new ArrayList<>(records.size());
        for (OutLogisticsDTO record : records) {
            LogisticsInfoListResultSetDto dto  = new LogisticsInfoListResultSetDto();

            String companyName = record.getCompanyName();
            if (StringUtils.isNotBlank(companyName) && ! companyName.contains(EX_NAME_SUFFIX)){
                companyName = Assert.append(companyName, EX_NAME_SUFFIX);
            }
            dto.setLogisticsName(companyName);
            dto.setLogisticsNo(record.getLogiticsNo());
            List<JSONObject> logistics = record.getAllLogistics();
            List<LogisticsInfoListResultSetDto.LogisticsSec> logisticsSec = new ArrayList<>( logistics.size());
            for (JSONObject logistic : logistics) {
                LogisticsInfoListResultSetDto.LogisticsSec lac = new LogisticsInfoListResultSetDto.LogisticsSec();
                lac.setLogisticsAddress( logistic.getString("context"));
                String time1 = logistic.getString("time");
                if (StringUtils.isNotBlank(time1)){
                    long time = Assert.formatDateToDateStamp(time1);
                    lac.setLogisticsTime(time);
                } else{
                    lac.setLogisticsTime(System.currentTimeMillis());
                }

                logisticsSec.add(lac);
            }
            dto.setLogisticsSecList(logisticsSec);
            array.add(dto);
        }

        //  int totalcount = records.size();
        // (Integer pageNum, Integer pageSize, Integer totalpage, Long totalcount)
        return ResultMap.successu().setListdata(array).page();//page(1,totalcount, 1, (long)totalcount);
    }
}

/**
测试集：
 List<LogisticsInfoListResultSetDto> array = new ArrayList<>();
 for (int i = 0; i< 20; i++) {
 LogisticsInfoListResultSetDto dto  = new LogisticsInfoListResultSetDto();
 dto.setLogisticsName("顺丰"+1);
 dto.setLogisticsNo(java.util.UUID.randomUUID().toString().split("-")[0]+i);

 List<LogisticsInfoListResultSetDto.LogisticsSec> logisticsSec = new ArrayList<>();
 for (int k = 0; k< 20; k++) {
 LogisticsInfoListResultSetDto.LogisticsSec lac = new LogisticsInfoListResultSetDto.LogisticsSec();
 lac.setLogisticsAddress("成都市"+i);
 lac.setLogisticsTime(System.currentTimeMillis()+"");
 logisticsSec.add(lac);
 }
 dto.setLogisticsSecList(logisticsSec);
 array.add(dto);
 }

 return ResultMap.successu().setListdata(array).page();
 */


