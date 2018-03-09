package com.yatang.xc.xcr.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.o2o.dto.StoreO2ODto;
import com.yatang.xc.xcr.annotations.Payload;
import com.yatang.xc.xcr.annotations.SessionToken;
import com.yatang.xc.xcr.dto.inputs.*;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IShopSettingsInformationService;
import com.yatang.xc.xcr.service.IStockService;
import com.yatang.xc.xcr.service.QRCodeService;
import com.yatang.xc.xcr.util.*;
import com.yatang.xcsm.remote.api.dto.ShopNoticeDTO;
import com.yatang.xcsm.remote.api.dto.ShopPictureDTO;
import com.yatang.xcsm.remote.api.dubboxservice.PushShopNoticeDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺管理
 * @author dongshengde
 */
@Controller
@RequestMapping("/User/")
public class StoreAction {

	private @Value("${STATE_OK}")String STATE_OK;
	private @Value("${STATE_ERR}")String STATE_ERR;
	/**TYPE  1为店铺公告*/
	private static final String TYPE_NOTICE="1";
	/**TYPE 2为店铺介绍*/
	private static final String TYPE_INTRODUCTION="2";
    /**公告或介绍 [不通过] 状态 2*/
    private final static String UNPASS="2";
    /**店铺的公告或介绍的是否显示标识,[不显示 0]*/
    private final static int HIDDEN = 0;
    /**C端是否展示 [1是展示]*/
    private final static int DISPLAY_C  = 1;
    /**公告或介绍 [审核中...] 状态 1 此状态针对雅堂移动端*/
    private final static String  UNDER_EVIEW = "1";
    /**雅堂移动端 未发布 0*/
    private final static String UNPUBLISHED  = "0";
    /** C端审核中*/
    private final static String AUDIT  = "0";
    /**店铺公告发布状态 1：审核已通过*/
    private final static String AUDIT_HAS_PASSED  = "1";
    /**3：审核已通过 雅堂*/
    private final static String AUDIT_HAS_PASSED_YT ="3";
    /**Logger*/
	private static Logger log = LoggerFactory.getLogger(StoreAction.class);

	@Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private IStockService stockService;
	@Autowired
	private OrgnazitionO2ODubboService o2oDubboService;
	@Autowired
	private PushShopNoticeDubboService pushShopNoticeDubboService;
    @Autowired
    private IShopSettingsInformationService shopSettingsInformationService;


	///<Summary>
	///   设置店铺位置
	///</Summary>
	@SessionToken
	@RequestMapping(value="SetShopAddress",method=RequestMethod.POST)
	public ResultMap setShopAddress(@Payload SetShopAddressDto dto){
		return shopSettingsInformationService.setShopAddress(dto);
	}

	///<Summary>
	///   设置配送信息
	///</Summary>
	@SessionToken
	@RequestMapping(value="SetDistributeInfo",method=RequestMethod.POST)
	public ResultMap setShopInfo(@Payload SetShopInfoDto dto){
		return shopSettingsInformationService.setShopInfo(dto);
	}

	///<Summary>
	///  设置营业时间
	///</Summary>
	@SessionToken
	@RequestMapping(value="SetShopTime",method=RequestMethod.POST)
	public ResultMap setShopTime(@Payload SetShopTimeDto dto){
		return shopSettingsInformationService.setShopTime(dto);
	}

	///<Summary>
	///  店铺介绍发布
	///</Summary>
	@SessionToken
	@RequestMapping(value="IntroduceRelease",method=RequestMethod.POST)
	public ResultMap introduceRelease(@Payload IntroduceReleaseDto dto){
		return shopSettingsInformationService.introduceRelease(dto);
	}

	///<Summary>
	///  店铺公告发布
	///</Summary>
	@SessionToken
	@RequestMapping(value="NoticesRelease",method=RequestMethod.POST)
	public ResultMap noticesRelease(@Payload NoticesReleaseDto dto){
		return shopSettingsInformationService.noticesRelease(dto);
	}

	///<Summary>
	///  设置介绍在店铺中显示
	///</Summary>
	@SessionToken
	@RequestMapping(value="SetIntroduceStatue",method=RequestMethod.POST)
	public ResultMap setIntroduceStatue(@Payload SetIntroduceStatueDto dto){
		return shopSettingsInformationService.setIntroduceStatue(dto);
	}

	///<Summary>
	///  设置公告在店铺中显示
	///</Summary>
	@SessionToken
	@RequestMapping(value="SetNoticesShown",method=RequestMethod.POST)
	public ResultMap SetNoticesShown(@Payload SetNoticesShownDto dto){
		return shopSettingsInformationService.setNoticesShown(dto);
	}


    ///<Summary>
    ///   营业状态修改v2.0
    ///</Summary>
    @SessionToken
    @RequestMapping( value ="SetShopBusiness" , method = RequestMethod.POST)
    public ResultMap setShopBusiness(@Payload BusinessStateDto model){
        return stockService.setShopBusiness(model);
    }

    ///<Summary>
    ///  设置自动接单v2.2
    ///</Summary>
    @SessionToken
    @RequestMapping( value ="SetReciveStatus", method = RequestMethod.POST)
    public ResultMap setReciveStatus(@Payload AutomaticReceiptDto model){
        return stockService.setReciveStatus(model);
    }

    ///<Summary>
    ///  设置店铺简称v2.2
    ///</Summary>
    @SessionToken
    @RequestMapping(value ="SetStoreAbbrevy" , method = RequestMethod.POST)
    public ResultMap setStoreAbbrevy(@Payload ShopAbbreviationDto model){
        return stockService.setStoreAbbrevy(model);
    }


    /**
     * 设置店铺信息
     * @param msg
     * @param response
     * msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","Latitude":"111.154554","Longitude":"30.245545","Address":"四川省成都市","OpenTime":"1497431707000","CloseTime":"1497432707000","StartPrice":"20","DeliveryFee":"5","DeliveryScope":"10","LocationAddress":"仑膦大道","BusinessStatus":"0","FreeDeliveryFee":"5"}
     * 设置简称
     * msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","StoreAbbrevy":"减减肥吃醋"}
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value="SetShopInfo", method = RequestMethod.POST)
    public void addStoreInfo(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SetShopInfo");
        JSONObject json = new JSONObject();
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        if (stateJson.getString("State").equals(STATE_OK)) {//营业状态
            String businessStatus = jsonTemp.getString("BusinessStatus");
            String reciveStatus = jsonTemp.getString("ReciveStatus");
            String storeAbbrevy = jsonTemp.getString("StoreAbbrevy");
            if (businessStatus != null && !businessStatus.equals("")) {//设置营业状态
                StoreO2ODto storeO2ODto = new StoreO2ODto();
                storeO2ODto.setO2oStatus(Integer.parseInt(businessStatus));
                storeO2ODto.setId(jsonTemp.getString("StoreSerialNo"));
                Long getSignArrayStartTime = System.currentTimeMillis();
                log.info("调用StoreAction.SetShopBusiness接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + " 请求数据是：" + JSONObject.toJSONString(JSONObject.toJSONString(storeO2ODto)));
                @SuppressWarnings("rawtypes")
                Response res = o2oDubboService.updateStore(storeO2ODto);
                log.info("于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "调用StoreAction.SetShopBusiness接口   调用结束" + " 响应数据是：" + JSONObject.toJSONString(res) + " 所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));
                if (res != null && res.getCode().equals("200")) {
                    json = CommonUtil.pageStatus(json, STATE_OK, StateEnum.STATE_0.getDesc());
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, "主数据保存失败");
                }
            } else if (reciveStatus != null && !reciveStatus.equals("")) {//自动接单
                StoreO2ODto storeO2ODto = new StoreO2ODto();
                storeO2ODto.setO2oAutoOrder(Integer.parseInt(reciveStatus));
                storeO2ODto.setId(jsonTemp.getString("StoreSerialNo"));
                Long getSignArrayStartTime = System.currentTimeMillis();
                log.info("调用StoreAction.SetReciveStatus接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + " 请求数据是：" + JSONObject.toJSONString(storeO2ODto));

                Response res = o2oDubboService.updateStore(storeO2ODto);
                log.info("于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "调用StoreAction.SetReciveStatus接口   调用结束" + " 响应数据是：" + JSONObject.toJSONString(res) + " 所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));

                if (res != null && res.getCode().equals("200")) {
                    json = CommonUtil.pageStatus(json, STATE_OK, StateEnum.STATE_0.getDesc());
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, "主数据保存失败");
                }
            } else if (storeAbbrevy != null && !storeAbbrevy.equals("")) {//店铺简称
                StoreO2ODto storeO2ODto = new StoreO2ODto();
                storeO2ODto.setSimpleName(storeAbbrevy);
                storeO2ODto.setId(jsonTemp.getString("StoreSerialNo"));
                Long getSignArrayStartTime = System.currentTimeMillis();
                log.info("调用StoreAction.SetStoreAbbrevy接口的开始时间：" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + " 请求数据是：" + JSONObject.toJSONString(JSONObject.toJSONString(storeO2ODto)));

                Response res = o2oDubboService.updateStore(storeO2ODto);
                log.info("于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "调用StoreAction.SetStoreAbbrevy接口   调用结束" + " 响应数据是：" + JSONObject.toJSONString(res) + " 所花费时间为：" + CommonUtil.costTime(getSignArrayStartTime));

                if (res != null && res.getCode().equals("200")) {
                    json = CommonUtil.pageStatus(json, STATE_OK, StateEnum.STATE_0.getDesc());
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, "主数据保存失败");
                }
            } else {//店铺基本数据
                String freeDeliveryFee = jsonTemp.getString("FreeDeliveryFee");
                if (freeDeliveryFee == null || freeDeliveryFee.equals("")) {
                    freeDeliveryFee = "100";
                }
                String latitude = jsonTemp.getString("Latitude");
                String longitude = jsonTemp.getString("Longitude");
                String openTime = jsonTemp.getString("OpenTime");
                String closeTime = jsonTemp.getString("CloseTime");
                String startPrice = jsonTemp.getString("StartPrice");
                String deliveryFee = jsonTemp.getString("DeliveryFee");
                String deliveryScope = jsonTemp.getString("DeliveryScope");
                String storeId = jsonTemp.getString("StoreSerialNo");
                String isFree = jsonTemp.getString("IsFreeDelivery");
                String locationAddress = jsonTemp.getString("LocationAddress");
                log.info("门店保存信息方法中参数：满多少元免配送：" + freeDeliveryFee + ",,经度latitude：" + latitude + "，，纬度longitude：" + longitude + "，，起送费startPrice：" + startPrice + ",,配送费deliveryFee：" + deliveryFee + ",,开业时间：" + openTime + ",,关门时间：" + closeTime + ",,配送距离：" + deliveryScope + ",,门店号：" + storeId + ",,地址信息：" + locationAddress);
                StoreO2ODto storeDto = new StoreO2ODto();
                storeDto.setLatitude(new BigDecimal(latitude));
                storeDto.setLongitude(new BigDecimal(longitude));
                storeDto.setStartTime(openTime);
                storeDto.setEndTime(closeTime);
                storeDto.setDistributionAmount(new BigDecimal(startPrice));//起送费
                storeDto.setDistributionTip(new BigDecimal(deliveryFee));//配送费
                storeDto.setDistributionScope(deliveryScope);
                storeDto.setId(storeId);
                storeDto.setCode(storeId);
                BigDecimal bfreeDeliveryFee = new BigDecimal(freeDeliveryFee);
                storeDto.setDistributionFreeMoney(bfreeDeliveryFee);
                storeDto.setLocationAddress(locationAddress);
                if (isFree != null && !isFree.equals("")) {
                    storeDto.setFreightFlag(Integer.parseInt(isFree));
                }
                long startTime = System.currentTimeMillis();
                log.info("于时间" + DateUtils.getLogDataTime(startTime, null) + "开始调用StoreAction.SetShopInfo请求数据是：" + JSONObject.toJSONString(storeDto));
                Response<?> res = o2oDubboService.updateStore(storeDto);
                log.info("于时间" + DateUtils.getLogDataTime(startTime, null) + "结束调用StoreAction.SetShopInfo响应数据是：" + JSONObject.toJSONString(res) + " 耗时为:" + CommonUtil.costTime(startTime));

                if (res != null && res.getCode().equals("200")) {
                    json = CommonUtil.pageStatus(json, STATE_OK, StateEnum.STATE_0.getDesc());
                } else {
                    json = CommonUtil.pageStatus(json, STATE_ERR, StateEnum.STATE_2.getDesc());
                }
            }
        } else {
            json.put("Status", stateJson);
        }

        log.info("于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + " response to XCR_APP data is:  " + json + " 用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
        response.getWriter().print(json);
    }


	/**
	 * 查询店铺信息
	 * @param msg
	 * @param response
	 * msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111"}
	 */
	@RequestMapping(value = "GetShopInfo", method = RequestMethod.POST)
	public void queryStoreInfo(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg.trim(), "GetShopInfo");
		JSONObject json = new JSONObject();
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		if (stateJson.getString("State").equals(STATE_OK)) {

			long o2ostartTime = System.currentTimeMillis();
			log.info("于时间" + DateUtils.getLogDataTime(o2ostartTime, null) + "开始调用o2oDubboService.queryStoreById请求数据是：" + jsonTemp.getString("StoreSerialNo"));
			Response<?> res = o2oDubboService.queryStoreById(jsonTemp.getString("StoreSerialNo"));
			log.info("于时间" + DateUtils.getLogDataTime(o2ostartTime, null) + "结束调用o2oDubboService.queryStoreById响应数据是：" + JSONObject.toJSONString(res) + " 耗时为:" + CommonUtil.costTime(o2ostartTime));
			if(res == null || res.getResultObject() == null){
				response.getWriter().print(CommonUtil.pageStatus(json, "M02", "未能查询店铺信息"));
				return;
			}

			/**
			 * @since  2.5.1
			 * */
			String storeSerialNo = Assert.toStr(jsonTemp.getString("StoreSerialNo"));
			ShopNoticeDTO paramShopNoticeDTO = new ShopNoticeDTO();
			paramShopNoticeDTO.setShopCode(storeSerialNo);
			com.yatang.xcsm.common.response.Response<List<ShopNoticeDTO>> shopNoticesByShopInfo = pushShopNoticeDubboService.getShopNoticesByShopInfo(paramShopNoticeDTO);
			if(shopNoticesByShopInfo == null || !shopNoticesByShopInfo.isSuccess()){
				log.info("The remote method PushShopNoticeDubboService.getShopNoticesByShopInfo() excute fail. parames ==> " + JSON.toJSONString(paramShopNoticeDTO));
				log.info("PushShopNoticeDubboService.shopNoticesByShopInfo() resultSet ==> "+ JSON.toJSONString(shopNoticesByShopInfo));
			}

			StoreO2ODto st = (StoreO2ODto) res.getResultObject();
			JSONObject js = new JSONObject();
			js.put("Latitude", StringUtils.replaceNULLToStr(st.getLatitude()));
			js.put("Longitude", StringUtils.replaceNULLToStr(st.getLongitude()));
			js.put("Address", StringUtils.replaceNULLToStr(st.getAddress()));
			String startTime = StringUtils.replaceNULLToStr(st.getStartTime());

			List<ShopNoticeDTO> resultList = shopNoticesByShopInfo.getResultObject();
			if(resultList == null || resultList.size() == 0 || ObjectUtils.isNull(resultList.get(0))){
				log.info("The remote method PushShopNoticeDubboService.getShopNoticesByShopInfo() result is null ==> " + JSON.toJSONString(shopNoticesByShopInfo));
				js.put("IsIntroduceShown",0);
				js.put("IntroduceStatue",0);
				js.put("IsNoticesShown",0);
				js.put("NoticesStatue",0);
				js.put("IntroducePicUrls",new JSONArray());
				js.put("NoticesDetial","");

			} else{
                for (ShopNoticeDTO notic : resultList) {
                    if (TYPE_NOTICE.equals(notic.getType())) {
                        /**审核状态只有这三个   审核状态【0待审核，默认。1审核通过，2不通过】 C:端的【0待审核】 等于雅堂的 【1 审核中】*/
                        String status = notic.getCheckStatus();
                        /**雅堂移动端 公告状态*/
                        String mobailStatus = UNPUBLISHED;
                        /**店铺公告内容 店铺公告内容，处于审核中或审核已通过时返回*/
                        if (AUDIT.equals(status)) {
                            js.put("NoticesDetial", notic.getNewNoticeContent());
                            mobailStatus = UNDER_EVIEW;
                        }

                        /**1审核通过*/
                        if (AUDIT_HAS_PASSED.equals(status)) {
                            js.put("NoticesDetial", notic.getNewNoticeContent());
                            mobailStatus = AUDIT_HAS_PASSED_YT;
                        }

                        /**2不通过*/
                        if (UNPASS.equals(status)) {
                            mobailStatus = status;
                            js.put("NoticesDetial", "");
                        }

                        /**店铺公告发布状态 0：未发布，1：审核中，2：审核未通过，3：审核已通过*/
                        js.put("NoticesStatue", mobailStatus);
                        /**C端： 是否在店铺中显示【1展示，2店家设置不展示，3平台设置不展示 ||  雅堂移动端 ： 店铺公告显示 0：否，1：是*/
                        int displaySetting = Integer.parseInt(notic.getDisplaySetting());
                        if (DISPLAY_C != displaySetting) {
                            displaySetting = HIDDEN;
                        }
                        js.put("IsNoticesShown", displaySetting);
                    }

                    if (TYPE_INTRODUCTION.equals(notic.getType())) {
                        /**审核状态只有这三个   审核状态【0待审核，默认。1审核通过，2不通过】*/
                        String status = notic.getCheckStatus();
                        /**雅堂移动端 公告状态*/
                        String mobailStatus = UNPUBLISHED;
                        /**店铺公告内容 店铺公告内容，处于审核中或审核已通过时返回*/
                        if (AUDIT.equals(status)) { mobailStatus = UNDER_EVIEW; }
                        /**1审核通过*/
                        if (AUDIT_HAS_PASSED.equals(status)) { mobailStatus = AUDIT_HAS_PASSED_YT; }
                        /**2不通过*/
                        if (UNPASS.equals(status)) { mobailStatus = status; }

                        /**店铺介绍发布状态 0：未发布，1：审核中，2：审核未通过，3：审核已通过*/
                        js.put("IntroduceStatue", mobailStatus);
                        /**C端： 是否在店铺中显示【1展示，2店家设置不展示，3平台设置不展示 ||  雅堂移动端 ： 店铺介绍显示 0：否，1：是*/
                        int displaySetting = Integer.parseInt(notic.getDisplaySetting());
                        if (DISPLAY_C != displaySetting) {
                            displaySetting = HIDDEN;
                        }
                        js.put("IsIntroduceShown", displaySetting);
                        /**店铺介绍图片列表 店铺介绍图片列表，处于审核中或审核已通过时返回*/
                        List<ShopPictureDTO> pics = notic.getOldPictures();
                        if (pics != null && pics.size() > 0) {
                            List<JSONObject> introducePicUrls = new ArrayList<>();
                            for (ShopPictureDTO pic : pics) {
                                JSONObject picUrl = new JSONObject();
                                /**图片链接*/
                                picUrl.put("PicUrl", pic.getPicturePath());
                                introducePicUrls.add(picUrl);
                            }
                            js.put("IntroducePicUrls", introducePicUrls);
                        } else {
                            js.put("IntroducePicUrls", new JSONArray());
                        }
                    }
                }
                // Else End.
            }

            if (js.get("NoticesStatue") == null){ js.put("NoticesStatue", 0);}
            if (js.get("IsNoticesShown") == null) { js.put("IsNoticesShown", 0);}
            if (js.get("IntroduceStatue") == null){  js.put("IntroduceStatue", 0);}
            if (js.get("IsIntroduceShown") == null){ js.put("IsIntroduceShown", 0);}

			log.info("营业开始时间：" + startTime);
			String start = "";
			if (startTime.length() >= 5) {//只保留时分，删除秒
				start = startTime.substring(0, 5);
			} else {
				start = "09:00";
			}

			js.put("OpenTime", start);
			String endTime = StringUtils.replaceNULLToStr(st.getEndTime());
			log.info("营业关门时间：" + endTime);
			String end = "";
			if (endTime.length() >= 5) {
				end = endTime.substring(0, 5);
			} else {
				end = "21:00";
			}

			js.put("CloseTime", end);
			if (st.getDistributionAmount() != null) {
				js.put("StartPrice", st.getDistributionAmount().toString().split("\\.")[0]);
			} else {
				js.put("StartPrice", "");
			}

			if (st.getDistributionTip() != null) {
				js.put("DeliveryFee", st.getDistributionTip().toString().split("\\.")[0]);
			} else {
				js.put("DeliveryFee", "");
			}

			js.put("DeliveryScope", StringUtils.replaceNULLToStr(st.getDistributionScope().toString().substring(0, st.getDistributionScope().toString().length() - 1)));
			js.put("LocationAddress", StringUtils.replaceNULLToStr(st.getLocationAddress()));
			js.put("BusinessStatus", StringUtils.replaceNULLToStr(st.getO2oStatus()));
			js.put("ReciveStatus", StringUtils.replaceNULLToStr(st.getO2oAutoOrder()));
			js.put("StoreAbbrevy", StringUtils.replaceNULLToStr(st.getSimpleName()));
			js.put("DeliveryType", StringUtils.replaceNULLToStr(st.getDeliveryMode()));
			js.put("FreeDeliveryFee", StringUtils.replaceNULLToStr(st.getDistributionFreeMoney().toString().substring(0, st.getDistributionFreeMoney().toString().length() - 3)));
			js.put("IsFreeDelivery", StringUtils.replaceNULLToStr(st.getFreightFlag()));
			js.put("ShareStatue", st.getUsedWithPlatfrom()+"");//v2.5.1
			
			String codeUrl = qrCodeService.getQRCodeUrl(jsonTemp.getString("StoreSerialNo"));
			js.put("StoreCodeUrl", codeUrl);
			json.put("Status", stateJson);
			json.put("mapdata", js);
		} else {
			json.put("Status", stateJson);
		}

		log.info( DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + "response to XCR_APP data is:  " + json + "用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}

    /**
	 * v2.5.1 设置商家优惠券于平台优惠券是否共用
	 * @author gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "SetShareStatue", method = RequestMethod.POST)
	public void setShareStatue(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SetShareStatue");
		JSONObject json = new JSONObject();
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		if (stateJson.getString("State").equals(STATE_OK)) {
			StoreO2ODto storeO2ODto = new StoreO2ODto();
			storeO2ODto.setUsedWithPlatfrom(jsonTemp.getInteger("ShareStatue"));
			storeO2ODto.setId(jsonTemp.getString("StoreSerialNo"));
			Long startTime = System.currentTimeMillis();
			log.info("于时间" + DateUtils.getLogDataTime(startTime, null) + "开始调用o2oDubboService.updateStore请求数据是：" + JSONObject.toJSONString(storeO2ODto));
			Response<Integer> result = o2oDubboService.updateStore(storeO2ODto);
			log.info("于时间" + DateUtils.getLogDataTime(startTime, null) + "结束调用o2oDubboService.updateStore响应数据是：" + JSONObject.toJSONString(result) + "\n耗时为:" + CommonUtil.costTime(startTime));
			if(result!=null){
				if (!result.isSuccess()){
					log.info("于时间" + DateUtils.getLogDataTime(startTime, null)+ "结束调用updateStore响应数据出错，返回:"+JSONObject.toJSONString(result));
					stateJson=CommonUtil.pageStatus2(StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
				}
			}else{
				stateJson=CommonUtil.pageStatus2(StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
				log.error("于时间" + DateUtils.getLogDataTime(startTime, null)+ "结束调用updateStore响应数据出错，返回为null");
			}
		}
		json.put("Status", stateJson);
		log.info("于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法" + jsonTemp.getString("method") + "执行结束！" + "\nresponse to XCR_APP data is:  " + json + "\n用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}


}
