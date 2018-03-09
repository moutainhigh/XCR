package com.yatang.xc.xcr.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.VersionAppQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.VersionDubboService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;

/**
 * 
* <版本管理>
*		
* @author: zhongrun
* @version: 1.0, 2017年9月8日
 */
@Controller
@RequestMapping("System")
public class VersionAction {
	private static Logger log = LoggerFactory.getLogger(VersionAction.class);

	
	@Autowired
	private VersionDubboService versionDubboService;
	
	@Value("${SYSTEM_CODE}")
	private String SYSTEM_CODE;
	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_OUTDATE}")
	String STATE_OUTDATE;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${INFO_OK}")
	String INFO_OK;
	
	/**
	 * 
	* <app版本更新>
	*
	* @param msg
	* @param response
	* @param Type
	* @throws Exception
	 */
	@RequestMapping("/AppUpdate")
	public void appUpdate(@RequestBody String msg,
		HttpServletResponse response,@CookieValue(value = "Type") Integer Type) throws Exception{
		VersionAppQueryDTO versionAppQueryDTO = updateAppVo2DTO(msg, Type);
		
		log.info("appUpdate request data is:"+JSONObject.toJSONString(versionAppQueryDTO));
		long dcStartTime=System.currentTimeMillis();
		log.info("\n******于时间"+DateUtils.getLogDataTime(dcStartTime, null));
		Response<VersionDTO> dubboResult = versionDubboService.findForApp(versionAppQueryDTO);
		log.info("appUpdate response data is:"+JSONObject.toJSONString(dubboResult));
		log.info("\n******于时间"+DateUtils.getLogDataTime(dcStartTime, null)+"结束调用更新版本findForApp接口响应时间为：" + 
		"\n*******耗时为:"+CommonUtil.costTime(dcStartTime));
		
		JSONObject json = updateAppDubboResult2Json(dubboResult);		
		response.getWriter().print(json);
	}

	private JSONObject updateAppDubboResult2Json(Response<VersionDTO> dubboResult) {
		JSONObject json = new JSONObject();
		if (dubboResult.getCode().equals("100")) {
			json.put("Status", CommonUtil.pageStatus2("M01", "无更新"));
		}else {
			if (dubboResult.isSuccess()) {
				VersionDTO value = dubboResult.getResultObject();
				JSONObject jsonObject = new JSONObject();			
				jsonObject.put("Version", value.getVersionCode());
				jsonObject.put("Desc", value.getDescription());
				jsonObject.put("ApkUrl", value.getApkUrl());
				jsonObject.put("IsMandatory", value.getIsLiveUp());
				json.put("Status", CommonUtil.pageStatus2("M00", "有更新"));
			json.put("mapdata", jsonObject);	
			}
						
		}
		return json;
	}

	private VersionAppQueryDTO updateAppVo2DTO(String msg, Integer Type) {
		VersionAppQueryDTO versionAppQueryDTO = new VersionAppQueryDTO();
		try {
			msg = URLDecoder.decode(msg.substring(4,msg.length()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject jsonTemp = JSONObject.parseObject(msg);
		
		versionAppQueryDTO.setCurrentVersion(jsonTemp.getString("CurrentVersion"));
		versionAppQueryDTO.setCode(Integer.parseInt(jsonTemp.getString("CurrentCode")));
		versionAppQueryDTO.setType(Type);
		return versionAppQueryDTO;
	}
	
	/**
	 * 
	* <供应链模块更新>
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping("/AppModuleUpdate")
	public void appModuleUpdate(@RequestBody String msg,
			HttpServletResponse response) throws Exception{
		VersionAppQueryDTO versionAppQueryDTO = updateSourseVo2DTO(msg, 2);
			
			log.info("appUpdate request data is:"+JSONObject.toJSONString(versionAppQueryDTO));
			long dcStartTime=System.currentTimeMillis();
			log.info("\n******于时间"+DateUtils.getLogDataTime(dcStartTime, null));
			Response<VersionDTO> dubboResult = versionDubboService.findForGongYingLian(versionAppQueryDTO);
			log.info("appUpdate response data is:"+JSONObject.toJSONString(dubboResult));
			log.info("\n******于时间"+DateUtils.getLogDataTime(dcStartTime, null)+"结束调用更新版本findForGongYingLian接口响应时间为：" + 
			"\n*******耗时为:"+CommonUtil.costTime(dcStartTime));
			
			JSONObject json = updateSourseDubboResult2Json(dubboResult);		
		response.getWriter().print(json);
		
		
	}
	
	
	private VersionAppQueryDTO updateSourseVo2DTO(String msg, Integer Type) {
		VersionAppQueryDTO versionAppQueryDTO = new VersionAppQueryDTO();
		try {
			msg = URLDecoder.decode(msg.substring(4,msg.length()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject jsonTemp = JSONObject.parseObject(msg);
		
		versionAppQueryDTO.setCurrentVersion(jsonTemp.getString("ModuleId"));
		versionAppQueryDTO.setCode(Integer.parseInt(jsonTemp.getString("CurrentVersion")));
		versionAppQueryDTO.setType(Type);
		return versionAppQueryDTO;
	}
	
	private JSONObject updateSourseDubboResult2Json(Response<VersionDTO> dubboResult) {
		JSONObject json = new JSONObject();
		if (dubboResult.getCode().equals("100")) {
			json.put("Status", CommonUtil.pageStatus2("M01", "无更新"));
		}else {
			if (dubboResult.isSuccess()) {
				VersionDTO value = dubboResult.getResultObject();
				JSONObject jsonObject = new JSONObject();			
				jsonObject.put("Version", value.getCode().toString());
				jsonObject.put("Desc", value.getDescription());
				jsonObject.put("ZipUrl", value.getApkUrl());
				jsonObject.put("ModuleId", value.getVersionCode());
				json.put("Status", CommonUtil.pageStatus2("M00", "有更新"));
			json.put("mapdata", jsonObject);	
			}
						
		}
		return json;
	}
	
	

}
