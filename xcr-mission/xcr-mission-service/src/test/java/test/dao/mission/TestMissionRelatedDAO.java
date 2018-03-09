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
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.biz.mission.dao.MissionRelatedDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedPO;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionRelatedDAO {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionRelatedDAO dao ;

    

    
    private MissionRelatedPO initPo(){
        MissionRelatedPO related = new MissionRelatedPO();
        Timestamp newDate = new Timestamp(new Date().getTime());
        related.setCreateTime(newDate);
        related.setLastModifyTime(newDate);
        related.setReason("原因");
        related.setStatus("状态");
        related.setType("类型");
        related.setMissonRelatedDescription("描述");
        related.setMissonRelatedName("任务关联名称");
        return related;
    }
 
    private MissionRelatedPO initPo2(){
        MissionRelatedPO related = new MissionRelatedPO();
        Timestamp newDate = new Timestamp(new Date().getTime() + 1000 * 60 * 60 * 24);
        related.setCreateTime(newDate);
        related.setLastModifyTime(newDate);
        related.setReason("原因x");
        related.setStatus("状态x");
        related.setType("类型x");
        related.setMissonRelatedDescription("描述x");
        related.setMissonRelatedName("任务关联名称x");        
        return related;
    }
    
    @Test
    public void testInsert() {
        
        MissionRelatedPO related = initPo();
        dao.insert(related);
        
    }
    @Test
    public void testDeleteByPrimaryKey() {
        
        MissionRelatedPO related = initPo();
        dao.insert(related);
        log.info("insert id :"+related.getId());
        dao.deleteByPrimaryKey(related.getId());
    }
    @Test
    public void testSelectByPrimaryKey() {
//        MissionRelatedPO related = initPo();
//        dao.insert(related);
        MissionRelatedPO result = dao.selectByPrimaryKey(50l);
        log.info("result:"+JSON.toJSONString(result));
    }  
    
    @Test
    public void testUpdateByPrimaryKey() {
//        MissionRelatedPO related1 = initPo();
        MissionRelatedPO related2 = new MissionRelatedPO();
        related2.setId(51l);
//        dao.insert(related1);  
//        related2.setId(related1.getId());
        related2.setReason("SYS_CREATE_test");
        dao.updateByPrimaryKeySelective(related2);

    }     
}
