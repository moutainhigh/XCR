package com.yatang.xc.xcr.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.pos.cloud.dto.ResultDTO;
import com.yatang.xc.pos.cloud.dto.RoleAuthorityDTO;
import com.yatang.xc.pos.cloud.dubboservice.XcrUserDubboService;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionExecuteQueryDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainQueryDubboService;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.util.ActionUserUtil;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.TokenUtil;

/**
 * 
* <角色权限管理>
*		
* @author: zhongrun
* @version: 1.0, 2017年12月14日
 */
@Controller
@RequestMapping("User")
public class JurisdictionAction {
	
	private static Logger log = LoggerFactory.getLogger(JurisdictionAction.class);
	
	@Value("${INFO_OK}")
	String INFO_OK;
	@Value("${SYSTEM_CODE}")
	private String SYSTEM_CODE;
	@Value("${STATE_OK}")
	String STATE_OK;
	@Value("${STATE_OUTDATE}")
	String STATE_OUTDATE;
	@Value("${STATE_ERR}")
	String STATE_ERR;
	
	@Value("${jiamengshang}")
	String jiamengshang;
	@Value("${franchisee}")
	String franchisee;
	@Value("${shopowner}")
	String shopowner;
	@Value("${cashier}")
	String cashier;
	@Value("${maxSize}")
	String maxSize;
	
	@Autowired
	XcrUserDubboService xcrUserDubboService;
	@Autowired
    private MissionExecuteDubboService missionExecuteDubboService;
	@Autowired
    private TrainQueryDubboService trainQueryDubboService;
	
	/*@RequestMapping("zhong")
	public void revenueOrOutDetial(String num){
		Response<List<ResultDTO>> queryRoleByLoginAccountDubboService = xcrUserDubboService.queryRoleByLoginAccount(num);
		Response<List<RoleAuthorityDTO>> queryPermissionsByLoginAccountDubboResult = xcrUserDubboService.queryPermissionsByLoginAccount(num);
		System.out.println(queryRoleByLoginAccountDubboService);
	}*/
	
	@RequestMapping("permisErro")
	public void revenueOrOutDetial(HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		log.error("No Permission!!!!");
		json.put("Status", CommonUtil.pageStatus2("M02", "无此权限"));
		response.getWriter().print(json);
	}
	
	/**
	* <查询角色权限>
	* @param msg
	* @param response
	* @throws Exception
	 */
	@RequestMapping("NLSJurisdictList")
	public @ResponseBody  ResultMap nLSJurisdictList(@RequestBody String msg, HttpServletResponse response) throws Exception{
		JSONObject jsonTemp = CommonUtil.methodBefore(msg, "NLSJurisdictList");
		JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
		JSONObject json = new JSONObject();
		JSONObject mapdata = new JSONObject();

		if (stateJson.getString("State").equals(STATE_OK)) {

			Response<List<ResultDTO>> queryRoleByLoginAccountDubboService = xcrUserDubboService.queryRoleByLoginAccount(jsonTemp.getString("UserId"));
			log.info("Call queryRoleByLoginAccount Request Data Is:"+jsonTemp.getString("UserId")+"And queryRoleByLoginAccount Response Data Is:"+JSONObject.toJSONString(queryRoleByLoginAccountDubboService));
			Response<List<RoleAuthorityDTO>> queryPermissionsByLoginAccountDubboResult = xcrUserDubboService.queryPermissionsByLoginAccount(jsonTemp.getString("UserId"));
			log.info("Call queryPermissionsByLoginAccount Request Data Is:"+jsonTemp.getString("UserId")+"And queryPermissionsByLoginAccount Response Data Is:"+JSONObject.toJSONString(queryPermissionsByLoginAccountDubboResult));

            if (queryRoleByLoginAccountDubboService==null
                    ||!queryRoleByLoginAccountDubboService.isSuccess()
                    || queryRoleByLoginAccountDubboService.getResultObject() == null
                    || queryRoleByLoginAccountDubboService.getResultObject().size() == 0) {

                log.error("Call queryRoleByLoginAccount Return False or Null");
                return ResultMap.failll("服务器正忙，请稍后再试");
            }

            Boolean isFranchiser = false;
            JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
            if (tokenJson != null) {
                isFranchiser = tokenJson.getBoolean("isFranchiser");
            }

            JSONArray jurisdictStatues = roleListInfoTransfer(
					queryRoleByLoginAccountDubboService, isFranchiser);

			mapdata.put("JurisdictStatues", jurisdictStatues);
			mapdata.put("JurisdictList",  permisInfoTransfer(response, json, queryPermissionsByLoginAccountDubboResult));
		}

		return ResultMap.successu().mapData(mapdata);
	}

	/**
	 * 角色组组装
	 * @param queryRoleByLoginAccountDubboService
	 * @param isFranchiser
	 * @return
	 */
	private JSONArray roleListInfoTransfer(
			Response<List<ResultDTO>> queryRoleByLoginAccountDubboService,
			Boolean isFranchiser) {
		JSONArray jurisdictStatues = new JSONArray();
		List<ResultDTO> resultDTOs = queryRoleByLoginAccountDubboService.getResultObject();
		for (ResultDTO resultDTO : resultDTOs) {
		    if ("franchisee".equals(resultDTO.getRoleCode())) {
		        if (isFranchiser) {
		            jurisdictStatues.add(0);
		        } else {
		            jurisdictStatues.add(1);
		        }
		    } else if ("cashier".equals(resultDTO.getRoleCode())) {
		        jurisdictStatues.add(3);
		    } else if ("shopowner".equals(resultDTO.getRoleCode())) {
		        jurisdictStatues.add(2);
		    } else {
		        jurisdictStatues.add(3);
		        log.error("This User Role Out Of System!!");
		    }
		}
		return jurisdictStatues;
	}

	/**
	 * 
	* <权限信息转换>
	*
	* @param response
	* @param json
	* @param queryPermissionsByLoginAccountDubboResult
	* @return
	* @throws IOException
	 */
	private JSONArray permisInfoTransfer(HttpServletResponse response, JSONObject json,
			Response<List<RoleAuthorityDTO>> queryPermissionsByLoginAccountDubboResult) throws IOException {
		if (queryPermissionsByLoginAccountDubboResult==null||!queryPermissionsByLoginAccountDubboResult.isSuccess()||queryPermissionsByLoginAccountDubboResult.getResultObject()==null||queryPermissionsByLoginAccountDubboResult.getResultObject().size()==0) {
			log.error("Call queryPermissionsByLoginAccount Return False or Null");
			json.put("Status", CommonUtil.pageStatus2("M02", "服务器正忙，请稍后再试"));
			response.getWriter().print(json);
		}
		JSONArray jurisdictList = new JSONArray();
		List<RoleAuthorityDTO> roleAuthorityDTOs = queryPermissionsByLoginAccountDubboResult.getResultObject();
		for (RoleAuthorityDTO roleAuthorityDTO : roleAuthorityDTOs) {
			JSONObject juJsonObject = new JSONObject();
			juJsonObject.put("JurisdictCode", roleAuthorityDTO.getAuthorityCode());
			juJsonObject.put("JurisdictName", roleAuthorityDTO.getAuthorityName());
			jurisdictList.add(juJsonObject);
		}
		return jurisdictList;
	}


	/**
	 * 
	* <角色信息转换>
	*
	* @param queryRoleByLoginAccountDubboService
	* @return
	 */
/*	private JSONArray roleDataTransfer(Response<List<ResultDTO>> queryRoleByLoginAccountDubboService,JSONObject jsonTemp) {
		JSONArray jurisdictStatues = new JSONArray();
    	Boolean isFranchiser = false;
    	JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
        if (tokenJson != null) {
        	isFranchiser = tokenJson.getBoolean("isFranchiser");
		}
		
		List<ResultDTO> resultDTOs = queryRoleByLoginAccountDubboService.getResultObject();
		for (ResultDTO resultDTO : resultDTOs) {
			if ("franchisee".equals(resultDTO.getRoleCode())) {
				if (isFranchiser) {
					jurisdictStatues.add(0);
				}else {
					jurisdictStatues.add(1);
				}
			}else if ("cashier".equals(resultDTO.getRoleCode())) {
				jurisdictStatues.add(3);
			}else if ("shopowner".equals(resultDTO.getRoleCode())) {
				jurisdictStatues.add(2);
			}else {
				jurisdictStatues.add(3);
				log.error("This User Role Out Of System!!");
			}
		}
		return jurisdictStatues;
	}*/
	
	/**
	 * 
	* <店铺主页数据>
	*
	* @param msg
	* @param response
	* @throws Exception
	 */
    @RequestMapping(value = "SignIn2", method = RequestMethod.POST)
    public void signIn2(@RequestBody String msg, HttpServletResponse response) throws Exception {
        JSONObject jsonTemp = CommonUtil.methodBefore(msg, "SignIn2");
        JSONObject stateJson = ActionUserUtil.getStateJson(jsonTemp);
        JSONObject json = new JSONObject();
        if (stateJson.getString("State").equals(STATE_OK)) {
        	JSONObject tokenJson = TokenUtil.getTokenFromRedis(jsonTemp.getString("UserId"));
        	Boolean isF5Account=false;
        	//Boolean isFranchiser = false;
            if (tokenJson != null) {
            	isF5Account = tokenJson.getBoolean("isF5Account");
            	//isFranchiser = tokenJson.getBoolean("isFranchiser");
			}
            if (!isF5Account) {
            	JSONArray jsono = new JSONArray();
            	List<String> data = iconTata();
            	for (int i = 1; i <= Integer.parseInt(maxSize); i++) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("FunType", i);
					jsonObject.put("PermisStatue", 1);
					jsonObject.put("FunName", data.get(i-1));
					conditionTransfer(jsonTemp, i,jsonObject);
					jsono.add(jsonObject);
				}
            	JSONObject jsonList = new JSONObject();
            	jsonList.put("rows", jsono);
            	json.put("listdata", jsonList);
			}else {
				Response<List<ResultDTO>> queryRoleByLoginAccountDubboService = xcrUserDubboService.queryRoleByLoginAccount(jsonTemp.getString("UserId"));
				log.info("Call queryRoleByLoginAccount Request Data Is:"+jsonTemp.getString("UserId")+"And Response Data Is:"+JSONObject.toJSONString(queryRoleByLoginAccountDubboService));
				if (queryRoleByLoginAccountDubboService==null||!queryRoleByLoginAccountDubboService.isSuccess()) {
					log.error("Call queryRoleByLoginAccount Return False Or Null!!");
					json.put("Status", CommonUtil.pageStatus2("M02", "服务器正忙，请稍后再试"));
					response.getWriter().print(json);
				}
            	List<ResultDTO> resultDTOs = queryRoleByLoginAccountDubboService.getResultObject();
            	JSONArray jsono = new JSONArray();
            	List<String> roleCode = new ArrayList<String>();
            	for (ResultDTO resultDTO : resultDTOs) {
            		roleCode.add(resultDTO.getRoleCode());
				}
            		//Use Maximum Permission!!
    				if (roleCode.contains("franchisee")) {
    					transferByStringCode(franchisee,jsono,jsonTemp);
    				}else if (roleCode.contains("shopowner")) {
    					transferByStringCode(shopowner,jsono,jsonTemp);
    				}else if (roleCode.contains("cashier")) {
    					transferByStringCode(cashier,jsono,jsonTemp);
    				}else {
    					transferByStringCode(cashier,jsono,jsonTemp);
    					log.error("This User Role Out Of System!!");
    				}
            	JSONObject jsonList = new JSONObject();
            	jsonList.put("rows", jsono);
            	json.put("listdata", jsonList);
			}
        }
        json.put("Status", stateJson);
		response.getWriter().print(json);
        
    }
    
    void transferByStringCode(String code,JSONArray json,JSONObject jsonTemp){
    	
    	String[] siCode = code.split("-");
    	int flag = 0;
    	List<String> perListName = iconTata();
    	for (int j = 1; j <= Integer.parseInt(maxSize); j++) {
    		int intCode = 0;
    		if(flag<siCode.length){
    			intCode = Integer.parseInt(siCode[flag]);
    		}
    		JSONObject jsonObject = new JSONObject();
			if (j==intCode) {
				jsonObject.put("PermisStatue", 1);
				conditionTransfer(jsonTemp, intCode, jsonObject);
				if (flag<siCode.length) {
					flag++;
				}
			}else {
				jsonObject.put("PermisStatue", 0);
				jsonObject.put("FunNum", "");
			}
			jsonObject.put("FunType", j);
			jsonObject.put("FunName",perListName.get(j-1));
			json.add(jsonObject);
		}
    }

	private List<String> iconTata() {
		List<String> perListName = new ArrayList<String>();
    	perListName.add("店铺收入");
    	perListName.add("交易流水");
    	perListName.add("消息");
    	perListName.add("外送订单");
    	perListName.add("店铺活动");
    	perListName.add("我要进货");
    	perListName.add("外送商品");
    	perListName.add("库存管理");
    	perListName.add("门店商品");
    	perListName.add("数据统计");
    	perListName.add("结算管理");
    	perListName.add("我的任务");
    	perListName.add("小超课堂");
    	perListName.add("在线客服");
		return perListName;
	}

	private void conditionTransfer(JSONObject jsonTemp, int intCode, JSONObject jsonObject) {
		if (intCode==12/*表示為任務*/) {
			Integer taskNum = missionInfoTransfer(jsonTemp);
			jsonObject.put("FunNum", taskNum.toString());
		}else if (intCode==13/*表示為課堂*/) {
			Response<Long> result = trainQueryDubboService.queryMaxClassReleaseTime();
			 if (result != null && result.isSuccess()) {
		           Long i = result.getResultObject();
		           jsonObject.put("FunNum", i.toString());
		       } else {
		           log.error("Call queryMaxClassReleaseTime Return False Or Null!!");
		       }
		}else {
			jsonObject.put("FunNum", "");
		}
	}

    /**
     * 
    * <任务数据转换>
    *
    * @param jsonTemp
    * @return
     */
	private int missionInfoTransfer(JSONObject jsonTemp) {
		MissionExecuteQueryDto queryDto = new MissionExecuteQueryDto();
		queryDto.setMerchantId(jsonTemp.getString("StoreSerialNo"));
		queryDto.setStatus(EnumMissionExecuteStatus.STATUS_INIT.getCode());
		long qmStartTime = System.currentTimeMillis();
		log.info("\n******于时间" + DateUtils.getLogDataTime(qmStartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto.toString());
		Response<Integer> res1 = missionExecuteDubboService.queryMissionExecuteCount(queryDto);
		log.info("\n******于时间" + DateUtils.getLogDataTime(qmStartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto.toString()
		        + "\n******耗时为:" + CommonUtil.costTime(qmStartTime));
		// 任务审核中的任务数目
		MissionExecuteQueryDto queryDto1 = new MissionExecuteQueryDto();
		queryDto1.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
		queryDto1.setStatus(EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode());
		long qm2StartTime = System.currentTimeMillis();
		log.info("\n******于时间" + DateUtils.getLogDataTime(qm2StartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto1.toString());
		Response<Integer> res2 = missionExecuteDubboService.queryMissionExecuteCount(queryDto1);
		log.info("\n******于时间" + DateUtils.getLogDataTime(qm2StartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto1.toString()
		        + "\n******耗时为:" + CommonUtil.costTime(qm2StartTime));
		// 未完成任务的数目
		MissionExecuteQueryDto queryDto2 = new MissionExecuteQueryDto();
		queryDto2.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
		queryDto2.setStatus(EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode());
		long qm3StartTime = System.currentTimeMillis();
		log.info("\n*****于时间" + DateUtils.getLogDataTime(qm3StartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto2.toString());
		Response<Integer> res3 = missionExecuteDubboService.queryMissionExecuteCount(queryDto2);
		log.info("\n*****于时间" + DateUtils.getLogDataTime(qm3StartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto2.toString()
		        + "\n******耗时为:" + CommonUtil.costTime(qm3StartTime));
		// 几条完成状态的任务数目
		MissionExecuteQueryDto queryDto3 = new MissionExecuteQueryDto();
		queryDto3.setMerchantId(jsonTemp.get("StoreSerialNo").toString());
		queryDto3.setStatus(EnumMissionExecuteStatus.STATUS_FINISHED.getCode());
		long qm4StartTime = System.currentTimeMillis();
		log.info("\n******于时间" + DateUtils.getLogDataTime(qm4StartTime, null) + "开始调用任务服务queryMissionExecuteCount接口请求数据为：" + queryDto3.toString());
		Response<Integer> res4 = missionExecuteDubboService.queryMissionExecuteCount(queryDto3);
		log.info("\n******于时间" + DateUtils.getLogDataTime(qm4StartTime, null) + "结束调用任务服务queryMissionExecuteCount接口响应数据为：" + queryDto3.toString()
		        + "\n******耗时为:" + CommonUtil.costTime(qm4StartTime));
		// 提示几条未做的任务
		int taskNum = res1.getResultObject() + res2.getResultObject() + res3.getResultObject()
		        + res4.getResultObject();
		return taskNum;
	}
	
}
