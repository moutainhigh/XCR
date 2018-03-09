package com.yatang.xc.xcr.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.yatang.xc.mbd.biz.org.dto.StoreSettlementInfoDto;
import com.yatang.xc.mbd.biz.org.dubboservice.StoreSettlementInfoDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.HttpsClientUtil;
import com.itbt.common.EncryptUtils;
import com.yatang.xc.mbd.biz.org.dto.FranchiseeDto;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.pojo.Key;
import com.yatang.xc.xcr.service.BankCardService;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DESEncrype;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.StringUtils;
import com.yatang.xc.xcr.util.TokenUtil;

/**
 * @author dongshengde
 * 关于银行卡的类，一个是校验四要素，一个查询加盟商银行的信息
 */
@Controller
@RequestMapping("/User/")
public class BranchBankListAction {
    private static Logger log = LoggerFactory.getLogger(BranchBankListAction.class);

    @Value("${STATE_OK}")
    String STATE_OK;
    @Value("${STATE_ERR}")
    String STATE_ERR;
    @Value("${INFO_OK}")
    String INFO_OK;

    @Value("${ADDBANK_PROTOCOL}")
    private String ADDBANK_PROTOCOL;
    @Value("${BINDINGBANKTEST_URL}")
    String BINDINGBANKTEST_URL;
    @Value("${APPKEY_BANK}")
    private String APPKEY_BANK;
    @Value("${ACCESSKEY_BANK}")
    private String ACCESSKEY_BANK;

    private static final String VALIDATE_ORIGIN = "7";
    private @Autowired
    OrganizationService organizationDubboService;
    private @Autowired
    BankCardService bankCardService;
    private @Autowired
    StoreSettlementInfoDubboService storeSettlementInfoDubboService;


    /**
     * 设置银行卡信息：msg={"UserId":"jms_001000","StoreSerialNo":"A001000","Token":"1111","OpenAccount":"XC050321","Cardholder":"董胜得","ID":"410225198905164693","PhoneNum":"18111649280","BankCardNum":"6226192004946034","BankCardId":"","BankCardName":"中国建行","Province":"sichuan","ProvinceId":"32","City":"chengdu","CityId":"34","BankCardBranch":"nonghangliebiao","BankCardBranchId":"423"}
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "SetBankCardMsg", method = RequestMethod.POST)
    public void setBankCardInfo(String msg,
                                @CookieValue(value = "DeviceId") String DeviceId,
                                @CookieValue(value = "Type") int Type,
                                @CookieValue(value = "AppVersion", required = false) String appVersion,
                                HttpServletResponse response) throws Exception {
        Key key = CommonUtil.getDESKeyFromRedis(DeviceId, Key.class);
        JSONObject json = new JSONObject();
        log.info("\n*******设备id:" + DeviceId + "于接口SetBankCardMsg请求的数据为：" + msg + " DESKey为：" + key.getDesKey());
        String decodeMsg = DESEncrype.decryptDES(msg, key.getDesKey());
        JSONObject jsonTemp = CommonUtil.methodBefore("msg=" + decodeMsg, "SetBankCardMsg");
//		JSONObject jsonTemp = CommonUtil.methodBefore("msg=" + msg, "SetBankCardMsg");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {
            stateJson = bankCardService.setBankCard(jsonTemp, stateJson);
        }
        json.put("Status", stateJson);
        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！\n**********用时为："
                + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()) + "版本号是：" + appVersion);
        if (appVersion != null) {
            log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime"))
                    + "\n**********response to BankCarAction data is after:  " + json);
            response.getWriter().print(json);
        } else {
            log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime"))
                    + "\n**********response to BankCarAction data is2 before:  "
                    + DESEncrype.encryptDES(json.toJSONString(), key.getDesKey()));
            response.getWriter().print(DESEncrype.encryptDES(json.toJSONString(), key.getDesKey()));
        }
    }

    /**
     * gaodawei
     * 校验银行卡添加v2.1.1
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "CheckBankCard", method = RequestMethod.POST)
    public void checkBankCard(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "CheckBankCard");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (jsonTemp.getString("flag").equals(STATE_OK)) {

            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));

            long orgStartTime = System.currentTimeMillis();
            log.info("\n*****************于" + DateUtils.getLogDataTime(orgStartTime, null) + "组织中心接口queryFranchiseeById请求数据为:" + tokenJson.getString("jmsCode"));
            Response<FranchiseeDto> result = organizationDubboService.queryFranchiseeById(tokenJson.getString("jmsCode"));
            log.info("\n*****************于" + DateUtils.getLogDataTime(orgStartTime, null) + "组织中心接口queryFranchiseeById响应数据为:" + JSON.toJSONString(result) + "\n*************花费时间为：" + CommonUtil.costTime(orgStartTime));

            if (result.isSuccess()) {
                JSONObject mapdata;
                Map<String, Object> mapdata_map = new HashMap<>();
                FranchiseeDto franchiseeDto = result.getResultObject();
                String defaultStoreId = franchiseeDto.getDefaultStoreId();
                StoreDto defaultStoreDto = null;
                boolean flag = false;
                if (franchiseeDto != null) {
                    if (franchiseeDto.getFranchiseeName() != null && !"".equals(franchiseeDto.getFranchiseeName())) {
                        if (franchiseeDto.getIdentityCard() != null && !"".equals(franchiseeDto.getIdentityCard())) {
                            flag = true;
                        }
                    }
                }

                if (flag) {
                    if (defaultStoreId == null) {
                        log.info("\n*****************加盟商：" + jsonTemp.getString("LoginId") + "  默认门店信息是：" + ((defaultStoreId == null) ? "为null" : defaultStoreId));
                        json = CommonUtil.pageStatus(json, STATE_ERR, "用户信息异常");
                    } else {
                        for (StoreDto storeDto : franchiseeDto.getStores()) {
                            if (storeDto.getId().equals(defaultStoreId)) {
                                defaultStoreDto = storeDto;
                                break;
                            }
                        }

                        if (defaultStoreDto.getStoreType() == 1) {
                            json = CommonUtil.pageStatus(json, STATE_ERR, "直营店不允许添加银行卡");
                        } else {
                            mapdata_map.put("Cardholder", result.getResultObject().getFranchiseeName());
                            mapdata_map.put("ID", result.getResultObject().getIdentityCard());
                            mapdata_map.put("ServiceAgreeUrl", ADDBANK_PROTOCOL);

                            Response<StoreSettlementInfoDto> storeSerialNo = storeSettlementInfoDubboService.querySimpleStoreSettlementInfoByStoreCode(jsonTemp.getString("StoreSerialNo"));
                            if (storeSerialNo != null && storeSerialNo.getResultObject() != null && storeSerialNo.isSuccess()) {
                                StoreSettlementInfoDto resultObject = storeSerialNo.getResultObject();
                                /**银行卡卡号*/
                                mapdata_map.put("BankCardNum", resultObject.getSettleAccount());
                                /**开户行名称*/
                                mapdata_map.put("BankName", resultObject.getSettleAccountBank());
                                /**省*/
                                mapdata_map.put("Province", resultObject.getSettleAccountBankProvince() );
                                /**市*/
                                mapdata_map.put("City", resultObject.getSettleAccountBankCity() );
                                /**支行名称*/
                                mapdata_map.put("SubBranchName", resultObject.getBankBranchLineName());
                                /**银行卡照片*/
                                mapdata_map.put("BankCardImageUrl", resultObject.getBankImageUrl());
                                /**PhoneNumber*/
                                mapdata_map.put("PhoneNumber", resultObject.getPayeePhone());

                                /**开户行ID*/
                                mapdata_map.put("BankID", resultObject.getSettleAccountBankId());
                                /**支行名ID*/
                                mapdata_map.put("SubBranchID", resultObject.getPayeePhone());
                                /**省份Id*/
                                mapdata_map.put("ProvinceId", resultObject.getPayeePhone());
                                /**城市Id*/
                                mapdata_map.put("CityId", resultObject.getPayeePhone());
                            }

                            mapdata = StringUtils.replcNULLToStr(mapdata_map);
                            json.put("mapdata", mapdata);
                            json.put("Status", stateJson);
                        }
                    }


                }//flag  END
                else {
                    json = CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), "用户信息异常，请联系客服处理");
                }


            } // result.isSuccess()  END
            else {
                json = CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
            }


        }//jsonTemp.getString("flag").equals(STATE_OK) END
        else {
            json.put("Status", stateJson);
        }

        log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
                + jsonTemp.getString("method") + "执行结束！"
                + "\n**********response to XCR_APP data is:  " + json
                + "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }

    /**
     * @author dongshengde
     * 删除一账通验证的数据,给测试使用，UAT,PRD不上线
     * msg={"UserId":"jms_001000","StoreSerialNo":"A001000","Token":"dsd","OpenAccount":"loye66","BankCardNum":"6212264402030721942"}
     */
    @RequestMapping(value = "deleteBankData", method = RequestMethod.POST)
    public void deleteData(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "CheckBankCard");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (jsonTemp.getString("Token").equals("sit")) {
            String result = null;
            Map<String, String> params = getParams();
            params.put("origin", VALIDATE_ORIGIN);
            params.put("method", "userBankCardAction.deleteUserBankCardInfo");
            params.put("appSecret", ACCESSKEY_BANK);
            params.put("userName", jsonTemp.getString("OpenAccount"));
            params.put("accountNO", jsonTemp.getString("BankCardNum"));
            String sign = EncryptUtils.sign(params, ACCESSKEY_BANK);
            params.put("sign", sign);
            Long getSignArrayStartTime = System.currentTimeMillis();
            log.info("\n******调用BINDINGBANKTEST_URL接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
                    + "\n******请求用户" + jsonTemp.getString("UserId")
                    + "\n******请求数据是：" + JSONObject.toJSONString(params.toString()));
            result = HttpsClientUtil.sendHttpsPost(BINDINGBANKTEST_URL, params);
            log.info("\n*******于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "调用BINDINGBANKTEST_URL接口   调用结束"
                    + "\n*******用户是：" + jsonTemp.getString("UserId")
                    + "\n*******响应数据是：" + JSONObject.toJSONString(result));
            JSONObject res = JSONObject.parseObject(result);
            if (res.getString("code").equals("0")) {
                json = CommonUtil.pageStatus(json, "M00", "删除成功");
            } else if (res.getString("code").equals("1")) {
                json = CommonUtil.pageStatus(json, "M03", "不存在删除的数据");
            } else {
                json = CommonUtil.pageStatus(json, "M02", "删除失败");
            }
        } else {
            json.put("Status", stateJson);
        }
        response.getWriter().print(json.toString());
    }


    /**
     * 校验银行卡需要的公共信息
     * dongshengde
     *
     * @return
     */
    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("url", BINDINGBANKTEST_URL);
        params.put("v", "1.0");
        params.put("format", "json");
        params.put("appKey", APPKEY_BANK);
        params.put("timestamp", DateUtils.dateDefaultFormat(new Date()));
        return params;
    }

}
