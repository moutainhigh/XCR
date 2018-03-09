package com.yatang.xc.xcr.biz.mission.flow.engine.calc;

import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.base.RuleNameEqualsAgendaFilter;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.busi.common.exception.BusinessException;
import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;
import com.yatang.xc.xcr.biz.mission.flow.engine.bo.RuleDefinitionGallery;

/**
 * 基于drools规则引擎计算
 * 
 * @author yangqingsong
 *
 */
@Service
public class DroolsRuleCalculate implements RuleCalculate {

	protected final Log		log					= LogFactory.getLog(this.getClass());
	// drools处理对象
	private static String	definitionRuleName	= "definitionRule";
	private static String	calculateRuleName	= "calculateRule";



	public DroolsRuleCalculate() {
		// 设置时间格式
		System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");

	}



	/**
	 * 获取任务条件信息
	 */
	@Override
	public RuleDefinitionDto calcDefinition(String rule) {
		StringReader reader = null;
		RuleDefinitionDto defineDto = null;
		log.info("start Definition rule:");
		try {
			RuleBase ruleBase = RuleBaseFactory.newRuleBase();
			// 装载规则
			reader = new StringReader(rule);
			PackageBuilder backageBuilder = new PackageBuilder();
			backageBuilder.addPackageFromDrl(reader);
			Package pack = backageBuilder.getPackage();
			ruleBase.addPackage(pack);

			// 执行计算
			RuleDefinitionGallery gallery = new RuleDefinitionGallery();
			StatefulSession statefulSession = ruleBase.newStatefulSession();
			statefulSession.insert(gallery);
			statefulSession.fireAllRules(new RuleNameEqualsAgendaFilter(definitionRuleName));
			defineDto = gallery.getRuleDefine();
			statefulSession.dispose();

			// 清理装载的规则
			ruleBase.removePackage(pack.getName());
		} catch (Exception e) {
			throw new BusinessException("BIZERROR00001", "规则引擎计算报错", e);
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
		return defineDto;
	}



	/**
	 * 执行计算操作
	 */
	@Override
	public RuleCalculateDto calculate(String rule, RuleCalculateDto calParam) throws BusinessException {
		log.info("start addPackage:");
		long time = System.currentTimeMillis();
		StringReader reader = null;
		try {
			RuleBase ruleBase = RuleBaseFactory.newRuleBase();
			// 装载规则
			reader = new StringReader(rule);
			PackageBuilder backageBuilder = new PackageBuilder();
			backageBuilder.addPackageFromDrl(reader);
			Package pack = backageBuilder.getPackage();

			ruleBase.addPackage(pack);

			// 执行计算
			StatefulSession statefulSession = ruleBase.newStatefulSession();
			statefulSession.insert(calParam);
			log.debug("start fire all rules:" + rule);
			statefulSession.fireAllRules(new RuleNameEqualsAgendaFilter(calculateRuleName));
			statefulSession.dispose();
			// 清理装载的规则
			ruleBase.removePackage(pack.getName());
		} catch (Exception e) {
			throw new BusinessException("BIZERROR00001", "规则引擎计算报错", e);
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
		log.info("end fire rules! cost:" + (System.currentTimeMillis() - time) + " restul :"
				+ JSON.toJSONString(calParam));
		return calParam;
	}
}
