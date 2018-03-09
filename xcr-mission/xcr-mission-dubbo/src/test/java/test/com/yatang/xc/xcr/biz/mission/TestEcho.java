package test.com.yatang.xc.xcr.biz.mission;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestEcho {

    protected final Log log = LogFactory.getLog(this.getClass());


    @Test
    public void testDate() {
        Date date = new Date();
        date.setTime(Long.valueOf("1492142400000"));
        log.info("date:" + date);
    }
}
