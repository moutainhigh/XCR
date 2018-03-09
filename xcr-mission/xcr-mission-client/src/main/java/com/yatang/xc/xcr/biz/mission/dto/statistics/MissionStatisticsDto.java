package com.yatang.xc.xcr.biz.mission.dto.statistics;

import java.io.Serializable;
import java.util.Date;

public class MissionStatisticsDto implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO 变量名称
	 */
	private static final long	serialVersionUID	= -670998208721575284L;
	/**
	 * 任务id
	 */
	private String				missionId;
	/**
	 * 任务名称
	 */
	private String				missionName;
	/**
	 * 开始时间
	 */
	private Date				startDate;
	/**
	 * 结束时间
	 */
	private Date				endDate;
	/**
	 * 达标数
	 */
	private Long				finishMissionCount;
	/**
	 * 达标数增加率
	 */
	private Float				finishMissionCountIncrease;
	/**
	 * 领取奖励数
	 */
	private Long				awardCount;
	/**
	 * 领取奖励增长率
	 */
	private Float				awardCountIncrease;
	/**
	 * 完成任务百分比
	 */
	private Float				finishMissionPercent;
	/**
	 * 完成任务百分比增加率
	 */
	private Float				finishMissionPercentIncrease;



	public String getMissionId() {
		return missionId;
	}



	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}



	public String getMissionName() {
		return missionName;
	}



	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}



	public Date getStartDate() {
		return startDate;
	}



	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public Date getEndDate() {
		return endDate;
	}



	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public Long getFinishMissionCount() {
		return finishMissionCount;
	}



	public void setFinishMissionCount(Long finishMissionCount) {
		this.finishMissionCount = finishMissionCount;
	}



	public Float getFinishMissionCountIncrease() {
		return finishMissionCountIncrease;
	}



	public void setFinishMissionCountIncrease(Float finishMissionCountIncrease) {
		this.finishMissionCountIncrease = finishMissionCountIncrease;
	}



	public Long getAwardCount() {
		return awardCount;
	}



	public void setAwardCount(Long awardCount) {
		this.awardCount = awardCount;
	}



	public Float getAwardCountIncrease() {
		return awardCountIncrease;
	}



	public void setAwardCountIncrease(Float awardCountIncrease) {
		this.awardCountIncrease = awardCountIncrease;
	}



	public Float getFinishMissionPercent() {
		return finishMissionPercent;
	}



	public void setFinishMissionPercent(Float finishMissionPercent) {
		this.finishMissionPercent = finishMissionPercent;
	}



	public Float getFinishMissionPercentIncrease() {
		return finishMissionPercentIncrease;
	}



	public void setFinishMissionPercentIncrease(Float finishMissionPercentIncrease) {
		this.finishMissionPercentIncrease = finishMissionPercentIncrease;
	}

}
