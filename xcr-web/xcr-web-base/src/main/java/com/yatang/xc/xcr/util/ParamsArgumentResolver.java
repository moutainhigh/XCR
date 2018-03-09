package com.yatang.xc.xcr.util;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.RedisKeyEnum;
import com.yatang.xc.xcr.exceptions.YTBusinessException;
import com.yatang.xc.xcr.model.Keys;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.xmlbeans.impl.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.security.PrivateKey;
/**
 * @Author : BobLee
 * @CreateTime : 2017年11月23日 上午11:43:44
 * @Summary : 密文报文解密
 */
public class ParamsArgumentResolver {

	public final static String DEVICE_ID = "DeviceId";
	public final static String TYPE = "Type";
	public final static String APPVERSION = "AppVersion";
	private static Logger logger = LoggerFactory.getLogger(ParamsArgumentResolver.class);
	
	/**
	 * <Summary>
	 * 			@param : 密文报文
	 * 			@return : 明文报文
	 * 			@Summary : 解密客户端参数
	 *  </Summary>
	 */
	public static String decryptReq(String body,String deviceId,String type) {
		if(StringUtils.isBlank(deviceId)){throw new YTBusinessException("DeviceId is not be empty ,plz check");}
		if(StringUtils.isBlank(body)){throw new YTBusinessException("body is not be empty ,plz check");}

        /**他们原来使用的是Key进行存储 后将Key移到base包下更名为Keys ，Key和Keys内容一模一样 只是报名变了*/
		Object keyForObject = SpringContext.getBean(RedisService.class).getKeyForObject(RedisKeyEnum.Key, deviceId, Keys.class);
		if(keyForObject  == null){ throw new YTBusinessException("The parameter {{DeviceId}} is illegal"); }

		try {
		    /**他们原来使用的是Key进行存储 后将Key移到base包下更名为Keys ，Key和Keys内容一模一样 只是报名变了*/
            Keys key = JSON.parseObject(JSON.toJSONString(keyForObject), Keys.class);
            /**请求报文URL解码*/
            String newMsg=URLDecoder.decode(body, "UTF-8");
			if( "1".equals(type)) { newMsg=URLDecoder.decode(newMsg, "UTF-8"); }

			String decryptMsg ="";
			try{
				/**将RSA 私钥解码*/
                byte[] decodePrivateKey = Base64.decode(key.getPrivateKey().getBytes());
                /**获取RSA私钥*/
                PrivateKey priKey = RSAHelper.decodePrivateKeyFromXml(com.yatang.xc.xcr.util.StringUtils.bytesToString(decodePrivateKey));
                /**RSA私钥解密请求报文*/
				 byte[] bt2 = RSAHelper.decryptData(Base64.decode(newMsg.getBytes()), priKey);
				/**请求报文 明文*/
				 decryptMsg = com.yatang.xc.xcr.util.StringUtils.bytesToString(bt2);
                /**Debug 日志*/
				logger.info(String.format( "The decodePrivateKey==> %s , decryptData ==> ",new String(decodePrivateKey),decryptMsg));
			}catch(Exception ex){
				try{
				    /**RSA 解密失败 则使用DES 解密*/
					decryptMsg = DESEncrype.decryptDES(newMsg, key.getPrivateKey());
                    logger.info(String.format( "The decodePrivateKey==> %s , decryptData ==> ",key.getPrivateKey(),decryptMsg));
				}catch(Exception desEx){
				    logger.error(ExceptionUtils.getStackTrace(desEx));
					throw new YTBusinessException( "The type DeviceId is: {{DeviceId}} DES解密失败");
				}
			}
			return decryptMsg;
		} catch (Exception e) {
			logger.error("body==>" + body +"  DeviceId ==>" + deviceId);
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new YTBusinessException("The parameter  is illegal");
		}
	}

}
