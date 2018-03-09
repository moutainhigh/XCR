package com.yatang.xc.xcr.biz.core.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yatang.xc.xcr.biz.core.dao.MessagePushDao;
import com.yatang.xc.xcr.biz.core.domain.MessagePushPO;
import com.yatang.xc.xcr.biz.core.service.MessagePushService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 消息推送管理服务实现.
 * @作者: huangjianjun
 * @创建时间: 2017年3月28日-下午3:00:02 .
 * @版本: 1.0 .
 * @param <T>
 */
@Service("messagePushService")
public class MessagePushServiceImpl implements MessagePushService{
	@Autowired
	private MessagePushDao messagePushDao;

	@Override
	public PageInfo<MessagePushPO> getMsgListPage(int pageNum, int pageSize,
			String status,String shopCode,String provinceId) {
		Map<String, Object> paramMap = new HashMap<String, Object>(); // 业务条件查询参数
		paramMap.put("status",status); // 状态
		paramMap.put("shopCode",shopCode);
		paramMap.put("provinceId", StringUtils.isEmpty(provinceId) ? "noProvinceId":provinceId);
		PageHelper.startPage(pageNum, pageSize);
		List<MessagePushPO> list = messagePushDao.listBy(paramMap);
		PageInfo<MessagePushPO> pageInfo = new PageInfo<MessagePushPO>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<MessagePushPO> getMsgListPageBack(int pageNum, int pageSize, String status) {
		Map<String, Object> paramMap = new HashMap<String, Object>(); // 业务条件查询参数
		paramMap.put("status",status); // 状态
		PageHelper.startPage(pageNum, pageSize);
		List<MessagePushPO> list = messagePushDao.listByForBack(paramMap);
		PageInfo<MessagePushPO> pageInfo = new PageInfo<MessagePushPO>(list);
		return pageInfo;
	}

	@Override
	public void saveMsg(MessagePushPO bean) {
		messagePushDao.insert(bean);
	}

	@Override
	public MessagePushPO findByPrimaryKey(Long id) {
		return messagePushDao.getById(id);
	}

	@Override
	public void updateByPrimaryKey(MessagePushPO bean) {
		messagePushDao.update(bean);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		messagePushDao.deleteById(id);
	}

	@Override
	public long savaOrUpdate(MessagePushPO po) {
		long id = 0;
		if(po != null){
			if(po.getId() != null && po.getId() != 0){
				messagePushDao.update(po);
			}else{
				id = messagePushDao.insert(po);
			}
		}
		return id;
	}

	@Override
	public Integer getMsgCount() {
		return messagePushDao.getNewMsgCount();
	}

	@Override
	public boolean checkTitleExist(String title) {
		return messagePushDao.checkTitleExist(title) >= 1;
	}

}
