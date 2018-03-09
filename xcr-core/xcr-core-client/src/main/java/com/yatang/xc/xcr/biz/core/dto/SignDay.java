package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到天数信息
 * Created by wangyang on 2017/12/18.
 */
public class SignDay implements Serializable {

    private static final long serialVersionUID = 3328402073430326875L;
    private Date lastSignDay;
    private Integer signDays;

    public SignDay(Date lastSignDay, Integer signDays) {
        this.lastSignDay = lastSignDay;
        this.signDays = signDays;
    }

    public Date getLastSignDay() {
        return lastSignDay;
    }

    public void setLastSignDay(Date lastSignDay) {
        this.lastSignDay = lastSignDay;
    }

    public Integer getSignDays() {
        return signDays;
    }

    public void setSignDays(Integer signDays) {
        this.signDays = signDays;
    }
}
