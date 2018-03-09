package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;
import java.util.Date;

public class ExpireQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -1905957686351053432L;

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */

    private String status;

    private String reason;

    private Date now;


    public Date getNow() {
        return now;
    }


    public void setNow(Date now) {
        this.now = now;
    }


    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

}
