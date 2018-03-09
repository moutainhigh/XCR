package com.yatang.xc.xcr.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.StringUtil;
import com.yatang.xc.xcr.vo.LaunchPageVO;
import com.yatang.xc.xcr.vo.PageResultModel;
import com.yatang.xc.xcr.vo.ReturnClass;

/**
 * 启动页广告设置类
 * @author dongshengde
 *
 */
@Controller
@RequestMapping(value = "xcr")
public class LaunchPageAction {
	private static Logger log = LoggerFactory.getLogger(LaunchPageAction.class);
	@Autowired
	private XcAdvertisementDubboService xcAdvertisementDubboService;

	@ResponseBody
	@RequestMapping(value = "adList.htm", method = { RequestMethod.POST })
	public PageResultModel<LaunchPageVO> getADList(HttpServletResponse response) throws Exception {
		PageResultModel<LaunchPageVO> pageResultModel = new PageResultModel<LaunchPageVO>();
		XcAdvertisementQueryDTO xcAdvertisementQueryDTO = new XcAdvertisementQueryDTO();
		xcAdvertisementQueryDTO.setType(2);
		Long getSignArrayStartTime = System.currentTimeMillis();
		log.info("\n*****************调用LaunchPageAction.adList接口的开始时间："
				+ DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
				+ JSONObject.toJSONString(JSONObject.toJSONString(xcAdvertisementQueryDTO)));
		Response<List<XcAdvertisementDTO>> res = xcAdvertisementDubboService.findAllById(xcAdvertisementQueryDTO);
		StringUtil.printLogMethod(res, getSignArrayStartTime, "LaunchPageAction.adList");
		List<XcAdvertisementDTO> xcAdvertisementDTOs = res.getResultObject();
		List<LaunchPageVO> launchPageVOs = new ArrayList<>();
		for (XcAdvertisementDTO xcAdvertisementDTO : xcAdvertisementDTOs) {
			SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			LaunchPageVO launchPageVO = new LaunchPageVO();
			launchPageVO.setLaunchAURL(StringUtil.replaceNULLToStr(xcAdvertisementDTO.getActiveUrl()));
			launchPageVO.setCurrentState(xcAdvertisementDTO.getState());
			launchPageVO.setId(xcAdvertisementDTO.getId());
			launchPageVO.setImagePreview(xcAdvertisementDTO.getImgUrl());
			launchPageVO.setLastModifyTime(dateFormater2.format(xcAdvertisementDTO.getLastModifyTime()));
			launchPageVOs.add(launchPageVO);
		}
		pageResultModel.setData(launchPageVOs);
		log.info("接口adList.htm返回的结果：" + JSONObject.toJSONString(pageResultModel));
		return pageResultModel;
	}

	/**
	 * 更改图片
	 * @param launchPageVO
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "saveLaunchImage", method = { RequestMethod.POST })
	public ReturnClass saveLaunchImage(@RequestBody LaunchPageVO launchPageVO, HttpServletResponse response)
			throws IOException {
		ReturnClass returnClass = new ReturnClass();
		XcAdvertisementDTO xcAdvertisementDTO = new XcAdvertisementDTO();
		xcAdvertisementDTO.setImgUrl(launchPageVO.getImagePreview());
		xcAdvertisementDTO.setId(launchPageVO.getId());
		xcAdvertisementDTO.setActiveUrl(launchPageVO.getLaunchAURL());
		Long getSignArrayStartTime = System.currentTimeMillis();
		log.info("\n*****************调用LaunchPageAction.saveLaunchImage接口的开始时间："
				+ DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
				+ JSONObject.toJSONString(JSONObject.toJSONString(xcAdvertisementDTO)));
		Response<Boolean> res = xcAdvertisementDubboService.updateAdvertisement(xcAdvertisementDTO);
		log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
				+ "调用LaunchPageAction.saveLaunchImage接口   调用结束" + "\n*****************响应数据是："
				+ JSONObject.toJSONString(res.getResultObject()) + "\n***************所花费时间为："
				+ CommonUtil.costTime(getSignArrayStartTime));
		if (res.isSuccess()) {
			returnClass.setFlag(true);
			returnClass.setErrorMessage("更改成功");
		} else {
			returnClass.setFlag(false);
			returnClass.setErrorMessage("更改失败");
		}
		return returnClass;
	}

	/**
	 * 更改起始页图片状态
	 * @param launchPageVO
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "updateLaunchState", method = { RequestMethod.POST })
	public ReturnClass updateLaunchState(@RequestBody LaunchPageVO launchPageVO, HttpServletResponse response)
			throws IOException {
		ReturnClass returnClass = new ReturnClass();
		XcAdvertisementUpdateDTO xcAdvertisementUpdateDTO = new XcAdvertisementUpdateDTO();
		xcAdvertisementUpdateDTO.setId(launchPageVO.getId());
		xcAdvertisementUpdateDTO.setState(launchPageVO.getCurrentState());
		Long getSignArrayStartTime = System.currentTimeMillis();
		log.info("\n*****************调用LaunchPageAction.updateLaunchState接口的开始时间："
				+ DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
				+ JSONObject.toJSONString(JSONObject.toJSONString(xcAdvertisementUpdateDTO)));
		Response<Boolean> res = xcAdvertisementDubboService.updateState(xcAdvertisementUpdateDTO);
		log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
				+ "调用LaunchPageAction.updateLaunchState接口   调用结束" + "\n*****************响应数据是："
				+ JSONObject.toJSONString(res.getResultObject()) + "\n***************所花费时间为："
				+ CommonUtil.costTime(getSignArrayStartTime));
		if (res.isSuccess()) {
			returnClass.setFlag(true);
			returnClass.setErrorMessage("更改成功");
		} else {
			returnClass.setFlag(false);
			returnClass.setErrorMessage("更改失败");
		}
		return returnClass;
	}
}
