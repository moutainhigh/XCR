package test.service.mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.biz.mission.bo.MissionInfoQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionBuyListPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;
import com.yatang.xc.xcr.biz.mission.domain.MissionUserHistoryPO;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

/**
 * 
 * Dao 的 junit test 类
 * @author : zhaokun
 * @date : 2017年3月20日 上午9:16:50  
 * @version : 2017年3月20日  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionService {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MissionService missionService;
 
    @Test
    public void tsetCreateMissionInfo(){
        Date now = new Date();
        MissionInfoPO missionInfo = new MissionInfoPO();
        missionInfo.setBpmIdentity("bpmid");
        missionInfo.setCompleteTips("tips");
        missionInfo.setCourseId("coureid");
        missionInfo.setCreateTime(now);
        missionInfo.setDeleted(false);
        missionInfo.setDescription("description");
        missionInfo.setIconUrl("url");
        missionInfo.setId(7l);
        missionInfo.setLastModifyTime(now);
        missionInfo.setLink("link");
        missionInfo.setManualAudit(false);
        missionInfo.setMissonTemplateCode("T001");
        missionInfo.setName("name");
        missionInfo.setReason("reason");
        missionInfo.setRelated(false);
        missionInfo.setRule("rule");
        missionInfo.setSpecialAwardRemark("mark");
        missionInfo.setStatus("xxx");
        missionInfo.setTriggerInterfaceName("faceName");
        missionInfo.setType("type");
        List<MissionAwardPO> awards = new ArrayList<MissionAwardPO>();
        MissionAwardPO award1= new MissionAwardPO();
        award1.setAwardType("type");
        award1.setGrantNum(100.4);
        award1.setGrantStyle("style");
        award1.setId(9l);
        award1.setMissonInfoId(1l);
        awards.add(award1);
        MissionAwardPO award2= new MissionAwardPO();
        award2.setAwardType("type2");
        award2.setGrantNum(101.4);
        award2.setGrantStyle("style2");
        award2.setId(10l);
        award2.setMissonInfoId(1l);
        awards.add(award2);        
        List<MissionAttachmentPO> attachments = new ArrayList<MissionAttachmentPO>();
        MissionAttachmentPO att = new MissionAttachmentPO();
        att.setDescription("cxxx");
        att.setName("bxxx");
        att.setSort(1);
        att.setType("xx");
        att.setUrl("a");
        attachments.add(att);
        missionService.createMissionInfo(missionInfo, awards,attachments);
    }
    
    @Test
    public void tsetFindById(){
        MissionInfoPO mission = missionService.findById(21l);
        log.info("mission:"+JSON.toJSONString(mission));
    }
    
    @Test
    public void tsetQueryMissionInfo(){
        MissionInfoQuery query = new MissionInfoQuery();
        query.setId(1l);
        int count = missionService.queryMissionInfoCount(query);
        log.info("count:"+count);
        query.setId(null);
        query.setStatus("INIT");
        query.setStartIndex(0);
        query.setEndIndex(3);
        query.setOrderBy("last_modify_time");
        List<MissionInfoPO> missions = missionService.queryMissionInfo(query);
        if(missions!=null){
            log.info("mission size:"+missions.size());
            log.info("mission:"+JSON.toJSONString(missions));        
        }
    }   
    
    @Test
    public void tsetQueryMissionTemplateByMissionType(){
        List<MissionTemplatePO> templates = missionService.queryMissionTemplateByMissionType("RECOMMEND");
        log.info("mission:"+JSON.toJSONString(templates));
    }    
    
    
    @Test
    public void testRemoveMissionById(){
        boolean ok = missionService.removeMissionById(1l);
        log.info("ok:"+ok);
    }    
    
    @Test
    public void testUpdateMissionInfo(){
        Date now = new Date();
        MissionInfoPO missionInfo = new MissionInfoPO();
        missionInfo.setBpmIdentity("bpmid");
        missionInfo.setCompleteTips("tips");
        missionInfo.setCourseId("coureid");
        missionInfo.setCreateTime(now);
        missionInfo.setDeleted(false);
        missionInfo.setDescription("description");
        missionInfo.setIconUrl("url");
        missionInfo.setId(7l);
        missionInfo.setLastModifyTime(now);
        missionInfo.setLink("link");
        missionInfo.setManualAudit(false);
        missionInfo.setMissonTemplateCode("T001");
        missionInfo.setName("name");
        missionInfo.setReason("reason");
        missionInfo.setRelated(false);
        missionInfo.setRule("rule");
        missionInfo.setSpecialAwardRemark("mark");
        missionInfo.setStatus("xxx");
        missionInfo.setTriggerInterfaceName("faceName");
        missionInfo.setType("type");
        List<MissionAwardPO> awards = new ArrayList<MissionAwardPO>();
        MissionAwardPO award1= new MissionAwardPO();
        award1.setAwardType("type");
        award1.setGrantNum(100.4);
        award1.setGrantStyle("style");
        award1.setId(9l);
        award1.setMissonInfoId(1l);
        awards.add(award1);
        MissionAwardPO award2= new MissionAwardPO();
        award2.setAwardType("type2");
        award2.setGrantNum(101.4);
        award2.setGrantStyle("style2");
        award2.setId(10l);
        award2.setMissonInfoId(1l);
        awards.add(award2);        
        boolean ok = missionService.updateMissionInfo(missionInfo, awards,null);
        log.info("ok:"+ok);
    }   
    @Test
    public void testUpdateMissionInfoStatus(){
        UpdateStatusQuery query = new UpdateStatusQuery();
        query.setId(2l);
        query.setStatus("newx");
        query.setOldStatus(new String[]{"INIT"});
        query.setLastModifyTime(new Date());
        query.setReason("testReason");
        boolean ok = missionService.updateMissionInfoStatus(query);
        log.info("ok:"+ok);
    }    
    
    @Test
    public void testSelectTemplateByCode(){
        Long now = new Date().getTime();
        log.info("now:"+now+" int now:"+now.intValue());
        Long xx = 1491444659195l;
        log.info("xx:"+xx+" int xx:"+xx.intValue());
        MissionTemplatePO template = missionService.selectTemplateByCode("T002");
        log.info("template:"+JSON.toJSONString(template));
    }       
    
    @Test
    public void queryMissionByCourseId(){
        List<MissionInfoPO> list = missionService.queryMissionByCourseId("coureid", null);
        log.info("list:"+JSON.toJSONString(list));
    }   
    
    @Test
    public void selectUserHistoryByMerchantId(){
        MissionUserHistoryPO list = missionService.selectUserHistoryByMerchantId("A091550");
        log.info("list:"+JSON.toJSONString(list));
    }   
    
    @Test
    public void updateUserHistory(){
        MissionUserHistoryPO user = new MissionUserHistoryPO();
        user.setId(17l);
        user.setRealNameCheckResult("108");
        user.setCompanyId("test");
        boolean list = missionService.updateUserHistory(user, null);
        log.info("list:"+JSON.toJSONString(list));
    }   
    @Test
    public void insertUserHistory(){
        MissionUserHistoryPO user = new MissionUserHistoryPO();
        user.setCompanyId("companyId");
        user.setLastModifyTime(new Date());
        user.setLogin("echo");
        user.setMerchantId("merchantId_echo");
        user.setUserName("user_echo");
        user.setRealName("xxx");
        user.setRegistrationId("ooo");
        user.setRealNameCheckResult("108");
        boolean list = missionService.insertUserHistory(user);
        log.info("list:"+JSON.toJSONString(list));
    }    
    
    @Test
    public void createBuyList(){
        MissionBuyListPO buylist = new MissionBuyListPO();
        buylist.setLogin("login");
        buylist.setMissionId("xx");
        buylist.setOrderId("xxx");
        buylist.setProductCount(5);
        buylist.setProductIds("productIds");
        buylist.setReturnValue("returnValue");
        buylist.setUsedProductCount(3);
        buylist.setExecuteId("4");
        boolean list = missionService.createBuyList(buylist);
        log.info("list:"+JSON.toJSONString(list));
    }    
    
    @Test
    public void updateBuyList(){
        MissionBuyListPO buylist = new MissionBuyListPO();
        buylist.setId(3l);
        buylist.setLogin("login2");
        buylist.setMissionId("xx2");
        buylist.setOrderId("xxx2");
        buylist.setProductCount(6);
        buylist.setProductIds("productIds2");
        buylist.setReturnValue("returnValue2");
        buylist.setExecuteId("9");
        buylist.setUsedProductCount(4);
        boolean list = missionService.updateBuyList(buylist);
        log.info("list:"+JSON.toJSONString(list));
    }    
    
    @Test
    public void queryBuyListBy(){
        List<MissionBuyListPO> list = missionService.queryBuyListBy("xaopp",2L,null);
        log.info("list:"+JSON.toJSONString(list));
    }    
    
    @Test
    public void queryMerchantIdFromUserHistory(){
    	Set<String> list = missionService.queryMerchantIdFromUserHistory();
        log.info("list:"+JSON.toJSONString(list));
    }    
    
    
}
