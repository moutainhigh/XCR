package com.yatang.xc.xcr.web.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.yatang.xc.xcr.web.report.MyInfluxdbReporter;

import metrics_influxdb.HttpInfluxdbProtocol;
import metrics_influxdb.InfluxdbProtocol;

/**
 * 埋点数据记录处理拦截器 Created by wangyang on 2017/7/1.
 */
public class BuryingPointInterceptor extends RequestInterceptor {

	private MetricRegistry metrics = null;
	private static Logger log = LoggerFactory.getLogger(BuryingPointInterceptor.class);

	private String username;
	private String password;

	private String host;
	private Integer port;

	private InfluxdbProtocol protocol = null;
	private ScheduledReporter reporter = null;
	private int reporterMinutes = 10;

	private Map<String, Counter> counterMap = new HashMap<String, Counter>();

	private static final String GOODS_DETAIL_QUERY = "/User/GoodsDetial";

	/**
	 * 初始化 metric 做统计数据库连接
	 */
	public void init() {
		try {
			metrics = new MetricRegistry();
			protocol = new HttpInfluxdbProtocol(host, port, username, password);
			log.info("HttpInfluxdbProtocol host:" + host + " port:" + port + " username:" + username + " password:"
					+ password);
			reporter = MyInfluxdbReporter.myForRegistry(metrics).protocol(protocol).convertRatesTo(TimeUnit.SECONDS)
					.convertDurationsTo(TimeUnit.MILLISECONDS).filter(MetricFilter.ALL).skipIdleMetrics(false).build();
			// reporter.start(1, TimeUnit.MINUTES);
			reporter.start(reporterMinutes, TimeUnit.MINUTES);
			log.info("BuryingPointInterceptor metrics: init");
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
	}



	/**
	 * 对加了BuryingPoint 注解的方法做调用次数统计
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		if (handler instanceof HandlerMethod) {
			// 做注解校验，如果没有注解不做记录
			HandlerMethod h = (HandlerMethod) handler;
			BuryingPoint bp = h.getMethodAnnotation(BuryingPoint.class);
			if (null == bp) {
				return;
			}
			String name = request.getRequestURI();
			String error = response.getHeader("errorStatus");

			if (response.getStatus() == 200 && StringUtils.isEmpty(error)) {
				count(name);
			}
			if (name.endsWith(GOODS_DETAIL_QUERY)) {
				String query = response.getHeader("STATISTICS_QUERY");
				log.info("response header param:" + query);
				if (!StringUtils.isEmpty(query)) {
					log.info("count name:" + name + "/" + query);
					count(name + "/" + query);
				}
			}
		}

	}



	private void count(String name) {
		Counter counter = null;
		if (counterMap.containsKey(name)) {
			counter = counterMap.get(name);
		} else {
			counter = metrics.counter(MetricRegistry.name(name));
			counterMap.put(name, counter);
		}
		counter.inc();
	}


	public int getReporterMinutes() {
		return reporterMinutes;
	}

	public void setReporterMinutes(int reporterMinutes) {
		this.reporterMinutes = reporterMinutes;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public InfluxdbProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(InfluxdbProtocol protocol) {
		this.protocol = protocol;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
