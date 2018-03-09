package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;

public class MissionByCourseIdQuery implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = 9119816165824519668L;

    private String status;

    private String courseId;


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public String getCourseId() {
        return courseId;
    }


    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

}
