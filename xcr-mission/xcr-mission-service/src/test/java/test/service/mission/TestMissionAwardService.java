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
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;
import com.yatang.xc.xcr.biz.mission.service.MissionAwardService;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionAwardService {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionAwardService missionAwardService;
    @Autowired
    private MissionAwardService awardService;
    
    @Test
    public void testBackMissionAwardToHistory(){
        Long executeId = 9l;
        List<MissionExecuteAwardPO> awards = awardService.queryExecuteAwardByExecuteId(executeId);
        missionAwardService.backMissionAwardToHistory(awards, executeId);
    }
    
    @Test
    public void queryAwardByMissionId(){
        List<MissionAwardPO> ok = awardService.queryAwardByMissionId(7L);
        log.info("ok:"+JSON.toJSONString(ok));
    }      
    
    @Test
    public void queryExecuteAwardByExecuteId(){
        List<MissionExecuteAwardPO> ok = awardService.queryExecuteAwardByExecuteId(11L);
        log.info("ok:"+JSON.toJSONString(ok));
    }
    
    @Test
    public void createExecuteAward(){
        MissionExecuteAwardPO eAward = new MissionExecuteAwardPO();
        eAward.setAwardType("type");
        eAward.setGrantNum(9.88);
        eAward.setGrantStyle("style");
        eAward.setGrantTime(new Date());
        eAward.setGrantUser("echo");
        eAward.setMissonExecuteIdInfoId(1l);
        boolean ok = awardService.createExecuteAward(eAward);
        log.info("ok:"+JSON.toJSONString(ok));
    }    
    
    @Test
    public void deleteExecuteAwardById(){
        boolean ok = awardService.deleteExecuteAwardById(3l);
        log.info("ok:"+JSON.toJSONString(ok));
    }
    
    @Test
    public void deleteExecuteAwardByExecuteId(){
        boolean ok = awardService.deleteExecuteAwardByExecuteId(11l);
        log.info("ok:"+JSON.toJSONString(ok));
    }
}
