package com.yatang.xc.xcr.biz.core.dubboservice;

import java.util.List;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.UpdateStateDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionAppQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionListQueryDTO;
/**
 * 
* 版本管理
*		
* @author: zhongrun
* @version: 1.0, 2017年8月8日
 */
public interface VersionDubboService {
	
	/**
	 * 
	* 获取列表/单独传id也可查询单独列
	*
	* @param versionListQueryDTO
	* @return
	 */
	Response<List<VersionDTO>> findVersionList(VersionListQueryDTO versionListQueryDTO); 
	
	/**
	 * 
	* 查询数量
	*
	* @param versionListQueryDTO
	* @return
	 */
	Response<Integer> findTotalByCondetion(VersionListQueryDTO versionListQueryDTO);
	
	/**
	 * 
	* 为app端提供的dubbo服务(查询是否有更新)
	*
	* @param versionAppQueryDTO
	* @return
	 */
	Response<VersionDTO> findForApp(VersionAppQueryDTO versionAppQueryDTO);
	
	/**
	 * 
	* 修改版本状态（发布/删除）
	*
	* @param updateStateDTO
	* @return
	 */
	Response<Boolean> updateState(UpdateStateDTO updateStateDTO);
	
	/**
	 * 
	* 新增版本
	*
	* @param versionDTO
	* @return
	 */
	Response<Boolean> insertVersion(VersionDTO versionDTO);
	
	/**
	 * 更新版本
	*
	* @param versionDTO
	* @return
	 */
	Response<Boolean> updateVersion(VersionDTO versionDTO);
	
	/**
	 * 
	* 为供应链包提供的服务
	*
	* @param versionAppQueryDTO
	* @return
	 */
	Response<VersionDTO> findForGongYingLian(VersionAppQueryDTO versionAppQueryDTO);
	
	/**
	 * 
	* <获取最大Version的Code>
	*
	* @return
	 */
	Response<Integer> findMaxVersion(VersionDTO versionDTO);
}
