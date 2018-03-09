package com.yatang.xc.xcr.biz.core.service;

import com.github.pagehelper.PageInfo;
import com.yatang.xc.xcr.biz.core.domain.MessagePushPO;

/**
 * @描述: 消息推送管理服务支撑接口.
 * @作者: huangjianjun
 * @创建时间: 2017-3-28,下午4:52:52 .
 * @版本: 1.0 .
 * @param <T>
 */
public interface MessagePushService {
	/**
	* @Description: 查询消息列表
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:04
	 */
	PageInfo<MessagePushPO>  getMsgListPage(int pageNum, int pageSize, String status,String shopCode,String provinceId);

	/**
	 * 消息列表后台
	 * @param pageNum
	 * @param pageSize
	 * @param status
     * @return
     */
	PageInfo<MessagePushPO>  getMsgListPageBack(int pageNum, int pageSize, String status);
	
	/**
	* @Description: 保存消息信息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	void saveMsg(MessagePushPO bean);
	
	/**
	* @Description: 根据主键查询消息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	MessagePushPO findByPrimaryKey(Long id);
	
	/**
	* @Description: 更新消息信息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	void updateByPrimaryKey(MessagePushPO bean);
	
	/**
	* @Description: 根据主键删除消息信息
	* @author huangjianjun
	* @date 2017年3月21日 下午8:56:55
	 */
	void deleteByPrimaryKey(Long id);
	
	/**
	* @Description: 保存修改
	* @author huangjianjun
	* @date 2017年3月30日 上午11:01:57
	 */
	long savaOrUpdate(MessagePushPO po);
	
	/**
	* @Description: 获取app页面右上角新消息记录数
	* @author huangjianjun
	* @date 2017年4月13日 下午8:12:31
	 */
	Integer getMsgCount();
	
	/**
	* @Description: 检查消息标题是否存在
	* @author huangjianjun
	* @date 2017年4月14日 下午2:52:23
	 */
	boolean checkTitleExist(String title);
}
