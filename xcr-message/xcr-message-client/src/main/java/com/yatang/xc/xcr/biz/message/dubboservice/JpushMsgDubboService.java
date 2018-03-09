package com.yatang.xc.xcr.biz.message.dubboservice;

import com.busi.common.resp.Response;

import java.util.List;
import java.util.Map;

/**
 * @描述: jpush消息推送服务接口
 * @作者: huangjianjun
 * @创建时间: 2017年4月7日-上午11:35:28 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface JpushMsgDubboService {
	/**
	* @Description: 推送通知至所有平台
	* @author huangjianjun
	* @date 2017年4月7日 上午11:37:04
	* @param title 推送标题 
    * @param content 推送内容 
	*/
	Response sendPushAll(String title, String content, Map<String, String> extras);
	
	/**
	* @Description: 定向推送通知至设备终端ID 
	* @author huangjianjun
	* @date 2017年4月7日 上午11:37:24
	* @param title 推送标题 
    * @param msgContent 推送内容 
	*/
	Response sendPushByRegesterId(List<String> regeSterIds, String title, String msgContent, Map<String, String> extras);
	
	/**
	* @Description: 推送消息至所有平台
	* @author huangjianjun
	* @date 2017年4月7日 上午11:37:04
	* @param title 推送标题 
    * @param content 推送内容 
	*/
	Response sendPushMsgAll(String title, String content);
	
	/**
	 * @Description: 定向推送消息至设备终端ID 
	 * @author huangjianjun
	 * @date 2017年4月7日 上午11:37:24
	 * @param title 推送标题 
	 * @param msgContent 推送内容 
	 */
	Response sendPushMsgByRegesterId(List<String> regeSterIds, String title, String msgContent);
}
