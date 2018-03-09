package com.yatang.xc.xcr.biz.message.dubboservice.impl;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.message.dubboservice.JpushMsgDubboService;
import com.yatang.xc.xcr.biz.message.enums.PublicEnum;
import com.yatang.xc.xcr.biz.message.util.JPushUtil;
import com.yatang.xc.xcr.biz.message.util.PropUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @描述: jpush消息推送服务
 * @作者: huangjianjun
 * @创建时间: 2017年4月7日-上午11:46:26 .
 * @版本: 1.0 .
 * @param <T>
 */
@Service("jpushMsgDubboService")
public class JpushMsgDubboServiceImpl implements JpushMsgDubboService {
	protected final Log log = LogFactory.getLog(this.getClass());

	private final static String appKey = PropUtil.use("jpush.properties").get("jpush.app.key");

	private final static String masterSecret = PropUtil.use("jpush.properties").get("jpush.master.secret");

	private final static String tag = PropUtil.use("jpush.properties").get("jpush.tag");

	/**
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
	 */
	private static long timeToLive = 60 * 60 * 24;

	private static JPushClient jPushClient = null;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes", "finally" })
	public  Response sendPushAll(String title,String content,Map<String, String> extras) {
		Response res = new Response();
		jPushClient = new JPushClient(masterSecret, appKey);
		try {
			jPushClient.sendPush(JPushUtil.buildPushAndroidObjectAllAlert(tag,content,extras));
			jPushClient.sendPush(JPushUtil.buildPushIOSObjectAllAlert(tag,content,extras));
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(null);
		} catch (Exception e) {
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_PUSH_MSG.getCode());
			log.error(ExceptionUtils.getFullStackTrace(e));
		} finally{
			return res;
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "finally", "unchecked" })
	public Response sendPushByRegesterId(List<String> regeSterIds,String title,String msgContent,Map<String, String> extras) {
		Response res = new Response();
		jPushClient = new JPushClient(masterSecret, appKey);
		try {
//	            PushPayload payload = JPushUtil.buildPushObjectByRegesterIds(regeSterIds,title,msgContent);
			PushPayload payload = JPushUtil.buildPushByRegesterIds(regeSterIds,title,msgContent,extras);
			PushResult pushResult = jPushClient.sendPush(payload);
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(pushResult);
		} catch (Exception e) {
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_PUSH_MSG.getCode());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}finally{
			return res;
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "finally", "unchecked" })
	public Response sendPushMsgAll(String title, String content) {
		Response res = new Response();
		jPushClient = new JPushClient(masterSecret, appKey);
		try {
			PushPayload payload = JPushUtil.buildPushObjectAllMessage(title, content);
			PushResult result = jPushClient.sendPush(payload);
//          PushResult result = jPushClient.sendMessageAll("全平台消息推送");
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(null);
		}catch (Exception e) {
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_PUSH_MSG.getCode());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}finally{
			return res;
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "finally", "unchecked" })
	public Response sendPushMsgByRegesterId(List<String> regeSterIds,
											String title, String msgContent) {
		Response res = new Response();
		jPushClient = new JPushClient(masterSecret, appKey);
		try {
			for (String registrationID : regeSterIds) {
				jPushClient.sendMessageWithRegistrationID(title, msgContent, registrationID);
			}
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(null);
		} catch (Exception e) {
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_PUSH_MSG.getCode());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}finally{
			return res;
		}
	}

}
