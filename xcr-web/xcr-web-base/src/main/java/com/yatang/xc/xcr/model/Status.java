package com.yatang.xc.xcr.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月23日 下午3:07:53
 * @Summary :
 */
@SuppressWarnings("serial")
@JsonSerialize
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)  
public class Status implements Serializable {

	private static final long serialVersionUID = -3320775636939853384L;
	
	/** 状态 */
	@JSONField(name="State")
	@JsonProperty("State")
	private String state;
	
	/** 状态ID */
	@JSONField(name="StateID")
	@JsonProperty("StateID")
	private String stateID;
	
	/** 状态描述 */
	@JSONField(name="StateDesc")
	@JsonProperty("StateDesc")
	private String stateDesc;
	
	/** 状态值 */
	@JSONField(name="StateValue")
	@JsonProperty("StateValue")
	private String stateValue;

	public Status setState(String state) {
		this.state = state;
		return this;
	}

	public Status setStateID(String stateID) {
		this.stateID = stateID;
		return this;
	}

	public Status setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
		return this;
	}

	public Status setStateValue(String stateValue) {
		this.stateValue = stateValue;
		return this;
	}

	public String getState() {
		return state;
	}

	public String getStateID() {
		return this.stateID;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public String getStateValue() {
		return stateValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 *  切勿修改ToString方法 
	 */
	@Override
	public String toString() {
		return "{\"State\":\"" + state + "\",\"StateID\":\"" + stateID + "\",\"StateDesc\":\"" + stateDesc+ "\",\"StateValue\":\"" + stateValue + "\"}  ";
	}

}
