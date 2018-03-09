package com.yatang.xc.xcr.biz.core.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.SupplyAdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO;

/**
 * 
* 供应链广告推荐
*		
* @author: zhongrun
* @version: 1.0, 2017年9月8日
 */
public interface SupplyChainAdvertisementDubboService {
	
	/**
	 * 
	* <获取所有组>
	*
	* @param xcAdvertisementQueryDTO
	* @return
	 */
	Response<List<AdvertisementGroupDTO>> findGroup(XcAdvertisementQueryDTO xcAdvertisementQueryDTO);
	/**
	 * 
	* <通过组Id获取组内七个模块>
	*
	* @param id
	* @return
	 */
	Response<List<AdvertisementDTO>> findAdvertisementByGroupId(Integer id);
	/**
	 * 
	* <更新组状态>
	*
	* @param xcAdvertisementUpdateDTO
	* @return
	 */
	Response<Boolean> updateGroupState(XcAdvertisementUpdateDTO xcAdvertisementUpdateDTO);
	/**
	 * 
	* <新增或者更新组>
	*
	* @param advertisementGroupDTO
	* @param advertisementDTOs
	* @return
	 */
	Response<Boolean> insertSupplyChainAdvertisement(AdvertisementGroupDTO advertisementGroupDTO,List<AdvertisementDTO> advertisementDTOs);
	/**
	 * 
	* <为app提供的dubbo服务>
	*
	* @return
	 */
	Response<List<SupplyAdvertisementGroupDTO>> findSupplyChainPublishAdver();
	
	Response<String> findGroupNameById(Integer id);

}
