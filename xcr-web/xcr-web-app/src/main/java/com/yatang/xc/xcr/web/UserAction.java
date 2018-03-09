package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import com.yatang.xc.mbd.biz.org.dto.*;
import com.yatang.xc.xcr.biz.core.dubboservice.ScanPaymentRegionSetDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.DES;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseFinanceByShopAndDateDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseFinanceByShopAndDateWithXcOrderDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.dubboservice.StoreSettlementInfoDubboService;
import com.yatang.xc.mbd.org.es.dubboservice.OrganzitionXCRBusinessIndexDubboService;
import com.yatang.xc.mbd.org.es.xcr.dto.SettlementBusinessNumberDto;
import com.yatang.xc.oles.biz.business.dto.ElecContractDTO;
import com.yatang.xc.oles.biz.business.dubboservice.ContractVerificationDubboService;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dto.SupplyAdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgQueryDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService;
import com.yatang.xc.xcr.biz.mission.dto.manage.UserDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.service.QRCodeService;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.EncryptUtils;
import com.yatang.xc.xcr.util.HttpClientUtil;
import com.yatang.xc.xcr.util.MD5;
import com.yatang.xc.xcr.util.SignUtils;
import com.yatang.xc.xcr.util.StringUtils;
import com.yatang.xc.xcr.util.TokenUtil;
import com.yatang.xc.xcr.web.interceptor.BuryingPoint;
import com.yatang.xcsm.remote.api.dto.PushPopularizeDTO;
import com.yatang.xcsm.remote.api.dubboxservice.PushPopularizeDubboxService;

@Controller
@RequestMapping("/User/")
public class UserAction {
    private static Logger log = LoggerFactory.getLogger(UserAction.class);
    @Autowired
    private RedisService<JSONObject> redisJsonService;
    @Autowired
    private OrganizationService organizationDubboService;
    @Autowired
    private OrgnazitionO2ODubboService orgnazitionO2ODubboService;
    @Autowired
    private MissionExecuteDubboService missionExecuteDubboService;
    @Autowired
    private AdvertisementDubboService advertisementDubboService;
    @Autowired
    private MsgQueryDubboService msgQueryDubboService;
    @Autowired
    private StoreSettlementInfoDubboService storeSettlementInfoDubboService;
    @Autowired
    private DataCenterXcrDubboService dataCenterXcrDubboService;
    @Autowired
    private SupplyChainAdvertisementDubboService scDubboService;
    @Autowired
    private PushPopularizeDubboxService pushPopularizeDubboxService;
    @Autowired
    private ContractVerificationDubboService contractVerificationDubboService;
    @Autowired
    private ApplyJoinXCAction applyJoinXCAction;
    @Autowired
    private ScanPaymentRegionSetDubboService scanPaymentRegionSetDubboService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private OrganzitionXCRBusinessIndexDubboService organzitionXCRBusinessIndexDubboService;

    @Value("${TOKEN_OUTTIME}")
    private Integer TOKEN_OUTTIME;
    @Value("${SYSTEM_CODE}")
    private String SYSTEM_CODE;
    @Value("${STATE_OK}")
    String STATE_OK;
    @Value("${STATE_OUTDATE}")
    String STATE_OUTDATE;
    @Value("${STATE_ERR}")
    String STATE_ERR;
    @Value("${INFO_OK}")
    String INFO_OK;
    // 电商加密用的key
    @Value("${YATANG_SIGNKEY}")
    String YATANG_SIGNKEY;
    // 供应链地址
    @Value("${YATANG_URL}")
    String YATANG_URL;
    // pos机的产品Id
    @Value("${POS_PRODUCT_ID}")
    String POS_PRODUCT_ID;
    // 活动链接
    @Value("${ADVERTIS_URL}")
    String ADVERTIS_URL;
    // 活动图片
    @Value("${ADVERTIS_PIC}")
    String ADVERTIS_PIC;
    // 图片Url前缀
    @Value("${URL_PREFIX}")
    String URL_PREFIX;
    @Value("${CONTRACT_URL}")
    String CONTRACT_URL;
    @Value("${RECEIPT_CODE_URL}")
    String RECEIPT_CODE_URL;

    //小超人域名修改之后在线聊天请求接口
    @Value("${DOMAIN_AFTER_MODIFY_LIVE_CHAT}")
    private String DOMAIN_AFTER_MODIFY_LIVE_CHAT;
    //小超人在线联系客服界面右侧显示连接
    @Value("${XCR_LIVE_CHAT_RIGHT}")
    private String XCR_LIVE_CHAT_RIGHT;
    //小超人客服分组ID
    @Value("${XC_CUSTOMER_SERVICE_ID}")
    private String XC_CUSTOMER_SERVICE_ID;

    @Value("${SIGN_KEY}")
    String SIGN_KEY; //签名秘钥
    @Value("${DES_KEY}")
    String DES_KEY;  //DES秘钥


    /**
     * 店面主页数据接口
     *
     * @param msg
     * @param response
     * @throws Exception msg={"UserId":"jms_630111","StoreSerialNo":"A630111","Token":"82c43a8e"}
     *                   鐘潤更改
     */
    @BuryingPoint
    @RequestMapping(value = "SignIn", method = RequestMethod.POST)
    public void signIn(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SignIn");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject mapJson = new JSONObject();
        JSONObject json = new JSONObject();
        JSONObject list = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            long dcStartTime = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = simpleDateFormat.format(new Date()).toString();
            log.info("\n******于时间" + DateUtils.getLogDataTime(dcStartTime, null) + "开始调用数据中心queryFinanceByShopAndDate+"
                    + "queryFinanceByShopAndDateWithXcOrder接口请求数据为:" + jsonTemp.getString("StoreSerialNo") + "日期:" + currentDate);
            Response<ResponseFinanceByShopAndDateDto> definedTimeDubboResult = dataCenterXcrDubboService.queryFinanceByShopAndDate(
                    jsonTemp.getString("StoreSerialNo"), currentDate, currentDate);
            Response<ResponseFinanceByShopAndDateWithXcOrderDto> orderdefinedTimeDubboResult = dataCenterXcrDubboService.queryFinanceByShopAndDateWithXcOrder(
                    jsonTemp.getString("StoreSerialNo"), currentDate, currentDate);
            log.info("\n******于时间" + DateUtils.getLogDataTime(dcStartTime, null) + "结束调用数据中心queryFinanceByShopAndDate接口响应数据为:"
                    + JSONObject.toJSONString(definedTimeDubboResult) + "结束调用数据中心queryFinanceByShopAndDateWithXcOrder接口响应数据为:"
                    + JSONObject.toJSONString(orderdefinedTimeDubboResult)
                    + "\n*******耗时为:" + CommonUtil.costTime(dcStartTime));
            //修改的今日营收
            Double totalMny = modifyTodayIncome(response, json,
                    definedTimeDubboResult, orderdefinedTimeDubboResult);
            DecimalFormat df = new DecimalFormat(".##");
            mapJson.put("RevenueNum", df.format(totalMny));
            //调用接口,100代表首页的广告
            Response<List<AdvertisementDTO>> response2 = advertisementDubboService.findstate(100);
            List<AdvertisementDTO> advertisementDTOs = response2.getResultObject();
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
            for (AdvertisementDTO advertisementDTO : advertisementDTOs) {
                JSONObject advertisement = new JSONObject();
                advertisement.put("AdvertisTitle", advertisementDTO.getPicName());
                advertisement.put("AdvertisUrl", advertisementDTO.getActivityUrl());
                advertisement.put("AdvertisPic", advertisementDTO.getPicUrl());
                jsonObjects.add(advertisement);
            }
            /*********************兼容旧版本代码**************************/
            list.put("rows", jsonObjects);
            json.put("listdata", list);
            /**********************兼容代码到此结束*************************/
            /**
             * @author gaodawei
             * @Date 2017年09月07日 上午10:28
             * @version 1.0.0
             * @function 供应链商品展示
             */
            long crStartTime = System.currentTimeMillis();
            log.info("\n*****于时间" + DateUtils.getLogDataTime(crStartTime, null) + "开始调用core服务接口findSupplyChainPublishAdver");
            Response<List<SupplyAdvertisementGroupDTO>> result = scDubboService.findSupplyChainPublishAdver();
            log.info("\n*****于时间" + DateUtils.getLogDataTime(crStartTime, null) + "响应数据是：" + JSONObject.toJSONString(result));
            log.info("\n*****于时间:" + DateUtils.getLogDataTime(crStartTime, null) + "调用core服务接口findSupplyChainPublishAdver接口   调用结束"
                    + "\n*****所花费时间为：" + CommonUtil.costTime(crStartTime));
            List<JSONObject> firstList = new ArrayList<JSONObject>();
            if (result.isSuccess()) {
                for (int i = 0; i < result.getResultObject().size(); i++) {
                    Map<String, Object> first = new HashMap<>();
                    List<JSONObject> secondList = new ArrayList<JSONObject>();
                    List<AdvertisementDTO> subAdsDtos = result.getResultObject().get(i).getAdvertisementPOs();
                    if (subAdsDtos != null) {
                        for (int j = 0; j < subAdsDtos.size(); j++) {
                            Map<String, Object> secondMap = new HashMap<>();
                            secondMap.put("AdId", subAdsDtos.get(j).getSore());
                            secondMap.put("AdPic", subAdsDtos.get(j).getPicUrl());
                            secondMap.put("AdJump", subAdsDtos.get(j).getActivityUrl());
                            secondList.add(StringUtils.replcNULLToStr(secondMap));
                        }
                    }
                    first.put("ShortcutFirstName", result.getResultObject().get(i).getGroupName());
                    first.put("ShortcutSecondList", secondList);
                    firstList.add(StringUtils.replcNULLToStr(first));
                }
            } else {
                stateJson = CommonUtil.pageStatus2(STATE_ERR, StateEnum.STATE_2.getDesc());
            }
            mapJson.put("ShortcutFirstList", firstList);
            mapJson.put("AdvertisList", JSONObject.parseArray(jsonObjects.toString()));
            /*********************供应链商品展示结束**********************/
            json.put("mapdata", mapJson);
        }
        json.put("Status", stateJson);
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！\n**********用时为："
                + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime())
                + "\n**********response to XCR_APP data is:  " + json);
        response.getWriter().print(json);
    }

    private Double modifyTodayIncome(
            HttpServletResponse response,
            JSONObject json,
            Response<ResponseFinanceByShopAndDateDto> definedTimeDubboResult,
            Response<ResponseFinanceByShopAndDateWithXcOrderDto> orderdefinedTimeDubboResult)
            throws IOException {
        if (!definedTimeDubboResult.isSuccess()) {
            log.info("调用数据中心queryFinanceByShopAndDate返回失败");
            json.put("Status", CommonUtil.pageStatus2("M02", "系统错误"));
            response.getWriter().print(json);
        }
        if (!orderdefinedTimeDubboResult.isSuccess()) {
            log.info("调用数据中心queryFinanceByShopAndDateWithXcOrder返回失败");
            json.put("Status", CommonUtil.pageStatus2("M02", "系统错误"));
            response.getWriter().print(json);
        }
        Double mny1 = (double) 0;
        Double mny2 = (double) 0;
        if (definedTimeDubboResult.getResultObject() != null) {
            mny1 = definedTimeDubboResult.getResultObject().getIncome();
        }
        if (orderdefinedTimeDubboResult.getResultObject() != null) {
            mny2 = orderdefinedTimeDubboResult.getResultObject().getTotalMny();
        }
        Double totalMny = mny1 + mny2;
        return totalMny;
    }

    /**
     * 店面主页数据接口2
     *
     * @param msg
     * @param response
     * @throws Exception msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111"}
     *                   SELECT * FROM YT_XCR_TRAIN_INFO ORDER WHERE STATUS='1' BY ID DESC LIMIT 1
     *                   董胜得更改
     */
   /* @RequestMapping(value = "SignIn2", method = RequestMethod.POST)
    public void signIn2(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SignIn2");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            JSONObject mapJson = new JSONObject();
            MissionExecuteQueryDto queryDto = new MissionExecuteQueryDto();
            queryDto.setMerchantId(jsonTemp.getString("StoreSerialNo"));
            queryDto.setStatus(EnumMissionExecuteStatus.STATUS_INIT.getCode());
            long qmStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(qmStartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto.toString());
            Response<Integer> res1 = missionExecuteDubboService.queryMissionExecuteCount(queryDto);
            log.info("\n******于时间" + DateUtils.getLogDataTime(qmStartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto.toString()
                    + "\n******耗时为:" + CommonUtil.costTime(qmStartTime));
            // 任务审核中的任务数目
            MissionExecuteQueryDto queryDto1 = new MissionExecuteQueryDto();
            queryDto1.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
            queryDto1.setStatus(EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode());
            long qm2StartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(qm2StartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto1.toString());
            Response<Integer> res2 = missionExecuteDubboService.queryMissionExecuteCount(queryDto1);
            log.info("\n******于时间" + DateUtils.getLogDataTime(qm2StartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto1.toString()
                    + "\n******耗时为:" + CommonUtil.costTime(qm2StartTime));
            // 未完成任务的数目
            MissionExecuteQueryDto queryDto2 = new MissionExecuteQueryDto();
            queryDto2.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
            queryDto2.setStatus(EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode());
            long qm3StartTime = System.currentTimeMillis();
            log.info("\n*****于时间" + DateUtils.getLogDataTime(qm3StartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto2.toString());
            Response<Integer> res3 = missionExecuteDubboService.queryMissionExecuteCount(queryDto2);
            log.info("\n*****于时间" + DateUtils.getLogDataTime(qm3StartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto2.toString()
                    + "\n******耗时为:" + CommonUtil.costTime(qm3StartTime));
            // 几条完成状态的任务数目
            MissionExecuteQueryDto queryDto3 = new MissionExecuteQueryDto();
            queryDto3.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
            queryDto3.setStatus(EnumMissionExecuteStatus.STATUS_FINISHED.getCode());
            long qm4StartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(qm4StartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto3.toString());
            Response<Integer> res4 = missionExecuteDubboService.queryMissionExecuteCount(queryDto3);
            log.info("\n******于时间" + DateUtils.getLogDataTime(qm4StartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto3.toString()
                    + "\n******耗时为:" + CommonUtil.costTime(qm4StartTime));
            // 提示几条未做的任务
            int taskNum = res1.getResultObject() + res2.getResultObject() + res3.getResultObject()
                    + res4.getResultObject();
            // Response<Integer> resMsg = msgQueryDubboService.getMsgCount();
            mapJson.put("TaskNum", String.valueOf(taskNum));
            *//**
     * 添加课堂发布时间
     *//*
            Response<Long> result = trainQueryDubboService.queryMaxClassReleaseTime();
            if (result != null && result.isSuccess()) {
                Long i = result.getResultObject();
                mapJson.put("MaxClassTime", i);

            } else {
                stateJson = CommonUtil.pageStatus2(STATE_ERR, StateEnum.STATE_2.getDesc());
            }
            json.put("mapdata", mapJson);
            json.put("Status", stateJson);
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }*/

    /**
     * 门店列表
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "StoreList", method = RequestMethod.POST)
    public void storeList(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "StoreList");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            long orgStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(orgStartTime, null) + "开始调用组织中心接口getAllianceBusinessById的数据是：" + tokenJson.getString("jmsCode"));
            Response<FranchiseeDto> res = organizationDubboService.queryFranchiseeById(tokenJson.getString("jmsCode"));
            log.info("\n******于时间" + DateUtils.getLogDataTime(orgStartTime, null) + "结束调用组织中心接口getAllianceBusinessById的响应数据是："
                    + JSONObject.toJSONString(res.getResultObject())
                    + "\n******耗时为:" + CommonUtil.costTime(orgStartTime));
            if (tokenJson.getBooleanValue("isF5Account")) {
                if (tokenJson.getBooleanValue("isFranchiser")) {
                    json = packStoreInfo(res, jsonTemp, stateJson, false);
                } else {
                    json = packStoreInfo(res, jsonTemp, stateJson, true);
                }
            } else {
                json = packStoreInfo(res, jsonTemp, stateJson, false);
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    public JSONObject packStoreInfo(Response<FranchiseeDto> res, JSONObject jsonTemp, JSONObject stateJson, boolean commonAccount) {
        JSONObject json = new JSONObject();
        JSONArray storeList = new JSONArray();
        JSONObject listdata = new JSONObject();
        String defaultStoreNo = jsonTemp.getString("StoreSerialNo");
        if (res.getCode().equals(INFO_OK)) {
            List<StoreDto> Stores = res.getResultObject().getStores();
            for (StoreDto store : Stores) {
                //过滤关闭门店
                if (store.getStoreTurnOn() == 1) {
                    continue;
                }
                if (commonAccount && !defaultStoreNo.equals(store.getId())) {
                    continue;
                }

                JSONObject storeJson = new JSONObject();
                storeJson.put("StoreSerialNo", store.getCode());
                storeJson.put("StoreName", store.getName());
                storeJson.put("StoreAddress", store.getAddress());
                // ==> Version 2.4 Start
                storeJson.put("StoreNo", store.getOrderCode() == null ? "No." : store.getOrderCode());
                storeJson.put("StoreContact", store.getContact());
                storeJson.put("StorePhone", store.getMobilePhone());
                String codeUrl = qrCodeService.getQRCodeUrl(jsonTemp.getString("StoreSerialNo"));
                storeJson.put("StoreCodeUrl", codeUrl);
                storeJson.put("StoreAbbreName", store.getSimpleName());
                storeJson.put("IsSupportTakeOut", store.getO2oFlag());
                storeJson.put("StoreLocation", store.getDistrictName());
                storeJson.put("ProvinceName", store.getProvinceName());
                storeJson.put("CityName", store.getCityName());
                // <== Version 2.4 End
                // ==> Version 2.5 Start
                String storeNo = store.getOrderCode() == null ? "No." : store.getOrderCode();
                log.info("Call getPayAmountByXcnumber Request Data Is:" + storeNo.substring(3, storeNo.length()));
                Response<Double> moneyDubboResult = contractVerificationDubboService.getPayAmountByXcnumber(storeNo.substring(3, storeNo.length()));
                log.info("Call getPayAmountByXcnumber Response Data Is:" + JSONObject.toJSONString(moneyDubboResult));
                if (!moneyDubboResult.isSuccess()) {
                    log.error("Call getPayAmountByXcnumber Failed!!");
                }
                Double mo = moneyDubboResult.getResultObject();
                if (mo == null) {
                    mo = 0.0;
                }
                storeJson.put("JoinBond", mo);
                storeJson.put("JoinContractUrl", "");
                if (store.getStoreType() != 1) {
                    String storeno = transferStoreNo(storeNo);
                    Map<String, Object> map = applyJoinXCAction.handleContractList(storeno, 2, "");
                    if (!CollectionUtils.isEmpty(map)) {
                        List<ElecContractDTO> contractList = (List<ElecContractDTO>) map.get("list");
                        if (!CollectionUtils.isEmpty(contractList)) {
                            storeJson.put("JoinContractUrl", CONTRACT_URL + "?MarketNo=" + storeno);
                        }
                    }
                }
                // <== Version 2.5 End
                // == >Version 2.5.1 Start
                Response<SettlementBusinessNumberDto> organzitionXCRBusinessIndexDubboServiceDubboResultResponse = organzitionXCRBusinessIndexDubboService.queryStoreSettlementBusiness(store.getCode());
                log.info("Call organzitionXCRBusinessIndexDubboService Request Data Is:" + store.getCode() + "And Response Data Is:" + JSONObject.toJSONString(organzitionXCRBusinessIndexDubboServiceDubboResultResponse));
                if (organzitionXCRBusinessIndexDubboServiceDubboResultResponse == null || !organzitionXCRBusinessIndexDubboServiceDubboResultResponse.isSuccess()) {
                    log.debug("Call queryStoreSettlementBusiness Return False!!");
                }
                String BusNum = null;
                if (organzitionXCRBusinessIndexDubboServiceDubboResultResponse != null) {
                    if (organzitionXCRBusinessIndexDubboServiceDubboResultResponse.getResultObject() != null) {
                        BusNum = organzitionXCRBusinessIndexDubboServiceDubboResultResponse.getResultObject().getBusinessNumber();
                    } else {
                        log.debug("Select StoreBusNo Is Null");
                    }
                }
                storeJson.put("StorePayUrl", getPayUrl(BusNum, store.getCode()));
                storeJson.put("StoreType", store.getStoreType());
                //2.5.1新增店铺身份二维码加密串
                storeJson.put("StoreIdentityCode", getStoreIdentityCode(store));
                // <== Version 2.5.1 End
                storeList.add(storeJson);
            }
            listdata.put("rows", storeList);
            listdata.put("totalcount", storeList.size());

            long startTime = System.currentTimeMillis();
            log.info("\n*********调用querySimpleStoreSettlementInfoByStoreCode接口的开始时间：" + DateUtils.getLogDataTime(startTime, null) + "\n*****************请求数据是：shopNo=" + jsonTemp.getString("StoreSerialNo"));
            Response<StoreSettlementInfoDto> result = storeSettlementInfoDubboService.querySimpleStoreSettlementInfoByStoreCode(jsonTemp.getString("StoreSerialNo"));
            log.info("\n*****************于时间:" + DateUtils.getLogDataTime(startTime, null) + "调用querySimpleStoreSettlementInfoByStoreCode接口   调用结束" + "\n*****************响应数据是：" + JSONObject.toJSONString(result) + "\n***************所花费时间为：" + CommonUtil.costTime(startTime));

            JSONObject mapdata = new JSONObject();
            Map<String, Object> mapdata_map = new HashMap<>();
            if (result.isSuccess()) {
                if (result.getResultObject() != null) {
                    mapdata_map.put("BankCardNum", result.getResultObject().getSettleAccount());
                    mapdata_map.put("BankCardName", result.getResultObject().getSettleAccountBank());
                    /**
                     * @since  2.7.1
                     * 银行卡校验状态
                     * */
                    mapdata_map.put("BankCardVerifyType", result.getResultObject().getIsAudit());
                } else {
                    mapdata_map.put("BankCardNum", "");
                    mapdata_map.put("BankCardName", "");
                    /**
                     * @since  2.7.1
                     * 银行卡校验状态
                     * */
                    mapdata_map.put("BankCardVerifyType", 0);
                }
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
            mapdata = StringUtils.replcNULLToStr(mapdata_map);
            json.put("listdata", listdata);
            json.put("mapdata", mapdata);
            json.put("Status", stateJson);
        } else {
            json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
        }
        return json;
    }

    private Object getPayUrl(String busNum, String shopCode) {
        log.info("门店编号获取的商户号为：store.getCode():" + shopCode + "  BusNum:" + busNum);
        if (StringUtils.isEmpty(busNum)) {
            return "";
        }
        //根据门店编号获取地区
        String provinceId = getProvinceIdByShopCode(shopCode);
        //判断该省份是否启用
        Response<Boolean> isShow = scanPaymentRegionSetDubboService.checkIsShow(provinceId);
        if (isShow != null && isShow.isSuccess()) {
            return RECEIPT_CODE_URL + busNum + "&userId=" + busNum + "&client=platform";
        }
        return "";
    }

    /**
     * 根据门店编号获取所在省份编号
     *
     * @param shopCode
     * @return
     */
    private String getProvinceIdByShopCode(String shopCode) {
        Response<StoreMainDto> storeMainDtoResponse = organizationDubboService.queryMainStoreById(shopCode);
        if (storeMainDtoResponse == null || !storeMainDtoResponse.isSuccess()) {
            return "";
        }
        return storeMainDtoResponse.getResultObject().getProvinceId();
    }

    /**
     * 店铺身份二维码加密串生成
     *
     * @param store
     * @return
     */
    private String getStoreIdentityCode(StoreDto store) {
        log.info("店铺身份二维码 -> 店铺信息 -> store:" + JSONObject.toJSONString(store));
        String shopCode = store.getCode();
        String shopName = store.getSimpleName();
        String deliveryWarehouseCode = store.getDeliveryWarehouseCode();
        SortedMap<String, String> sortedMap = getParamsMap(shopCode, shopName, deliveryWarehouseCode, SIGN_KEY);
        String json = JSON.toJSONString(sortedMap);
        try {
            return DES.encryptDES(json, DES_KEY);
        } catch (Exception e) {
            log.error("店铺身份二维码加密串生成 -> 加密异常 -> json:" + json);
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 组装加密参数
     *
     * @param shopCode
     * @param shopName
     * @param key
     * @return
     */
    private static SortedMap<String, String> getParamsMap(String shopCode, String shopName, String deliveryWarehouseCode, String key) {
        SortedMap<String, String> map = new TreeMap();
        map.put("shopCode", shopCode);
        map.put("shopName", shopName);
//        map.put("deliveryWarehouseCode", deliveryWarehouseCode);  //不使用该参数
        map.put("sign", getSign(map, key));
        return map;
    }

    /**
     * 组装签名
     *
     * @param map
     * @param key
     * @return
     */
    private static String getSign(SortedMap<String, String> map, String key) {
        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        return MD5.sign(preStr, "&key=" + key, "utf-8");
    }

    /**
     * <门店编号截取>
     *
     * @param storeNo
     * @return
     */
    private String transferStoreNo(String storeNo) {
        String storeno = "";
        try {
            storeno = storeNo.substring(3, storeNo.length());
        } catch (Exception e) {
            log.error("StoreNo Error And StoreNo = " + storeNo);
        }
        return storeno;
    }

    /**
     * 设置默认门店
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "SetStoreDefault", method = RequestMethod.POST)
    public void setStoreDefault(@CookieValue(value = "DeviceId") String DeviceId, @RequestBody String msg,
                                HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SetStoreDefault");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            long orgStartTime = System.currentTimeMillis();
            log.info("\n******于时间" + DateUtils.getLogDataTime(orgStartTime, null) + "开始调用组织中心接口restDefaultConvenientStore的请求数据是："
                    + jsonTemp.getString("StoreSerialNo"));
            Response<Integer> res = organizationDubboService.setDefaultStore(jsonTemp.getString("StoreSerialNo"));
            log.info("\n******于时间" + DateUtils.getLogDataTime(orgStartTime, null) + "结束调用组织中心接口getAllianceBusinessById的响应数据是："
                    + JSONObject.toJSONString(res)
                    + "\n******耗时为:" + CommonUtil.costTime(orgStartTime));
            if (res.getResultObject() == 1) {
                JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
                String key = SYSTEM_CODE + tokenJson.getString("jmsCode");
                JSONObject dataJson = CommonUtil.getRedisUserInfo(redisJsonService, key, organizationDubboService, tokenJson.getString("jmsCode"), SYSTEM_CODE, TOKEN_OUTTIME);
                UserDto userDto = new UserDto();
                userDto.setRegistrationId(DeviceId);// 设备号
                userDto.setUserName(dataJson.getString("loginId"));// 小超账号
                userDto.setLogin(dataJson.getString("ytAccount"));// 一账通账号
                userDto.setMerchantId(jsonTemp.getString("StoreSerialNo"));// 门店编号
                userDto.setRealName(dataJson.getString("ytAccount"));// 真实姓名
                userDto.setCompanyId(dataJson.getString("subCompanyId"));// 分公司ID
                userDto.setShopName(jsonTemp.getString("StoreName"));// name
                long imStartTime = System.currentTimeMillis();
                log.info("\n******于时间" + DateUtils.getLogDataTime(imStartTime, null) + "开始调用任务dubbo接口initMissionExecuteByMerchantId的请求数据是："
                        + JSONObject.toJSONString(userDto));
                Response<Boolean> resMission = missionExecuteDubboService.initMissionExecuteByMerchantId(userDto);
                log.info("\n******于时间" + DateUtils.getLogDataTime(imStartTime, null) + "结束调用任务dubbo接口initMissionExecuteByMerchantId的响应数据是："
                        + JSONObject.toJSONString(resMission)
                        + "\n******耗时为:" + CommonUtil.costTime(imStartTime));
                json = CommonUtil.pageStatus(json, STATE_OK, "正常");
                if (!resMission.isSuccess()) {
                    json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
                }
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 消息
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "MsgList", method = RequestMethod.POST)
    public void msgList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "MsgList");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        /**
         * flag为M00则向下调用service层接口，否则不调用直接响应
         */
        JSONObject json = new JSONObject();
        JSONObject listData = new JSONObject();
        JSONArray rowsList = new JSONArray();
        if (stateJson.getString("State").equals(STATE_OK)) {
            // 调用service层接口
            MsgPushDTO msgPushDTO = new MsgPushDTO();
            msgPushDTO.setStatus("1");// 取已经发布的消息
            String shopCode = jsonTemp.getString("StoreSerialNo"); //传入门店编号，过滤定向推送消息
            int pageIndex = jsonTemp.getIntValue("PageIndex");
            int pageSize = jsonTemp.getIntValue("PageSize");
            String provinceId = getProvinceIdByShopCode(shopCode);
            log.info("根据门店编号获取省份编号 -> provinceId:" + provinceId);
            Response<Map<String, Object>> res = msgQueryDubboService.getMsgList(msgPushDTO, pageIndex, pageSize,
                    shopCode, provinceId);
            if (res.isSuccess()) {
                Map<String, Object> map = res.getResultObject();
                for (MsgPushDTO msgDTO : (List<MsgPushDTO>) map.get("data")) {
                    JSONObject listJson = new JSONObject();
                    listJson.put("MsgId", msgDTO.getId().toString());
                    listJson.put("MsgName", msgDTO.getTitle());
                    listJson.put("MsgPic", msgDTO.getImageUrl());
                    listJson.put("MsgUrl", msgDTO.getMsgUrl());
                    listJson.put("MsgTime", msgDTO.getModifyTime().getTime());
                    listJson.put("MsgStatue", msgDTO.getType());
                    if (msgDTO.getType().equals("1")) {
                        listJson.put("MsgDetail", msgDTO.getContentHtml());
                    } else {
                        listJson.put("MsgDetail", "");
                    }
                    rowsList.add(listJson);
                }
                listData = CommonUtil.pagePackage(listData, jsonTemp,
                        Integer.valueOf(res.getResultObject().get("total").toString()), null);
                listData.put("rows", rowsList);
                json.put("listdata", listData);
                json.put("Status", stateJson);
            } else {
                json = CommonUtil.pageStatus(json, STATE_OK, StateEnum.STATE_0.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 雅堂采购接口
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "PurchaseList", method = RequestMethod.POST)
    public void purchaseList(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "PurchaseList");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        JSONObject mapJson = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            // 在redis中获取一账通号
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            String key = SYSTEM_CODE + tokenJson.getString("jmsCode");
            JSONObject dataJson = CommonUtil.getRedisUserInfo(redisJsonService, key, organizationDubboService, tokenJson.getString("jmsCode"), SYSTEM_CODE, TOKEN_OUTTIME);
            String loginAccount = dataJson.getString("ytAccount");
            loginAccount = EncryptUtils.chinaToUnicode(loginAccount);
            String signAccount = EncryptUtils.YLPWDEncrypt(loginAccount, YATANG_SIGNKEY);
            String url = YATANG_URL + signAccount;
            mapJson.put("Url", url);
            json.put("mapdata", mapJson);
        }
        json.put("Status", stateJson);
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 联系客服v2.0
     * gaodawei
     * 2017年7月3日(星期一)
     *
     * @param msg
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "ConnectCustomerSer", method = RequestMethod.POST)
    public void connectCustomerSer(@RequestBody String msg, HttpServletResponse response) throws IOException {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "ConnectCustomerSer");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
            Long startTime = System.currentTimeMillis();
            log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
                    + "开始调用组织中心接口:queryLiveGroupIdByStoreId \n***********请求数据是：" + jsonTemp.getString("StoreSerialNo"));
            Response<BranchCompanyDto> result = orgnazitionO2ODubboService
                    .queryLiveGroupIdByStoreId(jsonTemp.getString("StoreSerialNo"));
            log.info("\n***********于时间：" + DateUtils.getLogDataTime(startTime, null)
                    + "调用组织中心接口：queryLiveGroupIdByStoreId 调用结束 " + "\n***********响应数据是：："
                    + JSONObject.toJSONString(result) + "\n**********耗时为：" + CommonUtil.costTime(startTime));
            //用于请求放回联系客服连接
            if (result.isSuccess()) {
                JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
                Map<String, Object> param_map = new HashMap<>();
                param_map.put("userId", tokenJson.getString("jmsCode"));
                param_map.put("loginName", jsonTemp.getString("UserName").concat("_").concat(jsonTemp.getString("StoreSerialNo")));
                param_map.put("name", jsonTemp.getString("UserName").concat("_").concat(jsonTemp.getString("StoreSerialNo")));
                param_map.put("isUserLoggedIn", true);
                param_map.put("rightSidePageUrl", XCR_LIVE_CHAT_RIGHT);
                param_map.put("transferSkillId", XC_CUSTOMER_SERVICE_ID);
                param_map.put("skillId", com.yatang.xc.xcr.util.StringUtils.replaceNULLToStr(JSONObject
                        .parseObject(JSONObject.toJSONString(result.getResultObject())).getString("liveGroupId")));
                JSONObject CCSUrl_json = HttpClientUtil.okHttpPostJson(DOMAIN_AFTER_MODIFY_LIVE_CHAT,
                        com.yatang.xc.xcr.util.StringUtils.replcNULLToStr(param_map).toString());
                Map<String, Object> mapdata_map = new HashMap<>();
                if (CCSUrl_json != null) {
                    mapdata_map.put("CCSUrl", CCSUrl_json.getString("redirectUrl"));
                    json.put("mapdata", com.yatang.xc.xcr.util.StringUtils.replcNULLToStr(mapdata_map));
                    json.put("Status", stateJson);
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
                }
            } else {
                json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
            }
        } else {
            json.put("Status", stateJson);
        }
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * 门店扫码统计接口
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "RecomeStatistics", method = RequestMethod.POST)
    public void settlementManageList(@RequestBody String msg,
                                     HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "RecomeStatistics");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {

            PushPopularizeDTO pushPopularizeDTO = recomeStatisticsJson2DTO(jsonTemp);
            Long startTime = System.currentTimeMillis();
            log.info("\n*****Start At：" + DateUtils.getLogDataTime(startTime, null) + "Call queryPopularizeCountByShopCode Interface"
                    + "queryPopularizeCountByShopCode Request Data is:" + JSONObject.toJSONString(pushPopularizeDTO));
            com.yatang.xcsm.common.response.Response<PushPopularizeDTO> dubboResult = pushPopularizeDubboxService.queryPopularizeCountByShopCode(pushPopularizeDTO);
            Long endTime = System.currentTimeMillis();
            log.debug("Call queryPopularizeCountByShopCode Last_" + (endTime - startTime) + "_Time" + "queryPopularizeCountByShopCode Response Data is:" + JSONObject.toJSONString(dubboResult));
            if (!dubboResult.isSuccess()) {
                log.error("Call queryPopularizeCountByShopCode Failed!!");
                json.put("Status", CommonUtil.pageStatus2("M02", "系统错误"));
                response.getWriter().print(json);
            }
            JSONObject datamap = recomeStatisticsDTO2Json(dubboResult);
            json.put("mapdata", datamap);
        }
        json.put("Status", stateJson);
        response.getWriter().print(json);
    }

    /**
     * <结果转换>
     *
     * @param dubboResult
     * @return
     */
    private JSONObject recomeStatisticsDTO2Json(com.yatang.xcsm.common.response.Response<PushPopularizeDTO> dubboResult) {
        JSONObject datamap = new JSONObject();
        PushPopularizeDTO popularizeDTO = dubboResult.getResultObject();
        if (popularizeDTO == null) {
            datamap.put("PublicAttentionToday", 0);
            datamap.put("PublicAttentionYesterday", 0);
            datamap.put("PublicAttentionAll", 0);
            datamap.put("RegisterToday", 0);
            datamap.put("RegisterYesterday", 0);
            datamap.put("RegisterAll", 0);
            datamap.put("OrderToday", 0);
            datamap.put("OrderYesterday", 0);
            datamap.put("OrderAll", 0);
            return datamap;
        } else {
            datamap.put("PublicAttentionToday", popularizeDTO.getTodayFollowNum());
            datamap.put("PublicAttentionYesterday", popularizeDTO.getYesterdayFollowNum());
            datamap.put("PublicAttentionAll", popularizeDTO.getAllFollowNum());
            datamap.put("RegisterToday", popularizeDTO.getTodayRegisterNum());
            datamap.put("RegisterYesterday", popularizeDTO.getYesterdayRegisterNum());
            datamap.put("RegisterAll", popularizeDTO.getAllRegisterNum());
            datamap.put("OrderToday", popularizeDTO.getTodayPorderUserNum());
            datamap.put("OrderYesterday", popularizeDTO.getYesterdayPorderUserNum());
            datamap.put("OrderAll", popularizeDTO.getAllPorderUserNum());
            return datamap;
        }
    }

    /**
     * <查询条件转换>
     *
     * @param jsonTemp
     * @return
     */
    private PushPopularizeDTO recomeStatisticsJson2DTO(JSONObject jsonTemp) {
        PushPopularizeDTO pushPopularizeDTO = new PushPopularizeDTO();
        pushPopularizeDTO.setShopCode(jsonTemp.getString("StoreSerialNo"));
        return pushPopularizeDTO;
    }

    @ResponseBody
    @RequestMapping(value = "testAA", method = RequestMethod.POST)
    public void testAA(String msg, HttpServletResponse response) throws Exception {
        System.out.println("\n********msg:" + msg);
        String json = "{\"UserId\":\"jms_001436\",\"StoreSerialNo\":\"A001436\",\"Token\":\"高大伟顶顶顶顶\",\"GoodsCode\":\"10101205211\",\"tryBymbd\":true}";
        response.getWriter().print(json);
    }

    @ResponseBody
    @RequestMapping(value = "testBB", method = RequestMethod.POST)
    public String testBB(String msg, HttpServletResponse response) throws Exception {
        System.out.println("\n********msg:" + msg);
        String json = "{\"UserId\":\"jms_001436\",\"StoreSerialNo\":\"A001436\",\"Token\":\"高大伟顶顶顶顶\",\"GoodsCode\":\"10101205211\",\"tryBymbd\":true}";
        return json;
    }
}
