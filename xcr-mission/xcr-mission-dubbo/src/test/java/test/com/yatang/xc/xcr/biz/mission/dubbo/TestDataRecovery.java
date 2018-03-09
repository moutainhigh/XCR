package test.com.yatang.xc.xcr.biz.mission.dubbo;

import com.yatang.xc.xcr.biz.mission.DateUtil;
import com.yatang.xc.xcr.biz.mission.domain.UserSignPO;
import com.yatang.xc.xcr.biz.mission.service.MissionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wangyang on 2017/8/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestDataRecovery {

    protected final Log log = LogFactory.getLog(this.getClass());


    @Autowired
    private MissionService missionService;

    @Test
    public void testDate() throws ParseException{
    	
    	Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-12-31");
    	Date createTime = new SimpleDateFormat("yyyy-MM-dd").parse("2018-1-3");
    	int count = DateUtil.getDay(tempDate, createTime);
    	log.info("count: "+count);
    }
    
    @Test
    public void test1() {
        List<String> shopCodeList = missionService.getSignShopCodeList();
        log.info("missionService.getSignShopCodeList() -> shopCodeList.size:" + shopCodeList.size());
        if (CollectionUtils.isEmpty(shopCodeList)) {
            System.out.println("shopCodeList:" + shopCodeList);
        }
        try {
            for (String shopCode : shopCodeList) {
                log.info("shopCode:" + shopCode);
                Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-31");
                int tempContinueDay = 1;
                List<UserSignPO> userSignPOs = missionService.getSignListByShopCode(shopCode);
                if (CollectionUtils.isEmpty(userSignPOs)) {
                    continue;
                }
                for (UserSignPO po : userSignPOs) {
                    long id = po.getId();
                    Date createTime = po.getCreateTime();
                    int day = DateUtil.getDay(tempDate, createTime);
                    if (day == 1) {
                        tempContinueDay++;
                    }
                    if (day > 1) {
                        tempContinueDay = 1;
                    }
                    po.setContinueDay(tempContinueDay);
                    tempDate = createTime;
                    log.info("id:" + id + "  po.getContinueDay():" + po.getContinueDay());
                    missionService.updateContinueDayById(id, po.getContinueDay());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
