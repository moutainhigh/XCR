package com.yatang.xc.xcr.biz.core.domain;

import java.util.List;

public class ResultBranchBankPO {
	private List<BranchBankPO> branchBanks;
	private Integer pageIndex;
	private Integer pageSize;
	private Integer totalPage;
	private Integer totalCount;

	public List<BranchBankPO> getBranchBanks() {
		return branchBanks;
	}

	public void setBranchBanks(List<BranchBankPO> branchBanks) {
		this.branchBanks = branchBanks;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
}
