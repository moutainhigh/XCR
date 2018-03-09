package com.yatang.xc.xcr.biz.core.dubboservice;

import java.util.List;
import java.util.Map;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;

/**
 * @描述: 消息dubbo只读接口
 * @作者: huangjianjun
 * @创建时间: 2017年3月29日-下午4:13:21 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface MsgQueryDubboService {
	/**
	* @Description:后台获取消息列表 分页信息
	* @author huangjianjun
	* @date 2017年3月29日 下午5:11:46
	* @param  MsgPushDTO
	 */
	Response<Map<String,Object>> getMsgList(MsgPushDTO param,int pageNum,int pageSize,String shopCode,String provinceId);

	/**
	 * 后台运营获取消息列表
	 * @param param
	 * @param pageNum
	 * @param pageSize
     * @return
     */
	Response<Map<String,Object>> getMsgListBack(MsgPushDTO param,int pageNum,int pageSize);
	
	/**
	* @Description: 查询单条消息详情
	* @author huangjianjun
	* @date 2017年3月29日 下午6:08:35
	* @param  id
	 */
	Response<MsgPushDTO> findOneMsg(Long id);
	
	/**
	* @Description: 获取app页面右上角新消息记录数
	* @author huangjianjun
	* @date 2017年4月13日 下午8:12:31
	 */
	Response<Integer> getMsgCount();
}
