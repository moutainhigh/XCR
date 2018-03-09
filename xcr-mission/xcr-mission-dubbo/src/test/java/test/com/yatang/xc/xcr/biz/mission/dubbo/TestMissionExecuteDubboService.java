package test.com.yatang.xc.xcr.biz.mission.dubbo;

import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import com.yatang.xc.xcr.biz.mission.schedule.thread.ICreateMissionExecuteThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionExecuteQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.UserDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionBPMDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumAuditResult;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionOrderBy;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;

/**
 * 
 * MissionExecuteDubboService 测试类
 * 
 * @author : zhaokun
 * @date : 2017年3月27日 下午7:19:58
 * @version : 2017年3月27日 zhaokun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionExecuteDubboService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionExecuteDubboService service;

    @Autowired
    private MissionClassroomDubboService classService;
    @Autowired
    private MissionBPMDubboService bpmService;
    
    
    @Autowired
    private MissionExecuteFlowService missionExecuteFlowService;
    @Autowired
    private MissionExecuteService executeService;


    @Test
    public void testCreateMissionExecute()
            throws InterruptedException {
        // schedule.doJob();
        // Thread.sleep(10000);
        String merchantId = "A100027";
        ICreateMissionExecuteThread thread = missionExecuteFlowService.initCreateMissionExecuteThread(merchantId);
        thread.doRun();

    }


    @Test
    public void testQueryMissionExecute() {
        MissionExecuteQueryDto query = new MissionExecuteQueryDto();
        query.setStatus(EnumMissionExecuteStatus.STATUS_INIT.getCode());
        query.setMerchantId("m101");
        Response<Integer> result = service.queryMissionExecuteCount(query);
        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + result.getResultObject());

        Response<List<ViewMissionExecuteDto>> result2 = service.queryMissionExecute(query);
        if (result2 == null || !result2.isSuccess()) {
            fail(result2.getCode() + ":" + result2.getErrorMessage());
        }
        List<ViewMissionExecuteDto> list = result2.getResultObject();
        for (ViewMissionExecuteDto dto : list) {
            log.info("result2:" + JSON.toJSON(dto));
        }

    }


    @Test
    public void testExecuteMission() {
        Response<Boolean> result = service.executeMission("4639");
        log.info("result:" + JSON.toJSON(result));
//        if (result == null || !result.isSuccess()) {
//            fail(result.getCode() + ":" + result.getErrorMessage());
//        }
    }


//    @Test
//    public void testAutoAuditMission() {
//        Response<Boolean> result = service.autoAuditMission("3","user1");
//
//        if (result == null || !result.isSuccess()) {
//            fail(result.getCode() + ":" + result.getErrorMessage());
//        }
//    }


    @Test
    public void testautoAuditMissionByMerchantId() {
        Response<List<String>> result = service.autoAuditMissionByMerchantId("m101");
        List<String> successExecuteIds = result.getResultObject();
        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        if (successExecuteIds != null) {
            for (String id : successExecuteIds) {
                log.info("id:" + JSON.toJSON(id));
            }
        }

    }


    @Test
    public void testgoAndGetAward() {
        Response<List<AwardInfoDto>> result = service.goAndGetAward("15","");

        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
    }
    
    @Test
    public void testManualAuditAwardCallback() {
        Response<Boolean> result = bpmService.manualAuditAwardCallback("15", "xx", EnumAuditResult.AUDIT_RESULT_APPROVE.getCode(),"");

        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
    }

    @Test
    public void testManualAuditMissionCallback() {
        Response<Boolean> result = bpmService.manualAuditMissionCallback("15", "xx", EnumAuditResult.AUDIT_RESULT_APPROVE.getCode(),"");

        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
    }
    
    @Test
    public void queryMissionExecute() {
        MissionExecuteQueryDto query = new MissionExecuteQueryDto();
        query.setType("RECOMMEND");
        query.setStartIndex(1);
        query.setEndIndex(3);
        Response<List<ViewMissionExecuteDto>> result = service.queryMissionExecute(query);

        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSON(result));
    }
    
    @Test
    public void initMissionExecuteByMerchantId() {
        UserDto user = new UserDto();
        user.setLogin("login一账通账号");
        user.setMerchantId("merchantId");
        user.setRealName("张三");
        user.setRegistrationId("xxxx002");
        user.setUserName("小超账号");
        Response<Boolean> result = service.initMissionExecuteByMerchantId(user);

        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSON(result));
    } 
    
    @Test
    public void autoAuditMissionByMerchantId() {
    	long current =  System.currentTimeMillis();
        UserDto user = new UserDto();
        user.setLogin("YT201610241404402a");
        user.setMerchantId("A900005");
        user.setRealName("生产验证_小超加盟商");
        user.setRegistrationId("020000000002");
        user.setUserName("900005");
        Response<List<String>> result = service.autoAuditMissionByMerchantId(user.getMerchantId());

        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result cost : "+(System.currentTimeMillis()-current)+" return : " + JSON.toJSON(result));
    } 
    

    @Test
    public void testgoAndGetAwardByMerchantId() {
        String merchantId = "merchantId";
        MissionExecuteQuery query = new MissionExecuteQuery();
        query.setStatus(EnumMissionExecuteStatus.STATUS_FINISHED.getCode());
        query.setMerchantId(merchantId);
        List<MissionExecuteInfoPO> executes = executeService.queryMissionExecute(query);
        if(executes!=null){
            for(MissionExecuteInfoPO execute:executes){
                Response<List<AwardInfoDto>> result = service.goAndGetAward(String.valueOf(execute.getId()),"");
                if (result == null || !result.isSuccess()) {
                    fail(result.getCode() + ":" + result.getErrorMessage());
                }
            }
        }

    }
    @Test
    public void checkMissionRelatedAchieved(){
        
        Response<Boolean> result = service.checkMissionRelatedAchieved("19");
        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSON(result));
    }
    @Test
    public void queryMissionExecuteInHistory(){
        MissionExecuteQueryDto query = new MissionExecuteQueryDto();
        query.setMerchantId("");
        query.setOrderBy(EnumMissionOrderBy.TYPE_TIME_DESC.getCode());
        Response<List<ViewMissionExecuteDto>> result = service.queryMissionExecuteInHistory(query);
        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSON(result));
    }  

    @Test
    public void getAttachment(){

        Response<List<AttachmentDto>> result = bpmService.getAttachment("7339");
        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSON(result));
    }      
    
    @Test
    public void queryMissionExecuteByMerchantIdAndCourseId(){

        Response<ViewMissionExecuteDto> result = classService.queryMissionExecuteByMerchantIdAndCourseId(null,"429");
        if (result == null || !result.isSuccess()) {
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSON(result));
    }      
    
    
}
