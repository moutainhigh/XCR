package com.yatang.xc.xcr.biz.core.dubboservice;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;



/**
 * @描述: 消息dubbo业务服务接口
 * @作者: huangjianjun
 * @创建时间: 2017年3月28日-下午9:23:35 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface MsgDubboService {
	/**
	* @Description: 编辑消息
	* @Description:id为空为新增反则修改
	* @author huangjianjun
	* @date 2017年3月30日 上午10:45:28
	 */
	Response<MsgPushDTO> editMsg(MsgPushDTO dto);
	
	/**
	* @Description: 删除消息
	* @author huangjianjun
	* @date 2017年4月1日 下午2:10:17
	 */
	@SuppressWarnings("rawtypes")
	Response deleteMsg(Long id);
}
