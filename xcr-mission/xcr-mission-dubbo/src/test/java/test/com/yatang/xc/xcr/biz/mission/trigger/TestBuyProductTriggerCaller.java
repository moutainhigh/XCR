package test.com.yatang.xc.xcr.biz.mission.trigger;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.MissionTriggerCaller;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;

/**
 * 规则引擎验证
 * 
 * @author yangqingsong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestBuyProductTriggerCaller {
    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionExecuteService missionExecuteService;
    
    @Resource(name="buyProductTriggerCaller")
    private MissionTriggerCaller BuyProductTriggerCaller;
    
    
    @Test
    public void call(){
        MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById("3478");
        BuyProductTriggerCaller.call(execute);

    } 
    
}
