package com.yatang.xc.xcr.biz.mission.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到PO
 * Created by wangyang on 2017/7/7.
 */
public class UserSignPO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6317818105630432395L;
	private Long id;
    private String userId;    //用户登录名
    private String shopCode;    //门店编号
    private String score;       //奖励积分
    private String cash;        //奖励现金
    private int continueDay;    //连续签到天数
    private Date createTime;    //创建时间

    public UserSignPO() {
    }

    
    
    public UserSignPO(int continueDay) {
		super();
		this.continueDay = continueDay;
	}



	public UserSignPO(String userId, String shopCode, String score, String cash) {
        this.userId = userId;
        this.shopCode = shopCode;
        this.score = score;
        this.cash = cash;
    }

    public UserSignPO(Long id, String userId, String shopCode, String score, String cash, int continueDay, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.shopCode = shopCode;
        this.score = score;
        this.cash = cash;
        this.continueDay = continueDay;
        this.createTime = createTime;
    }

    public int getContinueDay() {
        return continueDay;
    }

    public void setContinueDay(int continueDay) {
        this.continueDay = continueDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserSignPO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", shopCode='" + shopCode + '\'' +
                ", score='" + score + '\'' +
                ", cash='" + cash + '\'' +
                ", continueDay=" + continueDay +
                ", createTime=" + createTime +
                '}';
    }
}
