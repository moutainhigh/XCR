package com.yatang.xc.xcr.biz.core.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.core.domain.BranchBankPO;

public interface BranchBankDao {
	/**
	 * 根据银行名获取支行列表
	 */
	List<BranchBankPO> selectBranchListByBankName(BranchBankPO branchBankPO);

	/**
	 * 根据银行id获取支行列表
	 */
	List<BranchBankPO> selectBranchListByBankCode(BranchBankPO branchBankPO);

	/**
	 * 查询数据和
	 * @param branchBankPO
	 * @return
	 */
	Integer selectCountByBankCode(BranchBankPO branchBankPO);

	/**
	 * 查询数据和
	 * @param branchBankPO
	 * @return
	 */
	Integer selectCountByBankName(BranchBankPO branchBankPO);

}
