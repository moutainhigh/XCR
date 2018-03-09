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
 * 日当面付
 * Created by wangyang on 2017/10/30.
 */
@Service
public class DayFacePayRuleCreator implements RuleCreator {

    protected final Log log = LogFactory.getLog(this.getClass());
    private static final String RULE_TYPE = EnumMissionRuleType.TYPE_DAY_FACE_PAY.getCode();

    /**
     * 生成方法
     */
    @Override
    public String build(BuildMissionRuleDto ruleDto) {
        String ruleDefine = null;

        log.info("DayFacePayRuleCreator -> RULE_TYPE:" + RULE_TYPE + "  ruleDto.getRuleType():" + ruleDto.getRuleType() + "  ruleDto.getRuleTemplate():" + ruleDto.getRuleTemplate());
        if (RULE_TYPE.equals(ruleDto.getRuleType()) && !StringUtils.isEmpty(ruleDto.getRuleTemplate())) {

            int facePayNumber = ruleDto.getDto().getFacePayNumber();
            log.info("日当面付笔数 -> 存入rule数据 -> facePayNumber:" + facePayNumber);
            ruleDefine = MessageFormat.format(ruleDto.getRuleTemplate(), String.valueOf(facePayNumber), true);

        } else {
            ruleDefine = RuleCreator.NOT_HANDLER;
        }
        return ruleDefine;
    }

    public static class RuleDefinition implements RuleDefinitionDto, RuleCalculateDto {

        private int facePayNumber = 0;

        private boolean isSuccess = false;

        public int getFacePayNumber() {
            return facePayNumber;
        }

        public void setFacePayNumber(int facePayNumber) {
            this.facePayNumber = facePayNumber;
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
        template.append("import com.yatang.xc.xcr.biz.mission.flow.engine.creator.rule.DayFacePayRuleCreator.RuleDefinition\r\r");
        template.append("rule \"definitionRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    eval(true);\r");
        template.append("    $gallery:RuleDefinitionGallery();\r");
        template.append("  then\r");
        template.append("    RuleDefinition ruleDefine = new RuleDefinition();\r");
        template.append("    ruleDefine.setFacePayNumber({0});\r");
        template.append("    $gallery.setRuleDefine(ruleDefine);\r");
        template.append("end\r\r");
        template.append("rule \"calculateRule\"\r");
        template.append("  no-loop true\r");
        template.append("  lock-on-active true\r");
        template.append("  salience 99\r");
        template.append("  when\r");
        template.append("    $ruleDefine:RuleDefinition(facePayNumber>={0})\r");
        template.append("  then\r");
        template.append("    $ruleDefine.setSuccess({1});\r");
        template.append("end");
        return template.toString();
    }

}
