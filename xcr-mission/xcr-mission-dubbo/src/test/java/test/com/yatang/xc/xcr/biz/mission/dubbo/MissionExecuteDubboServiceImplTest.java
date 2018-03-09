package test.com.yatang.xc.xcr.biz.mission.dubbo;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.busi.common.resp.Response;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberScoreInfoPoDto;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberScoreDubboService;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月15日 下午2:51:58
 * @Summary : 测试积分同步
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
public class MissionExecuteDubboServiceImplTest {

    private @Autowired MemberScoreDubboService memberScoreDubboService;
	
	@Test
	public void addScor(){
		String json = "{\"businessIdentity\":\"6efb0e8516dd409eb0c96599de4110e7\",\"changeReason\":\"签到\",\"createTime\":1510730150904,\"creatorId\":0,\"id\":0,\"memberCode\":\"jms_000122\",\"modifierId\":0,\"pageNum\":0,\"pageSize\":0,\"relateSystemCode\":\"code1\",\"relateSystemName\":\"小超人系统\",\"score\":90,\"scoreType\":1,\"shopCode\":\"A000122\",\"totalPage\":0}";
		  MemberScoreInfoPoDto score = JSON.parseObject(json, MemberScoreInfoPoDto.class);
         score.setBusinessIdentity(UUID.randomUUID().toString().replaceAll("-", ""));/**唯一标识 不重复唯一即可*/
         System.err.println(JSON.toJSONString(score));
         Response<Boolean> addScore = memberScoreDubboService.addScore(score);
         System.err.println(JSON.toJSONString(addScore));
	}
	
	
}
