package com.yatang.xc.xcr.service.impl;

import com.alibaba.fastjson.JSON;
import com.busi.common.resp.Response;
import com.yatang.xc.rp.dubboservice.XcrO2oProductDubboService;
import com.yatang.xc.rp.dubboservice.common.Paging;
import com.yatang.xc.rp.dubboservice.dto.QueryInDTO;
import com.yatang.xc.rp.dubboservice.dto.QueryOutDTO;
import com.yatang.xc.rp.dubboservice.dto.xcr.PrdQueryByShelvesDTO;
import com.yatang.xc.rp.dubboservice.dto.xcr.PrdRecommendDTO;
import com.yatang.xc.rp.dubboservice.dto.xcr.XcrO2oProductDTO;
import com.yatang.xc.xcr.dto.inputs.NewArrivalsDto;
import com.yatang.xc.xcr.dto.inputs.SpecialGoodsOutListDto;
import com.yatang.xc.xcr.dto.outputs.GoodsOutListResultDto;
import com.yatang.xc.xcr.dto.outputs.GoodsOutListResultListDto;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IOutGoodsService;
import com.yatang.xc.xcr.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 10:36
 * @Summary : 外送商品
 */
@Service
public class OutGoodsServiceImpl implements IOutGoodsService {

    private
    @Autowired
    XcrO2oProductDubboService productDubboService;

    /**
     * 1:超级会员专享商品
     */
    private static final String SVIP = "1";
    /**
     * 0:新品推荐商品
     */
    private static final String RECOMMEND = "0";
    /**
     * 图片服务地址前缀
     */
    private
    @Value("${OUTGOODSURL_PREFIX}")
    String imageUrl;
    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(OutGoodsServiceImpl.class);

    /**
     * 外送商品新品推荐
     *
     * @Summary : 外送商品新品推荐</br>
     * @since ： 2.5.1
     */
    @Override
    public ResultMap newArrivals(NewArrivalsDto model) {
        Assert.isNotEmpty(model);

        Response<Long> count = productDubboService.countShopRecommendProducts(model.getStoreSerialNo());
        if (count == null || !count.isSuccess()) {
            logger.error("The XcrProductDubboService.countShopRecommendProducts() Params ==> " + JSON.toJSONString(model.getStoreSerialNo()));
            logger.error("The XcrProductDubboService.countShopRecommendProducts() resultSet ==> " + JSON.toJSONString(count));
            return ResultMap.failll("外送新品推荐失败,请稍后重试");
        }

        if (model.getArrivalsStatue() == 1) {
            if (count.getResultObject() >= 20L) {
                logger.error("The XcrProductDubboService.countShopRecommendProducts()  count > 20 Params ==> " + JSON.toJSONString(model.getStoreSerialNo()));
                logger.error("The XcrProductDubboService.countShopRecommendProducts()  count > 20 resultSet ==> " + JSON.toJSONString(count));
                return ResultMap.failll("最多可添加20个新品推荐商品");
            }
        }


        PrdRecommendDTO produ = new PrdRecommendDTO();
        /**门店编号*/
        produ.setShopCode(model.getStoreSerialNo());
        /**商品国际码*/
        produ.setInternationalCode(model.getGoodsCode());
        /**是否为外送新品推荐*/
        produ.setO2oIsRecommend(model.isRecommend());

        List<PrdRecommendDTO> productSet = Arrays.asList(produ);
        Response<Boolean> updateProducts = productDubboService.updateProducts4Recommend(productSet);
        if (updateProducts == null || !updateProducts.isSuccess()) {
            logger.error("The XcrProductDubboService.updateProducts4Recommend() Params ==> " + JSON.toJSONString(productSet));
            logger.error("The XcrProductDubboService.updateProducts4Recommend() resultSet ==> " + JSON.toJSONString(updateProducts));
            return ResultMap.failll("外送新品推荐失败,请稍后重试");
        }
        return ResultMap.successu();
    }


    /**
     * @Summary : 外送商品特殊列表
     * @since ：2.5.1
     */
    @Override
    public ResultMap specialGoodsOutList(SpecialGoodsOutListDto model) {
        Assert.isNotEmpty(model);

        QueryInDTO queryPamras = new QueryInDTO();
        queryPamras.setPaging(new Paging(model.getPageIndex(), model.getPageSize()));
        PrdQueryByShelvesDTO queryParm = new PrdQueryByShelvesDTO(model.getStoreSerialNo());
        queryParm.setO2oOnShelves(model.getFrameType());
        queryPamras.setParams(queryParm);
        logger.info("query parameter ==> " + JSON.toJSONString(queryPamras));

        /**新品推荐商品 */
        String specialType = model.getSpecialType() == null ? "" : model.getSpecialType().toString();
        if (RECOMMEND.equals(specialType)) {
            Response<QueryOutDTO<XcrO2oProductDTO>> queryOutDTOResponse = productDubboService.queryRecommendProducts(queryPamras);
            if (queryOutDTOResponse == null || !queryOutDTOResponse.isSuccess() || queryOutDTOResponse.getResultObject() == null) {
                logger.error("XcrProductDubboService.querySvipExclusiveProducts fail result ==> " + JSON.toJSONString(queryPamras));
                logger.error("XcrProductDubboService.querySvipExclusiveProducts fail prams ==> " + JSON.toJSONString(queryOutDTOResponse));
                return ResultMap.failll("暂未查询到新品推荐商品相关信息");
            }
            GoodsOutListResultListDto dto = result(queryOutDTOResponse);
            return ResultMap.successu().setListdata(dto.getRows()).page(dto.getPageindex(), dto.getPagesize(), dto.getTotalpage(), dto.getTotalcount());
        }

        /**超级会员专享商品*/
        if (SVIP.equals(specialType)) {
            Response<QueryOutDTO<XcrO2oProductDTO>> queryOutDTOResponse = productDubboService.querySvipExclusiveProducts(queryPamras);
            if (queryOutDTOResponse == null || !queryOutDTOResponse.isSuccess() || queryOutDTOResponse.getResultObject() == null) {
                logger.error("XcrProductDubboService.querySvipExclusiveProducts fail result ==> " + JSON.toJSONString(queryPamras));
                logger.error("XcrProductDubboService.querySvipExclusiveProducts fail prams ==> " + JSON.toJSONString(queryOutDTOResponse));
                return ResultMap.failll("暂未查询到超级会员相关商品信息");
            }
            GoodsOutListResultListDto dto = result(queryOutDTOResponse);
            return ResultMap.successu().setListdata(dto.getRows()).page(dto.getPageindex(), dto.getPagesize(), dto.getTotalpage(), dto.getTotalcount());
        }
        return ResultMap.failll("暂未查询到相关数据");
    }

    /**
     * @param :
     * @Summary :  查超级会员专属商品的
     * @since ： 2.5.1
     */
    @Override
    public ResultMap querySvipExclusiveProducts() {
        return null;
    }

    ///<Summary>
    ///    返回参数整理
    ///</Summary>
    public GoodsOutListResultListDto result(Response<QueryOutDTO<XcrO2oProductDTO>> queryOutDTOResponse) {
        List<XcrO2oProductDTO> list = queryOutDTOResponse.getResultObject().getRecords();
        if (list == null || list.size() == 0) {
            return GoodsOutListResultListDto.none();
        }

        List<GoodsOutListResultDto> rows = new ArrayList<>(list.size());
        for (XcrO2oProductDTO pro : list) {
            GoodsOutListResultDto data = new GoodsOutListResultDto();
            data.setGoodsCode(pro.getItemNum());
            data.setGoodsName(pro.getItemDesc());
            String price = pro.getAdjustCost();
            if (org.apache.commons.lang3.StringUtils.isBlank(price)) {
                price = pro.getAdjustCost();
            }
            data.setGoodsPrice(price);
            data.setUnitName(pro.getUnit());
            data.setGoodsPic(imageUrl + pro.getThumbnailImage());
            rows.add(data);
        }
        Paging paging = queryOutDTOResponse.getResultObject().getPaging();
        GoodsOutListResultListDto listResultListDto = new GoodsOutListResultListDto();
        listResultListDto.setPageindex(paging.getPageNum());
        listResultListDto.setPagesize(paging.getPageSize());
        listResultListDto.setRows(rows);
        listResultListDto.setTotalcount(paging.getTotalSize());
        listResultListDto.setTotalpage(paging.getTotalPage());
        return listResultListDto;
    }

}
