package com.yatang.xc.xcr.biz.core.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO;

/**
 * 
* 启动页广告dubbo服务
*		
* @author: zhongrun
* @version: 1.0, 2017年9月8日
 */
public interface XcAdvertisementDubboService {

	/**
	 * 
	* 启动页新增广告
	*
	* @param xcAdvertisementDTO
	* @return
	 */
	Response<Boolean> insertAdvertisement(XcAdvertisementDTO xcAdvertisementDTO);
	
	/**
	 * 
	* 启动页更新广告
	*
	* @param xcAdvertisementDTO
	* @return
	 */
	Response<Boolean> updateAdvertisement(XcAdvertisementDTO xcAdvertisementDTO);
	
	/**
	 * 
	* 启动页更新状态
	*
	* @param xcAdvertisementUpdateDTO
	* @return
	 */
	Response<Boolean> updateState(XcAdvertisementUpdateDTO xcAdvertisementUpdateDTO);
	
	/**
	 * 
	* 获取广告列表（可通过id获取单个）
	*
	* @param xcAdvertisementQueryDTO
	* @return
	 */
	Response<List<XcAdvertisementDTO>> findAllById(XcAdvertisementQueryDTO xcAdvertisementQueryDTO);
	
	/**
	 * 
	* 为app提供的dubbo服务
	*
	* @return
	 */
	Response<List<XcAdvertisementDTO>> findForApp();
}
