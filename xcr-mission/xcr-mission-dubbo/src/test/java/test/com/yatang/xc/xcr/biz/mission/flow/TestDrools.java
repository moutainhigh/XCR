package test.com.yatang.xc.xcr.biz.mission.flow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.busi.common.exception.BusinessException;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;
import com.yatang.xc.xcr.biz.mission.dto.creator.SingleItemRuleDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.BuildMissionRuleDto;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionRuleType;
import com.yatang.xc.xcr.biz.mission.flow.MissionEngineService;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.RuleCalculate;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SimpleRuleCreator;

/**
 * 规则引擎验证
 * 
 * @author yangqingsong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestDrools {
    protected final Log log = LogFactory.getLog(this.getClass());
    
	@Autowired
	private MissionEngineService creator;
	
	@Autowired
	private RuleCalculate ruleCalculate;

	@Test
	public void testSimpleRule() throws BusinessException {

//		SimpleRuleDto dto = new SimpleRuleDto();
//		dto.setAwardNum(10d);
//		dto.setStatus(1);
		
		BuildMissionRuleDto rule = new BuildMissionRuleDto();
		rule.setRuleType(EnumMissionRuleType.TYPE_SIMPLE.getCode());
		String template = initTemplate();
		System.out.println("template:"+template);
		rule.setRuleTemplate(template);
		// 生成规则
		String ruleStr = this.creator.buildMissionRule(rule);
		
		// 根据定义文件,反算获取定义对象
		RuleDefinitionDto defineDto = this.ruleCalculate.calcDefinition(ruleStr);
		System.out.println("defineDto:"+JSON.toJSON(defineDto));

		// 根据计算对象,计算是否满足条件
		SimpleRuleCreator.RuleDefinition ruleDefinition = new SimpleRuleCreator.RuleDefinition();
		this.ruleCalculate.calculate(ruleStr, ruleDefinition);
		System.out.println("计算之后:" + ruleDefinition.hasSuccess()+":"+JSON.toJSON(ruleDefinition));
	}
	
	@Test
	public void testSingleItemRule() throws BusinessException {

//		SingleItemRuleDto dto = new SingleItemRuleDto();
//		dto.setType("product");
//		dto.setIdentity(Arrays.asList("10001","10002","20011"));
//		dto.setAwardNum(10d);
//		dto.setStatus(1);
		BuildMissionRuleDto rule = new BuildMissionRuleDto();
		rule.setRuleTemplate(initTemplate());
        String template = initTemplate();
        System.out.println("template:" + template);
        rule.setRuleTemplate(template);
		// 生成规则
		String ruleStr = this.creator.buildMissionRule(rule);
		// 根据定义文件,反算获取定义对象
		RuleDefinitionDto defineDto = this.ruleCalculate.calcDefinition(ruleStr);
		System.out.println(defineDto);
		System.out.println(JSON.toJSON(defineDto));

		// 根据计算对象,计算是否满足条件
		SingleItemRuleDto calcDto = (SingleItemRuleDto) defineDto;
		calcDto.setAwardNum(null);
		calcDto.setStatus(0);
		this.ruleCalculate.calculate(ruleStr, calcDto);
		System.out.println("计算之后:" + JSON.toJSON(defineDto));
	}
	

    protected String initTemplate() {
        StringBuffer template = new StringBuffer("package com.yatang.xc.mission.rule.simple\r");
        template.append("import com.yatang.xc.xcr.biz.mission.flow.engine.bo.RuleDefinitionGallery\r");
        template.append("import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SimpleRuleCreator.RuleDefinition\r\r");
        template.append("rule \"definitionRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    eval(true);\r");
        template.append("    $gallery:RuleDefinitionGallery();\r");
        template.append("  then\r");
        template.append("    RuleDefinition ruleDefine = new RuleDefinition();\r");
        template.append("    ruleDefine.setMissionFinished({0});\r");
        template.append("    $gallery.setRuleDefine(ruleDefine);\r");
        template.append("end\r\r");
        template.append("rule \"calculateRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    $ruleDefine:RuleDefinition(missionFinished=={0})\r");
        template.append("  then\r");
        template.append("    $ruleDefine.setSuccess({1});\r");
        template.append("end");
        return template.toString();
    }
}
