package com.yatang.xc.xcr.model;

import java.io.Serializable;

import com.yatang.xc.xcr.annotations.CookieValue;

/** 
* @author gaodawei 
* @Date 2017年11月30日 下午1:42:34 
* @version 1.0.0
* @function 
*/
@SuppressWarnings("serial")
public class PostKeyDto implements Serializable{

	private static final long serialVersionUID = -8171741474500076367L;
	
	@CookieValue(value = "DeviceId")
	private String deviceId;
	
	@CookieValue(value = "Type")
	private String type;
	
	@CookieValue(value = "AppVersion")
	private String appVersion;
	
	private String key;

	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "PostKeyDto [deviceId=" + deviceId + ", type=" + type + ", appVersion=" + appVersion + ", key=" + key
				+ "]";
	}
	
}
 