package com.yatang.xc.xcr.biz.core.converter;

import java.util.Date;

import org.apache.commons.beanutils.converters.DateTimeConverter;

/**
 * @描述: 日期转换器
 * @作者: huangjianjun
 * @创建时间: 2017年4月12日-下午4:23:30 .
 * @版本: 1.0 .
 * @param <T>
 */
public class DateConverter extends DateTimeConverter {

	public DateConverter() {
	}

	public DateConverter(Object defaultValue) {
		super(defaultValue);
	}


	@SuppressWarnings("rawtypes")
	protected Class getDefaultType() {
		return Date.class;
	}

	/*
	 * @see
	 * org.apache.commons.beanutils.converters.DateTimeConverter#convertToType
	 * (java.lang.Class, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected Object convertToType(Class arg0, Object arg1) throws Exception {
		if (arg1 == null) {
			return null;
		}
		String value = arg1.toString().trim();
		if (value.length() == 0) {
			return null;
		}
		return super.convertToType(arg0, arg1);
	}
}
