package test.dao.mission;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.mission.dao.MissionRelatedDetailDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedDetailPO;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionRelatedDetailDAO {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionRelatedDetailDAO dao ;

    

    
    private MissionRelatedDetailPO initPo(){
        MissionRelatedDetailPO related = new MissionRelatedDetailPO();
        related.setLevel(1);
        related.setMissonInfoId(1l);
        related.setMissonRelatedId(1l);
        return related;
    }
 
    private MissionRelatedDetailPO initPo2(){
        MissionRelatedDetailPO related = new MissionRelatedDetailPO();
        related.setLevel(2);
        related.setMissonInfoId(2l);
        related.setMissonRelatedId(2l);      
        return related;
    }
    
    @Test
    public void testInsert() {
        
        MissionRelatedDetailPO related = initPo();
        dao.insert(related);
        
    }
    @Test
    public void testDeleteByPrimaryKey() {
        
        MissionRelatedDetailPO related = initPo();
        dao.insert(related);
        log.info("insert id :"+related.getId());
        dao.deleteByPrimaryKey(related.getId());
    }
    @Test
    public void testSelectByPrimaryKey() {
        MissionRelatedDetailPO related = initPo();
        dao.insert(related);
        MissionRelatedDetailPO result = dao.selectByPrimaryKey(related.getId());
        log.info("result id :"+result.getId());
    }  
    @Test
    public void testUpdateByPrimaryKey() {
        MissionRelatedDetailPO related1 = initPo();
        MissionRelatedDetailPO related2 = initPo2();
        dao.insert(related1);  
        related2.setId(related1.getId());
        dao.updateByPrimaryKeySelective(related2);
    }     
}
