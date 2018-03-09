package com.yatang.xc.xcr.biz.mission.influxdb;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfluxdbBuilder {

	@Value("${Influxdb.username}")
	private String		username;
	@Value("${Influxdb.password}")
	private String		password;
	@Value("${Influxdb.url}")
	private String		url;

	private InfluxDB	influxDB;

	protected final Log	log	= LogFactory.getLog(this.getClass());

	public InfluxdbBuilder(){
			
	}

	public InfluxdbBuilder(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}



	public InfluxDB build() {
		try {
			if (influxDB == null) {
				synchronized (this) {
					if (influxDB == null) {
						influxDB = InfluxDBFactory.connect(url, username, password);
					}
				}
			}
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return influxDB;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}
}
