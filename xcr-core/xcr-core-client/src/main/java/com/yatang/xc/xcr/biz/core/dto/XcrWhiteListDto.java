package com.yatang.xc.xcr.biz.core.dto;

/**
 * @Author: BobLee
 * @CreateTime:  2017年12月11日 11:15:25
 */
public class XcrWhiteListDto implements java.io.Serializable {
	
	/**
     * 权限控制账户
     */
    private String userAccount;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

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
        XcrWhiteListDto other = (XcrWhiteListDto) that;
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