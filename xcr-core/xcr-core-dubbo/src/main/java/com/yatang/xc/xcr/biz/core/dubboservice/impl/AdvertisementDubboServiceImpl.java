package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import java.text.SimpleDateFormat;
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
import com.yatang.xc.xcr.biz.core.domain.AdverWhenStateToPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementGroupPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementGroupQueryPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementPO;
import com.yatang.xc.xcr.biz.core.domain.AdvertisementUpdatePO;
import com.yatang.xc.xcr.biz.core.domain.StatePO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dto.StateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.AdverEnum;
import com.yatang.xc.xcr.biz.core.service.AdvertisementService;

@Service("advertisementDubboService")
@Transactional
public class AdvertisementDubboServiceImpl implements AdvertisementDubboService{
	
	private  static Integer MAX_LENGTH=10000;
	
	protected final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	AdvertisementService advertisementService;

	/**
	 * @param id 广告id
	 * @param state 广告状态
	 * 运维更新广告
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService#updateState(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Response<Boolean> updateState(Integer id, Integer state) {
		AdvertisementUpdatePO updatePO = new AdvertisementUpdatePO();
		updatePO.setId(id);
		updatePO.setState(state);
		try {
			advertisementService.updateAdvertisement(updatePO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}	
	}

	/**
	 * @param state 广告状态
	 * 运维根据state查询广告信息
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService#findBystate(java.lang.Integer)
	 */
	@Override
	public Response<List<AdvertisementDTO>> findBystate(StateDTO state) {
		
		StatePO statePO = BeanConvertUtils.convert(state, StatePO.class);
		try {
			List<AdvertisementPO> advertidements = advertisementService.findAdvertisementByState(statePO);
			List<AdvertisementDTO> advertisementDTOs = BeanConvertUtils.convertList(advertidements, AdvertisementDTO.class);
			return new Response<List<AdvertisementDTO>>(true,advertisementDTOs);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<List<AdvertisementDTO>>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}

	}

	/**
	 * @param advertisementDTO 广告信息
	 * 运维添加广告
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService#insertAdvertis(com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO)
	 */
	@Override
	public Response<Boolean> insertAdvertis(AdvertisementDTO advertisementDTO) {
		AdvertisementPO advertisementPO = BeanConvertUtils.convert(advertisementDTO, AdvertisementPO.class);
		try {	
			Integer total = advertisementService.validateTotalAdver(advertisementDTO.getGroupId());
			if (total>=5) {
				return new Response<Boolean>(false,"单个广告组广告数量不能超过五个",AdverEnum.ADVER_GROUP_INCLUDE_CODE.getCode());
			}
			AdvertisementPO advertisementPO2 = advertisementService.findAdverByName(advertisementDTO.getPicName());
			if (advertisementPO2!=null) {
				return new Response<Boolean>(false,"数据重复",AdverEnum.ADVER_EXSIST_CODE.getCode());
			}
			advertisementPO.setCreateTime(new Date().toString());
			advertisementService.insertAdvertisement(advertisementPO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}	
	}

	/**
	 * app端调用查询指定位置广告
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService#findstate()
	 */
	@Override
	public Response<List<AdvertisementDTO>> findstate(Integer positionCode) {
		
		StatePO statePO = appConditionPackage(positionCode);
		try {
			List<AdvertisementPO> advertidements = advertisementService.findAdvertisementByState(statePO);
			List<AdvertisementDTO> advertisementDTOs = BeanConvertUtils.convertList(advertidements, AdvertisementDTO.class);
			return new Response<List<AdvertisementDTO>>(true,advertisementDTOs);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<List<AdvertisementDTO>>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}

	}

	private StatePO appConditionPackage(Integer positionCode) {
		StatePO statePO = new StatePO();
		statePO.setState(1);
		statePO.setLength(MAX_LENGTH);
		statePO.setPositionCode(positionCode);
		return statePO;
	}

	/**
	 * 运维添加分组
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService#insertAdvertisementGroup(com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO)
	 */
	@Override
	public Response<Boolean> insertAdvertisementGroup(
			AdvertisementGroupDTO advertisementGroupDTO) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		advertisementGroupDTO.setCreateTime(simpleDateFormat.format(new Date()).toString());	
		advertisementGroupDTO.setLastModifyTime(simpleDateFormat.format(new Date()).toString());
		AdvertisementGroupPO advertisementGroupPO = BeanConvertUtils.convert(advertisementGroupDTO, AdvertisementGroupPO.class);
		try {
			AdvertisementGroupPO  advertisementGroupPO2 = advertisementService.findAdverGroupByName(advertisementGroupDTO.getGroupName());
			if (advertisementGroupPO2!=null) {
				return new Response<Boolean>(false,AdverEnum.ADVER_GROUP_EXSIST_CODE.getMsg(),AdverEnum.ADVER_GROUP_EXSIST_CODE.getCode());
			}
			
			advertisementService.insertAdvertisementGroup(advertisementGroupPO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}

	}

	/**
	 * 修改组状态
	 * @see com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService#updateAdvertisementGroup(com.yatang.xc.xcr.biz.core.dto.AdvertisementUpdateDTO)
	 */
	@Override
	public Response<Boolean> updateAdvertisementGroup(
			AdvertisementUpdateDTO updateDTO) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		updateDTO.setLastModifyTime(simpleDateFormat.format(new Date()).toString());
		AdvertisementUpdatePO updatePO = BeanConvertUtils.convert(updateDTO,AdvertisementUpdatePO.class );
		try {
			if (updateDTO.getState()==2) {
				AdverWhenStateToPO staToPO = new AdverWhenStateToPO();
				staToPO.setId(updateDTO.getId());
				//修改组名为游离组
				advertisementService.updateWhenStateISTo(staToPO);
				updatePO.setState(0);
				//修改组下广告状态为未启用
				advertisementService.updateAdverByGroupState(updatePO);
				updatePO.setState(2);
				//删除组
				advertisementService.updateAdvertisementGroup(updatePO);
			}else {
				if (updateDTO.getState()==1) {
					Integer adverTotal2 = advertisementService.findAdverTotalByPosition(updateDTO.getPositionCode());
					if (adverTotal2>0) {
						return new Response<Boolean>(false,AdverEnum.ADVER_POSITION_INCLUDE_CODE.getMsg(),AdverEnum.ADVER_POSITION_INCLUDE_CODE.getCode());
					}
					//修改组下广告状态
					advertisementService.updateAdverByGroupState(updatePO);
					//修改组状态
					advertisementService.updateAdvertisementGroup(updatePO);
				}else {
					//修改组下广告状态
					advertisementService.updateAdverByGroupState(updatePO);
					//修改组状态
					advertisementService.updateAdvertisementGroup(updatePO);
				}				
			}
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<List<AdvertisementGroupDTO>> findAdvertisementGroup(
			AdvertisementGroupQueryDTO advertisementGroupDTO) {
		AdvertisementGroupQueryPO advertisementGroupPO = BeanConvertUtils.convert(advertisementGroupDTO, AdvertisementGroupQueryPO.class);
		try {
			List<AdvertisementGroupPO>  advertisementGroupPOs = advertisementService.findAdvertisementGroup(advertisementGroupPO);
			List<AdvertisementGroupDTO> advertisementGroupDTO2 = BeanConvertUtils.convertList(advertisementGroupPOs, AdvertisementGroupDTO.class);		
			return new Response<List<AdvertisementGroupDTO>>(true,advertisementGroupDTO2);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<List<AdvertisementGroupDTO>>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<Integer> findAdvertisementTotal() {
		try {
			Integer total = advertisementService.findAdvertisementTotal();
			return new Response<Integer>(true,total);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Integer>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<Integer> findAdvertisementGroupTotal() {
		try {
			Integer total = advertisementService.findAdvertisementGroupTotal();
			return new Response<Integer>(true,total);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Integer>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}

}

	@Override
	public Response<AdvertisementDTO> findAdvertisementByid(Integer id) {
		try {
			AdvertisementPO advertisementPO = advertisementService.findAdvertisementById(id);
			AdvertisementDTO advertisementDTO = BeanConvertUtils.convert(advertisementPO, AdvertisementDTO.class);
			return new Response<AdvertisementDTO>(true,advertisementDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<AdvertisementDTO>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<Boolean> updateAdver(AdvertisementDTO advertisementDTO) {
		
		AdvertisementPO updatePO = BeanConvertUtils.convert(advertisementDTO,AdvertisementPO.class );
		try {
			advertisementService.updateAdertise(updatePO);
			return new Response<Boolean>(true,true);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Boolean>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
}

	@Override
	public Response<Integer> validateTotalAdver(Integer id) {
		try {
			Integer total = advertisementService.validateTotalAdver(id);
			return new Response<Integer>(true,total);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<Integer>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<AdvertisementGroupDTO> findAdverGroupById(Integer id) {
		try {
			AdvertisementGroupPO advertisementGroupPO = advertisementService.findAdverGroupById(id);
			AdvertisementGroupDTO advertisementGroupDTO = BeanConvertUtils.convert(advertisementGroupPO, AdvertisementGroupDTO.class);
			return new Response<AdvertisementGroupDTO>(true,advertisementGroupDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<AdvertisementGroupDTO>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<AdvertisementDTO> findAdverByName(String name) {
		try {
			AdvertisementPO advertisementPO = advertisementService.findAdverByName(name);
			AdvertisementDTO advertisementDTO = BeanConvertUtils.convert(advertisementPO, AdvertisementDTO.class);
			return new Response<AdvertisementDTO>(true,advertisementDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<AdvertisementDTO>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}

	@Override
	public Response<AdvertisementGroupDTO> findAdverGroupByName(String name) {
		try {
			AdvertisementGroupPO advertisementGroupPO = advertisementService.findAdverGroupByName(name);
			AdvertisementGroupDTO advertisementGroupDTO = BeanConvertUtils.convert(advertisementGroupPO, AdvertisementGroupDTO.class);
			return new Response<AdvertisementGroupDTO>(true,advertisementGroupDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<AdvertisementGroupDTO>(false,e.getMessage(),AdverEnum.ERROR_CODE.getCode());
		}
	}
}