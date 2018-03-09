package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.oc.b.member.biz.core.dto.LevelPrivilegeDto;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberInfoDto;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberLevelDto;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberScoreInfoPoDto;
import com.yatang.xc.oc.b.member.biz.core.dto.MonthScoreDto;
import com.yatang.xc.oc.b.member.biz.core.dto.PrivilegeInfoDto;
import com.yatang.xc.oc.b.member.biz.core.dto.page.Page;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberInfoDubboService;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberLevelDubboService;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberScoreDubboService;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.PrivilegeInfoDubboService;
import com.yatang.xc.oc.b.member.biz.core.enums.SystemTypeEnum;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.TokenUtil;
/**
 * 
* <会员中心>
*		
* @author: zhongrun
* @version: 1.0, 2017年10月31日
 */
@Controller
@RequestMapping("/User/")
public class VipCenterAction {
	
	private static Logger log = LoggerFactory.getLogger(VipCenterAction.class);
	
	@Autowired
	private MemberInfoDubboService memberInfoDubboService;
	
	@Autowired
	private PrivilegeInfoDubboService privilegeInfoDubboService;
	
	@Autowired
	private MemberLevelDubboService memberLevelDubboService;
	
	@Autowired
	private MemberScoreDubboService memberScoreDubboService;

	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	@Value("${INFO_OK}")
	String INFO_OK;
	@Value("${VIP_SYSTEM_CODE}")
	String VIP_SYSTEM_CODE;
	
	/**
	 * 
	* <会员信息>
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping("MemberInfo")
	public void memberInfo(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "MemberInfo");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			Long startTime = System.currentTimeMillis();
			JSONObject mapdata = msgWrap(response, jsonTemp, json);
			Long endTime = System.currentTimeMillis();
			log.info("Call queryMemberInfoByCode+getMemberLevelDtoByScore+getPrivilegeInfoByMemberCode Cast " + (endTime - startTime));
			json.put("mapdata", mapdata);
		}
		json.put("Status", stateJson);
		response.getWriter().print(json);
	}

	/**
	 * 
	* <接口转换>
	*
	* @param response
	* @param jsonTemp
	* @param json
	* @return
	* @throws IOException
	 */
	private JSONObject msgWrap(HttpServletResponse response, JSONObject jsonTemp, JSONObject json) throws IOException {
		JSONObject mapdata = new JSONObject();
		JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
		log.info("Call queryMemberInfoByCode Request Data Is:"+tokenJson.getString("jmsCode"));
		Response<MemberInfoDto> queryMemberInfoByCodeDubboResult = memberInfoDubboService.queryMemberInfoByCode(tokenJson.getString("jmsCode"));
		log.info("Call queryMemberInfoByCode Response Data Is:"+JSONObject.toJSONString(queryMemberInfoByCodeDubboResult));
		
		memberInfoDubboResultTransfer(response, json, mapdata,queryMemberInfoByCodeDubboResult);
		
		log.info("Call getPrivilegeInfoByMemberCode Request Data Is:"+tokenJson.getString("jmsCode"));
		Response<LevelPrivilegeDto> getPrivilegeInfoByCodeDubboResult = privilegeInfoDubboService.getPrivilegeInfoByMemberCode(tokenJson.getString("jmsCode"),SystemTypeEnum.SYSTEM_GYL.getInCode());
		log.info("Call getPrivilegeInfoByMemberCode Response Data Is:"+JSONObject.toJSONString(getPrivilegeInfoByCodeDubboResult));
		
		JSONArray privilegeList = privilegeDubboResultTransfer(response, json, getPrivilegeInfoByCodeDubboResult);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rows", privilegeList);
		json.put("listdata", jsonObject);
		return mapdata;
	}

	/**
	 * 
	* <会员信息dubbo转换>
	*
	* @param response
	* @param json
	* @param mapdata
	* @param queryMemberInfoByCodeDubboResult
	* @return
	* @throws IOException
	 */
	private void memberInfoDubboResultTransfer(HttpServletResponse response, JSONObject json,
			JSONObject mapdata, Response<MemberInfoDto> queryMemberInfoByCodeDubboResult) throws IOException {
		if (queryMemberInfoByCodeDubboResult==null||!queryMemberInfoByCodeDubboResult.isSuccess()) {
			log.error("Call queryMemberInfoByCode Failed!!");
			json.put("Status", CommonUtil.pageStatus2("M02", "系统异常"));
			response.getWriter().print(json);
		}
		MemberInfoDto memberInfoDto = queryMemberInfoByCodeDubboResult.getResultObject();
		if (memberInfoDto==null) {
			mapdata.put("TimeInterval", timeTransfer());
			mapdata.put("VipIdentify", 0);
			mapdata.put("AccumulatedIntegral", 0);
			//mapdata.put("NextVipIdentify", "");
		}
		else {
		//Find the relative position of VIP in the list of VIP levels based on the VIP name
		Integer VIPlevel = findVipByName(memberInfoDto);
		String transferDay = timeTransfer();
		mapdata.put("TimeInterval", transferDay);
		mapdata.put("VipIdentify", VIPlevel);
		mapdata.put("AccumulatedIntegral", memberInfoDto.getNear12ScoreCount());
		}
	}

	/**
	 * 
	* <通过VIP名字获取VIP相对等级（默认VIP等级名字不相同）>
	*
	* @param memberInfoDto
	* @return
	 */
	private Integer findVipByName(MemberInfoDto memberInfoDto) {
		Response<List<MemberLevelDto>> memberLevelDubboServiceDubboResult = memberLevelDubboService.queryAllMemberLevelListAndPrivilege();
		log.info("Call queryAllMemberLevelListAndPrivilege End"/*+JSONObject.toJSONString(memberLevelDubboServiceDubboResult)*/);
		if (memberLevelDubboServiceDubboResult==null||!memberLevelDubboServiceDubboResult.isSuccess()) {
			log.error("Call queryAllMemberLevelListAndPrivilege Return False Or Null!!");
			return 0;
		}
		List<MemberLevelDto> memberLevelDtos = memberLevelDubboServiceDubboResult.getResultObject();
		Integer VIPlevel=0;
		try {
		for (MemberLevelDto memberLevelDto : memberLevelDtos) {
				if (!memberInfoDto.getMemberLevelName().equals(memberLevelDto.getMemberLevelName())) {
					VIPlevel++;
				}else {
					break;
				}
		}
		} catch (Exception e) {
			log.error("memberInfoDto.getMemberLevelName Is Null And Msg Is"+JSONObject.toJSONString(e));
		}
		return VIPlevel;
	}

	/**
	 * 
	* <时间转换>
	*
	* @return
	 */
	private String timeTransfer() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String endDay = DateUtils.getLastDayOfLastMonth();
		String startDay = DateUtils.getFirstMonthDay(format);
		Date sDate = DateUtils.stringToSimpleDateFormat(startDay);
		String startString = DateUtils.getfirstDayOfLastYear(sDate);
		String ss = startString.replaceAll("-", ".");
		String ee = endDay.replaceAll("-", ".");
		String transferDay =  ss+"-"+ee;
		return transferDay;
	}

	/**
	 * 
	* <特权dubbo转换>
	*
	* @param response
	* @param json
	* @param getPrivilegeInfoByCodeDubboResult
	* @return
	* @throws IOException
	 */
	private JSONArray privilegeDubboResultTransfer(HttpServletResponse response, JSONObject json,
			Response<LevelPrivilegeDto> getPrivilegeInfoByCodeDubboResult) throws IOException {
		if (!getPrivilegeInfoByCodeDubboResult.isSuccess()) {
			log.error("Call getPrivilegeInfoByMemberCode Failed!!");
			json.put("Status", CommonUtil.pageStatus2("M02", "系统异常"));
			response.getWriter().print(json);
		}
		JSONArray privilegeList = new JSONArray();
		LevelPrivilegeDto levelPrivilegeDto = getPrivilegeInfoByCodeDubboResult.getResultObject();
		if (levelPrivilegeDto==null) {
			return privilegeList;
		}
		List<PrivilegeInfoDto> privilegeInfoDtos = levelPrivilegeDto.getPrivilegeInfoDtoList();
		if (privilegeInfoDtos!=null) {
			for (PrivilegeInfoDto privilegeInfoDto : privilegeInfoDtos) {
				JSONObject privilege = privilegeTransfer(privilegeInfoDto);
				privilegeList.add(privilege);
			}
		}
		return privilegeList;
	}

	/**
	 * 
	* <特权信息转换>
	*
	* @param privilegeInfoDto
	* @return
	 */
	private JSONObject privilegeTransfer(PrivilegeInfoDto privilegeInfoDto) {
		JSONObject privilege = new JSONObject();
		privilege.put("PrivilegeName", privilegeInfoDto.getPrivilegeName());
		privilege.put("PrivilegeInfo", privilegeInfoDto.getPrivilegeDescription());
		privilege.put("PrivilegeType", privilegeInfoDto.getRelateSystemCode());//系统间编码类型
		return privilege;
	}
	
	/**
	 * 
	* <我的积分>
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping("IntegralInfo")
	public void integralInfo(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "IntegralInfo");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			Long startTime = System.currentTimeMillis();
			myScoreDataTransfer(jsonTemp, json);
			Long endTime = System.currentTimeMillis();
			log.info("Call My Score Cast " + (endTime - startTime));
		}
		json.put("Status", stateJson);
		response.getWriter().print(json);
	}

	/**
	 * 
	* <我的积分数据中转换>
	*
	* @param jsonTemp
	* @param json
	 */
	private void myScoreDataTransfer(JSONObject jsonTemp, JSONObject json) {
		JSONObject tokenJson=TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
		Response<MonthScoreDto> getMonthScoreDubboResult = memberScoreDubboService.getMonthScore(tokenJson.getString("jmsCode"));
		log.info("Call getMonthScore Response Data Is:"+JSONObject.toJSONString(getMonthScoreDubboResult));
		JSONObject mapdata = monthScoreDUbboResultTransfer(getMonthScoreDubboResult);
		nextLevelScore(jsonTemp, mapdata);
		json.put("mapdata", mapdata);
		MemberScoreInfoPoDto memberScoreInfoPoDto = memberScoreRequestDataTransfer(tokenJson.getString("jmsCode"));
		log.info("Call findMemberScoreInfoPo Request Data Is:"+JSONObject.toJSONString(memberScoreInfoPoDto)+":"+ jsonTemp.getInteger("PageSize")+":"+jsonTemp.getInteger("PageIndex"));
		Response<Page<MemberScoreInfoPoDto>> findMemberScoreInfoPoDubboResult = memberScoreDubboService.findMemberScoreInfoPo(memberScoreInfoPoDto, jsonTemp.getInteger("PageIndex") ,jsonTemp.getInteger("PageSize"));
		log.info("Call findMemberScoreInfoPo Response Data Is:"+JSONObject.toJSONString(findMemberScoreInfoPoDubboResult));
		
		JSONObject jsonP = memberScoreDubboTransfer(jsonTemp, findMemberScoreInfoPoDubboResult);
		json.put("listdata", jsonP);
	}

	/**
	 * 
	* <下一级所需分数>
	*
	* @param jsonTemp
	* @param mapdata
	 */
	private void nextLevelScore(JSONObject jsonTemp, JSONObject mapdata) {
			Long score = mapdata.getLong("ThisMonthIntegral");
			log.info("Call getMemberLevelDtoByScore Request Data Is:"+score);
			Response<MemberLevelDto> getMemberLevelDtoByScoreDubborResult = privilegeInfoDubboService.getMemberLevelDtoByScore(score);
			log.info("Call getMemberLevelDtoByScore Response Data Is:"+JSONObject.toJSONString(getMemberLevelDtoByScoreDubborResult));
			if (!getMemberLevelDtoByScoreDubborResult.isSuccess()) {
				log.error("Call getMemberLevelDtoByScore Return False!!");
			}
			if (getMemberLevelDtoByScoreDubborResult.getResultObject()!=null) {
				String levelName = getMemberLevelDtoByScoreDubborResult.getResultObject().getMemberLevelName();
				MemberInfoDto memberInfoDto = new MemberInfoDto ();
				memberInfoDto.setMemberLevelName(levelName);
				Integer VIPLevel = findVipByName(memberInfoDto);
				Long arriveScore = getMemberLevelDtoByScoreDubborResult.getResultObject().getArriveScore();
				mapdata.put("PreGradeNeedGral", (arriveScore-score)<0?0:(arriveScore-score));
				mapdata.put("PreNextGrade", VIPLevel);
				
			}else {
				mapdata.put("PreGradeNeedGral", 0);
				mapdata.put("PreNextGrade", 0);
			}
	}

	/**
	 * 
	* <关于月度积分接口返回数据转换>
	*
	* @param getMonthScoreDubboResult
	 */
	private JSONObject monthScoreDUbboResultTransfer(Response<MonthScoreDto> getMonthScoreDubboResult) {
		JSONObject mapdata = new JSONObject();
		if (getMonthScoreDubboResult==null||!getMonthScoreDubboResult.isSuccess()) {
			log.error("Call getMonthScore Return False Or Null!!");
			systemData(mapdata);
			return mapdata;
		}
		
		MonthScoreDto monthScoreDto = getMonthScoreDubboResult.getResultObject();
		if (monthScoreDto==null) {
			systemData(mapdata);
			return mapdata;
		}
		log.info("Call queryMemberLevelNext Request Data Is:"+monthScoreDto.getBeforeScore());
		Response<MemberLevelDto> queryMemberLevelNextDubboResult = memberScoreDubboService.queryMemberLevelNext(monthScoreDto.getBeforeScore());
		log.info("Call queryMemberLevelNext Response Data Is:"+JSONObject.toJSONString(queryMemberLevelNextDubboResult));
		if (!queryMemberLevelNextDubboResult.isSuccess()) {
			log.error("Call queryMemberLevelNext Return False!!");
		}
		MemberLevelDto memberLevelDto = queryMemberLevelNextDubboResult.getResultObject();
		MemberInfoDto memberInfoDto = new MemberInfoDto ();
		memberInfoDto.setMemberLevelName(memberLevelDto.getMemberLevelName());
		Integer level = findVipByName(memberInfoDto);
		mapdata.put("ActNextMonthGrade", level);
		mapdata.put("ThisMonthIntegral",monthScoreDto.getCurrentScore());
		return mapdata;
	}

	private void systemData(JSONObject mapdata) {
		mapdata.put("ActNextMonthGrade", 0);
		mapdata.put("ThisMonthIntegral",0);
	}

	/**
	 * 
	* <获取积分列表请求数据转换>
	*
	* @param jsonTemp
	* @return
	 */
	private MemberScoreInfoPoDto memberScoreRequestDataTransfer(String userId) {
		MemberScoreInfoPoDto memberScoreInfoPoDto = new MemberScoreInfoPoDto();
		memberScoreInfoPoDto.setMemberCode(userId);
		return memberScoreInfoPoDto;
	}

	/**
	 * 
	* <积分列表数据转换>
	*
	* @param jsonTemp
	* @param findMemberScoreInfoPoDubboResult
	* @return
	 */
	private JSONObject memberScoreDubboTransfer(JSONObject jsonTemp,
			Response<Page<MemberScoreInfoPoDto>> findMemberScoreInfoPoDubboResult) {
		JSONObject jsonP = new JSONObject();
		JSONArray memList = new JSONArray();
		
		if (findMemberScoreInfoPoDubboResult==null||!findMemberScoreInfoPoDubboResult.isSuccess()) {
			log.error("Call findMemberScoreInfoPo Return False Or null!!");
			jsonP.put("rows", memList);
			return jsonP;
		}
		Page<MemberScoreInfoPoDto> memPage = findMemberScoreInfoPoDubboResult.getResultObject();
		List<MemberScoreInfoPoDto> memberScoreInfoPoDtos = memPage.getList();
		if (memberScoreInfoPoDtos!=null) {
			for (MemberScoreInfoPoDto memberScoreInfoPoDto2 : memberScoreInfoPoDtos) {
				JSONObject jsonob = signalScoreTransfer(memberScoreInfoPoDto2);
				memList.add(jsonob);
			}
		}else {
			JSONObject jsonob = new JSONObject();
			memList.add(jsonob);
		}
		jsonP.put("rows", memList);
		jsonP.put("totalcount", memPage.getTotal());
		jsonP.put("pagesize", Integer.parseInt(jsonTemp.getString("PageSize")));
		jsonP.put("pageindex",Integer.parseInt( jsonTemp.getString("PageIndex")));
		jsonP.put("totalpage", memPage.getTotal()/(Integer.parseInt(jsonTemp.getString("PageSize"))+1)+1);
		return jsonP;
	}

	/**
	 * 
	* <单个积分转换数据>
	*
	* @param memberScoreInfoPoDto2
	* @return
	 */
	private JSONObject signalScoreTransfer(MemberScoreInfoPoDto memberScoreInfoPoDto2) {
		JSONObject jsonob = new JSONObject();
		jsonob.put("IntegralName", memberScoreInfoPoDto2.getChangeReason());
		jsonob.put("Date", DateUtils.dateSimpleFormat(memberScoreInfoPoDto2.getCreateTime()));
		jsonob.put("IntegralValue", memberScoreInfoPoDto2.getScore());
		jsonob.put("StoreNo", memberScoreInfoPoDto2.getShopCode());
		return jsonob;
	}
	
	/**
	 * 
	* <会员等级>
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping("GradeInfo")
	public void gradeInfo(@RequestBody String msg, HttpServletResponse response) throws Exception {
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "GradeInfo");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		if (stateJson.getString("State").equals(STATE_OK)) {
			
			Long startTime = System.currentTimeMillis();
			Response<List<MemberLevelDto>> dubboResult = memberLevelDubboService.queryAllMemberLevelListAndPrivilege();
			log.info("Call queryAllMemberLevelListAndPrivilege End");
			JSONObject jsonList = memberLevelDubboResultTransfer(dubboResult);
			Long endTime = System.currentTimeMillis();
			log.info(" And Call queryAllMemberLevelListAndPrivilege Cast " + (endTime - startTime));
			json.put("listdata", jsonList);
		}
		json.put("Status", stateJson);
		response.getWriter().print(json);
	}

	/**
	 * 
	* <会员等级接口返回数据转换>
	*
	* @param dubboResult
	* @return
	 */
	private JSONObject memberLevelDubboResultTransfer(Response<List<MemberLevelDto>> dubboResult) {
		JSONObject jsonList = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		if (dubboResult==null||!dubboResult.isSuccess()) {
			log.error("Call queryAllMemberLevelListAndPrivilege Return False Or Null!!");
			jsonList.put("rows", jsonArray);
			return jsonList;
		}
		List<MemberLevelDto> memberLevelDtos = dubboResult.getResultObject();
		Integer identify = 0;
		for (MemberLevelDto memberLevelDto : memberLevelDtos) {
			JSONObject jsonObject = memberLevelTransfer(identify, memberLevelDto);
			identify++;
			jsonArray.add(jsonObject);
		}
		jsonList.put("rows", jsonArray);
		return jsonList;
	}

	/**
	 * 
	* <会员等级转换>
	*
	* @param identify
	* @param memberLevelDto
	* @return
	 */
	private JSONObject memberLevelTransfer(Integer identify, MemberLevelDto memberLevelDto) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("VipIdentify", identify);
		jsonObject.put("NeedIntegral", memberLevelDto.getArriveScore());
		JSONArray jsonArray2 = new JSONArray();
		List<PrivilegeInfoDto> privilegeInfoDtos = memberLevelDto.getPrivilegeList();
		if(privilegeInfoDtos!=null){
			for (PrivilegeInfoDto privilegeInfoDto : privilegeInfoDtos) {
				JSONObject jsonObject2 = privilegeInfoTransfer(privilegeInfoDto);
				jsonArray2.add(jsonObject2);
			}
		}
		jsonObject.put("PrivilegeList", jsonArray2);
		return jsonObject;
	}

	/**
	 * 
	* <特权信息转换>
	*
	* @param privilegeInfoDto
	* @return
	 */
	private JSONObject privilegeInfoTransfer(PrivilegeInfoDto privilegeInfoDto) {
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("PrivilegeInfo", privilegeInfoDto.getPrivilegeDescription());
		jsonObject2.put("PrivilegeName", privilegeInfoDto.getPrivilegeName());
		jsonObject2.put("PrivilegeType", privilegeInfoDto.getRelateSystemCode());//系统编码由产品定义
		return jsonObject2;
	}

}
