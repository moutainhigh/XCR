package com.yatang.xc.xcr.web.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.vo.CommonListVo;
import com.yatang.xc.xcr.vo.PageResultModel;

/** 
 * @author gaodawei 
 * @Date 2017年9月5日 上午9:28:38 
 * @version 1.0.0
 * @function 雅堂小超人商家版首页供应链推荐商品管理
 */
@Controller
@RequestMapping("xcr/sc")
public class SupplyChainGoodsAction {

	@Autowired
	private SupplyChainAdvertisementDubboService supplyChainService;

	/**
	 * 商品组列表页面跳转
	 * @return
	 */
	@RequestMapping(value = "scGroup", method = {RequestMethod.POST, RequestMethod.GET})
	public String scGroup() {
		return "screen/supplyChain/scGrouplist";
	}
	
	/**
	 * 商品组新增与编辑页面跳转
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "scMng", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView scMng(String method,Integer groupId,ModelMap modelMap,HttpServletRequest request) throws UnsupportedEncodingException {
		long startTime=System.currentTimeMillis();
		CommonUtil.LogRecond("scMng----->findAdvertisementByGroupId", "method:"+method+" groupId:"+groupId, 0,startTime);
		if(method.equals("1")){
			Response<List<AdvertisementDTO>> result=supplyChainService.findAdvertisementByGroupId(groupId);
			Response<String> groupName=supplyChainService.findGroupNameById(groupId);
			CommonUtil.LogRecond("scMng----->findAdvertisementByGroupId", JSONObject.toJSONString(result), 1,startTime);
			CommonUtil.LogRecond("scMng----->findGroupNameById", JSONObject.toJSONString(groupName), 1,startTime);
			modelMap.put("list", result.getResultObject()); //放置物流信息
			modelMap.put("groupName", groupName.getResultObject()); //放置物流信息
		}
        modelMap.put("method", method); //放置物流信息
        modelMap.put("groupId", groupId); //放置物流信息
		return new ModelAndView("screen/supplyChain/scAdd", modelMap);
	}

	@ResponseBody
	@RequestMapping(value = "scGrouplist", method = {RequestMethod.POST})
	public PageResultModel<AdvertisementGroupDTO> scGrouplist(@RequestBody XcAdvertisementQueryDTO paramDto,HttpServletResponse response) throws IOException {
		PageResultModel<AdvertisementGroupDTO> returnResult = new PageResultModel<>();
		long qlStartTime=System.currentTimeMillis();
		CommonUtil.LogRecond("scGrouplist----->findGroup", JSONObject.toJSONString(paramDto), 0,qlStartTime);
		Response<List<AdvertisementGroupDTO>> result=supplyChainService.findGroup(paramDto);
		CommonUtil.LogRecond("scGrouplist----->findGroup", JSONObject.toJSONString(result), 1,qlStartTime);
		if(result.isSuccess()){
			returnResult.setRows(result.getPageNum());
			returnResult.setTotal(result.getPageNum());
			returnResult.setData(result.getResultObject());
		}
		return returnResult;
	}
	
	@ResponseBody
	@RequestMapping(value = "scAddOrUpdate", method = {RequestMethod.POST})
	public void scAdd(@RequestBody CommonListVo<AdvertisementDTO> paramDto,HttpServletResponse response) throws IOException {
		AdvertisementGroupDTO groupDTO = new AdvertisementGroupDTO();
		groupDTO.setGroupName(paramDto.getBack());
		groupDTO.setPositionCode(3);
		if(paramDto.getId()!=null){
			groupDTO.setId(Integer.parseInt(paramDto.getId()));
		}
		long startTime=System.currentTimeMillis();
		CommonUtil.LogRecond("scAddOrUpdate----->insertSupplyChainAdvertisement", JSONObject.toJSONString(paramDto), 0,startTime);
		Response<Boolean> result=supplyChainService.insertSupplyChainAdvertisement(groupDTO, paramDto.getParamList());
		CommonUtil.LogRecond("scAddOrUpdate----->insertSupplyChainAdvertisement", JSONObject.toJSONString(result), 1,startTime);
		response.getWriter().print(JSONObject.toJSONString(result));
	}
	
	@ResponseBody
	@RequestMapping(value = "scSynState", method = {RequestMethod.POST})
	public void scSynState(XcAdvertisementUpdateDTO paramDto,HttpServletResponse response) throws IOException {
		long startTime=System.currentTimeMillis();
		CommonUtil.LogRecond("scSynState----->updateGroupState", JSONObject.toJSONString(paramDto), 0,startTime);
		Response<Boolean> result=supplyChainService.updateGroupState(paramDto);
		CommonUtil.LogRecond("scSynState----->updateGroupState", JSONObject.toJSONString(result), 1,startTime);
		response.getWriter().print(JSONObject.toJSONString(result));
	}
}
