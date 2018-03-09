package com.yatang.xc.xcr.biz.mission.dubboservice.imp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.statistics.MissionStatisticsDto;
import com.yatang.xc.xcr.biz.mission.dto.statistics.StatisticsDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.StatisticsDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumError;
import com.yatang.xc.xcr.biz.mission.enums.EnumStatisticsItem;
import com.yatang.xc.xcr.biz.mission.service.BuryingPointStatisticsService;

@Service("statisticsDubboService")
@Transactional(propagation=Propagation.REQUIRED)
public class StatisticsDubboServiceImpl implements StatisticsDubboService {

	protected final Log						log			= LogFactory.getLog(this.getClass());

	@Autowired
	private BuryingPointStatisticsService	service;

	@Value("${Influxdb.statistics.namemap}")
	private String							namemapstr;

	private Map<String, String>				namemap;
	private final SimpleDateFormat			format		= new SimpleDateFormat("yyyy-MM-dd");
	private static final long				ONEDAYMM	= 1000 * 60 * 60 * 24 * 1;



	@PostConstruct
	private void init() {
		namemap = convertStrToMap(namemapstr);
	}



	@Override
	public Response<Map<String, StatisticsDto>> showStatisticsMap(Date startTime, Date endTime) {
		log.info("startTime:" + startTime + " endTime:" + endTime + " namemap:" + namemap);
		Map<String, StatisticsDto> statisticsMap = new HashMap<String, StatisticsDto>();
		Response<Map<String, StatisticsDto>> result = new Response<Map<String, StatisticsDto>>();
		result.setSuccess(false);
		if (startTime != null && endTime != null) {
			try {
				if (namemap != null && !namemap.isEmpty()) {
					for (String key : namemap.keySet()) {
						StatisticsDto dto = new StatisticsDto();
						dto.setItemCode(key);
						dto.setName(EnumStatisticsItem.toMap().get(key));
						Long count = service.queryByNameAndDate(namemap.get(key), startTime, endTime);
						long period = endTime.getTime() - startTime.getTime();
						Date lastStartTime = new Date(startTime.getTime() - period);
						Date lastEndTime = new Date(endTime.getTime() - period);
						Long lastCount = service.queryByNameAndDate(namemap.get(key), lastStartTime, lastEndTime);
						dto.setCount(count);

						if (count != null && lastCount != null && lastCount != 0) {
							dto.setIncrease(((count - lastCount) / (float) lastCount));
						}
						log.info("name:" + key + " count:" + count + " lastCount:" + lastCount + " increase:"
								+ dto.getIncrease());
						statisticsMap.put(key, dto);
					}
				}
				result.setResultObject(statisticsMap);
				result.setSuccess(true);
			} catch (Exception e) {
				result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
				result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
		return result;
	}



	@Override
	public Response<Map<String, StatisticsDto>> showStatisticsMap() {
		Response<Map<String, StatisticsDto>> result = new Response<Map<String, StatisticsDto>>();
		result.setSuccess(false);
		try {
			Date today = new Date();
			Date tomorrow = new Date(today.getTime() + ONEDAYMM);
			today = format.parse(format.format(today));
			tomorrow = format.parse(format.format(tomorrow));
			return showStatisticsMap(today, tomorrow);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}



	@Override
	public Response<Map<String, MissionStatisticsDto>> showMissionStatisticsMap() {
		Map<String, MissionStatisticsDto> statisticsMap = new HashMap<String, MissionStatisticsDto>();
		Response<Map<String, MissionStatisticsDto>> result = new Response<Map<String, MissionStatisticsDto>>();
		result.setSuccess(false);
		try {

			result.setResultObject(statisticsMap);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}



	private static Map<String, String> convertStrToMap(String mapText) {
		if (mapText == null || mapText.equals("")) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] keyValText = mapText.split(","); // 转换为数组
		for (String str : keyValText) {
			String[] keyValue = str.split("="); // 转换key与value的数组
			if (keyValue.length < 1) {
				continue;
			}
			String key = keyValue[0]; // key
			String value = keyValue[1]; // value
			map.put(key, value);
		}
		return map;
	}

}
