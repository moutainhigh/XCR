package com.yatang.xc.xcr.util;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月22日 15:16
 * @Summary :
 */
public final class Assert {

	public  static long formatDateToDateStamp(String formatDate){
		isNotEmpty(formatDate);
		try {
			return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formatDate).getTime();
		} catch (Exception e) {
            throw new  IllegalArgumentException(e);
		}
	}

    /** 拼接任意多个字符串 */
    public static String append(String... appendStrs) {
        if (appendStrs != null && appendStrs.length > 0) {
            if (appendStrs.length == 1) {
                return appendStrs[0];
            }

            StringBuilder append = new StringBuilder(appendStrs.length);
            for (String appendStr : appendStrs) {
                if(StringUtils.isNotBlank(appendStr)){
                    append.append(appendStr);
                }
            }
            return append.toString().trim();
        }
        return null;
    }

	/**为空则扔出异常信息为默认信息*/
	public static  void isNotEmpty(String text){
		isNotEmpty(text,"参数为空");//The params is not be empty
	}

	public  static void isNotEmpty(String text,String message){
		if (StringUtils.isBlank(text)) { throw new  IllegalArgumentException(message); }
	}

	/**1为真0为假*/
	public static int bool2Int(boolean boll){
		if(boll){ return 1; }
		return 0;
	}

	/**1为真0为假*/
	public static boolean string2Bool(String str){
		isNotEmpty(str);
		try {
			return Boolean.parseBoolean(str);
		} catch (Exception e) {
			if("1".equals(str.trim())){ return true; }
			if("0".equals(str.trim())){ return false; }
		}
		return false;
	}

	public static void isNotEmpty(Serializable obj){
		if(ObjectUtils.isNull(obj)){ throw new IllegalArgumentException("参数为空 ==> "  + obj.getClass().getSimpleName()); }
	}

	/** 设置Map值 */
	public static  void transfor(Map<String, Object> map, Map<String, Object> params) {
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			params.put(key, map.get(key));
		}
	}

	/**Object toString*/
	public static  String toString(Object object){
		if(object ==  null){ return null; }
		return object.toString().trim();
	}

	/**
	 * 把null转成成空字符串
	 * @param obj
	 * @return
	 */
	public static String toStr(Object obj) {
		if (obj == null || "null".equals(obj + "")) { return ""; }
		return obj.toString().trim();
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) { if (!Character.isDigit(str.charAt(i))) { return false; } }
		return true;
	}

	/** String 2 BigDecimal*/
	public static BigDecimal convert(String number){
		if(StringUtils.isBlank(number)){return BigDecimal.ZERO;}
		try {
			if("0.0".equals(number) || "0.00".equals(number) || "0".equals(number)){ return BigDecimal.ZERO; }
			if(number.indexOf(".") == -1 && !number.contains(".")){ return new BigDecimal(number); }
			Double.parseDouble(number);
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			BigDecimal doubleNumber = new BigDecimal(df.format(number));
			return doubleNumber.setScale(2, BigDecimal.ROUND_DOWN);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("输入字符串并非数字 ==>  "+ number);
		}
	}

	/** String 2 BigDecimal*/
	public static BigDecimal conver(String number){
		if(StringUtils.isBlank(number)){throw new IllegalArgumentException("输入为空无法进行数值转换");}
		return  convert(number);
	}

}
