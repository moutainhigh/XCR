package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.yatang.xc.xcr.biz.core.domain.AdvertisementGroupPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementPO;
import com.yatang.xc.xcr.biz.core.domain.SupplyAdvertisementGroupPO;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementQueryPO;
import com.yatang.xc.xcr.biz.core.domain.XcAdvertisementUpdatePO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.SupplyAdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.AdverEnum;
import com.yatang.xc.xcr.biz.core.service.AdvertisementService;

/**
 * 
* 供应链商品广告推广
*		
* @author: zhongrun
* @version: 1.0, 2017年9月6日
 */
@Service("supplyChainAdvertisementDubboService")
@Transactional
public class SupplyChainAdvertisementDubboServiceImpl implements SupplyChainAdvertisementDubboService{
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	AdvertisementService advertisementService;
	
	/**
	 * 查询所有组
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService#findGroup()
	 */
	@Override
	public Response<List<AdvertisementGroupDTO>> findGroup(XcAdvertisementQueryDTO xcAdvertisementQueryDTO) {
		Response<List<AdvertisementGroupDTO>> response = new Response<List<AdvertisementGroupDTO>>();
			
		try {
			XcAdvertisementQueryPO xcAdvertisementQueryPO = BeanConvertUtils.convert(xcAdvertisementQueryDTO, XcAdvertisementQueryPO.class);
			xcAdvertisementQueryPO.setType(3);
			List<AdvertisementGroupPO> advertisementGroupPOs = advertisementService.findGroup(xcAdvertisementQueryPO);
			Integer total = advertisementService.findGroupTotal(xcAdvertisementQueryPO);
			List<AdvertisementGroupDTO> advertisementGroupDTOs = BeanConvertUtils.convertList(advertisementGroupPOs, AdvertisementGroupDTO.class);
			response.setResultObject(advertisementGroupDTOs);
			response.setSuccess(true);
			response.setPageNum(total);
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
	 * 查询组对应的广告模块
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService#findAdvertisementByGroupId(java.lang.Integer)
	 */
	@Override
	public Response<List<AdvertisementDTO>> findAdvertisementByGroupId(Integer id) {
		Response<List<AdvertisementDTO>> response = new Response<List<AdvertisementDTO>>();
		try {
			XcAdvertisementQueryPO xcAdvertisementQueryPO = new XcAdvertisementQueryPO();
			xcAdvertisementQueryPO.setType(3);
			List<AdvertisementPO> advertisementPOs = advertisementService.findAdverByGroupId(id);
			List<AdvertisementDTO> advertisementDTOs = BeanConvertUtils.convertList(advertisementPOs, AdvertisementDTO.class);
			response.setResultObject(advertisementDTOs);
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
	 * 更新组的状态（1：发:0：禁:2：删除）
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService#updateGroupState(com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO)
	 */
	@Override
	public Response<Boolean> updateGroupState(XcAdvertisementUpdateDTO xcAdvertisementUpdateDTO) {
		Response<Boolean> response = new Response<Boolean>();
		try {
			XcAdvertisementUpdatePO xcAdvertisementUpdatePO = BeanConvertUtils.convert(xcAdvertisementUpdateDTO, XcAdvertisementUpdatePO.class);
			advertisementService.updateGroupState(xcAdvertisementUpdatePO);
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
	 * 新增或更新组模块
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService#insertSupplyChainAdvertisement(com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO, java.util.List)
	 */
	@Override
	public Response<Boolean> insertSupplyChainAdvertisement(AdvertisementGroupDTO advertisementGroupDTO,
			List<AdvertisementDTO> advertisementDTOs) {
		try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				AdvertisementGroupPO advertisementGroupPO = BeanConvertUtils.convert(advertisementGroupDTO, AdvertisementGroupPO.class);
				List<AdvertisementPO> advertisementPOs = BeanConvertUtils.convertList(advertisementDTOs, AdvertisementPO.class);
				Date date = new Date();
				advertisementGroupPO.setLastModifyTime(simpleDateFormat.format(date));
				for (AdvertisementPO advertisementPO : advertisementPOs) {
					advertisementPO.setLastModifyTime(date);
				}
				if (advertisementGroupPO.getId()!=null) {
					//更新组和组内模块
					advertisementService.updateGroup(advertisementGroupPO);
					if (!advertisementDTOs.isEmpty()&&advertisementDTOs!=null) {
						advertisementService.updateChain(advertisementPOs);
					}
					return new Response<Boolean>(true,true);
				}else {
					if (advertisementDTOs.size()!=7) {
						return new Response<Boolean>(false,"插入数据不足七个","500");
					}
					//新增组和组内模块
					//位置为最大位置+1
					Integer code = advertisementService.findMaxCode();
					if (code==null) {
						code=0;
					}
					advertisementGroupPO.setAddrCode(code+1);
					advertisementService.insertGroup(advertisementGroupPO);
					for (AdvertisementPO advertisementPO : advertisementPOs) {
						advertisementPO.setGroupId(advertisementGroupPO.getId());
						advertisementPO.setState(0);
						advertisementPO.setType(advertisementGroupPO.getPositionCode());
					}
					if (!advertisementDTOs.isEmpty()&&advertisementDTOs!=null) {
						advertisementService.insertChain(advertisementPOs);
					}
					return new Response<Boolean>(true,true);
				}
		} catch (Exception e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
				return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	/**
	 * 为app提供的接口
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService#findSupplyChainPublishAdver()
	 */
	@Override
	public Response<List<SupplyAdvertisementGroupDTO>> findSupplyChainPublishAdver() {
		try {
			List<SupplyAdvertisementGroupPO> supplyAdvertisementGroupPOs = advertisementService.findPublishAdver();
			List<SupplyAdvertisementGroupDTO> supplyAdvertisementGroupDTOs =new ArrayList<SupplyAdvertisementGroupDTO>();
			
			for (SupplyAdvertisementGroupPO supplyAdvertisementGroupPO : supplyAdvertisementGroupPOs) {
				SupplyAdvertisementGroupDTO supplyAdvertisementGroupDTO = BeanConvertUtils.convert(supplyAdvertisementGroupPO, SupplyAdvertisementGroupDTO.class);
				List<AdvertisementDTO> advertisementDTOs = BeanConvertUtils.convertList(supplyAdvertisementGroupPO.getAdvertisementPOs(), AdvertisementDTO.class);
				supplyAdvertisementGroupDTO.setAdvertisementDTOs(advertisementDTOs);
				supplyAdvertisementGroupDTOs.add(supplyAdvertisementGroupDTO);
			}
			
			//List<SupplyAdvertisementGroupDTO> supplyAdvertisementGroupDTOs = BeanConvertUtils.convertList(supplyAdvertisementGroupPOs, SupplyAdvertisementGroupDTO.class);
			return new Response<List<SupplyAdvertisementGroupDTO>>(true,supplyAdvertisementGroupDTOs);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<List<SupplyAdvertisementGroupDTO>>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<String> findGroupNameById(Integer id) {
		try {
			if (id==null) {
				return new Response<String>(false,"id不能为空",AdverEnum.ERROR_CODE.getCode());
			}
			String name = advertisementService.findGroupNameById(id);
			return new Response<String>(true,name);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<String>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	

}
