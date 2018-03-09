/**
 * 
 */ 
package test.com.yatang.xc.xcr.biz.core.service;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.core.domain.ActivityPO;
import com.yatang.xc.xcr.biz.core.service.ActivityService;

/** 
* @author gaodawei 
* @Date 2017年7月25日 下午2:04:10 
* @version 1.0.0
* @function 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestActivityService {

	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private ActivityService activityService;
	
	@Test
	public void testEnroll() {
		ActivityPO activityPO=new ActivityPO();
		activityPO.setProvince("四川");
		activityPO.setCity("成都");
		activityPO.setUserId("jms_900005");
		activityPO.setStoreNo("A900005");
		activityPO.setUsername("李金里");
		activityPO.setPhone("13285674885");
		activityPO.setUserPhoto("http://img.jsqq.net/uploads/allimg/150111/1_150111080328_19.jpg");
		activityPO.setStorePhoto("http://img2.imgtn.bdimg.com/it/u=3040167765,1565863557&fm=26&gp=0.jpg");
		activityPO.setType("1");
		activityPO.setCreateTime(new Date());
		activityPO.setUpdateTime(new Date());
		boolean flag=activityService.enroll(activityPO,1000);
		log.info("\n*****实体toString方法为："+flag);
	}
	
	@Test
	public void testGetInfoByUserId() {
		boolean flag=activityService.getInfoByUserId("jms_900001","2");
		log.info("\n*****查询获得的信息是："+flag);
	}
	
	@Test
	public void testGetEnrollCount(){
		log.info("\n*****已经报名年会的人数有："+activityService.getEnrollCount("1")+" 个");
	}

}
 