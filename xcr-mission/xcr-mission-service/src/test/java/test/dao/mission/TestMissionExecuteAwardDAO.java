package test.dao.mission;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteAwardDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionExecuteAwardDAO {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionExecuteAwardDAO dao ;

    

    
    private MissionExecuteAwardPO initPo(){
        MissionExecuteAwardPO award = new MissionExecuteAwardPO();
        Timestamp newDate = new Timestamp(new Date().getTime());
        award.setAwardType("奖励类型");
        award.setGrantNum(2d);
        award.setGrantStyle("发放方式");
        award.setMissonExecuteIdInfoId(1l);
        award.setGrantTime(newDate);
        return award;
    }
 
    private MissionExecuteAwardPO initPo2(){
        MissionExecuteAwardPO award = new MissionExecuteAwardPO();
        Timestamp newDate = new Timestamp(new Date().getTime() + 1000 * 60 * 60 * 24);
        award.setAwardType("奖励类型x");
        award.setGrantNum(3d);
        award.setGrantStyle("发放方式x");
        award.setMissonExecuteIdInfoId(2l);    
        award.setGrantTime(newDate);
        return award;
    }
    
    @Test
    public void testInsert() {
        
        MissionExecuteAwardPO Award = initPo();
        dao.insert(Award);
        
    }
    @Test
    public void testDeleteByPrimaryKey() {
        
        MissionExecuteAwardPO Award = initPo();
        dao.insert(Award);
        log.info("insert id :"+Award.getId());
        dao.deleteByPrimaryKey(Award.getId());
    }
    @Test
    public void testSelectByPrimaryKey() {
        MissionExecuteAwardPO Award = initPo();
        dao.insert(Award);
        MissionExecuteAwardPO result = dao.selectByPrimaryKey(Award.getId());
        log.info("result id :"+result.getId());
    }  
    @Test
    public void testUpdateByPrimaryKey() {
        MissionExecuteAwardPO Award1 = initPo();
        MissionExecuteAwardPO Award2 = initPo2();
        dao.insert(Award1);  
        Award2.setId(Award1.getId());
        dao.updateByPrimaryKeySelective(Award2);
    }     
}
