/**
 * 
 */
package test.com.yatang.xc.xcr.biz.core;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.ActivityDto;
import com.yatang.xc.xcr.biz.core.dubboservice.ActivityDubboService;

/**
 * @author gaodawei
 * @Date 2017年7月25日 下午2:39:35
 * @version 1.0.0
 * @function
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TestActivityDubboService {

	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired 
	private ActivityDubboService activityDubboService;

	@Test
	public void testEnroll() {
		ActivityDto activityDto=new ActivityDto();
		activityDto.setProvince("四川");
		activityDto.setCity("成都");
		activityDto.setUserId("jms_900005");
		activityDto.setStoreNo("A900005");
		activityDto.setUsername("李金福");
		activityDto.setPhone("13285674885");
		activityDto.setBranch_company("四川子公司");
		activityDto.setUserPhoto("aaabc");
		activityDto.setStorePhoto("aaabc");
		activityDto.setType("1");
		activityDto.setCreateTime(new Date());
		activityDto.setUpdateTime(new Date());
		Response<?> flag=activityDubboService.enroll(activityDto,1000);
		log.info("\n*****返回的数据是："+flag);
	}
	
	@Test
	public void testGetInfoByUserId(){
		Response<?> flag=activityDubboService.getInfoByUserId("jms_900004","2");
		log.info("\n*****返回的数据是："+flag);
	}

}
