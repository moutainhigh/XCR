package com.yatang.xc.xcr.web.action;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.statistics.StatisticsDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.StatisticsDubboService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;


/**
 * 统计
 * 2017年7月11日(星期二)
 * @author gaodawei
 */
@Controller
@RequestMapping(value = "xcr/count")
public class DataCountAction {
	
	private static Logger log = LoggerFactory.getLogger(DataCountAction.class);

	@Autowired
	private StatisticsDubboService statisticsDubboService;
	
	@RequestMapping(value = "statisticsOperate", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView statisticsOperate(String param,Date startDate,Date endDate) throws UnsupportedEncodingException {
		log.info("\n***********前台请求的数据是param：   "+param+"    startDate:"+startDate+"     endDate:"+endDate);
		ModelAndView model=new ModelAndView();
		if(param!=null){
			switch(param){
			case "1":
				startDate=DateUtils.getDateBefore(new Date(), 0);
				endDate=DateUtils.getDateBefore(new Date(), -1);
				break;
			case "2":
				startDate=DateUtils.getDateBefore(new Date(), 1);
				endDate=DateUtils.getDateBefore(new Date(), 0);
				break;
			case "3":
				startDate=DateUtils.getDateBefore(new Date(), 7);
				endDate=DateUtils.getDateBefore(new Date(), 0);
				break;
			case "4":
				startDate=DateUtils.getDateBefore(new Date(), 30);
				endDate=DateUtils.getDateBefore(new Date(), 0);
				break;
			default:
				break;
			}
		}else{
			if(startDate!=null && endDate!=null){
				endDate=DateUtils.getDateBefore(endDate, -1);
				if((endDate.getTime()-startDate.getTime())>(24* 3600000)){
					param="5";
				}
			}else{
				startDate=DateUtils.getDateBefore(new Date(), 0);
				endDate=DateUtils.getDateBefore(new Date(), -1);
			}
		}
		Long startTime=System.currentTimeMillis();
		log.info("\n*****************调用statisticsDubboService.showStatisticsMap接口的开始时间："+DateUtils.getLogDataTime(startTime, null)
				+"\n*****************请求数据是：startDate="+startDate+"   endDate="+endDate);
		Response<Map<String, StatisticsDto>> result=statisticsDubboService.showStatisticsMap(startDate, endDate);
		Map<String, StatisticsDto> map=handleIncrease(result.getResultObject());
		log.info("\n*****************于时间:"+DateUtils.getLogDataTime(startTime, null)+"调用statisticsDubboService.showStatisticsMap接口   调用结束"
				+"\n*****************响应数据是："+map
				+ "\n***************所花费时间为：" + CommonUtil.costTime(startTime));
		if(result!=null){
			if(result.isSuccess()){
				model.addObject("jsonObj", map);
				model.addObject("condition", param);
			}
		}
		model.setViewName("screen/dataCount/StatisticsContent");
		return model;
	}
	
	public static Map<String, StatisticsDto> handleIncrease(Map<String, StatisticsDto> map){
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		 for (String in : map.keySet()) {
			 if(map.get(in).getIncrease()!=null){
				 map.get(in).setIncrease(Float.valueOf(decimalFormat.format(map.get(in).getIncrease()*100)));
			 }
		 }
		 log.info("\n*****************处理后的Map为："+JSONObject.toJSONString(map));
		 return map;
	}

}
