package com.yatang.xc.xcr.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.*;
import com.yatang.xc.xcr.web.interceptor.BuryingPoint;
import org.apache.xerces.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 商品管理相关action
 *
 * @Date 2017年6月1日(星期四)
 */
@Controller
@RequestMapping("/User/")
public class GoodsAction {
    private static Logger log = LoggerFactory.getLogger(GoodsAction.class);
    @Value("${STATE_OK}")
    String STATE_OK;
    @Value("${STATE_ERR}")
    String STATE_ERR;
    @Value("${STATE_SEVEN}")
    String STATE_SEVEN;
    @Value("${INFO_OK}")
    String INFO_OK;
    @Value("${INFO_ERR}")
    String INFO_ERR;

    @Value("${APPKEY}")
    private String appKey;
    @Value("${ACCESSKEY}")
    private String accessKey;
    //添加商品请求接口
    @Value("${ADDGOODS_URL}")
    String ADDGOODS_URL;
    //商品调价请求接口
    @Value("${GOODSPRICEADJUST_URL}")
    String GOODSPRICEADJUST_URL;
    @Value("${UPDATENAMEANDUNIT_URL}")
    String UPDATENAMEANDUNIT_URL;
    //扫条码查询商品信息
    @Value("${SCAN_GOODSDETAIL_URL}")
    String SCAN_GOODSDETAIL_URL;
    //门店导入商品数据
    @Value("${SHOPIMPORTGOODS_URL}")
    String SHOPIMPORTGOODS_URL;
    //获取商品分类列表
    @Value("${CLASSIFYLIST_URL}")
    String CLASSIFYLIST_URL;
    //获取商品分类列表
    @Value("${GOODSLIST_URL}")
    String GOODSLIST_URL;
    //获取商品成本价
    @Value("${COSTPRICE_URL}")
    String COSTPRICE_URL;


    /**
     * http方式，一期接口变更
     * gaodawei
     * 添加商品
     *
     * @param msg
     * @param response
     * @throws IOException msg={"UserId":"jms_901004","StoreSerialNo":"A901004","Token":"1111","GoodsCode":"","GoodsPrice":"","GoodsName":"奥利奥++%","UnitName":"袋","GoodsPrice":"5"}
     */
    @BuryingPoint
    @RequestMapping(value = "AddGoods", method = RequestMethod.POST)
    public void addGoods(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "AddGoods");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        /**
         * flag为M00则向下调用service层接口，否则不调用直接响应
         */
        if (stateJson.getString("State").equals(STATE_OK)) {
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            JSONObject result = null;
            String goodsId = "";
            StringBuffer param = new StringBuffer();
            param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
                    .append(tokenJson.getString("jmsCode")).append("&shopCode=")
                    .append(jsonTemp.getString("StoreSerialNo")).append("&itemNum=")
                    .append(jsonTemp.getString("GoodsCode")).append("&itemDesc=")
                    .append(Base64.encode(jsonTemp.getString("GoodsName").getBytes())).append("&orderUnit=")
                    .append(jsonTemp.getString("UnitName")).append("&guidePrice=")
                    .append(jsonTemp.getString("GoodsPrice")).append("&itemType=").append("1").append("&createUser=")
                    .append(jsonTemp.getString("StoreSerialNo")).append("&createTime=").append(new Date())
                    .append("&packageSize=").append("");
            result = HttpClientUtil.okHttpPost(ADDGOODS_URL, param.toString());
            if (result != null && result.getString("responseCode").equals(INFO_OK)) {
                goodsId = result.getJSONObject("data").getString("goodsId");
            }
            if (result != null && result.getString("responseCode").equals(INFO_OK)) {
                JSONObject subJson = new JSONObject();
                subJson.put("GoodsId", goodsId);
                json.put("Status", stateJson);
                json.put("mapdata", subJson);
            } else if (result != null && result.getString("responseCode").equals(INFO_ERR)) {
                json = CommonUtil.pageStatus(json, "M07", "该商品已存在");
            } else if (result != null && result.getString("responseCode").equals("103")) {
                json = CommonUtil.pageStatus(json, "M03", "名称包含特殊字符");
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * msg={"UserId":"jms_000039","StoreSerialNo":"A000039","Token":"1111","GoodsList":[{"GoodsCode":"6948960100221","GoodsPrice":"2","NewGoodsPrice":"3","GoodsName":"xiaoming","UnitName":"dai","GoodsId":"111"}]}
     * gaodawei,dongshengde
     * 商品调价接口
     * http方式，一期接口变更
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @BuryingPoint
    @RequestMapping(value = "ModifyGoodsPrices", method = RequestMethod.POST)
    public void modifyGoodsPrices(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ModifyGoodsPrices");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.get("State").toString().equals(STATE_OK)) {
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            JSONArray jsonList = (JSONArray) jsonTemp.get("GoodsList");
            JSONArray jsonArr = new JSONArray();
            if (jsonList.size() > 1) {
                for (int i = 0; i < jsonList.size(); i++) {
                    JSONObject subjson = jsonList.getJSONObject(i);
                    JSONObject goodsTempJson = new JSONObject();
                    goodsTempJson.put("itemNum", subjson.getString("GoodsCode"));
                    goodsTempJson.put("adjustType", "01");
                    goodsTempJson.put("orgLevel", "2");
                    goodsTempJson.put("startTime", "");
                    goodsTempJson.put("endTime", "");
                    goodsTempJson.put("createBy", "");
                    goodsTempJson.put("createTime", "");
                    goodsTempJson.put("approveBy", tokenJson.getString("jmsCode"));
                    goodsTempJson.put("approveTime", new Date().toString());
                    goodsTempJson.put("unitCost", subjson.getString("GoodsPrice"));
                    goodsTempJson.put("adjustCost", subjson.getString("NewGoodsPrice"));
                    goodsTempJson.put("itemDesc", subjson.getString("GoodsName"));
                    goodsTempJson.put("orderUnit", subjson.getString("UnitName"));
                    goodsTempJson.put("goodsId", subjson.getString("GoodsId"));
                    if (subjson.getString("ClassifyName") != null) {
                        goodsTempJson.put("itemTypeName", subjson.getString("ClassifyName"));
                    } else {
                        goodsTempJson.put("itemTypeName", "无分类");
                    }
                    jsonArr.add(goodsTempJson);
                }
                JSONObject result = null;
                JSONObject requestJson = new JSONObject();
                requestJson.put("appKey", appKey);
                requestJson.put("accessKey", accessKey);
                requestJson.put("alliBusiId", tokenJson.getString("jmsCode"));
                requestJson.put("shopCode", jsonTemp.getString("StoreSerialNo"));
                requestJson.put("info", jsonArr);
                String param = "data=" + Base64.encode(requestJson.toJSONString().getBytes());
                result = HttpClientUtil.okHttpPost(GOODSPRICEADJUST_URL, param);
                if (result.getString("responseCode").equals(INFO_OK)) {
                    json.put("Status", stateJson);
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, "更改价格失败");
                    log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime"))
                            + "  执行的方法" + jsonTemp.getString("method") + "执行结束！"
                            + "\n**********response to XCR_APP data is:  " + json + "\n**********用时为："
                            + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
                    response.getWriter().print(json);
                }
            } else {
                JSONObject jsonObject = updatePriceAndUnit(jsonTemp, tokenJson.getString("jmsCode"));
                if (jsonObject != null && jsonObject.getString("responseCode").equals("200")) {
                    json.put("Status", stateJson);
                } else if (jsonObject != null && jsonObject.getString("responseCode").equals("103")) {
                    json = CommonUtil.pageStatus(json, "M07", "名称包含特殊字符");
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, "更改名称和单位失败");
                }
            }

        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 商品调整价格和包装单位
     */
    /**
     * @param msg      传过来的门店信息
     * @param response 返回app数据
     * @throws IOException 异常
     * @author dongshengde
     */
    public JSONObject updatePriceAndUnit(JSONObject jsonTemp, String jmsCode) throws IOException {
        JSONObject param = new JSONObject();
        param.put("appKey", appKey);
        param.put("accessKey", accessKey);
        param.put("alliBusiId", jmsCode);
        param.put("shopCode", jsonTemp.getString("StoreSerialNo"));
        param.put("accessKey", accessKey);
        JSONObject subparam = new JSONObject();
        JSONObject jsonObject = jsonTemp.getJSONArray("GoodsList").getJSONObject(0);
        if (jsonObject.get("GoodsId") != null && !jsonObject.get("GoodsId").equals("")) {
            subparam.put("goodsId", jsonObject.get("GoodsId"));
        }
        if (jsonObject.get("GoodsCode") != null && !jsonObject.get("GoodsCode").equals("")) {
            subparam.put("itemNum", jsonObject.get("GoodsCode"));
        }
        if (jsonObject.get("GoodsName") != null && !jsonObject.get("GoodsName").equals("")) {
            subparam.put("itemDesc", jsonObject.get("GoodsName"));
        }
        if (jsonObject.get("NewGoodsPrice") != null && !jsonObject.get("NewGoodsPrice").equals("")) {
            subparam.put("adjustCost", jsonObject.get("NewGoodsPrice"));
        }
        param.put("subparam", subparam);
        Long getSignArrayStartTime = System.currentTimeMillis();
        log.info("\n*****************调用GOODSLIST_URL" + jmsCode + "接口的开始时间："
                + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
                + JSONObject.toJSONString(param));
        String paString = "data=" + URLEncoder.encode(param.toJSONString(), "UTF-8");
        JSONObject jsonResult = HttpClientUtil.okHttpPost(UPDATENAMEANDUNIT_URL, paString);
        log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "调用GOODSLIST_URL"
                + jsonObject.getString("UserId") + "接口   调用结束" + "\n*****************响应数据是：" + jsonResult
                + "\n***************所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));
        return jsonResult;
    }

    /**
     * http方式，一起接口变更
     * <p>
     * 查询成本价：msg={"UserId":"jms_000039","StoreSerialNo":"A000039","Token":"1111","GoodsCode":"6948960100221","CostType":"2"}
     * gaodawei
     * 查询商品内容接口//扫二维码得到商品详情
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @BuryingPoint
    @RequestMapping(value = "GoodsDetial", method = RequestMethod.POST)
    public void goodsDetial(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "GoodsDetial");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {
            //用于接收处理dubbo与http两种方式的数据
            JSONObject result = null;
            String goodsId = "";
            String itemTypeName = "";
            String itemDesc = "";
            String GoodsPrice = "";
            String orderUnit = "";
            String dataSource = "";
            JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            StringBuffer param = new StringBuffer();
            param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
                    .append(tokenJson.getString("jmsCode")).append("&shopCode=")
                    .append(jsonTemp.get("StoreSerialNo").toString()).append("&itemNum=")
                    .append(jsonTemp.get("GoodsCode").toString());
            result = HttpClientUtil.okHttpPost(SCAN_GOODSDETAIL_URL, param.toString());
            if (result != null && result.getString("responseCode").equals(INFO_OK)) {
                goodsId = result.getJSONObject("data").getString("goodsId");
                if (result.getJSONObject("data").getString("itemTypeName") != null) {
                    itemTypeName = result.getJSONObject("data").getString("itemTypeName");
                } else {
                    itemTypeName = "";
                }
                itemDesc = result.getJSONObject("data").getString("itemDesc");
                GoodsPrice = result.getJSONObject("data").getString("adjust_cost");
                orderUnit = result.getJSONObject("data").getString("orderUnit");
                dataSource = result.getJSONObject("data").getString("dataSource");
            }
            Map<String, Object> Goods_map = new HashMap<>();
            JSONObject subJson = new JSONObject();
            if (result.getString("responseCode") == null || result.getString("responseCode").equals(INFO_OK)) {
                Goods_map.put("GoodsId", goodsId);// 这个商品Id还需要门店提供
                Goods_map.put("GoodsStatue", "1");
                Goods_map.put("GoodsName", itemDesc);
                //2017.6.21 19:54 暂时写死GoodsStatue和ClassifyId
                Goods_map.put("ClassifyId", "01");
                Goods_map.put("ClassifyName", itemTypeName);
                Goods_map.put("GoodsPrice", GoodsPrice);
                if (dataSource.equals("0")) {
                    Goods_map.put("UnitName", "");
                } else {
                    if (orderUnit == null || orderUnit.equals("")) {
                        Goods_map.put("UnitName", "个");
                    } else {
                        Goods_map.put("UnitName", orderUnit);
                    }
                }
                Goods_map.put("GoodsCode", jsonTemp.get("GoodsCode"));
                Goods_map.put("DataSource", dataSource);
                if (jsonTemp.getString("CostType") != null) {
                    String string = queryCostPrice(jsonTemp);
                    Goods_map.put("CostPrice", string);
                }
                subJson = StringUtils.replcNULLToStr(Goods_map);
                json.put("mapdata", subJson);
                json.put("Status", stateJson);

                if ("0".equals(dataSource)) {
                    response.addHeader("STATISTICS_QUERY", "needAdd");
                } else if ("1".equals(dataSource)) {
                    response.addHeader("STATISTICS_QUERY", "needImport");
                }
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }

        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 查询成本价
     * 董胜得
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    public String queryCostPrice(JSONObject jsonTemp) throws IOException {
        JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
        StringBuffer param = new StringBuffer();
        param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
                .append(tokenJson.getString("jmsCode")).append("&shopCode=").append(jsonTemp.getString("StoreSerialNo"));
        param.append("&itemNum=").append(jsonTemp.get("GoodsCode")).append("&goodsId=").append("1");//商品id没用，但后台进行了空校验
        if (jsonTemp.getString("CostType").equals("1")) {
            param.append("&type=").append("2");
        } else if (jsonTemp.getString("CostType").equals("2")) {
            param.append("&type=").append("1");
        } else {
            param.append("&type=").append("2");
        }
        JSONObject jsonResult = HttpClientUtil.okHttpPost(COSTPRICE_URL, param.toString());
        String costPrice = null;
        if (jsonResult.getString("responseCode").equals("200")) {
            costPrice = jsonResult.getJSONObject("data").getString("costPrice");
        } else {
            costPrice = "";
        }
        return costPrice;

    }

    /**
     * http方式，一期接口变更
     * gaodawei
     * 从主数据库导入商品进入到门店
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @BuryingPoint
    @RequestMapping(value = "ShopImportGoods", method = RequestMethod.POST)
    public void shopImportGoods(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ShopImportGoods");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        /**
         * flag为M00则向下调用service层接口，否则不调用直接响应
         */
        if (stateJson.getString("State").equals(STATE_OK)) {
            JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
        	StringBuffer param = new StringBuffer();
            param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
                    .append(tokenJson.getString("jmsCode")).append("&shopCode=")
                    .append(jsonTemp.get("StoreSerialNo").toString()).append("&itemNum=")
                    .append(jsonTemp.get("GoodsCode").toString()).append("&goodsId=").append("");
            JSONObject result = HttpClientUtil.okHttpPost(SHOPIMPORTGOODS_URL, param.toString());
            if (result.getString("responseCode").equals(INFO_OK)) {
                String goodsId = result.getJSONObject("data").getString("goodsId");
                String itemTypeName = result.getJSONObject("data").getString("itemTypeName");
                String itemDesc = result.getJSONObject("data").getString("itemDesc");
                String GoodsPrice = result.getJSONObject("data").getString("adjust_cost");
                String orderUnit = result.getJSONObject("data").getString("orderUnit");
                Map<String, Object> mapdata_map = new HashMap<>();
                JSONObject mapdata = new JSONObject();
                mapdata_map.put("GoodsId", goodsId);
                mapdata_map.put("GoodsStatue", itemTypeName);
                mapdata_map.put("GoodsName", itemDesc);
                mapdata_map.put("ClassifyId", "01");
                mapdata_map.put("ClassifyName", "1");
                mapdata_map.put("GoodsPrice", GoodsPrice);
                if (orderUnit == null || orderUnit.equals("")) {
                    mapdata_map.put("UnitName", "个");
                } else {
                    mapdata_map.put("UnitName", orderUnit);
                }
                mapdata_map.put("GoodsCode", jsonTemp.get("GoodsCode"));
                mapdata = StringUtils.replcNULLToStr(mapdata_map);
                json.put("mapdata", mapdata);
                json.put("Status", stateJson);
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, "服务器异常");
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 暂时不做
     * 获得商品的分类
     * msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111"}
     */
    @RequestMapping(value = "ClassifyListCopy", method = RequestMethod.POST)
    public void classifyList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ClassifyList");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {
            JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
        	StringBuffer param = new StringBuffer();
            param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=")
                    .append(tokenJson.getString("jmsCode")).append("&shopCode=")
                    .append(jsonTemp.get("StoreSerialNo").toString());
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
            JSONObject jsonResult = HttpClientUtil.okHttpPost(CLASSIFYLIST_URL, param.toString());
            JSONObject listdata = new JSONObject();
            if (jsonResult == null || !jsonResult.get("responseCode").equals("200")) {
                stateJson.put("State", "M04");
                stateJson.put("StateID", "04");
                stateJson.put("StateValue", "04");
                stateJson.put("StateDesc", "无该分类");
                json.put("Status", stateJson);
            } else {
                JSONArray jsonArray = (JSONArray) jsonResult.get("data");
                for (int k = 0; k < jsonArray.size(); k++) {
                    JSONObject jsonSec = new JSONObject();
                    List<JSONObject> jsonObjects2 = new ArrayList<JSONObject>();
                    for (int i = 0; i < 5; i++) {
                        JSONObject jsonObject2 = new JSONObject();
                        JSONObject js = (JSONObject) jsonArray.get(k);
                        jsonObject2.put("ClassifyId", js.get("ITEMTYPECODE"));
                        jsonObject2.put("ClassifyName", js.get("ITEMTYPENAME"));
                        jsonObjects2.add(jsonObject2);
                    }
                    jsonSec.put("SecondList", jsonObjects2);
                    jsonSec.put("ClassifyFirstId", "111" + k);
                    jsonSec.put("ClassifyFirstName", "firstName" + k);
                    jsonObjects.add(jsonSec);
                }
                listdata.put("rows", jsonObjects);
                json.put("listdata", listdata);
                json.put("Status", stateJson);
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().println(json);
    }

    /**
     * 判断查询内容，如果查询内容为空，则为分类得到的商品列表，如果查询内容不为空，则为搜索得到的商品列表
     * msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","PageIndex":"1","PageSize":"5","ClassifyId":"901404001"}
     *
     * msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","PageIndex":"1","PageSize":"5","Search":"泡椒脆肚"}
     */
    /**
     * 商品列表v1.1
     * gaodawei 于2017年7月5日(星期三)修改
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "GoodsList", method = RequestMethod.POST)
    public void goodsList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "GoodsList");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {
            int start = (jsonTemp.getIntValue("PageIndex") - 1) * jsonTemp.getIntValue("PageSize");
            int end = start + jsonTemp.getIntValue("PageSize");
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            StringBuffer param = new StringBuffer();
            param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey).append("&alliBusiId=").append(tokenJson.getString("jmsCode")).append("&shopCode=").append(jsonTemp.getString("StoreSerialNo")).append("&start=").append(start).append("&end=").append(end);

            /**判断是否传了参数search，如果传了参数，则是根据条码搜索商品*/
            if (jsonTemp.getString("Search") != null && !jsonTemp.getString("Search").equals("")) {
                String search = jsonTemp.getString("Search");
                search = URLEncoder.encode(search, "UTF-8");
                param.append("&search=").append(search);
            }

            /**判断是否传了参数ClassifyId，如果传了参数，则是根据分类id搜索商品*/
            if (jsonTemp.getString("ClassifyId") != null && !jsonTemp.getString("ClassifyId").equals("")) {
                String classifyId = jsonTemp.getString("ClassifyId");
                param.append("&itemTypeCode=").append(classifyId);
            }

            log.info(GOODSLIST_URL+param.toString());
            JSONObject jsonResult = HttpClientUtil.okHttpPost(GOODSLIST_URL, param.toString());
            JSONObject listdata = new JSONObject();
            if (jsonResult != null && jsonResult.getString("responseCode").equals("200")) {
                JSONObject js;
                JSONArray jsonArray;
                try {
                    js = (JSONObject) jsonResult.get("data");
                    jsonArray = (JSONArray) js.get("rows");
                } catch (Exception e) {
                    log.info("response to XCR_APP data is: 方法：GoodsList的返回数据为空" , e);
                    throw new RuntimeException("response to XCR_APP data is: 方法：GoodsList的返回数据为空", e);
                }

                List<JSONObject> goodsReturnClasses = new ArrayList<JSONObject>();
                if (jsonArray != null && jsonArray.size() > 0) {
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jObject = (JSONObject) object;
                        jsonObject.put("GoodsId", StringUtils.replaceNULLToStr(jObject.get("GOODSID")));
                        jsonObject.put("ClassifyId", StringUtils.replaceNULLToStr(jObject.get("SHOPCODE")));
                        jsonObject.put("ClassifyName", StringUtils.replaceNULLToStr(jObject.getString("ITEMTYPENAME")));
                        if (jObject.getString("ORDERUNIT") == null || jObject.getString("ORDERUNIT").equals("")) {
                            jsonObject.put("UnitName", "个 ");
                        } else {
                            jsonObject.put("UnitName", jObject.getString("ORDERUNIT"));
                        }
                        jsonObject.put("GoodsName", StringUtils.replaceNULLToStr(jObject.get("ITEMDESC")));
                        jsonObject.put("GoodsCode", StringUtils.replaceNULLToStr(jObject.get("ITEMNUM")));
                        jsonObject.put("GoodsPrice", StringUtils.replaceNULLToStr(jObject.get("GUIDEPRICE")));
                        jsonObject.put("GoodsStatue", StringUtils.replaceNULLToStr(jObject.get("ITEMTYPE")));
                        goodsReturnClasses.add(jsonObject);
                    }

                    listdata.put("rows", goodsReturnClasses);
                    listdata = CommonUtil.pagePackage(listdata, jsonTemp, js.getIntValue("records"), null);
                    json.put("listdata", listdata);
                    json.put("Status", stateJson);
                } else {
                    json = NoDataClass.addKeyValue();
                }
            } else if (jsonResult != null && jsonResult.getString("responseCode").equals("101")) {//门店查找到的数据个数为0
                json = NoDataClass.addKeyValue();
            } else {
                /**门店返回错误信息*/
                json = CommonUtil.pageStatus(json, STATE_ERR, "获取数据失败");
            }
        } else {
            json.put("Status", stateJson);
        }

        log.debug("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + " response to XCR_APP data is:  " + json + " 用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);

    }

}
