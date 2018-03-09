package test.dao.mission;

import com.yatang.xc.xcr.biz.mission.dao.MissionInfoDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionSignDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.UserSignPO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Dao 的 junit test 类
 *
 * @author : zhaokun
 * @version : 2017年3月20日
 * @date : 2017年3月20日 上午9:16:50
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionInfoDAO {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionInfoDAO dao;

    @Autowired
    private MissionSignDAO signDAO;


    private MissionInfoPO initPo() {
        MissionInfoPO info = new MissionInfoPO();
        Timestamp newDate = new Timestamp(new Date().getTime());
        info.setCreateTime(newDate);

        info.setBpmIdentity("bpm标识");
        info.setCompleteTips("完成任务提示语");

        info.setIconUrl("图标url");
        info.setLink("链接");
        info.setMissonTemplateCode("模板编码");
        info.setRule("规则");
        info.setTriggerInterfaceName("触发接口名称");
        info.setLastModifyTime(newDate);
        info.setReason("原因");
        info.setStatus("状态");
        info.setType("类型");
        info.setManualAudit(false);
        info.setDeleted(true);
        return info;
    }

    private MissionInfoPO initPo2() {
        MissionInfoPO info = new MissionInfoPO();
        Timestamp newDate = new Timestamp(new Date().getTime() + 1000 * 60 * 60 * 24);
        info.setCreateTime(newDate);

        info.setBpmIdentity("bpm标识x");
        info.setCompleteTips("完成任务提示语x");

        info.setIconUrl("图标urlx");
        info.setLink("链接x");
        info.setMissonTemplateCode("模板编码x");
        info.setRule("规则x");
        info.setTriggerInterfaceName("触发接口名称x");
        info.setLastModifyTime(newDate);
        info.setReason("原因x");
        info.setStatus("状态x");
        info.setType("类型x");
        info.setManualAudit(true);
        info.setDeleted(false);
        return info;
    }

    @Test
    public void testInsert() {

        MissionInfoPO info = initPo();
        dao.insert(info);

    }

    //    @Test
//    public void testDeleteByPrimaryKey() {
//        
//        MissionInfoPO info = initPo();
//        dao.insert(info);
//        log.info("insert id :"+info.getId());
//        dao.deleteByPrimaryKey(info.getId());
//    }
    @Test
    public void testSelectByPrimaryKey() {
        MissionInfoPO info = initPo();
        dao.insert(info);
        MissionInfoPO result = dao.selectByPrimaryKey(info.getId());
        log.info("result id :" + result.getId());
    }

    @Test
    public void testUpdateByPrimaryKey() {
        MissionInfoPO info1 = initPo();
        MissionInfoPO info2 = initPo2();
        dao.insert(info1);
        info2.setId(info1.getId());
        dao.updateByPrimaryKeySelective(info2);
    }

    @Test
    public void testUserSign() {
        String shopCode = "A001000";
        try {
            Date validStartDay = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-09-15 00:00:00");
            Date validEndDay = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-09-15 23:59:59");
            List<UserSignPO> userSignPOList = signDAO.getSignPo(shopCode, validStartDay, validEndDay);
            System.out.println(userSignPOList);
            System.out.println(userSignPOList.size());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
