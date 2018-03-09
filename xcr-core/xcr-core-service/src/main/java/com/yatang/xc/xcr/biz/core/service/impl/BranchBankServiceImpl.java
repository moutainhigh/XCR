package com.yatang.xc.xcr.biz.core.service.impl;

import com.yatang.xc.xcr.biz.core.dao.BranchBankDao;
import com.yatang.xc.xcr.biz.core.domain.BranchBankPO;
import com.yatang.xc.xcr.biz.core.domain.ResultBranchBankPO;
import com.yatang.xc.xcr.biz.core.service.BranchBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取支行列表
 * @author dongshengde
 *
 */
@Service
public class BranchBankServiceImpl implements BranchBankService {
	@Autowired
	private BranchBankDao branchBankDao;

	/**
	 * 根据银行名获取支行列表
	 */
	@Override
	public ResultBranchBankPO findBranchListByBankName(BranchBankPO branchBankPO) {
		ResultBranchBankPO resultBranchBankObject = new ResultBranchBankPO();
		branchBankPO.setPageIndex(branchBankPO.getPageIndex() - 1);
		List<BranchBankPO> branchBankPOs = branchBankDao.selectBranchListByBankName(branchBankPO);
		Integer i = branchBankDao.selectCountByBankName(branchBankPO);
		resultBranchBankObject.setBranchBanks(branchBankPOs);
		resultBranchBankObject.setPageIndex(branchBankPO.getPageIndex());
		resultBranchBankObject.setPageSize(branchBankPO.getPageSize());
		resultBranchBankObject.setTotalCount(i);
		resultBranchBankObject.setTotalPage(i / branchBankPO.getPageSize() + 1);
		return resultBranchBankObject;
	}

	/**
	 * 根据银行id获取支行列表
	 */
	@Override
	public ResultBranchBankPO findBranchListByBankCode(BranchBankPO branchBankPO) {
		ResultBranchBankPO resultBranchBankObject = new ResultBranchBankPO();
		branchBankPO.setPageIndex(branchBankPO.getPageIndex() - 1);
		List<BranchBankPO> branchBankPOs = branchBankDao.selectBranchListByBankCode(branchBankPO);
		Integer i = branchBankDao.selectCountByBankCode(branchBankPO);
		resultBranchBankObject.setBranchBanks(branchBankPOs);
		resultBranchBankObject.setPageIndex(branchBankPO.getPageIndex());
		resultBranchBankObject.setPageSize(branchBankPO.getPageSize());
		resultBranchBankObject.setTotalCount(i);
		resultBranchBankObject.setTotalPage(i / branchBankPO.getPageSize() + 1);
		return resultBranchBankObject;
	}

}
