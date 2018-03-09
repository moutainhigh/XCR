package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;
import java.util.List;

public class ResultBranchBankDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6874722754552656687L;
	private List<BranchBankDTO> branchBanks;
	private Integer pageIndex;
	private Integer pageSize;
	private Integer totalPage;
	private Integer totalCount;

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

	public List<BranchBankDTO> getBranchBanks() {
		return branchBanks;
	}

	public void setBranchBanks(List<BranchBankDTO> branchBanks) {
		this.branchBanks = branchBanks;
	}
}
