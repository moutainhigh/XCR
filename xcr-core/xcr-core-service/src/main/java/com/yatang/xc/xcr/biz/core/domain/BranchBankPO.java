package com.yatang.xc.xcr.biz.core.domain;

public class BranchBankPO {
	private Integer bid;
	private String bankno;
	private String branchbankname;
	private String bankname;
	private String bankid;
	private String province;
	private String provinceid;
	private String city;
	private String cityid;
	private Integer pageIndex;
	private Integer pageSize;
	private Integer totalCount;

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankid() {
		return bankid;
	}

	public void setBankid(String bankid) {
		this.bankid = bankid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getBranchbankname() {
		return branchbankname;
	}

	public void setBranchbankname(String branchbankname) {
		this.branchbankname = branchbankname;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
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

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

}
