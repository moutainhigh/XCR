package com.yatang.xc.xcr.biz.mission.service.imp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.biz.mission.influxdb.InfluxdbBuilder;
import com.yatang.xc.xcr.biz.mission.service.BuryingPointStatisticsService;

@Service
public class BuryingPointStatisticsServiceImpl implements BuryingPointStatisticsService {

	protected final Log				log			= LogFactory.getLog(this.getClass());

	@Value("${Influxdb.database}")
	private String					database;

	@Autowired
	private InfluxdbBuilder			influxdbBuilder;

	private final SimpleDateFormat	utcFormat	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");



	@PostConstruct
	public void init() {
		TimeZone utcZone = TimeZone.getTimeZone("UTC");
		utcFormat.setTimeZone(utcZone);
	}



	public List<Result> query(String command) {
		QueryResult result = influxdbBuilder.build().query(new Query(command, database));
		List<Result> list = result.getResults();
		return list;
	}



	public long queryByNameAndDate(String name, Date startDate, Date endDate) {
		Long count = 0l;
		if (startDate != null && endDate != null) {
			String start = utcFormat.format(startDate);
			String end = utcFormat.format(endDate);
			StringBuilder query = new StringBuilder();
			query.append("select sum(count) from \"");
			query.append(name);
			query.append("\" where time >= '");
			query.append(start);
			query.append("' and time < '");
			query.append(end);
			query.append("'");

			// String querystr = "select DIFFERENCE(count) from \"" + name + "\"
			// where time >= '" + start + "' and time <= '"
			// + end + "'";
			log.info("query : " + query.toString());
			List<Result> results = query(query.toString());
			log.info("list : " + JSONObject.toJSONString(results));

			for (Result re : results) {
				if (re.getSeries() != null) {
					for (Series serie : re.getSeries()) {
						for (Object value : serie.getValues()) {
							if (value instanceof List) {
								List<?> list = (List<?>) value;
								if (list.size() > 1 && list.get(1) instanceof Double) {
									count += ((Double) list.get(1)).longValue();
								}
							}
						}
					}
				}
			}
		}
		log.info("count : " + count);
		return count;

	}



	public String getDatabase() {
		return database;
	}



	public void setDatabase(String database) {
		this.database = database;
	}

}
