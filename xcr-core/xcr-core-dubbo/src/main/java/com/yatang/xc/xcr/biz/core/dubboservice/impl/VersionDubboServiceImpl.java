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
import com.yatang.xc.xcr.biz.core.domain.UpdateStatePO;
import com.yatang.xc.xcr.biz.core.domain.VersionAppQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionListQueryPO;
import com.yatang.xc.xcr.biz.core.domain.VersionPO;
import com.yatang.xc.xcr.biz.core.dto.UpdateStateDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionAppQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionListQueryDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.VersionDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.AdverEnum;
import com.yatang.xc.xcr.biz.core.service.VersionService;

/**
 * 
* 版本管理dubbo服务
*		
* @author: zhongrun
* @version: 1.0, 2017年8月8日
 */
@Service("versionDubboService")
@Transactional
public class VersionDubboServiceImpl implements VersionDubboService{
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private VersionService versionService;

	@Override
	public Response<List<VersionDTO>> findVersionList(
			VersionListQueryDTO versionListQueryDTO) {		
		VersionListQueryPO versionListQueryPO=null;
		List<VersionPO> versionPOs =null;
		try {		
		if (versionListQueryDTO==null) {
			 versionPOs = versionService.findVersionList(versionListQueryPO);
		}else {
			versionListQueryPO = BeanConvertUtils.convert(versionListQueryDTO, VersionListQueryPO.class);
			 versionPOs = versionService.findVersionList(versionListQueryPO);
		}
		List<VersionDTO> versionDTOs = BeanConvertUtils.convertList(versionPOs, VersionDTO.class);
		return new Response<List<VersionDTO>>(true,versionDTOs);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));			
			return new Response<List<VersionDTO>>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
		
	}

	@Override
	public Response<Integer> findTotalByCondetion(VersionListQueryDTO versionListQueryDTO) {
		VersionListQueryPO versionListQueryPO=new VersionListQueryPO();
		Integer totals= null;
		try {		
			if (versionListQueryDTO==null) {
				totals = versionService.findTotalByCondetion(versionListQueryPO);
			}else {
				versionListQueryPO = BeanConvertUtils.convert(versionListQueryDTO, VersionListQueryPO.class);
				totals = versionService.findTotalByCondetion(versionListQueryPO);
			}
			return new Response<Integer>(true,totals);
			} catch (Exception e) {
				log.error(ExceptionUtils.getFullStackTrace(e));			
				return new Response<Integer>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
			}
		
	}

	@Override
	public Response<VersionDTO> findForApp(VersionAppQueryDTO versionAppQueryDTO) {
		Integer isLiveUp=0;
		VersionAppQueryPO versionAppQueryPO = BeanConvertUtils.convert(versionAppQueryDTO, VersionAppQueryPO.class);		
		try {
			List<VersionPO> versionPOs = versionService.findByTypeAndVersion(versionAppQueryPO);			
			if (versionPOs.isEmpty()||versionPOs==null) {
				Response<VersionDTO> response = new Response<VersionDTO>();
				response.setCode("100");
				response.setSuccess(true);				
				return response;
			}
			
			for (VersionPO versionPO : versionPOs) {
				if (versionPO.getIsLiveUp()==1) {
					isLiveUp=1;
				}
			}			
			
			
			VersionDTO versionDTO = BeanConvertUtils.convert(versionPOs.get(0), VersionDTO.class);
			if (isLiveUp==1) {
				versionDTO.setIsLiveUp(isLiveUp);
			}
			return new Response<VersionDTO>(true,versionDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));			
			return new Response<VersionDTO>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}

	}

	@Override
	public Response<Boolean> updateState(UpdateStateDTO updateStateDTO) {
		try {
			UpdateStatePO updateStatePO = BeanConvertUtils.convert(updateStateDTO, UpdateStatePO.class);
			if (updateStatePO.getState()!=null) {
				Date date = new Date();			
				updateStatePO.setPublishTime(date);
			}
			VersionListQueryPO versionListQueryPO= new VersionListQueryPO();
			versionListQueryPO.setId(updateStatePO.getId());
			List<VersionPO> versionPOs = versionService.findVersionList(versionListQueryPO);
			VersionPO versionPO = versionPOs.get(0);
			//校验是否有同名同系统的版本在发布状态
			List<VersionPO> versionPO2 = versionService.validateVersion(versionPO);		
			if (!versionPO2.isEmpty()&&updateStatePO.getIsDelate()==null) {
				if (versionPO2.get(0).getType()!=2) {
					return new Response<Boolean>(false,AdverEnum.ADVER_REPEAT_ERROR.getMsg(),AdverEnum.ADVER_REPEAT_ERROR.getCode());
				}	
			}
			
			versionService.updateStateAndPublish(updateStatePO);
			
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));			
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<Boolean> insertVersion(VersionDTO versionDTO) {
		VersionPO versionPO = BeanConvertUtils.convert(versionDTO, VersionPO.class);
		try {
			versionService.insertVersion(versionPO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));			
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<Boolean> updateVersion(VersionDTO versionDTO) {
		VersionPO versionPO = BeanConvertUtils.convert(versionDTO, VersionPO.class);
		try {
			versionService.updateVersion(versionPO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));			
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<VersionDTO> findForGongYingLian(
			VersionAppQueryDTO versionAppQueryDTO) {
		Integer isLiveUp=0;
		VersionAppQueryPO versionAppQueryPO = BeanConvertUtils.convert(versionAppQueryDTO, VersionAppQueryPO.class);		
		try {
			List<VersionPO> versionPOs = versionService.findGongYingLianVersion(versionAppQueryPO);			
			if (versionPOs.isEmpty()||versionPOs==null) {
				Response<VersionDTO> response = new Response<VersionDTO>();
				response.setCode("100");
				response.setSuccess(true);				
				return response;
			}
			
			for (VersionPO versionPO : versionPOs) {
				if (versionPO.getIsLiveUp()==1) {
					isLiveUp=1;
				}
			}			
			
			
			VersionDTO versionDTO = BeanConvertUtils.convert(versionPOs.get(0), VersionDTO.class);
			if (isLiveUp==1) {
				versionDTO.setIsLiveUp(isLiveUp);
			}
			return new Response<VersionDTO>(true,versionDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));			
			return new Response<VersionDTO>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}

	}

	/**
	 * 
	 * 推荐填写的code+1
	 */
	@Override
	public Response<Integer> findMaxVersion(VersionDTO versionDTO) {
		VersionPO versionPO = BeanConvertUtils.convert(versionDTO, VersionPO.class);
		try {
			Integer code = versionService.findMaxVersion(versionPO);
			if (code==null) {
				code=0;
			}
			return new Response<Integer>(true,code+1);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Integer>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}
	
	


}
