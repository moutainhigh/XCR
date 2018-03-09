package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;

public class BuildMissionRuleDto implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO 变量名称
	 */
	private static final long serialVersionUID = -8328089954235557374L;

	@Override
	public String toString() {
		return "BuildMissionRuleDto [ruleType=" + ruleType + ", ruleTemplate=" + ruleTemplate + "]";
	}

	/**
	 * 规则类型
	 */
	private String ruleType;

	/**
	 * 规则模板
	 */
	private String ruleTemplate;

	private CreateMissionInfoDto dto;

	public CreateMissionInfoDto getDto() {
		return dto;
	}

	public void setDto(CreateMissionInfoDto dto) {
		this.dto = dto;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleTemplate() {
		return ruleTemplate;
	}

	public void setRuleTemplate(String ruleTemplate) {
		this.ruleTemplate = ruleTemplate;
	}

}
