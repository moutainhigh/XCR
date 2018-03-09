package com.yatang.xc.xcr.biz.core.dubboservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @描述: 配置文件读取对象
 * @作者: huangjianjun
 * @创建时间: 2017年4月7日-下午5:35:32 .
 * @版本: 1.0 .
 * @param <T>
 */
public class Prop {
	private Properties properties;

	public Prop(String fileName) {
		this(fileName, "UTF-8");
	}

	public Prop(String fileName, String encoding) {
		this.properties = null;

		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (inputStream == null) throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
			this.properties = new Properties();
			this.properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public Prop(File file) {
		this(file, "UTF-8");
	}

	public Prop(File file, String encoding) {
		this.properties = null;

		if (file == null) throw new IllegalArgumentException("File can not be null.");
		if (!(file.isFile())) { throw new IllegalArgumentException("File not found : " + file.getName()); }
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			this.properties = new Properties();
			this.properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public String get(String key) {
		return this.properties.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}

	public Integer getInt(String key) {
		return getInt(key, null);
	}

	public Integer getInt(String key, Integer defaultValue) {
		String value = this.properties.getProperty(key);
		return Integer.valueOf((value != null) ? Integer.parseInt(value)
				: defaultValue.intValue());
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		String value = this.properties.getProperty(key);
		return Long.valueOf((value != null) ? Long.parseLong(value)
				: defaultValue.longValue());
	}

	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = this.properties.getProperty(key);
		return Boolean.valueOf((value != null) ? Boolean.parseBoolean(value)
				: defaultValue.booleanValue());
	}

	public boolean containsKey(String key) {
		return this.properties.containsKey(key);
	}

	public Properties getProperties() {
		return this.properties;
	}
}