package com.yatang.xc.xcr.biz.mission.bo;

import java.io.Serializable;

public class ExecuteByMerchantIdAndCourseIdQuery implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO 变量名称
	 */
	private static final long serialVersionUID = 6242019146310031696L;



	@Override
	public String toString() {
		return "ExecuteByMerchantIdAndCourseIdQuery [merchantId=" + merchantId + ", courseId=" + courseId + "]";
	}

	private String	merchantId;

	private String	courseId;

	private String	status;



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getMerchantId() {
		return merchantId;
	}



	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}



	public String getCourseId() {
		return courseId;
	}



	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

}
