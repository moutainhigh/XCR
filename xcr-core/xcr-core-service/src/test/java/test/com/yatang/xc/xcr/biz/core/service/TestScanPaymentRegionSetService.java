package test.com.yatang.xc.xcr.biz.core.service;

import com.yatang.xc.xcr.biz.core.service.ScanPaymentRegionSetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wangyang on 2017/12/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestScanPaymentRegionSetService {

    @Autowired
    private ScanPaymentRegionSetService scanPaymentRegionSetService;

    @Test
    public void test1() {
        String proviceId = "620000";
        boolean isShow = scanPaymentRegionSetService.checkIsShow(proviceId);
        System.err.println("isShow:" + isShow);
    }


}
