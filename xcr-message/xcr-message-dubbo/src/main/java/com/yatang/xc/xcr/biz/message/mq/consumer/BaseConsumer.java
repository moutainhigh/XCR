package com.yatang.xc.xcr.biz.message.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.o2o.dto.RegistrationParameterDto;
import com.yatang.xc.xcr.biz.message.dto.MqPushDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyang on 2017/11/29.
 */
@Component
public class BaseConsumer {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private OrgnazitionO2ODubboService orgnazitionO2ODubboService;
    @Autowired
    private OrganizationService organizationDubboService;


    /**
     * 门店集合获取推送地址集合
     *
     * @param shopCodeList
     * @return
     */
    protected List<String> getRegesterIdByShopCodeList(List<String> shopCodeList) {
        if (CollectionUtils.isEmpty(shopCodeList)) {
            return null;
        }
        List<String> regesterIdList = new ArrayList<>();
        List<String> businessIds = new ArrayList<>();
        for (String shopCode : shopCodeList) {
            String allianceBusinessId = getAllianceBusinessIdByShopCode(shopCode);
            if (StringUtils.isEmpty(allianceBusinessId)) {
                logger.error("该门店没有获取到加盟商信息:shopCode:" + shopCode);
                continue;
            }
            businessIds.add(allianceBusinessId);
            logger.info("【获取加盟商编号成功】shopCode:" + shopCode + "   allianceBusinessId:" + allianceBusinessId);
        }
        Response<List<RegistrationParameterDto>> res = orgnazitionO2ODubboService
                .queryRegistrationId(businessIds);
        if (res != null && res.isSuccess()) {
            List<RegistrationParameterDto> RegistrationParameterDtoList = res.getResultObject();
            if (CollectionUtils.isEmpty(RegistrationParameterDtoList)) {
                return null;
            }
            for (RegistrationParameterDto dto : RegistrationParameterDtoList) {
                String regesterId = dto.getRegistrationId();
                regesterIdList.add(regesterId);
                logger.info("【加盟商编号获取推送地址成功】" + JSONObject.toJSONString(dto));
            }
        }
        return regesterIdList;
    }

    /**
     * 根据门店编号获取推送唯一ID
     *
     * @param shopCode
     * @return
     */
    protected List<String> getRegesterIdByShopCode(String shopCode) {
        if (StringUtils.isEmpty(shopCode)) {
            logger.error("获取门店编号异常 -> shopCode:" + shopCode);
            return null;
        }
        String allianceBusinessId = getAllianceBusinessIdByShopCode(shopCode);
        if (StringUtils.isEmpty(allianceBusinessId)) {
            logger.info("没有获取到加盟商编号 -> shopCode:" + shopCode);
            return null;
        }
        List<String> shopCodeList = new ArrayList<>();
        shopCodeList.add(allianceBusinessId);
        logger.info("orgnazitionO2ODubboService start ...");
        Response<List<RegistrationParameterDto>> res = orgnazitionO2ODubboService
                .queryRegistrationId(shopCodeList);
        logger.info("orgnazitionO2ODubboService.queryRegistrationId(shopCodeList) result = "
                + res.isSuccess());
        if (!res.isSuccess()) {
            logger.info("没有获取到regesterId !!! shopCode:" + shopCode);
            return null;
        }
        List<RegistrationParameterDto> RegistrationParameterDtoList = res.getResultObject();
        if (CollectionUtils.isEmpty(RegistrationParameterDtoList)) {
            logger.error("res.getResultObject() == null or size == 0 !!! shopCode:" + shopCode);
            return null;
        }
        List<String> regesterIdList = new ArrayList<>();
        for (RegistrationParameterDto dto : RegistrationParameterDtoList) {
            String regesterId = dto.getRegistrationId();
            regesterIdList.add(regesterId);
            logger.info("推送地址 regesterId:" + regesterId);
            logger.info("被推送商家详细信息:" + JSONObject.toJSONString(dto));
        }
        return regesterIdList;
    }

    /**
     * 根据门店编号获取加盟商ID
     *
     * @param shopCode
     * @return
     */
    protected String getAllianceBusinessIdByShopCode(String shopCode) {
        if (StringUtils.isEmpty(shopCode)) {
            logger.info("没有获取到门店编号 !!!");
            return null;
        }
        long start = System.currentTimeMillis();
        Response<StoreDto> convenientStoreDTOResult = organizationDubboService.queryStoreById(shopCode);
        long end = System.currentTimeMillis();
        logger.info("organizationDubboService.getConvenientStoreById(shopCode) 结束调用 ....花费时间为：" + shopCode + "-"
                + (end - start));
        if (convenientStoreDTOResult == null || !convenientStoreDTOResult.isSuccess()) {
            logger.error("organizationDubboService.getConvenientStoreById(shopCode) 调用失败 shopCode：" + shopCode + "  convenientStoreDTOResult:" + JSONObject.toJSONString(convenientStoreDTOResult));
            return null;
        }
        StoreDto storeDto = convenientStoreDTOResult.getResultObject();
        if (storeDto == null) {
            logger.info("convenientStoreDTOResult.getResultObject() -> storeDto == null!!!shopCode:" + shopCode);
            return null;
        }
        return storeDto.getFranchiseeId();
    }

}
