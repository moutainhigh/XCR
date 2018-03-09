package com.yatang.xc.xcr.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import com.google.common.base.Charsets;
/**
 * 
 * @author caotao
 * 
 */
public class EncryptUtils {
	
	//private static final String defauleAppKey = "yatang.udc.pw.encrypt.key.addby.sivl";
	
	public static String YLPWDEncrypt(String inStr, String key) {
		byte[] _appkey = getAppKey(key);
		SecretKey deskey = new SecretKeySpec(_appkey, "DESede");
		try {
			Cipher cp = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cp.init(1, deskey);
			byte[] byteResul = cp.doFinal(inStr.getBytes());
			return byte2Base64(byteResul);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] getAppKey(String appKey) {
		byte[] bkey = appKey.getBytes();
		int start = (bkey.length > 24) ? 24 : bkey.length;
		byte[] bkey24 = new byte[24];
		for (int i = 0; i < start; ++i) {
			bkey24[i] = bkey[i];
		}
		for (int i = start; i < 24; ++i) {
			bkey24[i] = bkey[(i - start)];
		}
		return bkey24;
	}
	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); ++i) {
			int chr1 = str.charAt(i);
			if ((chr1 >= 19968) && (chr1 <= 171941))
				result = result + "\\u" + Integer.toHexString(chr1);
			else {
				result = result + str.charAt(i);
			}
		}
		return result;
	}
	public static String YLPWDDecode(String inStr, String key) {
		byte[] _appkey = getAppKey(key);
		SecretKey deskey = new SecretKeySpec(_appkey, "DESede");

		byte[] src = Base64.decodeBase64(inStr.getBytes());
		try {
			Cipher cp = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cp.init(2, deskey);
			byte[] result = cp.doFinal(src);
			return new String(result, Charsets.UTF_8);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String byte2Base64(byte[] b) {
		return new String(Base64.encodeBase64(b), Charsets.UTF_8);
	}
	public static void main(String[] args) {
		String test = "4ugigoo";
		test = chinaToUnicode(test);
		String key = "yatang.autoLogin.signKey.DS@JR";
		String ec = YLPWDEncrypt(test, key);
		System.out.println(ec);
		System.out.println(YLPWDDecode(ec, key));
		System.out.println(YLPWDDecode(
				"RYfc+Ncr7HJZJg6uJBDfHQ"	,	key));
	}
}
