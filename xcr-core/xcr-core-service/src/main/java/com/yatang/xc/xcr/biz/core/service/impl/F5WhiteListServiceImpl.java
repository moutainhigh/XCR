package com.yatang.xc.xcr.biz.core.service.impl;

import com.yatang.xc.xcr.biz.core.dao.XcrWhiteListDao;
import com.yatang.xc.xcr.biz.core.domain.XcrWhiteListPO;
import java.util.List;
import java.util.Map;
import com.yatang.xc.xcr.biz.core.service.IF5WhiteListService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月11日 11:39
 * @Summary :
 */
@Service 
@Transactional(rollbackFor=Exception.class)
public class F5WhiteListServiceImpl  implements IF5WhiteListService {

	private @Autowired XcrWhiteListDao mapper;

	///<Summary>
	/// @Function Description : 查询一条记录
	///</Summary>
	@Override
	public XcrWhiteListPO selectOne(Map<String, Object> param) {
		if (param == null  || param.size() == 0)return null;
		return mapper.one(param);
	}

}

