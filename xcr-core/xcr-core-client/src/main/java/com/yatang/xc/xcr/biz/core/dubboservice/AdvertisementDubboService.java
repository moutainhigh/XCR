package com.yatang.xc.xcr.biz.core.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dto.StateDTO;

public interface AdvertisementDubboService {
	
	/**
	 * 运维修改广告发布状态
	* <method description>
	*
	* @param id
	* @param state
	* @return
	 */
	Response<Boolean> updateState(Integer id,Integer state);
	
	/**
	 * 运维根据state查询广告
	* <method description>
	*
	* @param state
	* @return
	 */
	Response<List<AdvertisementDTO>> findBystate(StateDTO state);
	
	Response<Boolean> insertAdvertis(AdvertisementDTO advertisementDTO);
	
	/**
	 * app广告查询
	* <method description>
	*
	* @param positionCode
	* @return
	 */
	Response<List<AdvertisementDTO>> findstate(Integer positionCode);
	
	/**
	 * 
	* <method description>
	*
	* @param advertisementGroupPO
	* @return
	 */
	Response<Boolean> insertAdvertisementGroup(AdvertisementGroupDTO advertisementGroupDTO);
	
	/**
	 * 运维修改组的状态
	* <method description>
	*
	* @param updateDTO
	* @return
	 */
	Response<Boolean> updateAdvertisementGroup(AdvertisementUpdateDTO updateDTO);
	
	Response<List<AdvertisementGroupDTO>> findAdvertisementGroup(AdvertisementGroupQueryDTO advertisementGroupDTO);
	
	Response<Integer> findAdvertisementTotal();
	
	Response<Integer> findAdvertisementGroupTotal();
	
	Response<AdvertisementDTO> findAdvertisementByid(Integer id);
	
	
	Response<Boolean> updateAdver(AdvertisementDTO advertisementDTO);
	
	Response<Integer> validateTotalAdver(Integer id);
	
	
	Response<AdvertisementGroupDTO> findAdverGroupById(Integer id);
	
	Response<AdvertisementDTO> findAdverByName(String name);
	
	Response<AdvertisementGroupDTO> findAdverGroupByName(String name);

}
