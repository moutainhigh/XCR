package com.yatang.xc.xcr.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.mbd.pi.es.dto.ProductStoreIndexDto;
import com.yatang.xc.mbd.pi.es.dubboservice.ProductStoreIndexDubboService;
import com.yatang.xc.pos.cloud.dto.ImportProductResultDTO;
import com.yatang.xc.pos.cloud.dto.SuperManCommonProductAddDTO;
import com.yatang.xc.pos.cloud.dto.SuperManProductImportDTO;
import com.yatang.xc.pos.cloud.dto.SuperManSelfProductAddDTO;
import com.yatang.xc.pos.cloud.dubboservice.ISuperManShopProductDubboService;
import com.yatang.xc.rp.dubboservice.XcrProductDubboService;
import com.yatang.xc.rp.dubboservice.common.Paging;
import com.yatang.xc.rp.dubboservice.dto.QueryInDTO;
import com.yatang.xc.rp.dubboservice.dto.QueryOutDTO;
import com.yatang.xc.rp.dubboservice.dto.xcr.*;
import com.yatang.xc.xcr.dto.inputs.*;
import com.yatang.xc.xcr.dto.outputs.NewClassifyListResultSetDto;
import com.yatang.xc.xcr.dto.outputs.NewGoodsListResultSetDto;
import com.yatang.xc.xcr.enums.CommodityErrEnum;
import com.yatang.xc.xcr.model.RequestBaseModel;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.ICommodityManagementService;
import com.yatang.xc.xcr.util.Assert;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.TokenUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author : BobLee
 * @CreateTime : 2017年12月08日 14:54
 * @Summary : 新账号商品管理v2.6
 * @since 2.6
 */
@Service
public class CommodityManagementServiceImpl implements ICommodityManagementService {

    private final Logger logger = LoggerFactory.getLogger(CommodityManagementServiceImpl.class);

    private String IMPORT_FAILED = "导入失败";
    private static final String DETAIL_ERROR = "详情数据有误";
    private static final String DETAIL_GOODSCODE_ERROR = "商品条码信息有误";

    @Autowired
    private XcrProductDubboService xcrProductDubboService;
    @Autowired
    private ISuperManShopProductDubboService iSuperManShopProductService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProductStoreIndexDubboService productStoreIndexDubboService;

    /**
     * @param : [EditNewGoodsDto model]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary : 编辑商品
     * @since ： 2.6.0
     */
    @Override
    public ResultMap editNewGoods(EditNewGoodsDto model) {
        Assert.isNotEmpty(model);
        logger.info("[2.6.0编辑商品] 页面传入参数 -> EditNewGoodsDto:" + JSONObject.toJSONString(model));
        XcrPrdUpdateDTO xcrPrdUpdateDTO = getXcrPrdUpdateDTO(model);
        logger.info("[2.6.0编辑商品] dubbo入参 -> xcrProductDubboService.updateProduct(xcrPrdUpdateDTO) -> xcrPrdUpdateDTO:" + JSONObject.toJSONString(xcrPrdUpdateDTO));
        Response<Boolean> response = xcrProductDubboService.updateProduct(xcrPrdUpdateDTO);
        logger.info("[2.6.0编辑商品] dubbo返回 -> xcrProductDubboService.updateProduct(xcrPrdUpdateDTO) -> response:" + JSONObject.toJSONString(response));
        if (response == null) {
            return ResultMap.failll("编辑商品失败");
        }
        if (response.isSuccess()) {
            return ResultMap.successu();
        }
        if (spShotCodeExist(response)) {
            return ResultMap.failll("该简码店铺中已有，请更换");
        }
        return ResultMap.failll(response.getErrorMessage());
    }

    /**
     * 根据返回值判断简码是否存在
     *
     * @param response
     * @return
     */
    private boolean spShotCodeExist(Response<Boolean> response) {
        return response.getCode().equals("EXS003") || response.getErrorMessage().contains("当前门店已存在重复的spShortCode");
    }

    /**
     * @param : [NewGoodsDetailDto model]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary :  商品详情
     * @since ： 2.6.0
     */
    @Override
    public ResultMap newGoodsDetail(NewGoodsDetailDto model) {
        Assert.isNotEmpty(model);
        if(model.getGoodsCode().length()<4 || model.getGoodsCode().length()>18){
        	return ResultMap.failll(DETAIL_GOODSCODE_ERROR);
        }
        Response<XcrPrdDTO> result = getPrdByInternationalCode(model.getStoreSerialNo(), model.getGoodsCode(), true);
        if (result == null) {
            return ResultMap.failll(DETAIL_ERROR);
        }
        List<XcrPrdDTO> prdList = new ArrayList<>();
        if (result.isSuccess()) {
            if (result.getResultObject() != null) {
                prdList.add(result.getResultObject());
            }
        } else {
            return ResultMap.failll(DETAIL_ERROR);
        }
        return packGoodsDetailMapdata(prdList);
    }

    /**
     * @param : [AddNewGoodsDto model]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary :  导入商品
     * @since ： 2.6.0
     */
    @Override
    public ResultMap newShopImportGoods(AddNewGoodsDto model) {
        JSONObject tokenJson = TokenUtil.getTokenFromRedis(model.getUserId());
        List<SuperManProductImportDTO> paramList = new ArrayList<>();
        SuperManProductImportDTO dto = new SuperManProductImportDTO();
        dto.setFranchiseeId(tokenJson.getString("jmsCode"));
        dto.setShopCode(model.getStoreSerialNo());
        dto.setInternationalCode(model.getGoodsCode());
        dto.setLoginAccount(model.getUserId());
        paramList.add(dto);
        long startTime = System.currentTimeMillis();
        logger.info("\n*******于时间：" + DateUtils.getLogDataTime(startTime, null) + "开始调用订单接口:superManProductImport"
                + "\n*******请求数据为：" + JSONObject.toJSONString(paramList));
        Response<Map<String, ImportProductResultDTO>> result = iSuperManShopProductService.superManProductImport(paramList);
        logger.info("\n******于时间：" + DateUtils.getLogDataTime(startTime, null) + "调用订单接口:superManProductImport调用结束"
                + "\n*******响应数据为：" + JSONObject.toJSONString(result)
                + "\n*******所花费时间为：" + CommonUtil.costTime(startTime));
        if (result == null) {
            return ResultMap.failll(IMPORT_FAILED);
        }
        if (!result.isSuccess() && result.getErrorMessage() != null) {
            for (CommodityErrEnum e : CommodityErrEnum.values()) {
                if (e.getCode().equals(result.getCode())) {
                    if (result.getCode().equals("7102") && result.getErrorMessage().contains(e.getDesc())) {
                        IMPORT_FAILED = e.getErrTip();
                    } else {
                        IMPORT_FAILED = e.getErrTip();
                    }
                    break;
                }
            }
            return ResultMap.failll(IMPORT_FAILED);
        }
        Response<XcrPrdDTO> detailResult = getPrdByInternationalCode(model.getStoreSerialNo(), model.getGoodsCode(), false);
        return packImportGoodsMapdata(detailResult);
    }

    /**
     * 商品调价
     *
     * @param model
     * @return
     */
    @Override
    public ResultMap newModifyGoodsPrices(ModifyGoodsPricesDto model) {
        logger.info("【商品调价】 -> 传入参数 -》 ModifyGoodsPricesDto：" + JSONObject.toJSONString(model));
        String StoreSerialNo = model.getStoreSerialNo();
        if (com.yatang.xc.xcr.util.StringUtils.isEmpty(StoreSerialNo)) {
            return ResultMap.failll("没有获取到门店编号");
        }
        List<GoodsPricesDetail> goodsList = model.getGoodsList();
        if (CollectionUtils.isEmpty(goodsList)) {
            return ResultMap.failll("没有获取到待调价商品信息");
        }
        XcrPrdBatchAdjustPriceDTO xcrPrdBatchAdjustPriceDTO = new XcrPrdBatchAdjustPriceDTO(StoreSerialNo);
        for (GoodsPricesDetail goodsPricesDetail : goodsList) {
            String goodsCode = goodsPricesDetail.getGoodsCode(); //条形码
            String goodsPrice = goodsPricesDetail.getNewGoodsPrice();//商品价格
            if (com.yatang.xc.xcr.util.StringUtils.isEmpty(goodsCode) && com.yatang.xc.xcr.util.StringUtils.isEmpty(goodsPrice)) {
                logger.error("[该条形码或价格不存在]GoodsCode:" + goodsCode + "  NewGoodsPrice:" + goodsPrice);
                continue;
            }
            xcrPrdBatchAdjustPriceDTO.addCP(goodsCode, Double.valueOf(goodsPrice));
        }
        logger.info("【商品调价】 -> xcrProductDubboService.batchAdjustPrice(xcrPrdBatchAdjustPriceDTO) -> 传入参数：xcrPrdBatchAdjustPriceDTO：" + JSONObject.toJSONString(xcrPrdBatchAdjustPriceDTO));
        Response<Boolean> xcrProductResponse = xcrProductDubboService.batchAdjustPrice(xcrPrdBatchAdjustPriceDTO);
        logger.info("【商品调价】 -> xcrProductDubboService.batchAdjustPrice(xcrPrdBatchAdjustPriceDTO) -> 响应数据：xcrProductResponse：" + JSONObject.toJSONString(xcrProductResponse));
        if (xcrProductResponse == null) {
            return ResultMap.failll("调价失败");
        }
        if (xcrProductResponse.isSuccess()) {
            return ResultMap.successu();
        }
        return ResultMap.failll(xcrProductResponse.getErrorMessage());
    }

    /**
     * @param : [AddNewGoodsDto model]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary :  新增商品
     * @since ： 2.6.0
     */
    @Override
    public ResultMap addNewGoods(AddNewGoodsDto model) {
        Assert.isNotEmpty(model);
        Response<Boolean> response = addProduct(model);
        logger.info("\n******addNewGoods响应的数据为：" + JSONObject.toJSONString(response));
        if (response != null) {
            if (response.isSuccess()) {
                return ResultMap.successu();
            }
            return ResultMap.failll(getPrompt(response));
        }
        return ResultMap.failll("新增商品失败");
    }

    /**
     * 添加商品校验提示
     *
     * @param response
     * @return
     */
    private String getPrompt(Response<Boolean> response) {
        String errorMessage = response.getErrorMessage();
        String code = response.getCode();
        if (code.equals("7107")) {
            return "该简码店铺中已有，请更换";
        }
        if (errorMessage.contains("门店已存在重复的internationalCode")) {
            return "该商品条码店铺中已有，请更换";
        }
        return errorMessage;
    }


    /**
     * @param : [NewGoodsListDto model]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary :
     * @since ： 2.6.0
     */
    @Override
    public ResultMap newGoodsList(NewGoodsListDto model) {
        Assert.isNotEmpty(model);

        /**==Search==*/
        if (!StringUtils.isBlank(model.getSearch())) {
            return searchByTextCondition(model.getPageIndex(), model.getPageSize(), model.getSearch(), model.getStoreSerialNo());
        }

        /**==Classify Id==*/
        if (!StringUtils.isBlank(model.getClassifyId())) {
            return searchByClassifyId(model.getPageIndex(), model.getPageSize(), model.getClassifyId(), model.getStoreSerialNo());
        }

        /**上面可以直接 else 返回 */
        return ResultMap.successu().listData();
    }

    /**
     * @param : [model]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary : 分类列表v2_6
     * @JIRA : 查询商品分类：web端开发
     * @Issues : XCR-2818
     * @since ： 2.6.0
     */
    @Override
    public ResultMap newClassifyList(RequestBaseModel model) {
        Assert.isNotEmpty(model);

        Response<List<XcrPrdCategoryDTO>> response = xcrProductDubboService.querySecondLevelCategoryByShopCode(model.getStoreSerialNo());
        if (response == null || response.getResultObject() == null || !response.isSuccess()) {
            logger.error("The remote method XcrProductDubboService.querySecondLevelCategoryByShopCode execute fail, prams ==> {}", model.getStoreSerialNo());
            logger.error("The remote method XcrProductDubboService.querySecondLevelCategoryByShopCode execute fail, result ==>{} ", JSON.toJSONString(response));
            if (response != null && !StringUtils.isBlank(response.getErrorMessage())) {
                return ResultMap.failll(response.getErrorMessage());
            }
            return ResultMap.successu().listData();
        }

        List<XcrPrdCategoryDTO> records = response.getResultObject();
        if (records == null || records.size() == 0) {
            logger.info("The remote method XcrProductDubboService.queryProductsByCategory execute success, prams ==>{} ", JSON.toJSONString(model.getStoreSerialNo()));
            logger.info("The remote method XcrProductDubboService.queryProductsByCategory execute success, result ==> {}", JSON.toJSONString(response));
            return ResultMap.successu().listData();
        }

        List<NewClassifyListResultSetDto> array = new ArrayList<>(records.size());
        for (XcrPrdCategoryDTO record : records) {
            NewClassifyListResultSetDto dto = new NewClassifyListResultSetDto();
            dto.setClassifyId(record.getCategoryId());
            dto.setClassifyName(record.getCategoryName());
            array.add(dto);
        }

        return ResultMap.successu().setListdata(array).page(response.getPageNum(), array.size(), response.getPageNum(), ((long) array.size()));
    }

    /**
     * @param : [pageNum, pageSize, classifyId, storeSerialNo]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary : 根据分类ID查询商品列表
     * @JIRA : 根据分类ID查询商品列表：web端开发
     * @Issues : XCR-2820
     * @since ： 2.6.0
     */
    @Override
    public ResultMap searchByClassifyId(int pageNum, int pageSize, String classifyId, String storeSerialNo) {
        PrdQueryByCategoryDTO params = new PrdQueryByCategoryDTO(storeSerialNo);
        params.setSecondLevelCategoryId(classifyId);
        QueryInDTO query = new QueryInDTO();
        query.setPaging(new Paging(pageNum, pageSize));
        query.addSortField("updateTime", QueryInDTO.SORT.DESC);
        query.setParams(params);

        Response<QueryOutDTO<XcrPrdDTO>> response = xcrProductDubboService.queryProductsByCategory(query);
        if (response == null || response.getResultObject() == null || !response.isSuccess()) {
            logger.error("The remote method XcrProductDubboService.queryProductsByCategory execute fail, prams ==> {}", JSON.toJSONString(query));
            logger.error("The remote method XcrProductDubboService.queryProductsByCategory execute fail, result ==>{} ", JSON.toJSONString(response));
            if (response != null && !StringUtils.isBlank(response.getErrorMessage())) {
                return ResultMap.failll(response.getErrorMessage());
            }
            return ResultMap.successu().listData();
        }

        List<XcrPrdDTO> records = response.getResultObject().getRecords();
        if (records == null || records.size() == 0) {
            logger.info("The remote method XcrProductDubboService.queryProductsByCategory execute success, prams ==>{} ", JSON.toJSONString(query));
            logger.info("The remote method XcrProductDubboService.queryProductsByCategory execute success, result ==> {}", JSON.toJSONString(response));
            return ResultMap.successu().listData();
        }

        Paging paging = response.getResultObject().getPaging();
        return buildingResponseDatas(records).page(paging.getPageNum(), paging.getPageSize(), paging.getTotalPage(), paging.getTotalSize());
    }

    /**
     * @param : [pageNum, pageSize, searchText, storeSerialNo]
     * @return ：  com.yatang.xc.xcr.model.ResultMap
     * @Summary : 根据搜索条件查询商品列表
     * @JIRA : 根据搜索条件查询商品列表：web端开发
     * @Issues : XCR-2823
     * @since ： 2.6.0
     */
    @Override
    public ResultMap searchByTextCondition(int pageNum, int pageSize, String searchText, String storeSerialNo) {
        if (StringUtils.isBlank(searchText)) {
            return ResultMap.successu().listData();
        }
        if (Assert.isNumeric(searchText)) {
            Response<XcrPrdDTO> response = getPrdByInternationalCode(storeSerialNo, searchText, false);
            if (response == null || response.getResultObject() == null || !response.isSuccess()) {
                logger.error("The remote method XcrProductDubboService.queryProductByInternationalCode execute fail, prams ==> {}", searchText);
                logger.error("The remote method XcrProductDubboService.queryProductByInternationalCode execute fail, result ==>{} ", JSON.toJSONString(response));
                if (response != null && !StringUtils.isBlank(response.getErrorMessage())) {
                    return ResultMap.failll(response.getErrorMessage());
                }
                return ResultMap.successu().listData();
            }
            List<XcrPrdDTO> recordArray = new ArrayList<>();
            XcrPrdDTO record = response.getResultObject();
            recordArray.add(record);
            return buildingResponseDatas(recordArray).page(pageNum, pageSize, 1, 1L);
        }

        QueryInDTO query = new QueryInDTO();
        query.setPaging(new Paging(pageNum, pageSize));
        query.setParams(new PrdQueryByNameDTO(storeSerialNo, searchText));

        Response<QueryOutDTO<XcrPrdDTO>> response = xcrProductDubboService.queryProductsByName(query);
        if (response == null || response.getResultObject() == null || !response.isSuccess()) {
            logger.error("The remote method XcrProductDubboService.queryProductsByName execute fail, prams ==> {}", JSON.toJSONString(query));
            logger.error("The remote method XcrProductDubboService.queryProductsByName execute fail, result ==>{} ", JSON.toJSONString(response));
            if (response != null && !StringUtils.isBlank(response.getErrorMessage())) {
                return ResultMap.failll(response.getErrorMessage());
            }
            return ResultMap.successu().listData();
        }

        List<XcrPrdDTO> records = response.getResultObject().getRecords();
        if (records == null || records.size() == 0) {
            logger.info("The remote method XcrProductDubboService.queryProductsByName execute success, prams ==>{} ", JSON.toJSONString(query));
            logger.info("The remote method XcrProductDubboService.queryProductsByName execute success, result ==> {}", JSON.toJSONString(response));
            return ResultMap.successu().listData();
        }

        Paging paging = response.getResultObject().getPaging();
        if (paging == null) {
            return buildingResponseDatas(records).page(response.getPageNum(), records.size(), response.getPageNum(), ((long) records.size()));
        }
        return buildingResponseDatas(records).page(paging.getPageNum(), paging.getPageSize(), paging.getTotalPage(), paging.getTotalSize());
    }

    ///<Summary>
    ///   构建响应数据
    ///</Summary>
    private ResultMap buildingResponseDatas(List<XcrPrdDTO> records) {
        List<NewGoodsListResultSetDto> array = new ArrayList<>(records.size());
        for (XcrPrdDTO record : records) {
            NewGoodsListResultSetDto resultSetDto = new NewGoodsListResultSetDto();
            resultSetDto.setClassifyId(record.getSecondLevelCategoryId());
            resultSetDto.setGoodsId(record.getId());
            resultSetDto.setGoodsCode(record.getInternationalCode());
            resultSetDto.setGoodsName(record.getName());
            resultSetDto.setGoodsPrice(record.getSpPrice().toString());
            resultSetDto.setClassifyName(record.getSecondLevelCategoryName());
            resultSetDto.setUnitName(record.getUnit());
            array.add(resultSetDto);
        }

        if (array.size() == 0) {
            return ResultMap.successu().listData();
        }

        return ResultMap.successu().setListdata(array);
    }


    /**
     * 通过国际码查询商品，返回列表
     *
     * @param storeNo
     * @param InternationalCode
     * @param tryBymbd
     * @return
     */
    private Response<XcrPrdDTO> getPrdByInternationalCode(String storeNo, String InternationalCode, boolean tryBymbd) {
        PrdQueryByInternationalCodeDTO query = new PrdQueryByInternationalCodeDTO(storeNo, InternationalCode);
        // 门店没有，尝试从主数据查询（仅返回门店号，国际码，名称）
        query.setTryByMbd(tryBymbd);
        Long startTime = System.currentTimeMillis();
        logger.info("\n******于" + DateUtils.getLogDataTime(startTime, null) + " 请求主数据queryProductByInternationalCode接口"
                + "\n******请求参数：" + JSONObject.toJSONString(query));
        Response<XcrPrdDTO> result = xcrProductDubboService.queryProductByInternationalCode(query);
        logger.info("\n******于" + DateUtils.getLogDataTime(startTime, null) + " 请求主数据queryProductByInternationalCode接口结束"
                + "\n******result:" + JSONObject.toJSONString(result)
                + "\n******花费时间：" + CommonUtil.costTime(startTime));
        return result;
    }

    /**
     * newGoodsDetail接口组装返回的参数
     *
     * @param prdList
     * @return
     */
    private ResultMap packGoodsDetailMapdata(List<?> prdList) {
        JSONObject alyzJson = new JSONObject();
        Map<String, Object> item = new HashMap<>();
        if (prdList.size() == 0) {
            item.put("DataSource", 0);
        } else {
            alyzJson = JSONObject.parseObject(JSONObject.toJSONString(prdList.get(0)));
            item.put("DataSource", alyzJson.getInteger("dataSource"));
        }
        item.put("GoodsId", "");
        item.put("GoodsType", alyzJson.getBoolean("spSelf") != null ? (alyzJson.getBoolean("spSelf") ? 1 : 2) : 1);
        item.put("IsWeighingGoods", isWeightPrd(alyzJson.getString("productType")));
        item.put("IsAllowReturn", alyzJson.getInteger("spSupportReturn"));
        item.put("GoodsName", alyzJson.getString("name"));
        item.put("ClassifyId", alyzJson.getString("secondLevelCategoryId"));
        String classifyName = "";
        if (alyzJson.getString("firstLevelCategoryName") != null) {
            classifyName = alyzJson.getString("firstLevelCategoryName");
            if (alyzJson.getString("secondLevelCategoryName") != null) {
                classifyName += ">" + alyzJson.getString("secondLevelCategoryName");
                if (alyzJson.getString("thirdLevelCategoryName") != null) {
                    classifyName += ">" + alyzJson.getString("thirdLevelCategoryName");
                }
            }
        }
        item.put("ClassifyName", classifyName);
        item.put("GoodsPrice", alyzJson.getString("spPrice"));
        item.put("UnitName", alyzJson.getString("unit"));
        item.put("GoodsCode", alyzJson.getString("internationalCode"));
        item.put("GoodsSimpleCode", alyzJson.getString("spShortCode"));
        item.put("Specificate", alyzJson.getString("packingSpecifications"));
        item.put("ShelfLife", alyzJson.getString("qualityGuaranteePeriod"));
        item.put("Brand", alyzJson.getString("brandName"));
        item.put("TaxRate", alyzJson.getString("taxRate"));
        item.put("CostPrice", alyzJson.getString("costPrice"));
        double grossRate;
       if (alyzJson.getDoubleValue("spPrice") == 0 && alyzJson.getDoubleValue("costPrice") == 0) {
            grossRate = 0;
        }else if (alyzJson.getDoubleValue("spPrice") == 0) {
            grossRate = -100;
        } else {
            grossRate = (alyzJson.getDoubleValue("spPrice") - alyzJson.getDoubleValue("costPrice")) * 100 / alyzJson.getDoubleValue("spPrice");
        }
        item.put("GrossRate", CommonUtil.getFormatDouble(grossRate, 2) + "%");
        item.put("FrameType", alyzJson.getInteger("spOnShelves"));
        item.put("Remark", alyzJson.getString("spRemark"));
        JSONObject mapdata = com.yatang.xc.xcr.util.StringUtils.replcNULLToStr(item);
        return ResultMap.successu().setMapdata(mapdata);
    }

    /**
     * 处理商品是称重商品还是非称重商品
     *
     * @param prdType
     * @return
     */
    private Integer isWeightPrd(String prdType) {
        //商品类型:1常规商品 2打包商品 3附属商品 4称重商品 5加工称重品 6加工非称重品
        Integer isWeight = 0;
        if (prdType != null) {
            if (prdType.equals("4") || prdType.equals("5") || prdType.equals("6")) {
                isWeight = 1;
            }
        }
        return isWeight;
    }

    /**
     * 新增商品
     *
     * @param model
     * @return
     */
    private Response<Boolean> addProduct(AddNewGoodsDto model) {
        logger.info("【新增商品】 -> 传入参数：AddNewGoodsDto：" + JSONObject.toJSONString(model));
        Integer type = model.getGoodsType();
        if (type != null && type == 1) {
            return superManSelfProductAdd(model);
        }
        return superManCommonProductAdd(model);
    }

    /**
     * 新增普通商品
     *
     * @param model
     * @return
     */
    private Response<Boolean> superManCommonProductAdd(AddNewGoodsDto model) {
        logger.info("【新增普通商品】 -> model:" + JSONObject.toJSONString(model));
        SuperManCommonProductAddDTO superManCommonProductAddDTO = getSuperManCommonProductAddDTO(model);
        logger.info("【新增普通商品】 -> superManCommonProductAddDTO:" + JSONObject.toJSONString(superManCommonProductAddDTO));
        return iSuperManShopProductService.superManCommonProductAdd(superManCommonProductAddDTO);
    }

    /**
     * 新增自建商品
     *
     * @param model
     * @return
     */
    private Response<Boolean> superManSelfProductAdd(AddNewGoodsDto model) {
        logger.info("【新增自建商品】 -> model:" + JSONObject.toJSONString(model));
        SuperManSelfProductAddDTO superManSelfProductAddDTO = getSuperManSelfProductAddDTO(model);
        logger.info("【新增自建商品】 -> superManSelfProductAddDTO:" + JSONObject.toJSONString(superManSelfProductAddDTO));
        return iSuperManShopProductService.superManSelfProductAdd(superManSelfProductAddDTO);
    }

    /**
     * 普通商品参数组装
     *
     * @param model
     * @return
     */
    private SuperManCommonProductAddDTO getSuperManCommonProductAddDTO(AddNewGoodsDto model) {

        SuperManCommonProductAddDTO dto = new SuperManCommonProductAddDTO();
        Integer isWeighingGoods = model.getIsWeighingGoods();
        Integer frameType = model.getFrameType();
        Integer isAllowReturn = model.getIsAllowReturn();
        String goodsPrice = model.getGoodsPrice();
        String costPrice = model.getCostPrice();
        String stock = model.getInitStock();
        String shopCode = model.getStoreSerialNo();
        String productType = "1";
        productType = isWeighingGoods != null && isWeighingGoods == 1 ? "4" : productType;

        dto.setFranchiseeId(getFranchiseeIdByShopCode(shopCode));
        dto.setShopCode(shopCode);
        dto.setInternationalCode(model.getGoodsCode());
        dto.setName(model.getGoodsName());
        dto.setProductType(productType);
        dto.setSpShortCode(model.getGoodsSimpleCode());
        dto.setSpOnShelves(frameType != null ? String.valueOf(frameType) : "0");
        dto.setSpSupportReturn(isAllowReturn != null ? String.valueOf(isAllowReturn) : "0");
        dto.setSpRemark(model.getRemark());
        dto.setSpPrice(StringUtils.isNotEmpty(goodsPrice) ? Double.valueOf(goodsPrice) : 0.00);
        dto.setInitialQty(StringUtils.isNotEmpty(stock) ? Double.valueOf(stock) : 0.00);
        dto.setCostPrice(StringUtils.isNotEmpty(costPrice) ? Double.valueOf(costPrice) : 0.00);
        dto.setLoginAccount(model.getUserId());
        return dto;

    }

    /**
     * 自建商品参数组装
     *
     * @param model
     * @return
     */
    private SuperManSelfProductAddDTO getSuperManSelfProductAddDTO(AddNewGoodsDto model) {

        SuperManSelfProductAddDTO dto = new SuperManSelfProductAddDTO();
        Integer isWeighingGoods = model.getIsWeighingGoods();
        Integer frameType = model.getFrameType();
        Integer isAllowReturn = model.getIsAllowReturn();
        String goodsPrice = model.getGoodsPrice();
        String costPrice = model.getCostPrice();
        String stock = model.getInitStock();
        String shopCode = model.getStoreSerialNo();
        String productType = "1";
        productType = isWeighingGoods != null && isWeighingGoods == 1 ? "4" : productType;

        dto.setFranchiseeId(getFranchiseeIdByShopCode(shopCode));
        dto.setShopCode(shopCode);
        dto.setInternationalCode(model.getGoodsCode());
        dto.setName(model.getGoodsName());
        dto.setProductType(productType);
        dto.setSpShortCode(model.getGoodsSimpleCode());
        dto.setSpOnShelves(frameType != null ? String.valueOf(frameType) : "0");
        dto.setSpSupportReturn(isAllowReturn != null ? String.valueOf(isAllowReturn) : "0");
        dto.setSpRemark(model.getRemark());
        dto.setSpPrice(StringUtils.isNotEmpty(goodsPrice) ? Double.valueOf(goodsPrice) : 0.00);
        dto.setInitialQty(StringUtils.isNotEmpty(stock) ? Double.valueOf(stock) : 0.00);
        dto.setCostPrice(StringUtils.isNotEmpty(costPrice) ? Double.valueOf(costPrice) : 0.00);
        dto.setLoginAccount(model.getUserId());
        return dto;
    }

    /**
     * 获取商品编辑DTO
     *
     * @param model
     * @return
     */
    private XcrPrdUpdateDTO getXcrPrdUpdateDTO(EditNewGoodsDto model) {
        XcrPrdUpdateDTO dto = new XcrPrdUpdateDTO();
        Integer isWeighingGoods = model.getIsWeighingGoods(); //是否称重商品 1：是，0：否
        String goodsPrice = model.getGoodsPrice();
        String costPrice = model.getCostPrice();
        Integer isAllowRetuen = model.getIsAllowReturn();

        dto.setShopCode(model.getStoreSerialNo());
        dto.setInternationalCode(model.getGoodsCode());
        if (isWeighingGoods != null && isWeighingGoods == 1) {
            dto.setSpShortCode(model.getGoodsSimpleCode()); //门店商品简码
        }
        dto.setSpPrice(Double.valueOf(goodsPrice)); //门店销售价格
        if (com.busi.common.utils.StringUtils.isEmpty(costPrice)) {
        	costPrice = "0";
        }
        dto.setCostPrice(Double.valueOf(costPrice));
        if (isAllowRetuen != null) {
            dto.setSpSupportReturn(String.valueOf(isAllowRetuen)); //是否支持退货,0-不支持 1-支持
        }
        dto.setSpOnShelves(String.valueOf(model.getFrameType())); //门店商品上下架状态 0-下架 1-上架
        dto.setSpRemark(model.getRemark());
        return dto;
    }


    private String getUnitByGoodsCode(String goodsCode) {
        List<String> internationalCodes = new ArrayList<>();
        internationalCodes.add(goodsCode);
        Response<Map<String, ProductStoreIndexDto>> mapResponse = productStoreIndexDubboService.queryByInternationalCodes(internationalCodes);
        if (mapResponse == null || !mapResponse.isSuccess()) {
            logger.info("【根据国际码获取商品单位】 -> productStoreIndexDubboService.queryByInternationalCodes(internationalCodes) -> 失败 -> mapResponse:" + JSONObject.toJSONString(mapResponse));
            return "个";
        }
        Map<String, ProductStoreIndexDto> productStoreIndexDtoMap = mapResponse.getResultObject();
        if (CollectionUtils.isEmpty(productStoreIndexDtoMap)) {
            logger.info("【根据国际码获取商品单位】 -> mapResponse.getResultObject() == null !!!");
            return "个";
        }
        ProductStoreIndexDto productStoreIndexDto = productStoreIndexDtoMap.get(goodsCode);
        if (productStoreIndexDto == null) {
            logger.info("【根据国际码获取商品单位】 -> productStoreIndexDtoMap.get(goodsCode) -> productStoreIndexDto == null !!!");
            return "个";
        }
        return productStoreIndexDto.getMinUnit();
    }

    private String getFranchiseeIdByShopCode(String shopCode) {
        Response<StoreDto> storeDtoResponse = organizationService.queryStoreById(shopCode);
        if (storeDtoResponse != null && storeDtoResponse.isSuccess() && storeDtoResponse.getResultObject() != null) {
            return storeDtoResponse.getResultObject().getFranchiseeId();
        }
        return "";
    }

    /**
     * 包装导入商品返回的数据
     *
     * @param detailResult
     * @return
     */
    private ResultMap packImportGoodsMapdata(Response<XcrPrdDTO> detailResult) {
        JSONObject alyzJson = JSONObject.parseObject(JSONObject.toJSONString(detailResult.getResultObject()));
        Map<String, Object> item = new HashMap<>();
        item.put("GoodsId", "");
        item.put("GoodsName", alyzJson.getString("name"));
        item.put("ClassifyId", alyzJson.getString("secondLevelCategoryId"));
        item.put("ClassifyName", alyzJson.getString("secondLevelCategoryName"));
        item.put("GoodsPrice", alyzJson.getString("spPrice"));
        item.put("UnitName", alyzJson.getString("unit"));
        item.put("GoodsCode", alyzJson.getString("internationalCode"));
        return ResultMap.successu().setMapdata(com.yatang.xc.xcr.util.StringUtils.replcNULLToStr(item));
    }


}
