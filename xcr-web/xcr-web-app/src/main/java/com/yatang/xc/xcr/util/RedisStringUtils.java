package com.yatang.xc.xcr.util;

public class RedisStringUtils {
	
	/***
	 * 
	 * 
	 * 功能说明：判断字符串是否为空
	 * @author liuli
	 * @createTime  2017年3月7日上午10:04:12
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null){
			return true;
		} 
		if("".equals(str) || "".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 
	 * 功能说明：判断字符串是否不为空
	 * @author liuli
	 * @createTime  2017年3月7日上午10:04:23
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	/**
	 * 
	 * 
	 * 功能说明：字符串转Integer
	 * @author liuli
	 * @createTime  2017年3月7日上午10:12:03
	 * @param str
	 * @return
	 */
	public static Integer stringToInteger(String str){
		if(isEmpty(str)){
			return null;
		}
		try{
			 return Integer.parseInt(str);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 
	 * 
	 * 功能说明：字符串转Long
	 * @author liuli
	 * @createTime  2017年3月7日上午10:12:21
	 * @param str
	 * @return
	 */
	public static Long stringToInLong(String str){
		if(isEmpty(str)){
			return null;
		}
		try{
			 return Long.parseLong(str);
		}catch(Exception e){
			return null;
		}
	}
}
