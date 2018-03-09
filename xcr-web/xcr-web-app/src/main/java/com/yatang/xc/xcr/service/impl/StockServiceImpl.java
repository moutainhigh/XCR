package com.yatang.xc.xcr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.o2o.dto.StoreO2ODto;

import com.yatang.xc.xcr.dto.inputs.AddToStockDto;
import com.yatang.xc.xcr.dto.inputs.AutomaticReceiptDto;
import com.yatang.xc.xcr.dto.inputs.BusinessStateDto;
import com.yatang.xc.xcr.dto.inputs.ShopAbbreviationDto;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IStockService;
import com.yatang.xc.xcr.util.Assert;
import com.yatang.xc.xcr.util.HttpClientUtil;
import com.yatang.xc.xcr.util.TokenUtil;
import com.yatang.xc.xcr.web.StockAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月04日 12:00
 * @Summary :
 */
@Service
public class StockServiceImpl  implements IStockService {

    private @Value("${APPKEY}") String appKey;
    private @Value("${ACCESSKEY}") String accessKey;
    private@Value("${ADDTOSTOCK_URL}")String ADDTOSTOCK_URL;
    private static Logger log = LoggerFactory.getLogger(StockAction.class);

    @Autowired
    private OrgnazitionO2ODubboService o2oDubboService;

    /**
     * @since ： 2.0
     * @param :  BusinessStateDto
     * @return ：  ResultMap
     * @Summary :  营业状态修改v2.0
     */
    @Override
    public ResultMap setShopBusiness(BusinessStateDto model) {
        Assert.isNotEmpty(model);

        StoreO2ODto qd = new StoreO2ODto();
        qd.setO2oStatus(model.getBusinessStatus());
        qd.setId(model.getStoreSerialNo());

        Response<Integer> integerResponse = o2oDubboService.updateStore(qd);
        if (integerResponse == null || !integerResponse.isSuccess()) {
            log.error("The remote method OrgnazitionO2ODubboService.updateStore execute fail, params ==> " + JSON.toJSONString(qd));
            log.error("The remote method OrgnazitionO2ODubboService.updateStore execute fail, result ==> " + JSON.toJSONString(integerResponse));
            return ResultMap.failll("设置营业状态失败，请稍后重试");
        }
        return ResultMap.successu();
    }

    /**
     * @since ： 2.0
     * @param : AutomaticReceiptDto
     * @return ：  ResultMap
     * @Summary : 设置自动接单v2.2
     */
    @Override
    public ResultMap setReciveStatus(AutomaticReceiptDto model) {
        Assert.isNotEmpty(model);

        StoreO2ODto qd = new StoreO2ODto();
        qd.setO2oAutoOrder(model.getReciveStatus());
        qd.setId(model.getStoreSerialNo());

        Response<Integer> integerResponse = o2oDubboService.updateStore(qd);
        if (integerResponse == null || !integerResponse.isSuccess()) {
            log.error("The remote method OrgnazitionO2ODubboService.updateStore execute fail, params ==> " + JSON.toJSONString(qd));
            log.error("The remote method OrgnazitionO2ODubboService.updateStore execute fail, result ==> " + JSON.toJSONString(integerResponse));
            return ResultMap.failll("设置自动接单失败，请稍后重试");
        }
        return ResultMap.successu();
    }

    /**
     * @since ： 2.0
     * @param : ShopAbbreviationDto
     * @return ：  ResultMap
     * @Summary :
     */
    @Override
    public ResultMap setStoreAbbrevy(ShopAbbreviationDto model) {
        Assert.isNotEmpty(model);

        StoreO2ODto qd = new StoreO2ODto();
        qd.setSimpleName(model.getStoreAbbrevy());
        qd.setId(model.getStoreSerialNo());

        Response<Integer> integerResponse = o2oDubboService.updateStore(qd);
        if (integerResponse == null || !integerResponse.isSuccess()) {
            log.error("The remote method OrgnazitionO2ODubboService.updateStore execute fail, params ==> " + JSON.toJSONString(qd));
            log.error("The remote method OrgnazitionO2ODubboService.updateStore execute fail, result ==> " + JSON.toJSONString(integerResponse));
            return ResultMap.failll("设置店铺简称失败，请稍后重试");
        }
        return ResultMap.successu();
    }


    /**
     * 商品入库
     *  @since ： 2.0
     * @Update 2.5.1
     * 门店盘点时入库错误提示，金额数量输入0错误提示：web端修改
     * @param
     * <pre>
     *     msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","GoodsCode":"6915324846590","GoodsCostPrice":"1111","StockPrice":"6666","Num":"6"}
     * </pre>
     */
    @Override
    public ResultMap addToStock(AddToStockDto model) {
    	JSONObject tokenJson=TokenUtil.getTokenFromRedis(model.getUserId());
        String params =
                String.format("appKey=%s&accessKey=%s&alliBusiId=%s&shopCode=%s&itemNum=%s&costPrice=%s&costMoney=%s&inoutPrice=%s&inoutMny=%s&inoutQty=%s",
                        appKey, accessKey, tokenJson.getString("jmsCode"), model.getStoreSerialNo(), model.getGoodsCode(), model.getGoodsCostPrice(), model.getStockPrice(),
                        model.getGoodsCostPrice(), model.getStockPrice(), model.getNum());

        JSONObject jsonResult = HttpClientUtil.okHttpPost(ADDTOSTOCK_URL, params);
        if (jsonResult != null) {
            String responseCode = jsonResult.get("responseCode").toString();
            String errMsg = Assert.toString(jsonResult.get("errMsg"));
            if ("200".equals(responseCode)) {
                return ResultMap.successu();
            }
            if ("103".equals(responseCode)) {
                log.error("[103] Call rest api " + ADDTOSTOCK_URL + " param==> " + params);
                return ResultMap.failll(errMsg);
            }
            if ("100".equals(responseCode)) {
                log.error("[100]  Call rest api " + ADDTOSTOCK_URL + " param==> " + params);
                return ResultMap.failll(errMsg);
            }
        }

        log.error("Call rest api  " + ADDTOSTOCK_URL + " failed");
        log.error("The api params ==> " + params);
        log.error(" The api  result ==> " + JSON.toJSONString(jsonResult));
        return ResultMap.failll("Server exception,wait a moment pls.");
    }
}
