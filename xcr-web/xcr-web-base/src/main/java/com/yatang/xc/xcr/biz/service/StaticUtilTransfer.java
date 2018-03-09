package com.yatang.xc.xcr.biz.service;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 配置静态工具类
 * 
 * @author : zhaokun
 * @date : 2017年12月6日 下午6:24:48
 * @version : 2017年12月6日 zhaokun
 */
public class StaticUtilTransfer {

	private Logger				log	= LoggerFactory.getLogger(StaticUtilTransfer.class);

	private Map<String, Object>	propertieMap;



	public Map<String, Object> getPropertieMap() {
		return propertieMap;
	}



	public void setPropertieMap(Map<String, Object> propertieMap) {
		this.propertieMap = propertieMap;
	}



	public void init() throws Exception {
		log.info("init start : propertieMap:" + propertieMap);
		if (propertieMap != null && !propertieMap.isEmpty()) {
			for (String key : propertieMap.keySet()) {

				log.info("start set key: " + key);
				int length = key.lastIndexOf('.');
				String className = key.substring(0, length);
				Class<?> threadClazz = Class.forName(className);
				String propertieName = key.substring(length + 1);
				Field fieldUrl = threadClazz.getDeclaredField(propertieName);
				fieldUrl.setAccessible(true);// 只有这里设置为true才可以修改
				Object value = castFromString(fieldUrl.getType(), propertieMap.get(key));
				fieldUrl.set(propertieName, value);
				log.info("end set as:\t" + value + "\t for:" + propertieName);

			}
			propertieMap.clear();
		}
		log.info("init end");
	}



	private Object castFromString(Class type, Object value) {
		if (!type.isInstance(value)) {
			String fileType = type.getName();
			if (fileType.equals("java.lang.Boolean")) {
				value = Boolean.valueOf((String) value);
			} else if (fileType.equals("java.lang.Integer")) {
				value = Integer.valueOf((String) value);
			} else if (fileType.equals("java.lang.Long")) {
				value = Long.valueOf((String) value);
			} else if (fileType.equals("java.lang.Double")) {
				value = Double.valueOf((String) value);
			} else if (fileType.equals("java.lang.Short")) {
				value = Short.valueOf((String) value);
			} else {
				log.warn("Undefined file type:"+fileType);
				value = type.cast(value);
			}
		}
		return value;
	}

}
