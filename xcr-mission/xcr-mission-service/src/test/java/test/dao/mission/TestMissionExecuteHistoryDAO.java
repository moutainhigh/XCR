package test.dao.mission;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteHistoryDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteHistoryPO;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionExecuteHistoryDAO {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionExecuteHistoryDAO dao ;

    

    
    private MissionExecuteHistoryPO initPo(Long id){
        MissionExecuteHistoryPO history = new MissionExecuteHistoryPO();
        Timestamp newDate = new Timestamp(new Date().getTime());
        history.setCreateTime(newDate);
        history.setId(id);
        history.setDeleted(true);
        history.setLastModifyTime(newDate);
        history.setMerchantId("门店id");
        history.setMissonInfoId(1l);
        history.setReason("原因");
        history.setStatus("状态");
        history.setType("类型");
        return history;
    }
 
    private MissionExecuteHistoryPO initPo2(Long id){
        MissionExecuteHistoryPO history = new MissionExecuteHistoryPO();
        Timestamp newDate = new Timestamp(new Date().getTime() + 1000 * 60 * 60 * 24);
        history.setCreateTime(newDate);
        history.setId(id);
        history.setDeleted(false);
        history.setLastModifyTime(newDate);
        history.setMerchantId("门店idxxx");
        history.setMissonInfoId(1l);
        history.setReason("原因xxx");
        history.setStatus("状态xxx");
        history.setType("类型xxx");
        return history;
    }
    
    @Test
    public void testInsert() {
        
        MissionExecuteHistoryPO history = initPo(1l);
        //dao.insert(history);
    }
    @Test
    public void testDeleteByPrimaryKey() {
        
        MissionExecuteHistoryPO history = initPo(2l);
        //dao.insert(history);
        dao.deleteByPrimaryKey(2l);
    }
    @Test
    public void testSelectByPrimaryKey() {
        MissionExecuteHistoryPO history = initPo(3l);
        //dao.insert(history);
        MissionExecuteHistoryPO result = dao.selectByPrimaryKey(3l);
        log.info(result);
    }  
    @Test
    public void testUpdateByPrimaryKey() {
        MissionExecuteHistoryPO history1 = initPo(4l);
        MissionExecuteHistoryPO history2 = initPo2(4l);
        //dao.insert(history1);  
        //dao.updateByPrimaryKeySelective(history2);
    }     
}
