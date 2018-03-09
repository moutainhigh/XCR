package test.com.yatang.xc.xcr.biz.mission.flow;

import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.schedule.thread.ICreateMissionExecuteThread;
import com.yatang.xc.xcr.biz.mission.service.MissionAwardService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 规则引擎验证
 * 
 * @author yangqingsong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionExecuteFlowService {
    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionExecuteFlowService missionExecuteFlowService;
    
    @Autowired
    private MissionAwardService awardService;
    
    @Test
    public void testBackMissionExecuteToHistory(){
        Long executeId = 14l;
        List<MissionExecuteAwardPO> awards = awardService.queryExecuteAwardByExecuteId(executeId);
        missionExecuteFlowService.backMissionExecuteToHistory(executeId, awards);
        
    }
    
    @Test
    public void initCreateMissionExecuteThread(){
        ICreateMissionExecuteThread result =  missionExecuteFlowService.initCreateMissionExecuteThread("m001");
        log.info("result:" + result);
    }
    
    @Test
    public void getOrganizationService(){
        OrganizationService result =  missionExecuteFlowService.getOrganizationService();
        log.info("result:" + result);
    }
    
    @Test
    public void startBpmProcess(){

        missionExecuteFlowService.startBpmProcess(2l,"T004","companyId","","");
        log.info("result:" + "");
    } 
    
    @Test
    public void redisSaveItem() throws InterruptedException{
        missionExecuteFlowService.getRedisService().saveItem("test_echo", 60, "true");
        while(true){
            String value = missionExecuteFlowService.getRedisService().getItem("test_echo");
            log.info("value:" + value);
            Thread.sleep(10);
        }
    }   
    
    @Test
    public void checkOrderCountFromDataCenter(){

        missionExecuteFlowService.checkOrderCountFromDataCenter("A011113");
        log.info("result:" + "");
    } 
    
}
