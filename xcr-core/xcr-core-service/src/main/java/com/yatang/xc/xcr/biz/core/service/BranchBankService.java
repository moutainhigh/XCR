package com.yatang.xc.xcr.biz.core.service;

import com.yatang.xc.xcr.biz.core.domain.BranchBankPO;
import com.yatang.xc.xcr.biz.core.domain.ResultBranchBankPO;

public interface BranchBankService {
	/**
	 * 根据银行名获取支行列表
	 */
	public ResultBranchBankPO findBranchListByBankName(BranchBankPO branchBankPO);

	/**
	 * 根据银行id获取支行列表
	 */
	public ResultBranchBankPO findBranchListByBankCode(BranchBankPO branchBankPO);
}
