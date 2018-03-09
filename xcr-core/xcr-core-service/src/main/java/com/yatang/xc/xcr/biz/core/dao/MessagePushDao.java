package com.yatang.xc.xcr.biz.core.dao;

import com.yatang.xc.xcr.biz.core.domain.MessagePushPO;

import java.util.List;
import java.util.Map;

/**
 * @描述: 消息推送数据访问接口
 * @作者: huangjianjun
 * @创建时间: 2017年3月28日-下午4:31:22 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface MessagePushDao{
	/**
	* @Description: 获取发布消息记录数
	* @author huangjianjun
	* @date 2017年4月14日 下午2:33:31
	 */
	Integer getNewMsgCount();
	
	/**
	* @Description:检查消息标题是否存在
	* @author huangjianjun
	* @date 2017年4月14日 下午2:33:37
	 */
	Integer checkTitleExist(String title);

	List<MessagePushPO> listBy(Map<String, Object> paramMap);

	List<MessagePushPO> listByForBack(Map<String, Object> paramMap);

	long insert(MessagePushPO entity);

	MessagePushPO getById(long id);

	int update(MessagePushPO entity);

	int deleteById(long id);
}
