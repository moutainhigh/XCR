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
import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.CreateMissionInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.UserDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionBPMDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionDubboService;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.mission.enums.EnumAuditResult;
import com.yatang.xc.xcr.biz.mission.enums.EnumAwardType;
import com.yatang.xc.xcr.biz.mission.enums.EnumGrantStyle;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionExecuteStatus;
import com.yatang.xc.xcr.biz.mission.enums.EnumMissionType;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;

/**
 * 
 * MissionDubboService 测试类
 * 
 * @author : zhaokun
 * @date : 2017年3月27日 下午7:19:58
 * @version : 2017年3月27日 zhaokun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
public class TestCreateTestData {

	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private MissionDubboService missionDubboservice;

	@Autowired
	private MissionExecuteDubboService missionexecuteDubboService;

	@Autowired
	private MissionExecuteService missionExecuteService;

	@Autowired
	private MissionBPMDubboService missionBPMDubboService;

	@Autowired
	private MissionClassroomDubboService missionClassroomDubboService;
	@Autowired
	private MissionExecuteFlowService executeFlowService;

	@Test
	public void testCreateMissionInfo() {
		CreateMissionInfoDto dto = new CreateMissionInfoDto();
		dto.setName("修改金融账号与实名认证");
		dto.setDescription("修改金融账号与实名认证");
		dto.setIconUrl("");
		// dto.setLink("testLink");
		dto.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
		dto.setAwardType(EnumAwardType.AWARD_TYPE_NONE.getCode());
		dto.setPublish(true);
		dto.setTemplateCode("T001");
		dto.setRelated(false);
		Response<Boolean> result = missionDubboservice.createMissionInfo(dto);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
	}

	@Test
	public void testCreateMissionInfo2() {
		CreateMissionInfoDto dto = new CreateMissionInfoDto();
		dto.setName("购买收银机");
		dto.setDescription("购买收银机");
		dto.setIconUrl("");
		// dto.setLink("testLink");
		dto.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
		dto.setAwardType(EnumAwardType.AWARD_TYPE_SCORE.getCode());
		dto.setPublish(true);
		dto.setTemplateCode("T002");
		dto.setRelated(false);
		List<AwardInfoDto> awards = new ArrayList<AwardInfoDto>();
		AwardInfoDto award = new AwardInfoDto();
		award.setAwardType(EnumAwardType.AWARD_TYPE_SCORE.getCode());
		award.setGrantStyle(EnumGrantStyle.GRANT_STYLE_REAL_TIME.getCode());
		award.setGrantNum(100.0);
		awards.add(award);
		dto.setAwards(awards);
		Response<Boolean> result = missionDubboservice.createMissionInfo(dto);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
	}

	@Test
	public void testCreateMissionInfo3() {
		CreateMissionInfoDto dto = new CreateMissionInfoDto();
		dto.setName("产生第一笔交易");
		dto.setDescription("产生第一笔交易");
		dto.setIconUrl("");
		// dto.setLink("testLink");
		dto.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
		dto.setAwardType(EnumAwardType.AWARD_TYPE_NONE.getCode());
		dto.setPublish(true);
		dto.setTemplateCode("T003");
		dto.setRelated(false);
		Response<Boolean> result = missionDubboservice.createMissionInfo(dto);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
	}

	@Test
	public void testCreateMissionInfo4() {
		CreateMissionInfoDto dto = new CreateMissionInfoDto();
		dto.setName("申请门头补贴");
		dto.setDescription("申请门头补贴");
		dto.setIconUrl("");
		// dto.setLink("testLink");
		dto.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
		dto.setAwardType(EnumAwardType.AWARD_TYPE_NONE.getCode());
		dto.setPublish(true);
		dto.setTemplateCode("T004");
		dto.setRelated(false);
		Response<Boolean> result = missionDubboservice.createMissionInfo(dto);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
	}

	@Test
	public void testCreateMissionInfo5() {
		CreateMissionInfoDto dto = new CreateMissionInfoDto();
		dto.setName("申请租金补贴");
		dto.setDescription("申请租金补贴");
		dto.setIconUrl("");
		// dto.setLink("testLink");
		dto.setType(EnumMissionType.MISSION_TYPE_RECOMMEND.getCode());
		dto.setAwardType(EnumAwardType.AWARD_TYPE_NONE.getCode());
		dto.setPublish(true);
		dto.setTemplateCode("T005");
		dto.setRelated(false);
		Response<Boolean> result = missionDubboservice.createMissionInfo(dto);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
	}

	@Test
	public void testCreateMissionInfo6() {
		CreateMissionInfoDto dto = new CreateMissionInfoDto();
		dto.setName("小超课堂-学习任务");
		dto.setDescription("小超课堂-学习任务");
		dto.setIconUrl("");
		// dto.setLink("testLink");
		dto.setType(EnumMissionType.MISSION_TYPE_STUDY.getCode());
		dto.setAwardType(EnumAwardType.AWARD_TYPE_NONE.getCode());
		dto.setPublish(true);
		dto.setTemplateCode("T006");
		dto.setRelated(false);
		Response<Boolean> result = missionDubboservice.createMissionInfo(dto);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
	}

	@Test
	public void testCreateMissionInfo7() {
		CreateMissionInfoDto dto = new CreateMissionInfoDto();
		dto.setName("连续签到-改版测试-奖励-test3");
		dto.setDescription("连续签到-改版测试-奖励-test3");
		dto.setIconUrl("");
		// dto.setLink("testLink");
		dto.setType(EnumMissionType.MISSION_TYPE_STUDY.getCode());
		dto.setAwardType(EnumAwardType.AWARD_TYPE_NONE.getCode());
		dto.setPublish(true);
		dto.setTemplateCode("T007");
		dto.setRelated(false);
		dto.setDurationTimeStart("2017-07-21 00:00:00");
		dto.setDurationTimeEnd("2017-07-31 00:00:00");
		dto.setValidTimeStart("2017-07-21 00:00:00");
		dto.setValidTimeEnd("2017-07-31 00:00:00");
		dto.setContinueDate(3);

		// 设置奖励
		List<AwardInfoDto> awards = new ArrayList<>();
		AwardInfoDto awardInfoDto = new AwardInfoDto();
		awardInfoDto.setAwardType("SCORE");
		awardInfoDto.setGrantNum(100.00);
		awards.add(awardInfoDto);
		dto.setAwards(awards);
		Response<Boolean> result = missionDubboservice.createMissionInfo(dto);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
	}

	@Test
	public void testPublishMissionInfo() {

		Response<Boolean> result = missionDubboservice.publishMissionInfo("11");
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}

	}

	@Test
	public void initMissionExecuteByMerchantId() {
		UserDto user = new UserDto();
		user.setLogin("900005");
		user.setMerchantId("A900005");
		user.setRealName("小丫");
		user.setRegistrationId("656F8464-757B-47AB-B63A-D8903360FDCD");
		user.setCompanyId("1");
		user.setUserName("44444");
		user.setShopName("xx");
		Response<Boolean> result = missionexecuteDubboService.initMissionExecuteByMerchantId(user);
		log.info("result:" + JSON.toJSON(result));
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}

	}

	@Test
	public void testExecuteMission() {

		String merchantId = "merchantId";
		MissionExecuteQuery query = new MissionExecuteQuery();
		query.setStatus(EnumMissionExecuteStatus.STATUS_INIT.getCode());
		query.setMerchantId(merchantId);
		List<MissionExecuteInfoPO> executes = missionExecuteService.queryMissionExecute(query);

		UserDto user = new UserDto();
		user.setLogin("login一账通账号");
		user.setMerchantId("merchantId");
		user.setRealName("张三");
		user.setRegistrationId("xxxx002");
		user.setUserName("小超账号");

		if (executes != null) {
			for (MissionExecuteInfoPO execute : executes) {
				Response<Boolean> result = missionexecuteDubboService.executeMission(String.valueOf(execute.getId()));
				if (result == null || !result.isSuccess()) {
					fail(result.getCode() + ":" + result.getErrorMessage());
				}
			}
		}

	}

	@Test
	public void autoAuditMissionByMerchantId() {
		UserDto user = new UserDto();
		user.setLogin("900005");
		user.setMerchantId("A900005");
		user.setRealName("etyuii");
		user.setRegistrationId("3ca348fc0d5c");
		user.setUserName("011113");
		Response<List<String>> result = missionexecuteDubboService.autoAuditMissionByMerchantId(user.getMerchantId());

		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
		log.info("result:" + JSON.toJSON(result));
	}

	@Test
	public void testgoAndGetAwardByMerchantId() {
		String merchantId = "A900005";
		MissionExecuteQuery query = new MissionExecuteQuery();
		query.setStatus(EnumMissionExecuteStatus.STATUS_FINISHED.getCode());
		query.setMerchantId("A900005");
		List<MissionExecuteInfoPO> executes = missionExecuteService.queryMissionExecute(query);
		if (executes != null) {
			for (MissionExecuteInfoPO execute : executes) {
				Response<List<AwardInfoDto>> result = missionexecuteDubboService
						.goAndGetAward(String.valueOf(execute.getId()), merchantId);
				if (result == null || !result.isSuccess()) {
					fail(result.getCode() + ":" + result.getErrorMessage());
				}
			}
		}

	}

	@Test
	public void manualAuditMissionCallback() {
		String merchantId = "merchantId";
		MissionExecuteQuery query = new MissionExecuteQuery();
		query.setStatus(EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode());
		query.setMerchantId(merchantId);
		List<MissionExecuteInfoPO> executes = missionExecuteService.queryMissionExecute(query);
		if (executes != null) {
			for (MissionExecuteInfoPO execute : executes) {
				Response<Boolean> result = missionBPMDubboService.manualAuditMissionCallback(
						String.valueOf(execute.getId()), null, EnumAuditResult.AUDIT_RESULT_REJECT.getCode(), "");
				if (result == null || !result.isSuccess()) {
					fail(result.getCode() + ":" + result.getErrorMessage());
				}
			}
		}

	}

	@Test
	public void courseMissionCallback() {

		Response<Boolean> result = missionClassroomDubboService.courseMissionCallback("merchantId", "courseId", null);
		if (result == null || !result.isSuccess()) {
			fail(result.getCode() + ":" + result.getErrorMessage());
		}
		log.info("result:" + JSON.toJSONString(result));
	}

	@Test
	public void expireMissionExecutes() {
		boolean ok = missionExecuteService.expireMissionExecutes(EnumMissionExecuteStatus.STATUS_INVALID.getCode());
		log.info("ok:" + JSON.toJSONString(ok));
	}

}
