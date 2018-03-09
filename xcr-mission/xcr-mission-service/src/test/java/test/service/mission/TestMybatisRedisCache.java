package test.service.mission;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.mission.redis.MybatisRedisCache;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test/applicationContext-test.xml" })
public class TestMybatisRedisCache {

	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private MybatisRedisCache mybatisRedisCache; 
	
    @Test
    public void testCache(){
    	mybatisRedisCache.showInfo();
    }
}
