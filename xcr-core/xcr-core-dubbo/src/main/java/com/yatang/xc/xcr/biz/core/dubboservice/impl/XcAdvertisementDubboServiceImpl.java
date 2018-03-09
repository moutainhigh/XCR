package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busi.common.resp.Response;
import com.busi.common.utils.BeanConvertUtils;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementPO;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementQueryPO;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementUpdatePO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.AdverEnum;
import com.yatang.xc.xcr.biz.core.service.AdvertisementService;

/**
 * 
* 启动页广告
*		
* @author: zhongrun
* @version: 1.0, 2017年9月5日
 */
@Service("xcAdvertisementDubboService")
@Transactional
public class XcAdvertisementDubboServiceImpl implements XcAdvertisementDubboService{
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	AdvertisementService advertisementService;

	/**
	 * 新增广告
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService#insertAdvertisement(com.yatang.xc.xcr.biz.core.dto.XcAdvertisementDTO)
	 */
	@Override
	public Response<Boolean> insertAdvertisement(XcAdvertisementDTO xcAdvertisementDTO) {
		XcAdvertisementPO xcAdvertisementPO = BeanConvertUtils.convert(xcAdvertisementDTO, XcAdvertisementPO.class);
		try {
			advertisementService.insertXcAdvertisement(xcAdvertisementPO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	/**
	 * 更新广告
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService#updateAdvertisement(com.yatang.xc.xcr.biz.core.dto.XcAdvertisementDTO)
	 */
	@Override
	public Response<Boolean> updateAdvertisement(XcAdvertisementDTO xcAdvertisementDTO) {
		XcAdvertisementPO xcAdvertisementPO = BeanConvertUtils.convert(xcAdvertisementDTO, XcAdvertisementPO.class);
		try {
			xcAdvertisementPO.setLastModifyTime(new Date());
			advertisementService.updateXcAdvertisement(xcAdvertisementPO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	/**
	 * 更新状态
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService#updateState(com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO)
	 */
	@Override
	public Response<Boolean> updateState(XcAdvertisementUpdateDTO xcAdvertisementUpdateDTO) {
		XcAdvertisementUpdatePO xcAdvertisementUpdatePO = BeanConvertUtils.convert(xcAdvertisementUpdateDTO, XcAdvertisementUpdatePO.class);
		try {
			advertisementService.updateState(xcAdvertisementUpdatePO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	/**
	 * 根据id查询广告
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService#findAllById(com.yatang.xc.xcr.biz.core.dto.XcAdvertisementQueryDTO)
	 */
	@Override
	public Response<List<XcAdvertisementDTO>> findAllById(XcAdvertisementQueryDTO xcAdvertisementQueryDTO) {
		Response<List<XcAdvertisementDTO>> response = new Response<List<XcAdvertisementDTO>>();
		XcAdvertisementQueryPO xcAdvertisementQueryPO = BeanConvertUtils.convert(xcAdvertisementQueryDTO, XcAdvertisementQueryPO.class);
		try {
			List<XcAdvertisementPO> xcAdvertisementPOs = advertisementService.findAllById(xcAdvertisementQueryPO);
			List<XcAdvertisementDTO> xcAdvertisementDTOs = BeanConvertUtils.convertList(xcAdvertisementPOs, XcAdvertisementDTO.class);
			response.setResultObject(xcAdvertisementDTOs);
			response.setSuccess(true);
			return response;
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			response.setErrorMessage(e.getMessage());
			response.setCode(AdverEnum.ERROR_CODE.getCode());
			response.setSuccess(false);
			return response;
		}
	}

	/**
	 * 为app提供的dubbo服务
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService#findForApp()
	 */
	@Override
	public Response<List<XcAdvertisementDTO>> findForApp() {
		
		try {
			List<XcAdvertisementPO> xcAdvertisementPOs = advertisementService.findAllForApp();
			List<XcAdvertisementDTO> xcAdvertisementDTOs = BeanConvertUtils.convertList(xcAdvertisementPOs, XcAdvertisementDTO.class);
			return new Response<List<XcAdvertisementDTO>>(true,xcAdvertisementDTOs);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<List<XcAdvertisementDTO>>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

}
