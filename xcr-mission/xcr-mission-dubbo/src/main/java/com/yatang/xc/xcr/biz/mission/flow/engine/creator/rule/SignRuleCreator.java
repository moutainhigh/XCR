package com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.BuildMissionRuleDto;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionRuleType;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.RuleCreator;

/**
 * 生成简单的规则
 * 
 * @author yangqingsong
 *
 */
@Service
public class SignRuleCreator implements RuleCreator {

    
    private static final String RULE_TYPE= EnumMissionRuleType.TYPE_SIGN.getCode();
    protected final Log log = LogFactory.getLog(this.getClass());
    
    /**
     * 生成方法
     */
    @Override
    public String build(BuildMissionRuleDto ruleDto) {
        String ruleDefine = null;
        log.info("SignRuleCreator -> RULE_TYPE:" + RULE_TYPE + "  ruleDto.getRuleType():" + ruleDto.getRuleType() + "  ruleDto.getRuleTemplate():" + ruleDto.getRuleTemplate());
        if (RULE_TYPE.equals(ruleDto.getRuleType()) && !StringUtils.isEmpty(ruleDto.getRuleTemplate())) {

        	int continueDate =  ruleDto.getDto().getContinueDate();
            ruleDefine = MessageFormat.format(ruleDto.getRuleTemplate(), continueDate, true);

        } else {
            ruleDefine = RuleCreator.NOT_HANDLER;
        }
        return ruleDefine;
    }

    public static class RuleDefinition implements RuleDefinitionDto, RuleCalculateDto {

        private int continueDate = 0;

        private boolean isSuccess = false;



        public int getContinueDate() {
			return continueDate;
		}


		public void setContinueDate(int continueDate) {
			this.continueDate = continueDate;
		}


		public void setSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }


        @Override
        public boolean hasSuccess() {
            return isSuccess;
        }
    }


    /**
     * 初始化模版
     * 
     * @return
     */

    protected String initTemplate() {
        StringBuffer template = new StringBuffer("package com.yatang.xc.mission.rule.simple\r");
        template.append("import com.yatang.xc.xcr.biz.mission.flow.engine.bo.RuleDefinitionGallery\r");
        template.append("import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.SignRuleCreator.RuleDefinition\r\r");
        template.append("rule \"definitionRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    eval(true);\r");
        template.append("    $gallery:RuleDefinitionGallery();\r");
        template.append("  then\r");
        template.append("    RuleDefinition ruleDefine = new RuleDefinition();\r");
        template.append("    ruleDefine.setContinueDate({0});\r");
        template.append("    $gallery.setRuleDefine(ruleDefine);\r");
        template.append("end\r\r");
        template.append("rule \"calculateRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    $ruleDefine:RuleDefinition(continueDate>={0})\r");
        template.append("  then\r");
        template.append("    $ruleDefine.setSuccess({1});\r");
        template.append("end");
        return template.toString();
    }

}
