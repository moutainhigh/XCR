package com.yatang.xc.xcr.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.FranchiseeDto;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.o2o.dto.RegistrationParameterDto;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberInfoDto;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberLevelDto;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberInfoDubboService;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberLevelDubboService;
import com.yatang.xc.pos.cloud.dto.ResponseDTO;
import com.yatang.xc.pos.cloud.dubboservice.XcrUserDubboService;
import com.yatang.xc.xcr.annotations.Payload;
import com.yatang.xc.xcr.biz.core.dto.BranchBankDTO;
import com.yatang.xc.xcr.biz.core.dto.ResultBranchBankDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.BranchBankListDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.F5WhiteListDubboService;
import com.yatang.xc.xcr.biz.mission.dto.manage.UserDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.dto.inputs.PostDesKey;
import com.yatang.xc.xcr.enums.RedisKeyEnum;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.pojo.Key;
import com.yatang.xc.xcr.pojo.UserInfoVo;
import com.yatang.xc.xcr.util.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/System/")
public class EntranceAction {

    private static Logger log = LoggerFactory.getLogger(EntranceAction.class);

    @Autowired
    private RedisService<Key> redisService;
    @Autowired
    private OrganizationService organizationDubboService;
    @Autowired
    private RedisService<JSONObject> redisJsonService;
    @Autowired
    private MissionExecuteDubboService missionExecuteDubboService;
    @Autowired
    private OrgnazitionO2ODubboService orgnazitionO2ODubboService;
    @Autowired
    private BranchBankListDubboService branchBankListDubboService;
    @Autowired
    private MemberLevelDubboService memberLevelDubboService;
    @Autowired
    private MemberInfoDubboService memberInfoDubboService;
    @Autowired
    private XcrUserDubboService xcrUserDubboService;
    @Autowired
    private F5WhiteListDubboService xcrWhiteListDubboService;


    private
    @Value("${APPKEY}")
    String appKey;
    private
    @Value("${INFO_OK}")
    String INFO_OK;
    private
    @Value("${STATE_OK}")
    String STATE_OK;
    private
    @Value("${ACCESSKEY}")
    String accessKey;
    private
    @Value("${STATE_ERR}")
    String STATE_ERR;
    private
    @Value("${SYSTEM_CODE}")
    String SYSTEM_CODE;
    private
    @Value("${TOKEN_OUTTIME}")
    Integer TOKEN_OUTTIME;
    /**
     * 用户已解约CODE
     */
    private
    @Value("${USER_TERMINATE}")
    String USER_TERMINATE;
    /**
     * 验证用户接口
     */
    private
    @Value("${CHECKACCOUNT_URL}")
    String CHECKACCOUNT_URL;


    /**
     * @param : [appVersion, DeviceId, Type, msg, request, response]
     * @return ：  void
     * @Summary :  登录功能
     * @since ： 2.6.0
     */
    @RequestMapping(value = "Login", method = RequestMethod.POST)
    public void login(
            @CookieValue(value = "AppVersion", required = false) String appVersion,
            @CookieValue(value = "DeviceId") String DeviceId,
            @CookieValue(value = "Type") int Type,
            @RequestBody String msg,
            HttpServletResponse response) throws Exception {

        /** base64 解密*/
        JSONObject jsonTemp = null;
        Key key = null;
        try {
            log.info(msg + "   DeviceId:" + DeviceId + "  AppVersion:" + appVersion + "   Type:" + Type);
            key = CommonUtil.getDESKeyFromRedis(DeviceId, Key.class);
            log.info("设备类型:" + (Type == 1 ? "android" : "IOS") + "    获得的私钥为 desKey:" + key.getDesKey());
            msg = URLDecoder.decode(msg.substring(4, msg.length()), "UTF-8");
            log.info("URLDecoder.decode后的msg:" + msg);
            jsonTemp = JSONObject.parseObject(DESEncrype.decryptDES(msg, key.getDesKey()));
            log.info(" 解密的结果：" + jsonTemp.toJSONString());
        } catch (Exception e) {
            log.error("解密抛出异常,服务器端DESKey :" + key.getDesKey());
            log.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
            response.getWriter().print(ResultMap.failll("请尝试重新打开APP登陆").toStringEclipse());
            return;
        }

        /**检查白名单中是否包含该项*/
        String userAccount = jsonTemp.getString("LoginId");
        String pwd = jsonTemp.getString("Pwd");
        Response<Boolean> existence = xcrWhiteListDubboService.isExistence(userAccount);
        if (existence == null || !org.apache.commons.lang3.StringUtils.isBlank(existence.getErrorMessage())) {
            log.info("[白名单异常] XcrWhiteListDubboService.isExistence(" + userAccount + ") result ==> " + JSON.toJSONString(existence));
            response.getWriter().print(ResultMap.failll(StateEnum.STATE_2.getDesc()).toStringEclipse());
            return;
        }

        JSONObject result = null;
        JSONObject stateJson = CommonUtil.pageStatus2("M00", "正常");
        String orgId = "";
        /**判断账号是否是 F5 系统的 也就是新老账号的区别 False=0 老账号,True=1 新账号[F5]*/
        boolean f5 = false;
        boolean isFranchiser = false;
        String defaultStoreId = null;
        /**F5*/
        if (existence.getResultObject()) {
            Response<ResponseDTO> userCheck = xcrUserDubboService.userCheck(userAccount, pwd);
            log.info("XcrUserDubboService.userCheck(" + userAccount + ", " + pwd + ") result ==> " + JSON.toJSONString(userCheck));

            if (userCheck == null || !userCheck.isSuccess() || userCheck.getResultObject() == null) {
                if (userCheck != null && !org.apache.commons.lang3.StringUtils.isBlank(userCheck.getErrorMessage())) {
                    response.getWriter().print(ResultMap.failll(userCheck.getErrorMessage()).toStringEclipse());
                } else {
                    response.getWriter().print(ResultMap.failll(StateEnum.STATE_2.getDesc()).toStringEclipse());
                }
                return;
            }

            ResponseDTO checkResultObject = userCheck.getResultObject();
            orgId = checkResultObject.getUserAccount();
            defaultStoreId = checkResultObject.getDefaultStoreNo();
            isFranchiser = checkResultObject.getAccountType() == 1 ? true : false;//1=加盟商账号 2=普通
            if (org.apache.commons.lang3.StringUtils.isBlank(orgId)) {
                stateJson = CommonUtil.pageStatus2("M02", StateEnum.STATE_2.getDesc());
            }
            f5 = true;
        }

        /** 原来的账号 local account */
        else {
            String params = String.format("appKey=%s&accessKey=%s&userCode=%s&passWord=%s", appKey, accessKey, userAccount, pwd);
            result = HttpClientUtil.okHttpPost(CHECKACCOUNT_URL, params.trim());
            if (result != null && INFO_OK.equals(result.getString("responseCode"))) {
                orgId = result.getJSONObject("data").getString("orgId");
            }
            stateJson = ActionEntranceUtil.getLoginStateJson(result, INFO_OK);
        }

        /**Common logic*/
        JSONObject mapJson = new JSONObject();
        JSONObject dataJson = new JSONObject();
        if (STATE_OK.equals(stateJson.getString("State"))) {

            Response<FranchiseeDto> res = organizationDubboService.queryFranchiseeById(orgId);
            log.info("The remote method OrganizationService.queryFranchiseeById(" + orgId + ")  result ==> " + JSON.toJSONString(res));

            if (res.isSuccess()) {
                if (res.getResultObject() != null) {
                    try {
                        UserInfoVo userVo = CommonUtil.userInfoPack(res.getResultObject());
                        String suffix = SYSTEM_CODE + userVo.getUserId();
                        log.info("login saveRedisJson suffix ==> " + suffix + "  UserInfoVo==>" + userVo);
                        synchronized (redisService) {
                            redisJsonService.saveObject(RedisKeyEnum.Key, suffix, TOKEN_OUTTIME, (JSONObject) JSON.toJSON(userVo));
                        }
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getFullStackTrace(e));
                    }

                    try {
                        FranchiseeDto franchiseeDto = res.getResultObject();
                        if (!f5) {
                            defaultStoreId = franchiseeDto.getDefaultStoreId();
                        }
                        StoreDto defaultStoreDto = null;
                        if (defaultStoreId == null) {
                            log.info("加盟商:" + userAccount + "  默认门店信息是：" + ((defaultStoreId == null) ? "为null" : defaultStoreId));
                            dataJson = CommonUtil.pageStatus(dataJson, STATE_ERR, "用户信息异常");
                        } else {
                            for (StoreDto storeDto : franchiseeDto.getStores()) {
                                if (defaultStoreId.equals(storeDto.getId())) {
                                    defaultStoreDto = storeDto;
                                    break;
                                }
                            }

                            UserDto userDto = new UserDto();
                            /**设备号*/
                            userDto.setRegistrationId(DeviceId);
                            /**小超账号*/
                            userDto.setUserName(jsonTemp.get("LoginId").toString());
                            /**一账通账号*/
                            userDto.setLogin(franchiseeDto.getYtAccount());
                            /**门店编号*/
                            userDto.setMerchantId(defaultStoreId);
                            /**加盟商真实姓名*/
                            userDto.setRealName(franchiseeDto.getName());
                            /**分公司ID*/
                            userDto.setCompanyId(defaultStoreDto.getBranchCompanyId());
                            userDto.setShopName(franchiseeDto.getDefaultStoreName());

                            /**调用初始化任务接口*/
                            Long missionStartTime = System.currentTimeMillis();
                            log.info("\n*******于" + DateUtils.getLogDataTime(missionStartTime, null) + "任务模块接口initMissionExecuteByMerchantId请求数据为:" + JSON.toJSONString(userDto));
                            Response<Boolean> resMission = missionExecuteDubboService.initMissionExecuteByMerchantId(userDto);
                            log.info("\n*******于" + DateUtils.getLogDataTime(missionStartTime, null) + "任务模块接口initMissionExecuteByMerchantId响应数据为:" + JSON.toJSONString(resMission) + "  花费时间为：" + CommonUtil.costTime(missionStartTime));

                            if (resMission != null && resMission.isSuccess()) {
                                Map<String, Object> mapJson_Map = new HashMap<>();
                                mapJson_Map.put("Token", TokenUtil.generateToken(orgId, userAccount, isFranchiser, f5));
                                mapJson_Map.put("UserId", userAccount);
                                mapJson_Map.put("UserNo", orgId);
//                                @Since 2.6
//                                mapJson_Map.put("UserName", defaultStoreDto.getFranchiseeName());
//                                mapJson_Map.put("UserPhone", defaultStoreDto.getMobilePhone());
                                mapJson_Map.put("UserName", franchiseeDto.getFranchiseeName());
                                mapJson_Map.put("UserPhone", franchiseeDto.getFranchiseePhone());


                                mapJson_Map.put("RUserInfoKey", "Key_" + SYSTEM_CODE + res.getResultObject().getId());
                                mapJson_Map.put("FinancialAccount", res.getResultObject().getYtAccount());
                                mapJson_Map.put("StoreSerialNoDefault", defaultStoreId);
                                mapJson_Map.put("StoreSerialNameDefault", defaultStoreDto.getName());
                                //==> Version 2.2
                                mapJson_Map.put("CityName", defaultStoreDto.getCityName());
                                mapJson_Map.put("StoreAbbreName", defaultStoreDto.getSimpleName());
                                mapJson_Map.put("BranchOfficeId", defaultStoreDto.getBranchCompanyId());
                                //==> Version 2.5
                                mapJson_Map.put("VipIdentify", memberDentifyTransfer(res.getResultObject().getId()));
                                mapJson_Map.put("StoreNo", defaultStoreDto.getOrderCode() == null ? "No." : defaultStoreDto.getOrderCode());
                                //<== Version 2.5.1
                                mapJson_Map.put("StoreType", defaultStoreDto.getStoreType());
                                //<== Version 2.6
                                mapJson_Map.put("LoginId", userAccount);
                                if (f5) {
                                    //[ 0：老账号，1：新账号 ]
                                    mapJson_Map.put("AccountType", "1");
                                } else {
                                    mapJson_Map.put("AccountType", "0");
                                }

                                mapJson = StringUtils.replcNULLToStr(mapJson_Map);
                                dataJson.put("mapdata", mapJson);
                                if (appVersion != null) {
                                    dataJson.put("mapdata", DESEncrype.encryptDES(mapJson.toString(), key.getDesKey()));
                                }
                                dataJson = CommonUtil.pageStatus(dataJson, STATE_OK, "正常");

                                /**获取推送唯一Id*/
                                String registPushId = jsonTemp.getString("RegisterId");
                                /**更新registID*/
                                if (registPushId != null) {
                                    /**门店号*/
                                    if (f5) {
                                        if (isFranchiser) {
                                            savePushRegistIdToMchId(orgId, registPushId);
                                        }
                                    } else {
                                        savePushRegistIdToMchId(orgId, registPushId);
                                    }
                                }
                                //Shiro Login
                                shiroLoginCondition(userAccount);
                            } else {
                                dataJson = CommonUtil.pageStatus(dataJson, STATE_ERR, "用户信息异常");
                            }
                        }
                    } catch (Exception e) {
                        dataJson = CommonUtil.pageStatus(dataJson, STATE_ERR, "服务器异常");
                        log.error(ExceptionUtils.getFullStackTrace(e));
                    }
                } else {
                    dataJson = CommonUtil.pageStatus(dataJson, STATE_ERR, "用户信息异常");
                    mapJson.put("Desc", result.getString("errMsg"));
                    dataJson.put("mapdata", mapJson);
                    if (appVersion != null) {
                        dataJson.put("mapdata", DESEncrype.encryptDES(mapJson.toString(), key.getDesKey()));
                    }
                }
            } else {
                if (res.getCode().equals(USER_TERMINATE)) {
                    dataJson = CommonUtil.pageStatus(dataJson, STATE_ERR, "加盟商已解约");
                }
            }
        } else {
            dataJson.put("Status", stateJson);
        }
        log.info("Response to xcrAPP data is：" + dataJson + "    appVersion号：" + appVersion);
        log.info("Response to xcrAPP data is字符串：" + dataJson.toString());
        if (appVersion != null) {
            response.getWriter().print(dataJson);
        } else {
            response.getWriter().print(DESEncrype.encryptDES(dataJson.toString(), key.getDesKey()));
        }
    }


    /**
     * Shiro Login
     *
     * @param userAccount
     */
    private void shiroLoginCondition(String userAccount) {
        //Insert Shiro User
        UsernamePasswordToken token = new UsernamePasswordToken(userAccount, "123456");
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {// 未登录状态
            currentUser.login(token);
        } else {// 已有登录用户强制退出已登录用，登录当前用户
//                        			String userName = (String) currentUser.getPrincipal();
            // 如果登录名不同
            currentUser.logout();
            currentUser.login(token);
        }
        Session session = currentUser.getSession();
        session.setAttribute("UserId", userAccount);
        //Insert Shiro User
    }


    /**
     * gaodawei 获取公钥与私钥，然后返回公钥给APP
     * @param DeviceId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "GetPublicKey", method = RequestMethod.POST)
    public void getPublicKey(@CookieValue(value = "DeviceId") String DeviceId, HttpServletResponse response) throws IOException {
        log.info("******GetPublicKey方法开始执行");
        KeyPair keyPair = RSAHelper.generateRSAKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String pubKeyStr = StringUtils.bytesToString(Bases64.encode(RSAHelper.encodePublicKeyToXml(publicKey).getBytes()));
        String priKeyStr = StringUtils.bytesToString(Bases64.encode(RSAHelper.encodePrivateKeyToXml(privateKey).getBytes()));
        /**先去取出，判断DESKey为不为null，不为null则将不为null的值取出重新保存*/
        Key key = CommonUtil.getDESKeyFromRedis(DeviceId, Key.class);
        if (key == null) {
            key = new Key();
        }
        key.setPrivateKey(priKeyStr);
        log.info("根据DeviceID将用户公钥存入redis -> DeviceId:{}",DeviceId);
        redisService.saveObject(RedisKeyEnum.Key, DeviceId, -1, key);
        JSONObject json = ActionEntranceUtil.getResponseData(pubKeyStr);
        log.debug("******Response to xcrAPP data is：" + json);
        response.getWriter().print(json);
    }


    /**
     * gaodawei 这里接受前台传过来的DESKey
     * @throws Exception
     @CookieValue(value = "DeviceId") String DeviceId,
     @CookieValue(value = "Type") int Type,
     */
//    @RequestMapping(value = "PostDesKey", method = RequestMethod.POST)
//    public void postDesKey(@CookieValue(value = "DeviceId") String DeviceId, @CookieValue(value = "Type") int Type,
//                           @RequestBody String msg, HttpServletResponse response) throws Exception {
//        log.info("\n*****************现在执行的是PostDesKey方法\n*****************接收到设备类型是：" + ((Type == 1) ? "Android" : "IOS"));
//        msg = msg.substring(4, msg.length());
//        log.info("\n*****************App请求解码前的数据为：" + msg);
//        String newMsg = "";
//        if (Type == 1) {
//            newMsg = URLDecoder.decode(URLDecoder.decode(msg, "UTF-8"), "UTF-8");
//        } else {
//            newMsg = URLDecoder.decode(msg, "UTF-8");
//        }
//        log.info("\n*****************App请求解码后的数据为：" + newMsg);
//        Long startTime = System.currentTimeMillis();
//        log.info("\n*****************调用redisService.getKeyForObject接口的开始时间：" + DateUtils.getLogDataTime(startTime, null));
//        Key key = CommonUtil.getDESKeyFromRedis(DeviceId, Key.class);
//        log.info("\n*****************于时间" + DateUtils.getLogDataTime(startTime, null) + "调用的redisService.getKeyForObject接口调用结束"
//                + "响应的数据为：" + JSONObject.toJSONString(key));
//        PrivateKey priKey = RSAHelper.decodePrivateKeyFromXml(StringUtils.bytesToString(Bases64.decode(key.getPrivateKey().getBytes())));
//        byte[] bt2 = RSAHelper.decryptData(Bases64.decode(newMsg.getBytes()), priKey);
//        JSONObject jsonDESKey = JSONObject.parseObject(StringUtils.bytesToString(bt2));
//        log.info("******************APP端传过来的DeviceId=" + DeviceId + ",DESKey为：" + jsonDESKey);
//        synchronized (redisService) {
//            key.setDesKey(jsonDESKey.getString("Key"));
//            redisService.saveObject(RedisKeyEnum.Key, DeviceId, -1, key);
//        }
//        JSONObject json = ActionEntranceUtil.getPostDesKeyData(CommonUtil.getDESKeyFromRedis(DeviceId, Key.class).getDesKey());
//        log.info("\n*****************Response to xcrAPP data is" + json);
//        response.getWriter().print(json);
//    }
    
    @RequestMapping(value = "PostDesKey", method = RequestMethod.POST)
    public void postDesKey(@Payload PostDesKey map, HttpServletResponse response) throws Exception {
    	 if (map.getType() == 1) {
    		 map.setMsg(URLDecoder.decode(map.getMsg(), "UTF-8"));
         }
        log.info("\n*****************现在执行的是PostDesKey方法\n*****************接收到设备类型是：" + ((map.getType() == 1) ? "Android" : "IOS"));
        log.info("\n*****************App请求解码前的数据为：{}" , map);
        Long startTime = System.currentTimeMillis();
        log.info("\n*****************调用redisService.getKeyForObject接口的开始时间：" + DateUtils.getLogDataTime(startTime, null));
        Key key = CommonUtil.getDESKeyFromRedis(map.getDeviceId(), Key.class);
        log.info("\n*****************于时间" + DateUtils.getLogDataTime(startTime, null) + "调用的redisService.getKeyForObject接口调用结束  响应的数据为：" + JSONObject.toJSONString(key));

        PrivateKey priKey = RSAHelper.decodePrivateKeyFromXml(StringUtils.bytesToString(Bases64.decode(key.getPrivateKey().getBytes())));
        byte[] bt2 = RSAHelper.decryptData(Bases64.decode(map.getMsg().getBytes()), priKey);
        JSONObject jsonDESKey = JSONObject.parseObject(StringUtils.bytesToString(bt2));
        log.info("\n******************APP端传过来的DeviceId=" + map.getDeviceId() + ",DESKey为：" + jsonDESKey);

        synchronized (redisService) {
            key.setDesKey(jsonDESKey.getString("Key"));
            redisService.saveObject(RedisKeyEnum.Key, map.getDeviceId(), -1, key);
        }

        JSONObject json = ActionEntranceUtil.getPostDesKeyData(CommonUtil.getDESKeyFromRedis(map.getDeviceId(), Key.class).getDesKey());
        log.info("\n*****************Response to xcrAPP data is" + json);
        response.getWriter().print(json);
    }


    /**
     * <VIP等级标识>
     *
     * @param memberCode
     * @return
     */
    private Integer memberDentifyTransfer(String memberCode) {
        log.info("Call queryMemberInfoByCode Request Data Is:" + memberCode);
        Response<MemberInfoDto> memLeResponse = memberInfoDubboService.queryMemberInfoByCode(memberCode);
        log.info("Call queryMemberInfoByCode Response Data Is:" + JSONObject.toJSONString(memLeResponse));
        if (memLeResponse == null) {
            log.error("<VIPCenterError> queryMemberInfoByCode Return Null !!");
            return 0;
        }
        if (!memLeResponse.isSuccess()) {
            log.error("Call queryMemberInfoByCode Return False!!");
        }
        MemberInfoDto memberLevelDto = memLeResponse.getResultObject();
        if (memberLevelDto == null) {
            log.warn("This User Doesn't have Level!!");
            return 0;
        }
        //Find the relative position of VIP in the list of VIP levels based on the VIP name
        Response<List<MemberLevelDto>> memberLevelDubboServiceDubboResult = memberLevelDubboService.queryAllMemberLevelListAndPrivilege();
        log.info("Call queryAllMemberLevelListAndPrivilege End");
        if (memberLevelDubboServiceDubboResult == null) {
            log.error("<VIPCenterError> queryAllMemberLevelListAndPrivilege Return Null !!");
            return 0;
        }
        if (!memberLevelDubboServiceDubboResult.isSuccess()) {
            log.error("Call queryAllMemberLevelListAndPrivilege Return False!!");
        }
        List<MemberLevelDto> memberLevelDtos = memberLevelDubboServiceDubboResult.getResultObject();
        if (memberLevelDtos == null) {
            return 0;
        }
        //Force matching VIP identifier and default list data is VIP level ascending
        Integer VIPlevel = 0;
        try {
            for (MemberLevelDto memberLevelDto1 : memberLevelDtos) {
                if (!memberLevelDto.getMemberLevelName().equals(memberLevelDto1.getMemberLevelName())) {
                    VIPlevel++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("memberInfoDto.getMemberLevelName Is Null And Msg Is" + JSONObject.toJSONString(e));
        }
        return VIPlevel;
    }

    /**
     * 将registID推送唯一标识存库 <method description>
     *
     * @param mchId
     * @param registPushId
     */
    private void savePushRegistIdToMchId(String mchId, String registPushId) {
        log.info("将registID推送唯一标识存库 ... mchId:" + mchId + "  registPushId:" + registPushId);

        List<RegistrationParameterDto> list = new ArrayList<>();
        RegistrationParameterDto registrationParameterDto = new RegistrationParameterDto();
        registrationParameterDto.setBusinessId(mchId);
        registrationParameterDto.setRegistrationId(registPushId);
        list.add(registrationParameterDto);
        log.info("orgnazitionO2ODubboService.updateRegistrationId(list); 开始调用 ... 传入参数:mchId:"
                + mchId + "  registPushId:" + registPushId);
        long startUpdate = System.currentTimeMillis();
        orgnazitionO2ODubboService.updateRegistrationId(list);
        long endUpdate = System.currentTimeMillis();
        log.info("orgnazitionO2ODubboService.updateRegistrationId(list); 调用结束 ... 花费时间为:" + (endUpdate - startUpdate));
    }


    /**
     * 根据支行id查询：msg={"BankCardId":"2","PageIndex":"1","PageSize":"10","Key":""}
     * <p>
     * 根绝支行名称查询：msg={"BankCardId":"2","PageIndex":"1","PageSize":"10","Key":"农行"}
     *
     * @param msg
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = {"GetBankCardBranch"}, method = RequestMethod.POST)
    public void getBankCardBranch(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "GetBankCardBranch");
        JSONObject json = new JSONObject();
        if (jsonTemp.getString("BankCardId") != null && !jsonTemp.getString("BankCardId").equals("")
                && jsonTemp.getString("Key") != null && !jsonTemp.getString("Key").equals("")) {
            BranchBankDTO branchBankDTO = new BranchBankDTO();
            branchBankDTO.setBankid(jsonTemp.getString("BankCardId"));
            branchBankDTO.setBranchbankname(jsonTemp.getString("Key"));
            branchBankDTO.setPageIndex((jsonTemp.getIntValue("PageIndex") - 1) * jsonTemp.getIntValue("PageSize") + 1);
            branchBankDTO.setPageSize(jsonTemp.getIntValue("PageSize"));
            Long getSignArrayStartTime = System.currentTimeMillis();
            log.info("\n*****************调用branchBankListDubboService.findBranchListByBankCode接口的开始时间："
                    + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
                    + JSONObject.toJSONString(JSONObject.toJSONString(branchBankDTO)));
            Response<ResultBranchBankDTO> res = branchBankListDubboService.findBranchListByBankName(branchBankDTO);
            log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
                    + "调用branchBankListDubboService.findBranchListByBankCode接口   调用结束" + "\n*****************响应数据是："
                    + JSONObject.toJSONString(JSONObject.toJSONString(res.getResultObject()))
                    + "\n***************所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));
            resolveResMethod(json, res);
        } else if (jsonTemp.getString("BankCardId") != null && !jsonTemp.getString("BankCardId").equals("")) {
            BranchBankDTO branchBankDTO = new BranchBankDTO();
            branchBankDTO.setBankid(jsonTemp.getString("BankCardId"));
            branchBankDTO.setPageIndex((jsonTemp.getIntValue("PageIndex") - 1) * jsonTemp.getIntValue("PageSize") + 1);
            branchBankDTO.setPageSize(jsonTemp.getIntValue("PageSize"));
            Long getSignArrayStartTime = System.currentTimeMillis();
            log.info("\n*****************调用branchBankListDubboService.findBranchListByBankCode接口的开始时间："
                    + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
                    + JSONObject.toJSONString(JSONObject.toJSONString(branchBankDTO)));
            Response<ResultBranchBankDTO> res = branchBankListDubboService.findBranchListByBankCode(branchBankDTO);
            log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
                    + "调用branchBankListDubboService.findBranchListByBankCode接口   调用结束" + "\n*****************响应数据是："
                    + JSONObject.toJSONString(JSONObject.toJSONString(res.getResultObject()))
                    + "\n***************所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));
            resolveResMethod(json, res);
        } else {
            json.put("Status", CommonUtil.pageStatus2("M02", "参数不正确"));
        }
        response.getWriter().print(json);
    }

    private void resolveResMethod(JSONObject json, Response<ResultBranchBankDTO> res) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        JSONObject listdata = new JSONObject();
        if (res != null && res.isSuccess()) {
            ResultBranchBankDTO resultBranchBankDTO = res.getResultObject();
            List<BranchBankDTO> branchBankDTOs = resultBranchBankDTO.getBranchBanks();
            for (BranchBankDTO branchBankDTO2 : branchBankDTOs) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("BankCardBranch", StringUtils.replaceNULLToStr(branchBankDTO2.getBranchbankname()));
                jsonObject.put("BankCardBranchId", StringUtils.replaceNULLToStr(branchBankDTO2.getBankno()));
                jsons.add(jsonObject);
            }
            listdata.put("rows", jsons);
            listdata.put("pageindex", resultBranchBankDTO.getPageIndex() + 1);
            listdata.put("pagesize", resultBranchBankDTO.getPageSize());
            listdata.put("totalpage", resultBranchBankDTO.getTotalPage());
            listdata.put("totalcount", resultBranchBankDTO.getTotalCount());
            //					listdata = CommonUtil.pagePackage(listdata, jsonTemp, js.getIntValue("records"), null);
            json.put("listdata", listdata);
            json = CommonUtil.pageStatus(json, "M00", "成功");

        } else {
            json = CommonUtil.pageStatus(json, "M02", "获取列表失败");
        }
    }

    /**
     * 404错误处理
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "error404", method = RequestMethod.POST)
    public void error404(HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        json = CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), "请求链接不存在");
        log.info("\n*******用户请求链接不存在，error404方法调用返回友好信息");
        response.getWriter().print(json);
    }

    /**
     * 500错误处理
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "error500", method = RequestMethod.POST)
    public void error500(HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        json = CommonUtil.pageStatus(json, StateEnum.STATE_2.getState(), "请求发生错误，请稍后再试");
        log.info("\n*******用户请求出错，error500方法调用返回友好信息");
        response.getWriter().print(json);
    }

}
