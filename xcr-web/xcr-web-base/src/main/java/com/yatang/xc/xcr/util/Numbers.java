package com.yatang.xc.xcr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.busi.common.exception.BusinessException;

/**
 * @Author : BobLee
 * @CreateTime : 2017年09月23日 上午11:24:26
 * @Summary : 数值相关
 */
public class Numbers {

	/**将字符串小数点前的数字转换成整型*/
	public static Integer convert(String str) {
		if(str == null || StringUtils.isBlank(str))return null;
		if(isFolat(str)) {// 浮点型
			str = str.trim().substring(0, str.indexOf("."));
		}
		if(str.contains(".") || str.indexOf(".") != -1) {// 非数值型
			str = str.trim().substring(0, str.indexOf("."));
		}
		if(isNumeric(str)) {
			return Integer.parseInt(str);
		}
		return null;
	}
	
	/**如果前面的数字为null则返回后面的数字*/
	public static Integer isNullReplace(Integer number,Integer replacenUmber) {
		if(number != null) {return  number;}
		return replacenUmber;
	}

	/**判断字符串是否是数字（整型）浮点型请出门左转*/ 
	public static boolean isNumeric(String str) {
		if(str != null && !StringUtils.isBlank(str)) {
			Matcher isNum = Pattern.compile("[0-9]*").matcher(str.trim());
			if( !isNum.matches() ){
				return false;
			} 
			return true;
		}
		throw new BusinessException("500","Input number is not be empty");
	}
	
	/**判断字符串是否是浮点型数字（浮点型）{@link #isFolat(String)}*/ 
	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException ex){
			return false;
		}
	}
	
	/**判断字符串是否是浮点型数字（浮点型）{@link #isDouble(String)} */ 
	public static boolean isFolat(String str) {
		if(str != null && !StringUtils.isBlank(str)) {
			Matcher isNum = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$").matcher(str.trim());
			if( !isNum.matches() ){
				return false;
			}
			return true;
		}
		return false;
	}

}
