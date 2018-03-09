package test.service.mission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.biz.mission.service.imp.BuryingPointStatisticsServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
public class TestBuryingPointStatisticsService {
	protected final Log							log		= LogFactory.getLog(this.getClass());

	@Autowired
	private BuryingPointStatisticsServiceImpl	service;
	private final SimpleDateFormat				format	= new SimpleDateFormat("yyyy-MM-dd");



	@Test
	public void testQuery() {
		List<Result> list = service.query("select * from \"/User/SignIn\"");
		for (Result re : list) {
			log.info("re : " + re.toString());
			for (Series serie : re.getSeries()) {
				for (Object value : serie.getValues()) {
					log.info("json : " + JSONObject.toJSONString(value));
				}
			}

		}
	}



	@Test
	public void testQueryByName() throws ParseException {
		Date start = format.parse("2017-07-10");
		Date end = format.parse("2017-07-11");
		service.queryByNameAndDate("/User/SignIn", start, end);
	}

}
