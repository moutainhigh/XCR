package test.com.yatang.xc.xcr.web.action;

import com.yatang.xc.xcr.service.SwiftPassService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * aop缓存测试
 * Created by wangyang on 2017/10/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class CacheTest {

    @Autowired
    private SwiftPassService swiftPassService;

    @Test
    public void test1() {
        String result = swiftPassService.getShopCodeByMchId("102512398649");
        System.err.println(result);
    }

}
