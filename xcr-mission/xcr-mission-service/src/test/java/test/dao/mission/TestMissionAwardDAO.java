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

import com.yatang.xc.xcr.biz.mission.dao.MissionAwardDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionAwardDAO {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionAwardDAO dao ;

    

    
    private MissionAwardPO initPo(){
        MissionAwardPO award = new MissionAwardPO();
        Timestamp newDate = new Timestamp(new Date().getTime());
        award.setAwardType("奖励类型");
        award.setGrantNum(2d);
        award.setGrantStyle("发放方式");
        award.setMissonInfoId(1l);
        return award;
    }
 
    private MissionAwardPO initPo2(){
        MissionAwardPO award = new MissionAwardPO();
        Timestamp newDate = new Timestamp(new Date().getTime() + 1000 * 60 * 60 * 24);
        award.setAwardType("奖励类型x");
        award.setGrantNum(3d);
        award.setGrantStyle("发放方式x");
        award.setMissonInfoId(2l);     
        return award;
    }
    
    @Test
    public void testInsert() {
        
        MissionAwardPO Award = initPo();
        dao.insert(Award);
        
    }
    @Test
    public void testDeleteByPrimaryKey() {
        
        MissionAwardPO Award = initPo();
        dao.insert(Award);
        log.info("insert id :"+Award.getId());
        dao.deleteByPrimaryKey(Award.getId());
    }
    @Test
    public void testSelectByPrimaryKey() {
        MissionAwardPO Award = initPo();
        dao.insert(Award);
        MissionAwardPO result = dao.selectByPrimaryKey(Award.getId());
        log.info("result id :"+result.getId());
    }  
    @Test
    public void testUpdateByPrimaryKey() {
        MissionAwardPO Award1 = initPo();
        MissionAwardPO Award2 = initPo2();
        dao.insert(Award1);  
        Award2.setId(Award1.getId());
        dao.updateByPrimaryKeySelective(Award2);
    }     
}
