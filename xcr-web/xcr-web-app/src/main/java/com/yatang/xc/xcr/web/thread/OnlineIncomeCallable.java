package com.yatang.xc.xcr.web.thread;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.DataCenterXcrDubboService;
import com.yatang.xc.dc.biz.facade.dubboservice.xcr.dto.ResponseFinanceByShopAndDateWithXcOrderDto;
import com.yatang.xc.xcr.util.DateUtils;

/**
 * 
* 店铺收入
*		
* @author: zhongrun
* @version: 1.0, 2017年8月11日
 */
@Scope("prototype")
@Service("onlineIncomeCallable")
public class OnlineIncomeCallable implements Callable<ResponseFinanceByShopAndDateWithXcOrderDto>{
	
	private static Logger log = LoggerFactory.getLogger(OnlineIncomeCallable.class);
	
	@Autowired
	private DataCenterXcrDubboService dataCenterXcrDubboService;
	
	public OnlineIncomeCallable(){
		
	}
	
	public OnlineIncomeCallable(String storeNo, String startTime, String endTime) {
		super();
		this.storeNo = storeNo;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	private String storeNo;
	
	private String startTime;
	
	private String endTime;
	

	public String getStoreNo() {
		return storeNo;
	}


	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	@Override
	public ResponseFinanceByShopAndDateWithXcOrderDto call() throws Exception {
		Long start = System.currentTimeMillis();
		log.info("\n*****Start At：" + DateUtils.getLogDataTime(start, null)+ "Call Data Center QueryFinanceByShopAndDateWithXcOrder Interface"
				+"This Thread QueryFinanceByShopAndDateWithXcOrder Request Data Is:"+JSONObject.toJSONString("StoreNo:"+storeNo+"StartTime:"+startTime+"EndTime:"+endTime));
		Response<ResponseFinanceByShopAndDateWithXcOrderDto>  currentDateDubboResult = dataCenterXcrDubboService.queryFinanceByShopAndDateWithXcOrder(storeNo, startTime, endTime);
		log.info("This Thread QueryFinanceByShopAndDateWithXcOrder Response Data Is:"+JSONObject.toJSONString(currentDateDubboResult));
		Long end = System.currentTimeMillis();
		log.info("This Thread QueryFinanceByShopAndDateWithXcOrder Tesponse Time is:"+(end-start));
		if (!currentDateDubboResult.isSuccess()) {
			log.error("Call Dubbo Error Msg Is:"+currentDateDubboResult.getErrorMessage());
			return null;
		}
		return currentDateDubboResult.getResultObject();
	}

}
