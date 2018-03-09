package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.datatable.TableDataResult;
import com.busi.common.resp.Response;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.QueryQrCodeOrderDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.QueryQrCodeOrderStatDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseQrCodeOrderDto;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseQrCodeOrderStatDto;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnDto;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnQuery;
import com.yatang.xc.xcr.biz.message.dubboservice.SwiftPassDubboService;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.NoDataClass;
import com.yatang.xc.xcr.util.StringUtils;

/** 
 * @author gaodawei 
 * @Date 2017年11月2日 下午2:16:07 
 * @version 1.0.0
 * @function 小超商家新加盟签约入驻
 */
@Controller
@RequestMapping("/User")
public class ScanPaymentAction {

	private static Logger log = LoggerFactory.getLogger(ScanPaymentAction.class);
	
	@Value("${SYSTEM_CODE}")
    String SYSTEM_CODE;
    @Value("${STATE_OK}")
    String STATE_OK;
    @Value("${STATE_ERR}")
    String STATE_ERR;
    @Value("${TOKEN_OUTTIME}")
    private Integer TOKEN_OUTTIME;

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
	private static String FOR_TEST_SKIP = "limit";

	@Autowired
	private DataCenterXcrDubboService DCXcrDubboService;
	private static String PAY_TYPE_ALIPAY="alipay";//支付方式：支付宝；PAY_TYPE_WEIXIN="weixin"

	@Autowired
	private SwiftPassDubboService swiftPassDubboService;

	/**
	 * 获取扫码支付分页流水
	 *
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/CollectTransactionList", method = RequestMethod.POST)
	public void getScanPaymentList(@RequestBody String msg, HttpServletResponse response) throws IOException {
		log.info("获取扫码支付分页流水 -> User/CollectTransactionList -> msg:" + msg);
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "Sign");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		json.put("Status", stateJson);
		if (!jsonTemp.getString("flag").equals(STATE_OK)) {
			response.getWriter().print(json);
			return;
		}
		String StoreSerialNo = jsonTemp.getString("StoreSerialNo");
		String pageNumberStr = jsonTemp.getString("PageIndex");
		String pageSizeStr = jsonTemp.getString("PageSize");
		log.info("获取扫码支付分页流水 -> User/CollectTransactionList -> StoreSerialNo:" + StoreSerialNo + "  pageNumberStr:" + pageNumberStr + " pageSizeStr:" + pageSizeStr);
		int pageNumber = StringUtils.isEmpty(pageNumberStr) ? DEFAULT_PAGE_NUMBER : Integer.valueOf(pageNumberStr);
		int pageSize = StringUtils.isEmpty(pageSizeStr) ? DEFAULT_PAGE_SIZE : Integer.valueOf(pageSizeStr);
		Response<SwiftPassReturnQuery> swiftPassResponse = swiftPassDubboService.getSwiftPassMessageList(StoreSerialNo, pageNumber, pageSize);
		if (swiftPassResponse == null) {
			log.error("获取扫码支付分页流水 -> swiftPassResponse == null");
			json = NoDataClass.addKeyValue(pageNumber, pageSize);
			response.getWriter().print(json);
			return;
		}
		if (!swiftPassResponse.isSuccess()) {
			log.error("获取扫码支付分页流水 -> swiftPassResponse.isSuccess = " + swiftPassResponse.isSuccess() + "  msg:" + swiftPassResponse.getErrorMessage());
			json = NoDataClass.addKeyValue(pageNumber, pageSize);
			response.getWriter().print(json);
			return;
		}
		SwiftPassReturnQuery swiftPassReturnQuery = swiftPassResponse.getResultObject();
		if (swiftPassReturnQuery == null) {
			log.error("获取扫码支付分页流水 -> swiftPassReturnQuery == null");
			json = NoDataClass.addKeyValue(pageNumber, pageSize);
			response.getWriter().print(json);
			return;
		}
		long totalCount = swiftPassReturnQuery.getTotalCount();
		List<SwiftPassReturnDto> swiftPassReturnDtos = swiftPassReturnQuery.getSwiftPassReturnDtoList();
		if (CollectionUtils.isEmpty(swiftPassReturnDtos)) {
			log.error("获取扫码支付分页流水 -> swiftPassReturnDtos == null or size == 0");
			json = NoDataClass.addKeyValue(pageNumber, pageSize);
			response.getWriter().print(json);
			return;
		}
		JSONObject listdata = new JSONObject();
		List<JSONObject> returnClasses = new ArrayList<>();
		for (SwiftPassReturnDto dto : swiftPassReturnDtos) {
			int totalFee = dto.getTotal_fee();
			String time = dto.getTime_end();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("CollectTransactionDaily", getCollectTransactionDaily(totalFee));
			jsonObject.put("Time", getTime(time));
			returnClasses.add(jsonObject);
		}
		listdata.put("rows", returnClasses);
		listdata = CommonUtil.pagePackage(listdata, jsonTemp, (int) totalCount, null);
		json.put("listdata", listdata);
		response.getWriter().print(json);
	}
	/**
	 * 获取扫码支付分页流水
	 *
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/NewCollectTransactionList", method = RequestMethod.POST)
	public void getNewScanPaymentList(@RequestBody String msg, HttpServletResponse response) throws IOException {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "NewCollectTransactionList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(StateEnum.STATE_0.getState()) || jsonTemp.getBooleanValue(FOR_TEST_SKIP)) {
			QueryQrCodeOrderStatDto paramDto=getQrCodeListParam(jsonTemp);
			long startTime=System.currentTimeMillis();
			log.info("\n*******于时间"+DateUtils.getLogDataTime(startTime, null)+"请求数据中心queryQrCodeOrderStat接口参数为："+JSONObject.toJSONString(paramDto));
			Response<TableDataResult<ResponseQrCodeOrderStatDto>> result=DCXcrDubboService.queryQrCodeOrderStat(paramDto);
			log.info("\n*******于时间"+DateUtils.getLogDataTime(startTime, null)+"请求数据中心queryQrCodeOrderStat接口结束"
					+"\n*******响应数据为："+JSONObject.toJSONString(result));
			if(result!=null){
				if(result.isSuccess()){
					JSONObject listdata = new JSONObject();
					List<JSONObject> rows = new ArrayList<>();
					if(result.getResultObject()!=null){
						for (ResponseQrCodeOrderStatDto dto : result.getResultObject().getData()) {
							Map<String, Object> map = new HashMap<>();
							map.put("CollectTransactionDaily", dto.getTotalFee());
							map.put("Time", DateUtils.stringToSimpleDateFormat(dto.getPayDate()).getTime());
							rows.add(StringUtils.replcNULLToStr(map));
						}
					}
					listdata.put("rows", rows); 
					listdata = CommonUtil.pagePackage(listdata, jsonTemp, result.getResultObject()!=null?result.getResultObject().getRecordsTotal():0, null);
					json.put("listdata", listdata);
				}else{
					stateJson=CommonUtil.pageStatus2(StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
				}
			}else{
				stateJson=CommonUtil.pageStatus2(StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
			}
		}
		json.put("Status", stateJson);
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}


	/**
	 * 收钱码交易详情v2.5
	 * @author gaodawei
	 * @param msg
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "CollectTransactionDetialList", method = RequestMethod.POST)
	public void collectTransactionDetialList(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "CollectTransactionDetialList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (jsonTemp.getString("flag").equals(StateEnum.STATE_0.getState()) || jsonTemp.getBooleanValue(FOR_TEST_SKIP)) {
			//假数据
			//json=ScanPaymentActionMock.getCollectTransactionDetialListMock(jsonTemp);
			//真数据
			QueryQrCodeOrderDto paramDto=getQrCodeDetailListParam(jsonTemp);
			long startTime=System.currentTimeMillis();
			log.info("\n*******于时间"+DateUtils.getLogDataTime(startTime, null)+"请求数据中心queryQrCodeOrder接口参数为："+JSONObject.toJSONString(paramDto));
			Response<TableDataResult<ResponseQrCodeOrderDto>> result=DCXcrDubboService.queryQrCodeOrder(paramDto);
			log.info("\n*******于时间"+DateUtils.getLogDataTime(startTime, null)+"请求数据中心queryQrCodeOrder接口结束"
					+"\n*******响应数据为："+JSONObject.toJSONString(result));
			if(result!=null){
				if(result.isSuccess()){
					json=packDetailList(jsonTemp,result);
				}else{
					stateJson=CommonUtil.pageStatus2(StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
				}
			}else{
				stateJson=CommonUtil.pageStatus2(StateEnum.STATE_2.getState(), StateEnum.STATE_2.getDesc());
			}
		}
		json.put("Status", stateJson);
		log.info("\n**********于" + DateUtils.getLogDataTime(null, jsonTemp.getDate("startExecuteTime")) + "  执行的方法"
				+ jsonTemp.getString("method") + "执行结束！" + "\n**********response to XCR_APP data is:  " + json
				+ "\n**********用时为：" + CommonUtil.costTime(jsonTemp.getDate("startExecuteTime").getTime()));
		response.getWriter().print(json);
	}


	/****************************以下是相关细节处理方法*******************************/
	/**
	 * 获得收钱码交易记录v2.5调用数据中心的请求参数
	 * @param jsonTemp
	 * @return
	 */
	private QueryQrCodeOrderStatDto getQrCodeListParam(JSONObject jsonTemp) {
		QueryQrCodeOrderStatDto paramDto=new QueryQrCodeOrderStatDto();
		paramDto.setShopCode(jsonTemp.getString("StoreSerialNo"));
		paramDto.setPageNum(jsonTemp.getIntValue("PageIndex"));
		paramDto.setPageSize(jsonTemp.getIntValue("PageSize"));
		return paramDto;
	}

	/**
	 * 获得收钱码交易明细v2.5调用数据中心的请求参数
	 * @param jsonTemp
	 * @return
	 */
	private QueryQrCodeOrderDto getQrCodeDetailListParam(JSONObject jsonTemp) {
		QueryQrCodeOrderDto paramDto=new QueryQrCodeOrderDto();
		paramDto.setShopCode(jsonTemp.getString("StoreSerialNo"));
		paramDto.setPageNum(jsonTemp.getIntValue("PageIndex"));
		paramDto.setPageSize(jsonTemp.getIntValue("PageSize"));
		paramDto.setPayDate(jsonTemp.getString("Date"));
		return paramDto;
	}
	/**
	 * 数据打包
	 * @param jsonTemp
	 * @param result
	 * @return
	 */
	private JSONObject packDetailList(JSONObject jsonTemp,Response<TableDataResult<ResponseQrCodeOrderDto>> result){
		JSONObject json = new JSONObject();
		JSONObject listdata = new JSONObject();
		List<JSONObject> rows = new ArrayList<>();
		if(result.getResultObject()!=null){
			for (ResponseQrCodeOrderDto dto : result.getResultObject().getData()) {
				Map<String, Object> map = new HashMap<>();
				map.put("CollectTransactionDaily", dto.getTotalFee());
				map.put("Date", jsonTemp.getString("Date"));
				map.put("Time", dto.getPayTime().substring(11));
				if(PAY_TYPE_ALIPAY.equals(dto.getTradeType())){
					map.put("PayType", 2);
				}else{
					map.put("PayType", 1);
				}
				rows.add(StringUtils.replcNULLToStr(map));
			}
		}
		listdata.put("rows", rows);
		listdata = CommonUtil.pagePackage(listdata, jsonTemp, result.getResultObject().getRecordsTotal(), null);
		JSONObject mapdata=new JSONObject();
		mapdata.put("Count", result.getResultObject().getRecordsTotal());
		json.put("listdata", listdata);
		json.put("mapdata", mapdata);
		return json;
	}
	
	/**
     * 获取显示金额
     *
     * @param totalFee
     * @return
     */
    private String getCollectTransactionDaily(int totalFee) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((double) totalFee / 100);
    }
    /**
     * 获取时间撮
     *
     * @param time
     * @return
     */
    private long getTime(String time) {
        String dateStr = getDateStr(time);
        if (StringUtils.isEmpty(dateStr)) {
            return 0;
        }
        Date date = DateUtils.stringToDefaultDateFormat(dateStr);
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }
    private String getDateStr(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        if (time.length() != 14) {
            return null;
        }
        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);
        String hour = time.substring(8, 10);
        String minute = time.substring(10, 12);
        String second = time.substring(12, 14);
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
	/**********************************************************************/
}