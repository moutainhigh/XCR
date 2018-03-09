package com.yatang.xc.xcr.vo;

public class LaunchPageVO {
	private Integer id;

	private String imagePreview;

	private String launchAURL;

	private Integer currentState;//1启用，0禁用

	private String lastModifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImagePreview() {
		return imagePreview;
	}

	public void setImagePreview(String imagePreview) {
		this.imagePreview = imagePreview;
	}

	public Integer getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getLaunchAURL() {
		return launchAURL;
	}

	public void setLaunchAURL(String launchAURL) {
		this.launchAURL = launchAURL;
	}

}
