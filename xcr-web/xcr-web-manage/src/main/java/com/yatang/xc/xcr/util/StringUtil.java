package com.yatang.xc.xcr.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.util.DateUtils;

/**
 * 
 * TODO<字符串操作>
 * 
 * @data: 2015-8-24 下午2:56:43
 */
public class StringUtil {
	private static Logger log = LoggerFactory.getLogger(StringUtil.class);
	public final static String UTF_8 = "utf-8";

	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 把null转成成空字符串
	 * @param obj
	 * @return
	 */
	public static String replaceNULLToStr(Object obj) {
		if (obj == null || "null".equals(obj + "")) {
			return "";
		}
		return obj.toString();
	}

	public static JSONObject replcNULLToStr(Map<String, Object> map) {
		String str = "";
		for (String key : map.keySet()) {
			if (map.get(key) == null) {
				map.put(key, str);
			}
		}
		JSONObject jsonObj = JSONObject.parseObject(JSONObject.toJSONString(map));
		return jsonObj;
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * byte 数组转换为 String.
	 * @return byte[]
	 */
	public static String bytesToString(byte[] encrytpByte) {
		String result = "";
		for (Byte bytes : encrytpByte) {
			result += (char) bytes.intValue();
		}
		return result;
	}

	/**
	 * 判断是否为手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		return phoneNumber.matches("^1[3,4,5,6,8,7]\\d{9}$");
	}

	/**
	 * 在打印列表日志的时候，，，只打印小于等于前300个字符
	 */
	public static void printLogMethod(Object res, Long timeS, String path) {
		if (res != null) {
			JSONObject res2 = JSONObject.parseObject(JSONObject.toJSONString(res));
			int i = res2.toString().length();
			if (i > 300) {
				log.info("\n*****************于时间:" + DateUtils.getLogDataTime(timeS, null) + "调用" + path + "接口   调用结束"
						+ "\n*****************响应数据是：" + res2.toString().substring(0, 300) + "前300个返回字符"
						+ "\n***************所花费时间为：" + CommonUtil.costTime(timeS));
			} else {
				log.info("\n*****************于时间:" + DateUtils.getLogDataTime(timeS, null) + "调用" + path + "接口   调用结束"
						+ "\n*****************响应数据是：" + res2.toString().substring(0, i) + "\n***************所花费时间为："
						+ CommonUtil.costTime(timeS));
			}
		}
	}
}
