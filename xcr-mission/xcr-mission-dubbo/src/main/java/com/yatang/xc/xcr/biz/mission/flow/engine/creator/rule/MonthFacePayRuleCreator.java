package com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule;

import com.yatang.xc.xcr.biz.mission.dto.RuleCalculateDto;
import com.yatang.xc.xcr.biz.mission.dto.RuleDefinitionDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.BuildMissionRuleDto;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionRuleType;
import com.yatang.xc.xcr.biz.mission.flow.engine.creator.RuleCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * 月度当面付
 * Created by wangyang on 2017/10/30.
 */
@Service
public class MonthFacePayRuleCreator implements RuleCreator {

    private static final String RULE_TYPE = EnumMissionRuleType.TYPE_MONTH_FACE_PAY.getCode();
    protected final Log log = LogFactory.getLog(this.getClass());

    /**
     * 生成方法
     */
    @Override
    public String build(BuildMissionRuleDto ruleDto) {
        String ruleDefine = null;
        log.info("MonthFacePayRuleCreator -> RULE_TYPE:" + RULE_TYPE + "  ruleDto.getRuleType():" + ruleDto.getRuleType() + "  ruleDto.getRuleTemplate():" + ruleDto.getRuleTemplate());
        if (RULE_TYPE.equals(ruleDto.getRuleType()) && !StringUtils.isEmpty(ruleDto.getRuleTemplate())) {

            double facePayAmount = ruleDto.getDto().getFacePayAmount();
            log.info("月度当面付金额 -> 存入rule数据 -> facePayAmount:" + facePayAmount);
            ruleDefine = MessageFormat.format(ruleDto.getRuleTemplate(), String.valueOf(facePayAmount), true);

        } else {
            ruleDefine = RuleCreator.NOT_HANDLER;
        }
        return ruleDefine;
    }

    public static class RuleDefinition implements RuleDefinitionDto, RuleCalculateDto {

        private double facePayAmount = 0.00;

        private boolean isSuccess = false;


        public double getFacePayAmount() {
            return facePayAmount;
        }

        public void setFacePayAmount(double facePayAmount) {
            this.facePayAmount = facePayAmount;
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
        template.append("import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.MonthFacePayRuleCreator.RuleDefinition\r\r");
        template.append("rule \"definitionRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    eval(true);\r");
        template.append("    $gallery:RuleDefinitionGallery();\r");
        template.append("  then\r");
        template.append("    RuleDefinition ruleDefine = new RuleDefinition();\r");
        template.append("    ruleDefine.setFacePayAmount({0});\r");
        template.append("    $gallery.setRuleDefine(ruleDefine);\r");
        template.append("end\r\r");
        template.append("rule \"calculateRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    $ruleDefine:RuleDefinition(facePayAmount>={0})\r");
        template.append("  then\r");
        template.append("    $ruleDefine.setSuccess({1});\r");
        template.append("end");
        return template.toString();
    }

}
