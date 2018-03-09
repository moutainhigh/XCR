package com.yatang.xc.xcr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.o2o.dto.StoreO2ODto;
import com.yatang.xc.xcr.dto.inputs.*;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IShopSettingsInformationService;
import com.yatang.xc.xcr.util.Assert;
import com.yatang.xcsm.remote.api.dto.ShopNoticeDTO;
import com.yatang.xcsm.remote.api.dto.ShopPictureDTO;
import com.yatang.xcsm.remote.api.dubboxservice.PushShopNoticeDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月29日 上午11:48:55
 * @Summary :
 */
@Service
public class ShopSettingsInformationServiceImpl implements IShopSettingsInformationService {

    @Autowired
    private OrgnazitionO2ODubboService o2oDubboService;
    @Autowired
    private PushShopNoticeDubboService pushShopNoticeDubboService;
    @Autowired
    private OrganizationService organizationService;

    /**
     * TYPE  1为店铺公告
     */
    private static final String TYPE_NOTICE = "1";
    /**
     * TYPE 2为店铺介绍
     */
    private static final String TYPE_INTRODUCTION = "2";
    /**
     * 雅堂： 显示 1
     */
    private final static String DISPLAY_YT = "1";
    /**
     * C端： 不显示
     */
    private final static String HIDDEN = "2";
    /**
     * 店铺介绍 图片数量
     */
    private static final Integer INTRODUCE_RELEASE_PIC_NEED = 4;
    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(ShopSettingsInformationServiceImpl.class);

    /**
     * 更新 经纬度
     *
     * @see #updateShopInfo(StoreO2ODto) 更新店铺信息#经纬度
     */
    @Override
    public ResultMap setShopAddress(SetShopAddressDto model) {
        Assert.isNotEmpty(model);
        StoreO2ODto storeDto = new StoreO2ODto();
        storeDto.setId(model.getStoreSerialNo());
        /**店铺编号*/
        storeDto.setCode(model.getStoreSerialNo());
        /**经度*/
        storeDto.setLongitude(new BigDecimal(model.getLongitude()));
        /** 纬度*/
        storeDto.setLatitude(new BigDecimal(model.getLatitude()));
        /**定位地址*/
        storeDto.setLocationAddress(model.getLocationAddress());

        ResultMap resultMap = updateShopInfo(storeDto);
        if (resultMap != null) {
            return resultMap;
        }
        return ResultMap.successu();
    }

    /**
     * 更新 起送范围 配送价格 配送公里
     *
     * @see #updateShopInfo(StoreO2ODto) 更新店铺信息#配送
     */
    @Override
    public ResultMap setShopInfo(SetShopInfoDto model) {
        Assert.isNotEmpty(model);
        StoreO2ODto storeDto = new StoreO2ODto();
        /**门店编号*/
        storeDto.setId(model.getStoreSerialNo());
        /**店铺编号*/
        storeDto.setCode(model.getStoreSerialNo());
        /**满多少元免配送费*/
        storeDto.setDistributionFreeMoney(Assert.convert(model.getFreeDeliveryFee()));
        /** 配送范围*/
        storeDto.setDistributionScope(model.getDeliveryScope());
        /** 配送费*/
        storeDto.setDistributionTip(Assert.convert(model.getDeliveryFee()));
        /**起送费*/
        storeDto.setDistributionAmount(Assert.convert(model.getStartPrice()));
        /** 是否面配送费 0： 否 1：是*/
        storeDto.setFreightFlag(model.getIsFreeDelivery());

        ResultMap resultMap = updateShopInfo(storeDto);
        if (resultMap != null) {
            return resultMap;
        }
        return ResultMap.successu();
    }

    /**
     * 更新营业时间
     *
     * @see #updateShopInfo(StoreO2ODto) 更新店铺信息#更新营业时间
     */
    @Override
    public ResultMap setShopTime(SetShopTimeDto model) {
        Assert.isNotEmpty(model);
        StoreO2ODto storeDto = new StoreO2ODto();
        storeDto.setId(model.getStoreSerialNo());
        storeDto.setCode(model.getStoreSerialNo());
        storeDto.setStartTime(model.getOpenTime());
        storeDto.setEndTime(model.getCloseTime());
        ResultMap resultMap = updateShopInfo(storeDto);
        if (resultMap != null) {
            return resultMap;
        }
        return ResultMap.successu();
    }

    /**
     * 店铺介绍发布
     *
     * @param model
     * @see #searchShopInfo(ShopNoticeDTO) 调用Dubbo查询店铺信息服务,将查询结果Set进DTO
     * @see #pub(Object, ShopNoticeDTO) 完成发布
     */
    @Override
    public ResultMap introduceRelease(IntroduceReleaseDto model) {
        Assert.isNotEmpty(model);
        ShopNoticeDTO dto = new ShopNoticeDTO();
        dto.setType(TYPE_INTRODUCTION);
        dto.setShopCode(model.getStoreSerialNo());

        ResultMap resultMap = searchShopInfo(dto);
        if (resultMap != null) return resultMap;

        List<String> introducePicUrls = model.getIntroducePicUrls();
        if (introducePicUrls == null || introducePicUrls.size() < INTRODUCE_RELEASE_PIC_NEED) {
            return ResultMap.failll("介绍图片至少需要" + INTRODUCE_RELEASE_PIC_NEED + "张");
        }

        List<ShopPictureDTO> newPictures = new ArrayList<>();
        for (Object picUrl : introducePicUrls) {
            if (picUrl == null) continue;
            ShopPictureDTO pic = new ShopPictureDTO();
            if (picUrl instanceof String) {
                pic.setPicturePath(picUrl.toString());
            }
            if (picUrl instanceof JSONObject) {
                JSONObject json = (JSONObject) picUrl;
                pic.setPicturePath(json.getString("PicUrl"));
            }
            newPictures.add(pic);
        }

        /**店铺介绍图片列表,当type为2时,此字段必填*/
        dto.setNewPictures(newPictures);
        return pub(model, dto);
    }

    /**
     * 店铺公告发布
     *
     * @param model
     * @see #searchShopInfo(ShopNoticeDTO) 调用Dubbo查询店铺信息服务,将查询结果Set进DTO
     * @see #pub(Object, ShopNoticeDTO) 完成发布
     */
    @Override
    public ResultMap noticesRelease(NoticesReleaseDto model) {
        Assert.isNotEmpty(model);
        ShopNoticeDTO dto = new ShopNoticeDTO();
        dto.setShopCode(model.getStoreSerialNo());
        dto.setType(TYPE_NOTICE);
        ResultMap resultMap = searchShopInfo(dto);
        if (resultMap != null) {
            return resultMap;
        }
        /**店铺公告内容,当type为1时,此字段必填*/
        dto.setNewNoticeContent(model.getNoticesDetail());
        return pub(model, dto);
    }


    /**
     * 设置介绍在店铺中显示
     *
     * @param model
     * @see #getShopInfo(ShopNoticeDTO) 获取发布的介绍或者公告信息
     * @see #settingDisplayStatus(String, int) 完成-更新
     */
    @Override
    public ResultMap setIntroduceStatue(SetIntroduceStatueDto model) {
        Assert.isNotEmpty(model);
        /**
         *C端：  是否在店铺中显示【1展示，2店家设置不展示，3平台设置不展示】
         * 雅堂：1显示,0是不显示* */
        String status = model.getIntroduceStatue();
        if (!DISPLAY_YT.equals(status)) {
            status = HIDDEN;
        }
        ShopNoticeDTO dto = new ShopNoticeDTO();
        dto.setShopCode(model.getStoreSerialNo());
        dto.setType(TYPE_INTRODUCTION);

        ResultMap map = getShopInfo(dto);
        if (map.getMapdata() == null) {
            return map;
        }
        Integer noticeId = Integer.parseInt(map.getMapdata().toString());
        noticeId = (noticeId == 0 ? null : noticeId);

        ResultMap re = settingDisplayStatus(status, noticeId);
        if (re != null) {
            return re;
        }
        return ResultMap.successu();
    }


    /**
     * 设置公告在店铺中显示
     *
     * @param model
     * @see #getShopInfo(ShopNoticeDTO) 获取发布的介绍或者公告信息
     * @see #settingDisplayStatus(String, int)   完成-更新
     */
    @Override
    public ResultMap setNoticesShown(SetNoticesShownDto model) {
        Assert.isNotEmpty(model);
        /**
         *C端：  是否在店铺中显示【1展示，2店家设置不展示，3平台设置不展示】
         * 雅堂：1显示,0是不显示* */
        String status = model.getNoticesShown();
        if (!DISPLAY_YT.equals(status)) {
            status = HIDDEN;
        }
        ShopNoticeDTO dto = new ShopNoticeDTO();
        dto.setShopCode(model.getStoreSerialNo());
        dto.setType(TYPE_NOTICE);

        ResultMap map = getShopInfo(dto);
        if (map.getMapdata() == null) {
            return map;
        }
        Integer noticeId = Integer.parseInt(map.getMapdata().toString());
        noticeId = (noticeId == 0 ? null : noticeId);

        ResultMap re = settingDisplayStatus(status, noticeId);
        if (re != null) {
            return re;
        }
        return ResultMap.successu();
    }


    /**
     * 调用Dubbo服务o2oDubboService.updateStore更新店铺信息
     *
     * @param storeDto
     */
    private ResultMap updateShopInfo(StoreO2ODto storeDto) {
        Response<Integer> result = o2oDubboService.updateStore(storeDto);
        if (result == null || !result.isSuccess() || result.getResultObject() == null) {
            logger.error("The remote method OrgnazitionO2ODubboService.updateStore() excute fail. parames ==> " + JSON.toJSONString(storeDto));
            logger.error("OrgnazitionO2ODubboService.updateStore() resultSet ==> " + JSON.toJSONString(result));
            return ResultMap.failll("Server exception");
        }
        return null;
    }


    /***
     * 调用Dubbo服务获取店铺相关信息
     * Call getShopNoticesByShopInfo() result Shop Info
     * @param dto
     * @return
     */
    private ResultMap searchShopInfo(ShopNoticeDTO dto) {
        Response<StoreDto> shopNoticesByShopInfo = organizationService.queryStoreById(dto.getShopCode());

        if (shopNoticesByShopInfo == null || !shopNoticesByShopInfo.isSuccess() || shopNoticesByShopInfo.getResultObject() == null) {
            logger.error("The remote method OrganizationService.queryStoreById() excute fail. parames ==> " + JSON.toJSONString(dto));
            logger.error("OrganizationService.queryStoreById() resultSet ==> " + JSON.toJSONString(shopNoticesByShopInfo));
            if (shopNoticesByShopInfo == null) return ResultMap.failll("获取店铺相关信息失败,请稍后重试");
            return ResultMap.failll(shopNoticesByShopInfo.getErrorMessage());
        }

        StoreDto notic = shopNoticesByShopInfo.getResultObject();
        /**店铺名称【全称】(必填)*/
        dto.setShopFullName(notic.getName());
        /**店铺简称(必填)*/
        dto.setShopName(notic.getSimpleName());
        /**加盟商编号 (必填)*/
        dto.setAllianceBusinessCode(notic.getFranchiseeId());
        /**加盟商名称(必填)*/
        dto.setAllianceBusinessName(notic.getFranchiseeName());
        /**所属运营中心 (必填)*/
        dto.setCompanyCentre(notic.getOperationCenterName());
        /**是否在店铺中显示【1展示，2店家设置不展示，3平台设置不展示】 (必填)*/
        ShopNoticeDTO newDto = new ShopNoticeDTO();
        newDto.setShopCode(dto.getShopCode());
        newDto.setType(dto.getType());
        com.yatang.xcsm.common.response.Response<List<ShopNoticeDTO>> query = pushShopNoticeDubboService.getShopNoticesByShopInfo(newDto);
        if (query != null && query.isSuccess() && query.getResultObject() != null && query.getResultObject().size() > 0) {
            List<ShopNoticeDTO> resultObject = query.getResultObject();
            for (ShopNoticeDTO shopNoticeDTO : resultObject) {
                if (dto.getType().equals(shopNoticeDTO.getType())) {
                    dto.setDisplaySetting(shopNoticeDTO.getDisplaySetting());
                }
            }
        }else{
            dto.setDisplaySetting("2");
        }

        /**提交人*/
        dto.setSubmitter(notic.getFranchiseeId());
        return null;
    }

    /***
     * 发布
     * @param model
     * @param dto
     */
    private ResultMap pub(Object model, ShopNoticeDTO dto) {
        ShopNoticeDTO newDto = new ShopNoticeDTO();
        newDto.setShopCode(dto.getShopCode());
        newDto.setType(dto.getType());
        com.yatang.xcsm.common.response.Response<List<ShopNoticeDTO>> query = pushShopNoticeDubboService.getShopNoticesByShopInfo(newDto);
        logger.info("query pushShopNoticeDubboService.getShopNoticesByShopInfo(" + JSON.toJSONString(newDto) + ") ");
        logger.info("query pushShopNoticeDubboService.getShopNoticesByShopInfo( ) resultset ==>" + JSON.toJSONString(query));
        if (query != null && query.getResultObject() != null && query.getResultObject().size() > 0) {
            /**完成-更新*/
            com.yatang.xcsm.common.response.Response<String> result = pushShopNoticeDubboService.updateShopNoticeForShop(dto);
            if (result == null || !result.isSuccess()) {
                logger.error("The remote method PushShopNoticeDubboService.updateShopNoticeForShop() excute fail. parames ==> " + JSON.toJSONString(dto));
                logger.error("PushShopNoticeDubboService.updateShopNoticeForShop() resultSet ==> " + JSON.toJSONString(result));
                if (result == null) return ResultMap.failll("更新发布信息失败");
                return ResultMap.failll(result.getErrorMessage());
            }
        } else {
            com.yatang.xcsm.common.response.Response<String> addShopNotice = pushShopNoticeDubboService.addShopNotice(dto);
            if (addShopNotice == null || !addShopNotice.isSuccess()) {
                logger.error("The remote method PushShopNoticeDubboService.addShopNotice() excute fail. parames ==> " + model.toString());
                logger.error("PushShopNoticeDubboService.addShopNotice() resultSet ==> " + JSON.toJSONString(addShopNotice));
                if (addShopNotice == null) return ResultMap.failll("发布失败");
                return ResultMap.failll(addShopNotice.getErrorMessage());
            }
        }
        return ResultMap.successu();
    }


    /**
     * 获取发布的介绍或者公告信息
     *
     * @param dto
     * @return
     */
    private ResultMap getShopInfo(ShopNoticeDTO dto) {
        logger.info("获取发布的介绍或者公告信息 ==>" + dto.toString());
        com.yatang.xcsm.common.response.Response<List<ShopNoticeDTO>> shopNoticesByShopInfo = pushShopNoticeDubboService.getShopNoticesByShopInfo(dto);
        if (shopNoticesByShopInfo == null || !shopNoticesByShopInfo.isSuccess()) {
            logger.error("The remote method PushShopNoticeDubboService.getShopNoticesByShopInfo() excute fail. parames ==> " + JSON.toJSONString(dto));
            logger.error("PushShopNoticeDubboService.shopNoticesByShopInfo() resultSet ==> " + JSON.toJSONString(shopNoticesByShopInfo));
            if (shopNoticesByShopInfo == null) return ResultMap.failll("获取发布信息失败");
            return ResultMap.failll(shopNoticesByShopInfo.getErrorMessage());
        }

        if (shopNoticesByShopInfo.getResultObject() == null || shopNoticesByShopInfo.getResultObject().size() == 0) {
            return ResultMap.failll("您暂未发布任何信息");
        }

        ShopNoticeDTO shopNoticeDTO = shopNoticesByShopInfo.getResultObject().get(0);
        if (shopNoticeDTO == null) return ResultMap.failll("您暂未发布任何信息");

        return ResultMap.successu(shopNoticeDTO.getNoticeId());
    }

    /**
     * 更新公告或介绍的显示状态
     *
     * @param status
     * @param noticeId
     * @return
     */
    private ResultMap settingDisplayStatus(String status, int noticeId) {
        logger.info(" 更新公告或介绍的显示状态 ==> status = " + status + "  noticeId = " + noticeId);
        com.yatang.xcsm.common.response.Response<String> updateDisplayStatus = pushShopNoticeDubboService.updateDisplayStatus(noticeId, status);
        if (updateDisplayStatus == null || !updateDisplayStatus.isSuccess()) {
            logger.error("The remote method PushShopNoticeDubboService.updateDisplayStatus() excute fail. noticeId ==> " + noticeId + "  status==>" + status);
            logger.error("PushShopNoticeDubboService.updateDisplayStatus() resultSet ==> " + JSON.toJSONString(updateDisplayStatus));
            return ResultMap.failll("设置失败");
        }
        return null;
    }

}
