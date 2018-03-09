package test.com.yatang.xc.xcr.biz.core.dao;

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

import com.yatang.xc.xcr.biz.core.dao.ActivityDao;
import com.yatang.xc.xcr.biz.core.domain.ActivityPO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestActivityDao {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private ActivityDao activityDao;
	
	@Test
	public void insert(){
		ActivityPO activityPO=new ActivityPO();
		activityPO.setProvince("四川");
		activityPO.setCity("成都");
		activityPO.setUserId("jms_900006");
		activityPO.setStoreNo("A900006");
		activityPO.setUsername("李金银");
		activityPO.setPhone("13285674885");
		activityPO.setBranch_company("四川子公司");
		activityPO.setUserPhoto("http://img.jsqq.net/uploads/allimg/150111/1_150111080328_19.jpg");
		activityPO.setStorePhoto("http://img2.imgtn.bdimg.com/it/u=3040167765,1565863557&fm=26&gp=0.jpg");
		activityPO.setType("1");
		activityPO.setCreateTime(new Date());
		activityPO.setUpdateTime(new Date());
		Long id=activityDao.insert(activityPO);
		log.info("\n*****实体toString方法为："+activityPO.toString());
	}

	@Test
	public void testGetBy() {
		Map<String, Object> map=new HashMap<>();
		map.put("userId", "jms_900001");
		ActivityPO obj=activityDao.getBy(map);
		log.info("\n*****查询获得的信息是："+obj.toString());
	}

}
