package com.yatang.xc.xcr.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.distribute.locks.zk.ReentrantZkDistributeLock;
import com.busi.distribute.locks.zk.ZkDistributeLockFactory;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganzitionXCRBusinessDubboService;
import com.yatang.xc.mbd.biz.org.xcr.dto.XcrStoreTicketDto;
import com.yatang.xc.xcr.biz.service.RedisService;
import com.yatang.xc.xcr.enums.RedisKeyEnum;
import com.yatang.xc.xcr.service.QRCodeService;
import com.yatang.xc.xcr.util.HttpClientUtil;

/**
 * 
* <获取二维码>
*		
* @author: zhongrun
* @version: 1.0, 2017年10月16日
 */
@Service("qrCodeService")
public class QRCodeServiceImpl implements QRCodeService{
	

	@Value("${STATE_OK}")
	String										STATE_OK;
	@Value("${APP_ID}")
	private String								APP_ID;
	@Value("${SECRETE}")
	private String								SECRETE;
	@Value("${zk.address}")
	private String								zkAddress;
	@Value("${BASE_NODE_PATH}")
	private String								BASE_NODE_PATH;
	@Value("${CREATE_TOKEN}")
	private String								CREATE_TOKEN;
	@Value("${CREATE_QRCODE}")
	private String								CREATE_QRCODE;

	private static Logger						log	= LoggerFactory.getLogger(QRCodeServiceImpl.class);

	@Autowired
	private OrganzitionXCRBusinessDubboService	organzitionXCRBusinessDubboService;
	
	@Autowired
	private RedisService<String> resRedisService;




	/**
	 * 
	* <保存二维码信息并返回url>
	*
	* @param storeNo
	* @param token
	* @return url
	* @throws Exception
	 */
	private String saveQRInfo(String storeNo, String token) throws Exception {
		String url="";
		String qrCodeInfo = createQRCode(token, storeNo);
		JSONObject jsoninfo = JSONObject.parseObject(qrCodeInfo);
		String ticket = jsoninfo.getString("ticket");
		if (ticket!=null) {
			url = jsoninfo.getString("url");
			XcrStoreTicketDto arg0 = new XcrStoreTicketDto();
			arg0.setStoreId(storeNo);
			arg0.setTicket(ticket);
			arg0.setTicketUrl(url);
			// 时效永久arg0.setExpireTime(null);
			log.info("Call updateStoreTicket Request Data Is:");
			Response<Integer> updateDubboResult = organzitionXCRBusinessDubboService.updateStoreTicket(arg0);
			log.info("Call updateStoreTicket Response Data Is:"+JSONObject.toJSONString(updateDubboResult));
			if (!updateDubboResult.isSuccess()) {
				log.error("Call updateDubboResult Failed!!");
			}
		}else {
			log.info("Get Ticket Is Null And Msg Is:"+JSONObject.toJSONString(jsoninfo));
			//resRedisService.removeByKey(RedisKeyEnum.Key, APP_ID);
		}
		return url;
	}


	/**
	 * 
	* <把请求数据转化成token>
	*
	* @return token
	 */
	private String tokenTransfer() {
		//String jsontokenstring = getToken(APP_ID, SECRETE);
		String jsontokenstring="";
		String token0 = resRedisService.getKeyForObject(RedisKeyEnum.Key, APP_ID, String.class);
		//resRedisService.removeByKey(RedisKeyEnum.Key, appid);
		if (token0!=null) {
			jsontokenstring= token0;
			}else {
				jsontokenstring = sycToken(APP_ID, SECRETE,1);
			} 
		JSONObject tokenjson = JSONObject.parseObject(jsontokenstring);
		String token = tokenjson.getString("access_token");
		String errorInfo = tokenjson.getString("errcode");
		String errorMsg = tokenjson.getString("errmsg");
		if(errorInfo!=null){
			log.info("ErrorMsg Is:"+errorMsg+" And ErrorCode Is:"+errorInfo);
		}
		return token;
	}


	/**
	 * 
	* <锁住创建token的线程>
	*
	* @param appid
	* @param secret 
	* @param flag flag:0表示需要强制清除redis缓存
	* @return
	 */
	private synchronized String sycToken(String appid, String secret,Integer flag) {
			ReentrantZkDistributeLock distLock = ZkDistributeLockFactory.getInstance().getZkDistributeLock(BASE_NODE_PATH, zkAddress);
			try {
				distLock.lock();
				if (flag==0) {
						String result = createToken(appid, secret);
						//resRedisService.saveObject(RedisKeyEnum.Key, appid, 7100, result);
						return result;
				}else{
					String tokenValidate = resRedisService.getKeyForObject(RedisKeyEnum.Key, appid, String.class);
					if (tokenValidate==null) {
						String result = createToken(appid, secret);
						//resRedisService.saveObject(RedisKeyEnum.Key, appid, 7100, result);
						return result;
					}else {
						return tokenValidate;
					}
				}
			}finally{
				distLock.unlock();
			}
	}
	
	/**
	 * 
	* <创建token>
	*
	* @param appid
	* @param secret
	* @return
	 */
	private String createToken(String appid, String secret) {
		StringBuffer param = new StringBuffer();
		param.append("grant_type=").append("client_credential");
		param.append("&appid=").append(appid).append("&secret=").append(secret);
		String result = HttpClientUtil.sendGet(CREATE_TOKEN, param.toString());
		log.info("Create Token https://api.weixin.qq.com/cgi-bin/token Response Data Is:"+result);
		resRedisService.saveObject(RedisKeyEnum.Key, appid, 7100, result);
		return result;
	}



	/**
	 * 
	 * <创建二维码>
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	private String createQRCode(String token, String storeNo) throws Exception {
		String url = CREATE_QRCODE;
		String url1 = url + token;
		String param = "{\"action_name\": \"QR_LIMIT_STR_SCENE\",\"action_info\": {\"scene\": {\"scene_str\": \""
				+ storeNo + "\"}}}";
		String result = HttpClientUtil.sendPostByJSon(url1, param);
		log.info("Create QRCode "+url1+" Response Data Is:"+result);
		//Token Expire Condition
		JSONObject tokenResult = JSONObject.parseObject(result);
		String errorInfo = tokenResult.getString("errcode");
		String errorMsg = tokenResult.getString("errmsg");
		if ("40001".equals(errorInfo)||errorInfo!=null) {
			log.warn("Token Expired!! Info Is:"+errorMsg);
			String tokenInfo = sycToken(APP_ID, SECRETE,0);
			//String tokenInfo = createToken(APP_ID, SECRETE);
			//resRedisService.saveObject(RedisKeyEnum.Key, APP_ID, 7100, tokenInfo);
			JSONObject tokenjson = JSONObject.parseObject(tokenInfo);
			String realToken = tokenjson.getString("access_token");
			result = HttpClientUtil.sendPostByJSon(url+realToken, param);
		}
		return result;
	}



	/**
	 * 获取二维码url
	 * @see com.yatang.xc.xcr.service.QRCodeService#getQRCodeUrl(java.lang.String)
	 */
	@Override
	public String getQRCodeUrl(String storeNo) {
		String codeUrl="" ;
		try {
			log.info("Call queryStoreTicket Request Data Is:"+storeNo);
			Response<XcrStoreTicketDto> queryDubboResult = organzitionXCRBusinessDubboService.queryStoreTicket(storeNo);
			log.info("Call queryStoreTicket Response Data Is:"+JSONObject.toJSONString(queryDubboResult));
			if (!queryDubboResult.isSuccess()) {
				log.error("Call queryStoreTicket Failed!!");
				return codeUrl;
			}
			// No CodeUrl Condition
			if (queryDubboResult.getResultObject() == null || queryDubboResult.getResultObject().getTicketUrl() == null
					|| "".equals(queryDubboResult.getResultObject().getTicketUrl())) {
				String token = tokenTransfer();
				codeUrl = saveQRInfo(storeNo, token);
			}
			// Exist CodeUrl Condition
			else {
				codeUrl=queryDubboResult.getResultObject().getTicketUrl();
			}
	
		} catch (Exception e) {
			log.error("Get QRCode Failed!! And Error Msg Is："+e);
			e.printStackTrace();
		}
		return codeUrl;
	}
	
	
	
	

}
