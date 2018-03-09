package com.yatang.xc.xcr.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.enums.StateEnum;
import com.yatang.xc.xcr.pojo.FranchiseeJoinVo;
import com.yatang.xc.xcr.util.HttpClientUtil;

/** 
* @author gaodawei 
* @Date 2017年10月24日 下午2:50:39 
* @version 1.0.0
* @function 申请加盟小超登记
*/
@Controller
@RequestMapping("/User/")
public class ApplyJoinXCEnrollAction {
	
	private static Logger log = LoggerFactory.getLogger(ApplyJoinXCEnrollAction.class);
	
	@Value("${JOIN_YTXC_URL}")
	String JOIN_YTXC_URL;
	
	private static Integer STR_LENGTH0=0;//用于定义长度0
	private static Integer STR_LENGTH_11=11;//用于定义长度11
	private static String NAME_NOT_NULL="姓名不能为空";
	private static String PHONE_NOT_NULL="电话不能为空";
	private static String PHONE_NOT_STYLE="电话格式不正确";
	private static String ADRESS_NOT_NULL="详细地址不能为空";
	/**
	 * 加盟雅堂小超登记
	 * @param franchiseeInfoVo
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "joinEnroll", method = RequestMethod.POST)
	public void joinEnroll(FranchiseeJoinVo fInfoVo,HttpServletResponse response) throws IOException {
		JSONObject checkJson=checkFVOParam(fInfoVo);
		if(checkJson.getBooleanValue("result")){
			StringBuffer param = new StringBuffer();
			param.append("joinType=0").append("&contactName=").append(fInfoVo.getfName())
			.append("&contactTel=").append(fInfoVo.getfPhone()).append("&contactCity=").append(fInfoVo.getDetailAddress())
			.append("&origin=1").append("&storeOpen=").append(fInfoVo.getStoreOpen());
			checkJson = requestHandle(param.toString());
		}
		response.getWriter().print(checkJson);
	}
	/**
	 * 处理请求可能出现相关的异常
	 * @param param
	 * @return
	 */
	public JSONObject requestHandle(String param){
		JSONObject joinResultJson=new JSONObject();
		try {
			joinResultJson=HttpClientUtil.okHttpPost(JOIN_YTXC_URL, param);
		} catch (Exception ex) {
			log.error("\n*****加盟出现异常："+ExceptionUtils.getFullStackTrace(ex));
			joinResultJson=returnJsonPack(false,StateEnum.STATE_2.getDesc());
		}
		return joinResultJson;
	}
	/**
	 *  加盟雅堂小超登记参数校验
	 * @param fInfoVo
	 * @return
	 */
	private JSONObject checkFVOParam(FranchiseeJoinVo fInfoVo){
		JSONObject returnJson=new JSONObject();
		returnJson.put("result", true);
		if(fInfoVo.getfName().length()==STR_LENGTH0){
			returnJson=returnJsonPack(false,NAME_NOT_NULL);
		}
		if(fInfoVo.getfPhone().length()==STR_LENGTH0){
			returnJson=returnJsonPack(false,PHONE_NOT_NULL);
		}
		if(fInfoVo.getfPhone().length()!=STR_LENGTH_11){
			returnJson=returnJsonPack(false,PHONE_NOT_STYLE);
		}
		if(fInfoVo.getDetailAddress().length()==STR_LENGTH0){
			returnJson=returnJsonPack(false,ADRESS_NOT_NULL);
		}
		return returnJson;
	}
	/**
	 * 封装响应的错误信息
	 * @param result
	 * @param msg
	 * @return
	 */
	private JSONObject returnJsonPack(boolean result,String msg){
		JSONObject returnJson=new JSONObject();
		returnJson.put("result", result);
		returnJson.put("msg", msg);
		return returnJson;
	}
}
 