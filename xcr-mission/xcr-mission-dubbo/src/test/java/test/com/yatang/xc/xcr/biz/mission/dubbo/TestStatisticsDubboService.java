package test.com.yatang.xc.xcr.biz.mission.dubbo;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.statistics.StatisticsDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.StatisticsDubboService;

/**
 * 
 * StatisticsDubboService 测试类
 * 
 * @author : zhaokun
 * @date : 2017年3月27日 下午7:19:58
 * @version : 2017年3月27日 zhaokun
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// {"classpath:test/dubbo/applicationContext-consumer.xml"})
@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
public class TestStatisticsDubboService {
	protected final Log				log	= LogFactory.getLog(this.getClass());

	@Autowired
	private StatisticsDubboService	service;

	private final SimpleDateFormat			format		= new SimpleDateFormat("yyyy-MM-dd");


	@Test
	public void showStatisticsMap() {

		Response<Map<String, StatisticsDto>> result = service.showStatisticsMap();
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
		log.info("result:" + JSON.toJSONString(result.getResultObject()));
	}
	
	@Test
	public void showStatisticsMapByDate() throws ParseException {
		Date start = format.parse("2017-07-20");
		Date end = format.parse("2017-07-21");
		Response<Map<String, StatisticsDto>> result = service.showStatisticsMap(start,end);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
		log.info("result:" + JSON.toJSONString(result.getResultObject()));
	}
}
