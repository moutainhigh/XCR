package test.com.yatang.xc.xcr.biz.mission.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.busi.common.exception.BusinessException;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.manage.BuildMissionRuleDto;
import com.yatang.xc.xcr.biz.mission.flow.MissionEngineService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;

/**
 * 任务引擎
 * 
 * @author yangqingsong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionEngineService {

    @Autowired
    private MissionEngineService missionEngine;

    @Autowired
    private MissionExecuteService executeService;

    @Test
    public void testBuildMission() {

        BuildMissionRuleDto rule = new BuildMissionRuleDto();

        try {
            this.missionEngine.buildMissionRule(rule);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testBuildSingleItemMission() {
        BuildMissionRuleDto rule = new BuildMissionRuleDto();

        try {
            this.missionEngine.buildMissionRule(rule);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void calculateMissionsByMissionId() {
        MissionExecuteInfoPO execute = executeService.findExecuteMissionById("17");
        try {
            this.missionEngine.calculateMission(execute);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

}
