package test.service.mission;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.biz.mission.bo.ExecutableMissionQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

/**
 * 
 * Dao 的 junit test 类
 * 
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50
 * @version : 2017年3月20日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionExecuteService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionExecuteService missionExecuteService;

    @Autowired
    private MissionService missionService;


    @Test
    public void testCreateMissionEexecute() {
        MissionInfoPO missionInof = missionService.findById(15l);
        MissionTemplatePO template = missionService.selectTemplateByCode("T001");
        MissionExecuteInfoPO ok = missionExecuteService.createMissionEexecute("m002", missionInof, template, "INIT");
        log.info("ok:" + JSON.toJSONString(ok));
    }


    @Test
    public void testDeleteExecuteById() {
        boolean ok = missionExecuteService.deleteExecuteById(2L);
        log.info("ok:" + JSON.toJSONString(ok));
    }


    @Test
    public void testUpdateMissionExecuteStatus() {
        UpdateStatusQuery query = new UpdateStatusQuery();
        query.setId(7335l);
        query.setLastModifyTime(new Date());
        query.setOldStatus(new String[] {"INIT"});
        query.setReason("TEST_BY_ECHO");
        query.setStatus("END");

        boolean ok = missionExecuteService.updateMissionExecuteStatus(query);
        log.info("ok:" + JSON.toJSONString(ok));
    }


    @Test
    public void testFindExecutableMissions() {
        List<MissionExecuteInfoPO> ok = missionExecuteService.findExecutableMissions("M002");
        log.info("ok:" + JSON.toJSONString(ok));
    }


    @Test
    public void tsetFindExecuteMissionByMerchantIdAndCourseId() {
        MissionExecuteInfoPO ok = missionExecuteService.findExecuteMissionByMerchantIdAndCourseId("M002", "C001",null);
        log.info("ok:" + JSON.toJSONString(ok));
    }


    @Test
    public void queryExecuteMissionByMissionIdAndMerchantId() {
        List<MissionExecuteInfoPO> ok = missionExecuteService.queryExecuteMissionByMissionIdAndMerchantId(15L, "M002");
        log.info("ok:" + JSON.toJSONString(ok));
    }


    @Test
    public void queryExecuteMissionByMissionIdAndMerchantIdInHistory() {
        List<MissionExecuteInfoPO> ok = missionExecuteService.queryExecuteMissionByMissionIdAndMerchantIdInHistory(15L, "M002");
        log.info("ok size:" + JSON.toJSONString(ok.size()));
        log.info("ok:" + JSON.toJSONString(ok));
    }


    @Test
    public void queryMissionExecute() {
        MissionExecuteQuery query = new MissionExecuteQuery();
        query.setMerchantId("m103");
        query.setStatus("INIT");
        query.setType("RECOMMEND");
        query.setOrderBy("sort");
        int ok = missionExecuteService.queryMissionExecuteCount(query);
        log.info("ok:" + JSON.toJSONString(ok));
        query.setStartIndex(0);
        query.setEndIndex(4);
        List<MissionExecuteInfoPO> mission = missionExecuteService.queryMissionExecute(query);
        log.info("mission size:" + JSON.toJSONString(mission.size()));
        log.info("mission:" + JSON.toJSONString(mission));
    }
    
    @Test
    public void queryMissionExecuteOrderByRelated() {
        MissionExecuteQuery query = new MissionExecuteQuery();
        query.setType("RECOMMEND");
        int ok = missionExecuteService.queryMissionExecuteCount(query);
        log.info("ok:" + JSON.toJSONString(ok));
        query.setStartIndex(0);
        query.setEndIndex(4);
        List<MissionExecuteInfoPO> mission = missionExecuteService.queryMissionExecuteOrderByRelated(query);
        log.info("mission size:" + JSON.toJSONString(mission.size()));
        log.info("mission:" + JSON.toJSONString(mission));
    }


    
    
    @Test
    public void testBackMissionExecuteToHistory() {

        boolean ok = missionExecuteService.backMissionExecuteToHistory(1l);
        log.info("ok:" + JSON.toJSONString(ok));
    }
   
    @Test
    public void expireMissionExecutes() {

        boolean ok = missionExecuteService.expireMissionExecutes("INVALID");
        log.info("ok:" + JSON.toJSONString(ok));
    }
    @Test
    public void removeExecuteMissionHasDelted() {

        int ok = missionExecuteService.invalidExecuteMissionHasDelted("INVALID");
        log.info("ok:" + JSON.toJSONString(ok));
    }
    
    
}
