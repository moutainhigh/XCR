package com.yatang.xc.xcr.biz.core.domain;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月11日 11:38
 * @Summary :
 */
public class XcrWhiteListPO implements java.io.Serializable {
	
	/** 
	* @Fields serialVersionUID : TODO 变量名称 
	*/
	private static final long serialVersionUID = -1707157040130464265L;
	/**
     * 权限控制账户
     * @mbg.generated 2017-12-11 11:15:25
     */
    private String userAccount;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public XcrWhiteListPO(String userAccount) {
        this.userAccount = userAccount;
    }

    public XcrWhiteListPO() { }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userAccount=").append(userAccount);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        XcrWhiteListPO other = (XcrWhiteListPO) that;
        return (this.getUserAccount() == null ? other.getUserAccount() == null : this.getUserAccount().equals(other.getUserAccount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserAccount() == null) ? 0 : getUserAccount().hashCode());
        return result;
    }
}