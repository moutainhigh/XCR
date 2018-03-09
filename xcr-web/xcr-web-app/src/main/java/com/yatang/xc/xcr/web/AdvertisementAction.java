package com.yatang.xc.xcr.web;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService;
import com.yatang.xc.xcr.util.CommonUtil;

/**
 * 广告
* <class description>
*		
* @author: zhongrun
* @version: 1.0, 2017年9月7日
 */
@RequestMapping("System")
@Controller
public class AdvertisementAction {
	
	private static Logger log = LoggerFactory.getLogger(AdvertisementAction.class);
	
	@Autowired
	XcAdvertisementDubboService xcAdvertisementDubboService;
	
	/**
	 * 
	* 首页启动广告
	*
	* @param msg
	* @param response
	* @param Type
	* @throws Exception
	 */
	@RequestMapping("/HomeAd")
	public void appLoadAdvertisement(@RequestBody String msg,
			HttpServletResponse response,@CookieValue(value = "Type") Integer Type) throws Exception{
		JSONObject json = new JSONObject();
		JSONObject mapdata = new JSONObject();
		log.info("Call xcAdvertisementDubboService.findForApp Start!");
		Response<List<XcAdvertisementDTO>> dubboResult =  xcAdvertisementDubboService.findForApp();
		log.info("Call xcAdvertisementDubboService.findForApp Dubbo Response Data Is :"+JSONObject.toJSONString(dubboResult));
		if (!dubboResult.isSuccess()) {
			json.put("Status", CommonUtil.pageStatus2("M01", "请求后台dubbo失败"));
			response.getWriter().print(json);
		}
		List<XcAdvertisementDTO> xcAdvertisementDTOs = dubboResult.getResultObject();
		Integer size = xcAdvertisementDTOs.size();
		Random random = new Random();
		if (size==1) {
			XcAdvertisementDTO xcAdvertisementDTO =xcAdvertisementDTOs.get(0);
			mapdata.put("AdPic", xcAdvertisementDTO.getImgUrl());
			mapdata.put("AdJump", xcAdvertisementDTO.getActiveUrl());
			
		}else {
			if (size!=0) {
				XcAdvertisementDTO xcAdvertisementDTO = xcAdvertisementDTOs.get(random.nextInt(size));
				mapdata.put("AdPic", xcAdvertisementDTO.getImgUrl());
				mapdata.put("AdJump", xcAdvertisementDTO.getActiveUrl());
			}else {
				mapdata.put("AdPic", "");
				mapdata.put("AdJump", "");
			}
		}
		json.put("Status", CommonUtil.pageStatus2("M00", "请求成功"));
		json.put("mapdata", mapdata);
		response.getWriter().print(json);
	}
	
		
}
