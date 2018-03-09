package test.dao.mission;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.mission.dao.MissionOfflineExistsListDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionOfflineExistsListPO;

/**
 * 
 * Dao 的 junit test 类
 * 
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50
 * @version : 2017年3月20日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionOfflineExitstListDAO {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionOfflineExistsListDAO dao;


    @Test
    public void testInsert() {
        MissionOfflineExistsListPO po = new MissionOfflineExistsListPO();
        po.setName("xx");
        po.setDescription("xxx");
        po.setMerchantId("fefe");
        po.setStatus("fefe1");
        po.setTemplateCode("t001");
        
        dao.insert(po);
    }

}
