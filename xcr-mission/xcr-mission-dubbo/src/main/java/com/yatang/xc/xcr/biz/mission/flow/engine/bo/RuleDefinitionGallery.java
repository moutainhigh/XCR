package com.yatang.xc.xcr.biz.mission.flow.engine.bo;

import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;

/**
 * 角色定义对象通道类<br>
 * 用于在分析rule，获取对应的rule定义信息
 * 
 * @author yangqingsong
 *
 */
public class RuleDefinitionGallery {

	private RuleDefinitionDto ruleDefine;

	public RuleDefinitionDto getRuleDefine() {
		return ruleDefine;
	}

	public void setRuleDefine(RuleDefinitionDto ruleDefine) {
		this.ruleDefine = ruleDefine;
	}

}
