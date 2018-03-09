package test.dao.mission;

import java.sql.Timestamp;
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
import com.yatang.xc.xcr.biz.mission.bo.ExpireQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteInfoDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteHistoryPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionExecuteInfoDAO {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionExecuteInfoDAO dao ;

    

    
    private MissionExecuteInfoPO initPo(){
        MissionExecuteInfoPO info = new MissionExecuteInfoPO();
        Timestamp newDate = new Timestamp(new Date().getTime());
        info.setCreateTime(newDate);

      
        info.setDeleted(true);
        info.setLastModifyTime(newDate);
        info.setMerchantId("门店id");
        info.setMissonInfoId(1l);
        info.setReason("原因");
        info.setStatus("状态");
        info.setType("类型");
        return info;
    }
 
    private MissionExecuteInfoPO initPo2(){
        MissionExecuteInfoPO info = new MissionExecuteInfoPO();
        Timestamp newDate = new Timestamp(new Date().getTime() + 1000 * 60 * 60 * 24);
        info.setCreateTime(newDate);

     
        info.setDeleted(false);
        info.setLastModifyTime(newDate);
        info.setMerchantId("门店idxxx");
        info.setMissonInfoId(1l);
        info.setReason("原因xxx");
        info.setStatus("状态xxx");
        info.setType("类型xxx");
        return info;
    }
    
    @Test
    public void testInsert() {
        
        MissionExecuteInfoPO info = initPo();
        dao.insert(info);
        
    }
    @Test
    public void testDeleteByPrimaryKey() {
        
        MissionExecuteInfoPO info = initPo();
        dao.insert(info);
        log.info("insert id :"+info.getId());
        dao.deleteByPrimaryKey(info.getId());
    }
    @Test
    public void testSelectByPrimaryKey() {
        MissionExecuteInfoPO info = initPo();
        dao.insert(info);
        MissionExecuteInfoPO result = dao.selectByPrimaryKey(info.getId());
        log.info("result id :"+result.getId());
    }  
    @Test
    public void testUpdateByPrimaryKey() {
        MissionExecuteInfoPO info1 = initPo();
        MissionExecuteInfoPO info2 = initPo2();
        dao.insert(info1);  
        info2.setId(info1.getId());
        dao.updateByPrimaryKeySelective(info2);
    }     
    
    @Test
    public void queryMissionExecute(){
        MissionExecuteQuery query = new MissionExecuteQuery();
        query.setMerchantId("m002");
        query.setStatus("INIT");
        query.setType("RECOMMEND");
        query.setOrderBy("sort");
        int ok = dao.queryMissionExecuteCount(query);
        log.info("ok:"+JSON.toJSONString(ok));
        query.setStartIndex(2);
        query.setEndIndex(4);
        List<MissionExecuteInfoPO> mission = dao.queryMissionExecute(query);
        log.info("mission size:"+JSON.toJSONString(mission.size()));
        log.info("mission:"+JSON.toJSONString(mission));
    }    
    
    @Test
    public void expireMissionExecutes(){
    	ExpireQuery query = new ExpireQuery();
    	Date now = new Date();
        query.setNow(now);
        query.setStatus("INVALID");
        query.setReason("AUTO_EXPIRE");
        int mission = dao.expireMissionExecutes(query);
        log.info("mission:"+JSON.toJSONString(mission));
    }    
    
    @Test
    public void expireNotExistMissionExecutes(){
    	ExpireQuery query = new ExpireQuery();
    	Date now = new Date();
        query.setNow(now);
        query.setStatus("INVALID");
        query.setReason("MISSION_NOT_EXIST");
        int mission = dao.expireNotExistMissionExecutes(query);
        log.info("mission:"+JSON.toJSONString(mission));
    }        
}
