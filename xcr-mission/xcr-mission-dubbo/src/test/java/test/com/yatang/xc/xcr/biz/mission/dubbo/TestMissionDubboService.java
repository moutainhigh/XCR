package test.com.yatang.xc.xcr.biz.mission.dubbo;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.CreateMissionInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.CreateMissionRelatedDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.MissionInfoQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.MissionRelateQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.MissionRelatedDetailDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.MissionTemplateDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.ViewMissionInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.ViewMissionRelatedDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumAwardType;
import com.yatang.xc.xcr.biz.mission.enums.EnumGrantStyle;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionType;

/**
 * 
 * MissionDubboService 测试类
 * @author : zhaokun
 * @date : 2017年3月27日 下午7:19:58  
 * @version : 2017年3月27日  zhaokun
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:test/dubbo/applicationContext-consumer.xml"})
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestMissionDubboService {
    protected final Log log = LogFactory.getLog(this.getClass());
    
	@Autowired
	private MissionDubboService service;
	

	
    @Test
    public void testCreateMissionInfo()  {
	    CreateMissionInfoDto dto = new CreateMissionInfoDto();
	    dto.setName("echo test");
	    dto.setDescription("修改金融账号与实名认证");
	    dto.setIconUrl("");
	    //dto.setLink("testLink");
	    dto.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
	    dto.setAwardType(EnumAwardType.AWARD_TYPE_NONE.getCode());
	    dto.setPublish(false);
	    dto.setTemplateCode("T002");
	    dto.setRelated(false);
//	    List<AwardInfoDto> awards = new ArrayList<AwardInfoDto>();
//	    AwardInfoDto award1 = new AwardInfoDto();
//	    award1.setAwardType(EnumAwardType.AWARD_TYPE_CASH.getCode());
//	    award1.setGrantStyle(EnumGrantStyle.GRANT_STYLE_BPM.getCode());
//	    award1.setGrantNum(100.05);
//	    awards.add(award1);
//	    AwardInfoDto award2 = new AwardInfoDto();
//	    award2.setAwardType(EnumAwardType.AWARD_TYPE_SCORE.getCode());
//	    award2.setGrantStyle(EnumGrantStyle.GRANT_STYLE_REAL_TIME.getCode());
//	    award2.setGrantNum(100d); 
//	    awards.add(award2);
//	    dto.setAwards(awards);
	    Response<Boolean> result = service.createMissionInfo(dto);
	    if(result==null || !result.isSuccess()){
	        fail(result.getCode() + ":" + result.getErrorMessage());
	    }
	}
    @Test
    public void testQueryMissionInofCount()  {
        MissionInfoQueryDto query = new MissionInfoQueryDto();
        query.setIsRelated(1);
        query.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
        query.setStatus(EnumMissionStatus.STATUS_INIT.getCode());
        Response<Integer> result = service.queryMissionInofCount(query);
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }else{
            log.info("result:"+result.getResultObject());
        }

    }
 
    @Test
    public void testUpdateMissionInfo()  {
        CreateMissionInfoDto dto = new CreateMissionInfoDto();
        dto.setId(111l);
        dto.setName("testName**");
        dto.setDescription("testDescription**");
        dto.setIconUrl("testIconUrl**");
        dto.setLink("testLink**");
        dto.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
        dto.setAwardType(EnumAwardType.AWARD_TYPE_CASH_AND_SCORE.getCode());
        dto.setPublish(true);
        dto.setTemplateCode("T002");
        dto.setRelated(false);        
        List<AwardInfoDto> awards = new ArrayList<AwardInfoDto>();
        AwardInfoDto award1 = new AwardInfoDto();
        award1.setAwardType(EnumAwardType.AWARD_TYPE_CASH.getCode());
//        award1.setGrantStyle(EnumGrantStyle.GRANT_STYLE_BPM.getCode());
        award1.setGrantNum(101.05);
        awards.add(award1);
        AwardInfoDto award2 = new AwardInfoDto();
        award2.setAwardType(EnumAwardType.AWARD_TYPE_SCORE.getCode());
        award2.setGrantStyle(EnumGrantStyle.GRANT_STYLE_REAL_TIME.getCode());
        award2.setGrantNum(101d); 
        awards.add(award2);
        dto.setAwards(awards);
        Response<Boolean> result = service.updateMissionInfo(dto);
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
    }    
    
    @Test
    public void testPublishMissionInfo()  {
        
        Response<Boolean> result = service.publishMissionInfo("15");
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        
    }
    
    @Test
    public void testRemoveMissionInfo()  {
        
        Response<Boolean> result = service.removeMissionInfo("15");
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        
    }
    
    @Test
    public void testCreateRelatedMission()  {
        CreateMissionRelatedDto dto = new CreateMissionRelatedDto();
        
        dto.setMissonRelatedName("testnmae");
        dto.setMissonRelatedDescription("xxx");
        dto.setPublish(true);
        List<MissionRelatedDetailDto> details = new ArrayList<MissionRelatedDetailDto>();
        MissionRelatedDetailDto detail1 = new MissionRelatedDetailDto();
        detail1.setLevel(1);
        detail1.setMissonInfoId(15l);
        details.add(detail1);
        MissionRelatedDetailDto detail2 = new MissionRelatedDetailDto();
        detail2.setLevel(2);
        detail2.setMissonInfoId(16l); 
        details.add(detail2);
        dto.setDetails(details);
        
        
        Response<Boolean> result = service.createRelatedMission(dto);
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
    }       
 
    @Test
    public void testUpdateRelatedMission()  {
        CreateMissionRelatedDto dto = new CreateMissionRelatedDto();
        
        dto.setMissonRelatedName("testnmaeooo");
        dto.setMissonRelatedDescription("xxxeeee");
        dto.setPublish(true);
        dto.setId(1l);
        List<MissionRelatedDetailDto> details = new ArrayList<MissionRelatedDetailDto>();
        MissionRelatedDetailDto detail1 = new MissionRelatedDetailDto();
        detail1.setLevel(1);
        detail1.setMissonInfoId(17l);
        details.add(detail1);
        MissionRelatedDetailDto detail2 = new MissionRelatedDetailDto();
        detail2.setLevel(2);
        detail2.setMissonInfoId(18l); 
        details.add(detail2);
        dto.setDetails(details);
        
        
        Response<Boolean> result = service.updateRelatedMission(dto);
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
    }       
    
    @Test
    public void testRemoveRelatedMissionInfo()  {
        
        Response<Boolean> result = service.removeMissionRelate("2");
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        
    } 
    
    @Test
    public void testPublishRelatedMission()  {
        
        Response<Boolean> result = service.publishRelatedMission("3");
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        
    }  
    
    @Test
    public void queryMissionTemplateByMissionType()  {
        
        Response<List<MissionTemplateDto>> result = service.queryMissionTemplateByMissionType("RECOMMEND");
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSONString(result.getResultObject()));
    }  
    
    @Test
    public void queryMissionExecute() {
        MissionInfoQueryDto query = new MissionInfoQueryDto();
        query.setId(763l);
//        query.setType("RECOMMEND");
        //query.setIsRelated(1);
//        query.setStartIndex(0);
//        query.setEndIndex(100);
        Response<List<ViewMissionInfoDto>> result = service.queryMissionInof(query);

        if (result == null || !result.isSuccess()) {
        	System.out.println(result);
//            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result size:"+result.getResultObject().size()+":" + JSON.toJSON(result));
    }
    
    @Test
    public void queryMissionRelate()  {
        MissionRelateQueryDto dto = new MissionRelateQueryDto();
        dto.setId(54l);
//        dto.setStartIndex(0);
//        dto.setEndIndex(10);
        Response<List<ViewMissionRelatedDto>> result = service.queryMissionRelate(dto);
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSONString(result.getResultObject()));
    }  
    
    @Test
    public void queryMissionRelateCount()  {
        MissionRelateQueryDto dto = new MissionRelateQueryDto();
        dto.setStartIndex(0);
        dto.setEndIndex(10);
        Response<Integer> result = service.queryMissionRelateCount(dto);
        if(result==null || !result.isSuccess()){
            fail(result.getCode() + ":" + result.getErrorMessage());
        }
        log.info("result:" + JSON.toJSONString(result.getResultObject()));
    }  
}
