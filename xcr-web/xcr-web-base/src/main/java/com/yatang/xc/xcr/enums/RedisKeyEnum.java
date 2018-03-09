package com.yatang.xc.xcr.enums;

import java.io.Serializable;

/**
 * 
 * 主要功能：给redis提供你key
 * @author liuli
 *
 */
public enum RedisKeyEnum implements Serializable {
	Key("Key","各种密钥"),;
	private String identifier;
	private String desc;
	
	private RedisKeyEnum(String identifier,String desc){
		this.identifier = identifier;
		this.desc = desc;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 
	 * 功能说明：通过标识符获取RedisKeyEnum对象
	 * @author liuli
	 * @createTime  2017年3月8日下午3:37:32
	 * @param identifier 标识符
	 * @return RedisKeyEnum
	 */
	public static RedisKeyEnum getRedisKeyEnumByIdentifier(String identifier){
		RedisKeyEnum[] redisKeyEnums = RedisKeyEnum.values();
		for (RedisKeyEnum redisKeyEnum : redisKeyEnums) {
			if(redisKeyEnum.identifier.equals(identifier)){
				return redisKeyEnum;
			}
		}
		return null;
	}
	
}
