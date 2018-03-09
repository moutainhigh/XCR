package com.yatang.xc.xcr.pojo;

import java.util.Date;

/** 
* @author gaodawei 
* @Date 2017年7月26日 上午10:13:54 
* @version 1.0.0
* @function 
*/
public class FranchiseeInfoVo {
	
	/**
	 * id
	 */
	private String id;

	/**
	 * 分公司id
	 */
	private String branchCompanyId;

	/**
	 * 默认门店id
	 */
	private String defaultStoreId;

	/**
	 * 默认门店名字
	 */
	private String defaultStoreName;

	/**
	 * 加盟商名称
	 */
	private String name;

	/**
	 * 身份证号
	 */
	private String identityCard;

	/**
	 * 一帐通账号
	 */
	private String ytAccount;

	/**
	 * 状态状态(0:营业；1：无效; 2:冻结)
	 */
	private Integer status;

	/**
	 * 删除标记(0:未删除；3：删除)
	 */
	private Integer deleteFlag;

	/**
	 * 银行账户
	 */
	private String bankAccount;

	/**
	 * 账期
	 */
	private String accountDate;

	/**
	 * bpm业务标记
	 */
	private String bpmBusinessFlag;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 修改人
	 */
	private String modifyBy;

	/**
	 * 修改时间
	 */
	private Date modifyDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBranchCompanyId() {
		return branchCompanyId;
	}

	public void setBranchCompanyId(String branchCompanyId) {
		this.branchCompanyId = branchCompanyId;
	}

	public String getDefaultStoreId() {
		return defaultStoreId;
	}

	public void setDefaultStoreId(String defaultStoreId) {
		this.defaultStoreId = defaultStoreId;
	}

	public String getDefaultStoreName() {
		return defaultStoreName;
	}

	public void setDefaultStoreName(String defaultStoreName) {
		this.defaultStoreName = defaultStoreName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getYtAccount() {
		return ytAccount;
	}

	public void setYtAccount(String ytAccount) {
		this.ytAccount = ytAccount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

	public String getBpmBusinessFlag() {
		return bpmBusinessFlag;
	}

	public void setBpmBusinessFlag(String bpmBusinessFlag) {
		this.bpmBusinessFlag = bpmBusinessFlag;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	
	@Override
	public String toString() {
		return "FranchiseeInfo [id=" + id + ", branchCompanyId=" + branchCompanyId + ", defaultStoreId="
				+ defaultStoreId + ", defaultStoreName=" + defaultStoreName + ", name=" + name + ", identityCard="
				+ identityCard + ", ytAccount=" + ytAccount + ", status=" + status + ", deleteFlag=" + deleteFlag
				+ ", bankAccount=" + bankAccount + ", accountDate=" + accountDate + ", bpmBusinessFlag="
				+ bpmBusinessFlag + ", createBy=" + createBy + ", createDate=" + createDate + ", modifyBy=" + modifyBy
				+ ", modifyDate=" + modifyDate + "]";
	}
	
	

}
 