package com.yatang.xc.xcr.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.prod.dubboservice.ProductXCRDubboService;
import com.yatang.xc.mbd.biz.prod.dubboservice.dto.CategoryDto;
import com.yatang.xc.rp.dubboservice.XcrO2oProductDubboService;
import com.yatang.xc.rp.dubboservice.common.Paging;
import com.yatang.xc.rp.dubboservice.dto.QueryInDTO;
import com.yatang.xc.rp.dubboservice.dto.QueryOutDTO;
import com.yatang.xc.rp.dubboservice.dto.xcr.PrdBatchShelves;
import com.yatang.xc.rp.dubboservice.dto.xcr.PrdQueryByNameOrItemNumDTO;
import com.yatang.xc.rp.dubboservice.dto.xcr.XcrO2oProductDTO;
import com.yatang.xc.xcr.annotations.Payload;
import com.yatang.xc.xcr.annotations.SessionToken;
import com.yatang.xc.xcr.dto.inputs.GoodsOutListDto;
import com.yatang.xc.xcr.dto.inputs.NewArrivalsDto;
import com.yatang.xc.xcr.dto.inputs.SpecialGoodsOutListDto;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IOutGoodsService;
import com.yatang.xc.xcr.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 外送商品订单
 *
 * @author dongshengde
 */
@Controller
@RequestMapping("/User/")
public class OutGoodsAction {

    private @Value("${STATE_OK}") String STATE_OK;
    private @Value("${OUTGOODSURL_PREFIX}") String imageUrl;
    private static Logger log = LoggerFactory.getLogger(OutGoodsAction.class);

    @Autowired private IOutGoodsService goodsService;
    @Autowired private XcrO2oProductDubboService xcrProductDubboService;
    @Autowired private ProductXCRDubboService productXCRDubboService;

    ///<Summary>
    ///   外送商品新品推荐
    ///</Summary>
    @SessionToken
    @RequestMapping(value = "NewArrivals", method = RequestMethod.POST)
    public ResultMap newArrivals(@Payload NewArrivalsDto dto) {
        return goodsService.newArrivals(dto);
    }


    ///<Summary>
    ///   预留接口
    ///   超级会员专属商品查询
    ///</Summary>
    @SessionToken
    @RequestMapping(value = "超级会员专属商品查询", method = RequestMethod.POST)
    public ResultMap svip(@Payload GoodsOutListDto dto) {
    	return goodsService.querySvipExclusiveProducts();
    }


    ///<Summary>
    ///   外送商品特殊列表
    ///</Summary>
    @SessionToken
    @RequestMapping(value = "SpecialGoodsOutList", method = RequestMethod.POST)
    public ResultMap specialGoodsOutList(@Payload SpecialGoodsOutListDto dto){
    	return goodsService.specialGoodsOutList(dto);
    }

    /**
     * 外送商品列表
     * @param msg
     * @param response
     * @throws IOException
     * 根据上下架状态查询商品：msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","PageIndex":"1","PageSize":"5","FrameType":"1"}
     * 根据分类id查询商品：msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","PageIndex":"1","PageSize":"5","FrameType":"1","ClassifyId":""}
     * 根据Search属性查询商品：msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","PageIndex":"1","PageSize":"5","Search":""}
     */
    @RequestMapping(value = "GoodsOutList", method = RequestMethod.POST)
    public void goodsOutList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "GoodsOutList");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {
            int start = jsonTemp.getIntValue("PageIndex");
            int end = jsonTemp.getIntValue("PageSize");
            List<JSONObject> goodsReturnClasses = new ArrayList<JSONObject>();
            JSONObject listdata = new JSONObject();

            Paging page = new Paging(start, end);
            QueryInDTO queryInDTO = new QueryInDTO();
            queryInDTO.setPaging(page);

            if (jsonTemp.getString("Search") != null && !jsonTemp.getString("Search").equals("")) {
                PrdQueryByNameOrItemNumDTO prdQueryByNameOrItemNumDTO = new PrdQueryByNameOrItemNumDTO(jsonTemp.getString("StoreSerialNo"));
                Boolean flag = Assert.isNumeric(jsonTemp.getString("Search"));
                Response<QueryOutDTO<XcrO2oProductDTO>> res = null;
                Long startTime = System.currentTimeMillis();
                log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)+ "开始调用OutGoodsAction.ClassifyList接口" + "\n***********请求数据为："+ JSONObject.toJSONString(queryInDTO));
                if (flag) {
                    prdQueryByNameOrItemNumDTO.setItemNum(jsonTemp.getString("Search"));
                    queryInDTO.setParams(prdQueryByNameOrItemNumDTO);
                    res = xcrProductDubboService.queryProductsByNameOrItemNum(queryInDTO);
                    if (res.getResultObject().getRecords() == null) {
                        prdQueryByNameOrItemNumDTO.setItemDesc(jsonTemp.getString("Search"));
                        prdQueryByNameOrItemNumDTO.setItemNum(null);
                        queryInDTO.setParams(prdQueryByNameOrItemNumDTO);
                        res = xcrProductDubboService.queryProductsByNameOrItemNum(queryInDTO);
                    }
                } else {
                    prdQueryByNameOrItemNumDTO.setItemDesc(jsonTemp.getString("Search"));
                    queryInDTO.setParams(prdQueryByNameOrItemNumDTO);
                    res = xcrProductDubboService.queryProductsByNameOrItemNum(queryInDTO);

                }
                json = resultResolveMethod(json, stateJson, goodsReturnClasses, listdata, startTime, res);
            } else if (jsonTemp.getString("FrameType") != null && !jsonTemp.getString("FrameType").equals("")) {
            	XcrO2oProductDTO xcrProductDTO = new XcrO2oProductDTO();
                xcrProductDTO.setShopCode(jsonTemp.getString("StoreSerialNo"));
                if (jsonTemp.getString("ClassifyId") != null && !jsonTemp.getString("ClassifyId").equals("")) {
                    xcrProductDTO.setThirdLevelCategoryId(jsonTemp.getString("ClassifyId"));
                }
                xcrProductDTO.setO2oOnShelves(jsonTemp.getString("FrameType"));
                queryInDTO.setParams(xcrProductDTO);
                Long startTime2 = System.currentTimeMillis();
                log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime2, null)+ "开始调用订单接口:OutGoodsAction.GoodsOutList\n***********请求数据为："+ JSONObject.toJSONString(queryInDTO));
                Response<QueryOutDTO<XcrO2oProductDTO>> res = xcrProductDubboService.queryProductsByXCRProduct(queryInDTO);
                json = resultResolveMethod(json, stateJson, goodsReturnClasses, listdata, startTime2, res);
            }
        } else {
            json.put("Status", stateJson);
        }
        log.debug("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }


    /**
     * 外送商品详情
     * <method description>
     *
     * @param msg
     * @param response
     * @throws Exception http://xcdev.yatang.com.cn:80/xcr-web-app/User/GoodsOutDetial.htm
     *                   msg={"Token":"aaa","UserId":"111111","StoreSerialNo":"A000305","GoodsCode":"6901028120692"}
     */
    @RequestMapping("GoodsOutDetial")
    public void goodsOutDetial(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "GoodsOutDetial");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {
            QueryInDTO queryInDTO = goodsOutDetialMsgJson2QueryDTO(jsonTemp);
            Long xcrqStartTime = System.currentTimeMillis();
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "开始调用queryProductsByNameOrItemNum接口的" + "\n*******请求数据是：" + JSONObject.toJSONString(queryInDTO));
            Response<QueryOutDTO<XcrO2oProductDTO>> dubboResponse = xcrProductDubboService.queryProductsByNameOrItemNum(queryInDTO);
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "结束调用queryProductsByNameOrItemNum接口" + "\n*******响应数据是：" + JSONObject.toJSONString(dubboResponse) + "\n*******耗时为:" + CommonUtil.costTime(xcrqStartTime));
            JSONObject resultJson = dubboResult2MapData(dubboResponse);
            json.put("mapdata", resultJson);
        }
        json.put("Status", stateJson);
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 商品批量上下架
     * <method description>
     * http://xcdev.yatang.com.cn:80/xcr-web-app/User/ModifyGoodsFrameType.htm
     * msg={"Token":"aaa","UserId":"111111","StoreSerialNo":"A000305","FrameType":"1","GoodsList":[{"GoodsCode":6921413225610},{"GoodsCode":6901028137270}]}
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping("ModifyGoodsFrameType")
    public void modifyGoodsFrameType(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ModifyGoodsFrameType");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            List<XcrO2oProductDTO> products = msg2modifyGoodsFrameTypeQuerryDTO(jsonTemp);

            Long xcrqStartTime = System.currentTimeMillis();
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "开始调用updateProducts接口" + "\n*******请求数据是：" + JSONObject.toJSONString(products));
            Response<Boolean> dubboResult = xcrProductDubboService.updateProducts(products);
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "结束调用updateProducts接口" + "\n*******响应数据是：" + JSONObject.toJSONString(dubboResult) + "\n*******耗时为:" + CommonUtil.costTime(xcrqStartTime));
            boolean isSuccess = dubboResult.isSuccess();
            if (isSuccess) {
                json.put("Status", stateJson);
            } else {
                json.put("Status", CommonUtil.pageStatus2("M02", "上下架失败"));
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 封装msg查询信息
     * <method description>
     *
     * @param jsonTemp
     * @return
     */
    private List<XcrO2oProductDTO> msg2modifyGoodsFrameTypeQuerryDTO(JSONObject jsonTemp) {
        String storeSerialNo = jsonTemp.get("StoreSerialNo").toString();
        String frameType = jsonTemp.get("FrameType").toString();
        JSONArray jsonArray = jsonTemp.getJSONArray("GoodsList");
        List<XcrO2oProductDTO> products = new ArrayList<>();
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject ob = (JSONObject) iterator.next();
            XcrO2oProductDTO product = new XcrO2oProductDTO();
            product.setShopCode(storeSerialNo);
            product.setItemNum(ob.getString("GoodsCode"));
            product.setO2oOnShelves(frameType);
            products.add(product);
        }
        return products;
    }

    /**
     * 外送商品调价
     * <method description>
     * http://xcdev.yatang.com.cn:80/xcr-web-app/User/ModifyOutGoodsPrice.htm
     * msg={"Token":"aaa","UserId":"111111","StoreSerialNo":"A000305","GoodsCode":6901028137270,"NewGoodsPrice":"5000"}
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping("ModifyOutGoodsPrice")
    public void modifyOutGoodsPrice(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ModifyOutGoodsPrice");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            List<XcrO2oProductDTO> xcrProductDTOs = modifyOutGoodsPriceMsg2QueryDTO(jsonTemp);

            Long xcrqStartTime = System.currentTimeMillis();
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "开始调用updateProducts接口" + "\n*******请求数据是：" + JSONObject.toJSONString(xcrProductDTOs));
            Response<Boolean> dubboResult = xcrProductDubboService.updateProducts(xcrProductDTOs);
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "结束调用updateProducts接口" + "\n*******响应数据是：" + JSONObject.toJSONString(dubboResult) + "\n*******耗时为:" + CommonUtil.costTime(xcrqStartTime));
            boolean isSuccess = dubboResult.isSuccess();
            if (isSuccess) {
                json.put("Status", stateJson);
            } else {
                json.put("Status", CommonUtil.pageStatus2("M02", "调价操作失败"));
            }

        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 查询条件封装
     * <method description>
     *
     * @param jsonTemp
     * @return
     */
    private List<XcrO2oProductDTO> modifyOutGoodsPriceMsg2QueryDTO(JSONObject jsonTemp) {
        String storeSerialNo = jsonTemp.getString("StoreSerialNo");
        String newGoodsPrice = jsonTemp.getString("NewGoodsPrice");
        String goodsCode = jsonTemp.getString("GoodsCode");
        String costPrice = jsonTemp.getString("CostPrice") == null ? "" : jsonTemp.getString("CostPrice");
        Integer innerQuantity = jsonTemp.getInteger("SVIPCastNum");
        Double vipPrice = jsonTemp.getDouble("SVIPGoodsPrcie");
        List<XcrO2oProductDTO> xcrProductDTOs = new ArrayList<XcrO2oProductDTO>();
        XcrO2oProductDTO xcrProductDTO = new XcrO2oProductDTO();
        xcrProductDTO.setShopCode(storeSerialNo);
        xcrProductDTO.setItemNum(goodsCode);
        xcrProductDTO.setCostPrice(costPrice);
        xcrProductDTO.setAdjustCost(newGoodsPrice);
        //==>Version 2.7.0 VIPStatus
        if(innerQuantity!=null){
        	xcrProductDTO.setO2oVipInnerQuantity(innerQuantity);
        }
        if(vipPrice!=null){
        	xcrProductDTO.setO2oVipPrice(vipPrice);
        }
        //<==Version 2.7.0
        xcrProductDTOs.add(xcrProductDTO);
        return xcrProductDTOs;
    }

    /**
     * 分类全部上下架商品
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping("ModifyAllGoodsFrameType")
    public void modifyAllGoodsFrameType(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ModifyAllGoodsFrameType");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {

            PrdBatchShelves arg0 = modifyAllGoodsFrameTypeJson2DTO(jsonTemp);

            Long xcrqStartTime = System.currentTimeMillis();
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "开始调用xcrProductDubboService.batchShelves接口" + "\n*******请求数据是：" + JSONObject.toJSONString(arg0));
            Response<Boolean> dubboResult = xcrProductDubboService.batchShelves(arg0);
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "结束调用xcrProductDubboService.batchShelves接口" + "\n*******响应数据是：" + JSONObject.toJSONString(dubboResult) + "\n*******耗时为:" + CommonUtil.costTime(xcrqStartTime));
            if(dubboResult!=null){
            	if (dubboResult.isSuccess()) {
                    json.put("Status", stateJson);
                } else {
                    json.put("Status", CommonUtil.pageStatus2("M02", "分类全部上下架失败！"));
                    log.info("Call xcrProductDubboService.batchShelves Failed");
                }
            }else{
            	json=CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 查询条件转换成dubbo DTO
     *
     * @param jsonTemp
     * @return
     */
    private PrdBatchShelves modifyAllGoodsFrameTypeJson2DTO(JSONObject jsonTemp) {
        String storeNo = jsonTemp.getString("StoreSerialNo");
        String classifyId = jsonTemp.getString("ClassifyId");
        String frameType = jsonTemp.getString("FrameType");
        PrdBatchShelves arg0 = new PrdBatchShelves();
        arg0.setShopCode(storeNo);
        arg0.setO2oOnShelves(frameType);
        if (!classifyId.isEmpty()) {
            arg0.setThirdLevelCategoryId(classifyId);
        }
        return arg0;
    }

    /**
     * 分类列表
     *
     * @param msg
     * @param response
     * @throws IOException msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111"}
     */
    @RequestMapping(value = "ClassifyList", method = RequestMethod.POST)
    public void classifyList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ClassifyList");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {

            //从后台调用方法获取返回值,此处可以传入分类id，调用其子分类，只需要分类id替换参数空字符串
            Long xcrqStartTime = System.currentTimeMillis();
            log.info("\n*******于时间" + DateUtils.getLogDataTime(xcrqStartTime, null) + "开始调用queryCategoryFisrtAndThird接口" + "\n*******请求数据是：无请求参数");
            Response<?> response2 = productXCRDubboService.queryCategoryFisrtAndThird();

            JSONObject listdata = new JSONObject();
            if (response2 != null && response2.getCode().equals("200") && response2.getResultObject() != null) {
                @SuppressWarnings("unchecked")
                ArrayList<CategoryDto> arrayList = (ArrayList<CategoryDto>) response2.getResultObject();
                List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
                for (CategoryDto categoryDto : arrayList) {
                    JSONObject jsonSec = new JSONObject();
                    jsonSec.put("ClassifyFirstId", StringUtils.replaceNULLToStr(categoryDto.getId()));
                    jsonSec.put("ClassifyFirstName", StringUtils.replaceNULLToStr(categoryDto.getCategoryName()));
                    List<JSONObject> jsonObjects2 = new ArrayList<JSONObject>();
                    if (categoryDto.getChildCategories() != null) {
                        ArrayList<CategoryDto> categoryDtos = (ArrayList<CategoryDto>) categoryDto.getChildCategories();
                        for (CategoryDto categoryDto2 : categoryDtos) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("ClassifyId", StringUtils.replaceNULLToStr(categoryDto2.getId()));
                            jsonObject.put("ClassifyName", StringUtils.replaceNULLToStr(categoryDto2.getCategoryName()));
                            jsonObject.put("ClassifyPic", imageUrl + StringUtils.replaceNULLToStr(categoryDto2.getIconImg()));
                            jsonObjects2.add(jsonObject);
                        }
                        jsonSec.put("SecondList", jsonObjects2);
                    }
                    jsonObjects.add(jsonSec);
                }
                listdata.put("rows", jsonObjects);
                listdata.put("pageindex", "");
                listdata.put("pagesize", "");
                listdata.put("totalpage", "");
                listdata.put("totalcount", "");
                json.put("listdata", listdata);
                json.put("Status", stateJson);
            } else if (response2 != null && response2.getCode().equals("200") && response2.getResultObject() != null) {
                json = NoDataClass.addKeyValue();
            }
        } else {
            json.put("Status", stateJson);
        }
        log.debug("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 查询条件转换
     * <method description>
     *
     * @param jsonTemp
     * @return
     */
    private QueryInDTO goodsOutDetialMsgJson2QueryDTO(JSONObject jsonTemp) {
        String storeSerialNo = jsonTemp.get("StoreSerialNo").toString();
        String goodsCode = jsonTemp.get("GoodsCode").toString();
        QueryInDTO queryInDTO = new QueryInDTO();
        PrdQueryByNameOrItemNumDTO paramsDTO = new PrdQueryByNameOrItemNumDTO(storeSerialNo);
        paramsDTO.setItemNum(goodsCode);
        queryInDTO.setParams(paramsDTO);
        return queryInDTO;
    }

    /**
     * dubbo结果封装
     *
     * @param dubboResponse
     * @return
     */
    private JSONObject dubboResult2MapData(Response<QueryOutDTO<XcrO2oProductDTO>> dubboResponse) {
        List<XcrO2oProductDTO> xcrProductDTOs = dubboResponse.getResultObject().getRecords();
        JSONObject resultJson = new JSONObject();
        //List Include One Info Defualt!
        if (xcrProductDTOs == null || xcrProductDTOs.isEmpty()) {
            return resultJson;
        }
        XcrO2oProductDTO xcrProductDTO = xcrProductDTOs.get(0);
        resultJson.put("GoodsOutStatue", xcrProductDTO.getProductType());
        resultJson.put("GoodsName", xcrProductDTO.getItemDesc());

        String classifyThirdName = transferClassifyName2Json(xcrProductDTO);
        String classifyFirstName = transferFirstName(xcrProductDTO);
        String classifySecName = transferSecName(xcrProductDTO);

        resultJson.put("ClassifyFirstName", classifyFirstName);
        resultJson.put("ClassifySecName", classifySecName);
        resultJson.put("ClassifyThirdName", classifyThirdName);
        resultJson.put("GoodsPrice", xcrProductDTO.getAdjustCost());
        resultJson.put("UnitName", xcrProductDTO.getUnit());
        resultJson.put("GoodsCode", xcrProductDTO.getItemNum());
        resultJson.put("FrameType", xcrProductDTO.getO2oOnShelves());
        resultJson.put("GoodsCostPrice", xcrProductDTO.getCostPrice());
        //==>Version 2.7.0 Start VIP Status
        resultJson.put("SVIPCastNum", xcrProductDTO.getO2oVipInnerQuantity()==null?"":xcrProductDTO.getO2oVipInnerQuantity());
        resultJson.put("SVIPGoodsPrcie", xcrProductDTO.getO2oVipPrice()==null?"":PriceUtil.formatPrice(xcrProductDTO.getO2oVipPrice()));
        //<==Version 2.7.0 End

        /**【外送商品】编辑商品-新品推荐设置、超级会员商品标签显示：web端开*/
        resultJson.put("IsNewArrivals", Assert.bool2Int(xcrProductDTO.getO2oIsRecommend()));
        resultJson.put("IsSuperVip", Assert.bool2Int(xcrProductDTO.getSvipExclusive()));
        return resultJson;
    }

    private String transferSecName(XcrO2oProductDTO xcrProductDTO) {
        String classifySecName = xcrProductDTO.getSecondLevelCategoryName() != null ? xcrProductDTO.getSecondLevelCategoryName() : "";
        return classifySecName;
    }

    private String transferFirstName(XcrO2oProductDTO xcrProductDTO) {
        String classifyFirstName = xcrProductDTO.getFirstLevelCategoryName() != null ? xcrProductDTO.getFirstLevelCategoryName() : "";
        return classifyFirstName;
    }

    /**
     * 商品分类封装
     * <method description>
     *
     * @param xcrProductDTO
     * @return
     */
    private String transferClassifyName2Json(XcrO2oProductDTO xcrProductDTO) {
        return xcrProductDTO.getThirdLevelCategoryName() != null ? xcrProductDTO.getThirdLevelCategoryName() : "";
    }

    /**
     * 提取方法
     *
     * @param json
     * @param stateJson
     * @param goodsReturnClasses
     * @param listdata
     * @param startTime
     * @param res
     * @return
     */
    private JSONObject resultResolveMethod(JSONObject json, JSONObject stateJson, List<JSONObject> goodsReturnClasses,
                                           JSONObject listdata, Long startTime, Response<QueryOutDTO<XcrO2oProductDTO>> res) {
        if (res != null && res.getCode().equals("200") && res.getResultObject().getRecords() != null) {
            goodsCategoryList(json, stateJson, goodsReturnClasses, listdata, res);
        } else if (res != null && res.getCode().equals("200") && res.getResultObject().getRecords() == null) {
            json = NoDataClass.addKeyValue();
        } else {
            json = CommonUtil.pageStatus(json, "M02", "获取列表失败");
        }
        return json;
    }

    /**
     * 抽取方法
     *
     * @param json
     * @param stateJson
     * @param goodsReturnClasses
     * @param listdata
     * @param res
     */
    private void goodsCategoryList(JSONObject json, JSONObject stateJson, List<JSONObject> goodsReturnClasses, JSONObject listdata, Response<QueryOutDTO<XcrO2oProductDTO>> res) {
        QueryOutDTO<XcrO2oProductDTO> queryOutDTO = res.getResultObject();
        Paging paging = queryOutDTO.getPaging();
        List<XcrO2oProductDTO> list = queryOutDTO.getRecords();
        log.info("图片url：" + imageUrl);
        for (XcrO2oProductDTO xcrProductDTO : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("GoodsName", StringUtils.replaceNULLToStr(xcrProductDTO.getItemDesc()));
            String gPrice = xcrProductDTO.getAdjustCost();
            if (gPrice != null && !gPrice.equals("")) {
                BigDecimal goodsP = new BigDecimal(gPrice);
                jsonObject.put("GoodsPrice", StringUtils.replaceNULLToStr(goodsP));
            } else {
                jsonObject.put("GoodsPrice", StringUtils.replaceNULLToStr(xcrProductDTO.getAdjustCost()));
            }
            jsonObject.put("GoodsCode", StringUtils.replaceNULLToStr(xcrProductDTO.getItemNum()));
            jsonObject.put("UnitName", StringUtils.replaceNULLToStr(xcrProductDTO.getUnit()));

            jsonObject.put("GoodsPic", imageUrl + StringUtils.replaceNULLToStr(xcrProductDTO.getThumbnailImage()));
            log.info("外送商品图片链接地址：" + jsonObject.getString("GoodsPic"));
            goodsReturnClasses.add(jsonObject);
        }
        listdata.put("rows", goodsReturnClasses);
        listdata.put("pageindex", paging.getPageNum());
        listdata.put("pagesize", paging.getPageSize());
        listdata.put("totalpage", paging.getTotalPage());
        listdata.put("totalcount", paging.getTotalSize());
        //					listdata = CommonUtil.pagePackage(listdata, jsonTemp, js.getIntValue("records"), null);
        json.put("listdata", listdata);
        json.put("Status", stateJson);
    }
}
