package com.yatang.xc.xcr.util; 

import java.security.MessageDigest;
import org.apache.commons.lang.StringUtils;

/** 
* @function 
* @author  作者 : gaodawei
* @Email   邮箱 : 695390275@qq.com
* @date    创建时间：2017年12月27日 下午7:45:10 
* @version 1.0 
 */
public class MD5EncryptUtil {
	
	@SuppressWarnings("unused")
	public static String compute(String inStr) {
		if(StringUtils.isEmpty(inStr)){
			return "";
		}
		MessageDigest md5Digest=null;
		try {
			md5Digest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = inStr.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++){
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
