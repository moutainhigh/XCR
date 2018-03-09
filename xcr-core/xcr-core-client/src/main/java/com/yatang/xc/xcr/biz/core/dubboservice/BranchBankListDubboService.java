package com.yatang.xc.xcr.biz.core.dubboservice;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.BranchBankDTO;
import com.yatang.xc.xcr.biz.core.dto.ResultBranchBankDTO;

public interface BranchBankListDubboService {
	/**
	 * 根据银行名获取支行列表
	 */
	public Response<ResultBranchBankDTO> findBranchListByBankName(BranchBankDTO branchBankPO);

	/**
	 * 根据银行id获取支行列表
	 */
	public Response<ResultBranchBankDTO> findBranchListByBankCode(BranchBankDTO branchBankPO);
}
