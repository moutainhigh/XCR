package test.service.mission;

import java.util.ArrayList;
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
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedDetailPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedPO;
import com.yatang.xc.xcr.biz.mission.service.MissionAwardService;
import com.yatang.xc.xcr.biz.mission.service.MissionRelatedService;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionRelatedService {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionRelatedService missionRelatedService;
    
    @Autowired
    private MissionAwardService awardService;
 
    @Test
    public void createRelatedMission(){
        MissionRelatedPO related =  new MissionRelatedPO();
        related.setCreateTime(new Date());
        related.setLastModifyTime(new Date());
        related.setMissonRelatedDescription("miaoshu");
        related.setMissonRelatedName("mingcheng");
        related.setReason("yuanyin");
        related.setStatus("zhuangtai");
        related.setType("leixing");
        List<MissionRelatedDetailPO> details = new ArrayList<MissionRelatedDetailPO>();
        MissionRelatedDetailPO deatil1 = new MissionRelatedDetailPO();
        deatil1.setLevel(1);
        deatil1.setMissonInfoId(1l);
        details.add(deatil1);
        MissionRelatedDetailPO deatil2 = new MissionRelatedDetailPO();
        deatil2.setLevel(2);
        deatil2.setMissonInfoId(2l);
        details.add(deatil2);       
        boolean ok = missionRelatedService.createRelatedMission(related, details);
        log.info("ok:" + JSON.toJSONString(ok));
    }
    
    @Test
    public void removeMissionRelatedById() {
        boolean ok = missionRelatedService.removeMissionRelatedById(4L);
        log.info("ok:" + JSON.toJSONString(ok));
    }
    
    @Test
    public void updateRelatedMission() {
        MissionRelatedPO related =  new MissionRelatedPO();
        related.setId(3l);
        related.setCreateTime(new Date());
        related.setLastModifyTime(new Date());
        related.setMissonRelatedDescription("miaoshu");
        related.setMissonRelatedName("mingcheng");
        related.setReason("yuanyin");
        related.setStatus("zhuangtai");
        related.setType("leixing");
        List<MissionRelatedDetailPO> details = new ArrayList<MissionRelatedDetailPO>();
        MissionRelatedDetailPO deatil1 = new MissionRelatedDetailPO();
        deatil1.setLevel(1);
        deatil1.setMissonInfoId(1l);
        details.add(deatil1);
        MissionRelatedDetailPO deatil2 = new MissionRelatedDetailPO();
        deatil2.setLevel(2);
        deatil2.setMissonInfoId(2l);
        details.add(deatil2);  
        boolean ok = missionRelatedService.updateRelatedMission(related, details);
        log.info("ok:" + JSON.toJSONString(ok));
    }
    
    @Test
    public void updateRelatedStatus() {
        UpdateStatusQuery query = new UpdateStatusQuery();
        query.setId(5l);
        query.setLastModifyTime(new Date());
        query.setStatus("INIT");
        query.setOldStatus(new String[]{"INIT"});
        query.setReason("new reasonX");
        boolean ok = missionRelatedService.updateRelatedStatus(query);
        log.info("ok:" + JSON.toJSONString(ok));
    }
    
    @Test
    public void findById() {
        MissionRelatedPO ok = missionRelatedService.findById(5L);
        log.info("ok:" + JSON.toJSONString(ok));
    }
    
    @Test
    public void findRelatedDetailsByMissionId() {
        List<MissionRelatedDetailPO> ok = missionRelatedService.findRelatedDetailsByMissionId(17L);
        log.info("ok:" + JSON.toJSONString(ok));
    }
    @Test
    public void findRelatedDetailsByRelatedId() {
        List<MissionRelatedDetailPO> ok = missionRelatedService.findRelatedDetailsByRelatedId(5L);
        log.info("ok:" + JSON.toJSONString(ok));
    }
    

}
