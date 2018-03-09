package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class UpdateStatusQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 2377271142929089217L;


    @Override
    public String toString() {
        return "UpdateStatusQuery [id=" + id + ", status=" + status + ", oldStatus=" + Arrays.toString(oldStatus) + ", reason=" + reason + ", lastModifyTime="
                + lastModifyTime + "]";
    }

    private Long id=0l;

    private String status;

    private String[] oldStatus;

    private String reason;

    private Date lastModifyTime;


    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }


    public Date getLastModifyTime() {
        return lastModifyTime;
    }


    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String[] getOldStatus() {
        return oldStatus;
    }


    public void setOldStatus(String[] oldStatus) {
        this.oldStatus = oldStatus;
    }

}
