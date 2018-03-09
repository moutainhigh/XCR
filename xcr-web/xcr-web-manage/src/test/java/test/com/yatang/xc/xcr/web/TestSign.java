package test.com.yatang.xc.xcr.web;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.UserSignDTO;
import com.yatang.xc.xcr.biz.core.dto.UserSignInfoDTO;
import com.yatang.xc.xcr.biz.core.dto.UserSignSetDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.UserSignDubboService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wangyang on 2017/7/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
public class TestSign {

    @Autowired
    private UserSignDubboService userSignDubboService;


    @Test
    public void test1(){
        UserSignDTO dto = new UserSignDTO();
        dto.setShopCode("A000436");
        dto.setUserId("jms_000436");
        Response response = userSignDubboService.sign(dto);
        System.err.println(response.isSuccess());
        System.err.println(response.getErrorMessage());
    }

    @Test
    public void test2(){
        UserSignDTO dto = new UserSignDTO();
        dto.setUserId("jms_000678");
        dto.setShopCode("A000678");
        Response<UserSignInfoDTO> res = userSignDubboService.getSignArray(dto);
        System.err.println(res.isSuccess());
        System.err.println(res.getErrorMessage());

        UserSignInfoDTO userSignInfoDTO = res.getResultObject();
        System.err.println(userSignInfoDTO);
        System.out.println("今日是否已签到："+userSignInfoDTO.getIsCurrentDateSign());
    }

    @Test
    public void test3(){
        UserSignSetDTO userSignSetDTO = userSignDubboService.getUserSignSetInfo();
        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+userSignSetDTO);
    }

    @Test
    public void testUpdate(){
        UserSignSetDTO userSignSetDTO = new UserSignSetDTO();
        userSignSetDTO.setType(0);
        userSignSetDTO.setData("2.0");
        userSignSetDTO.setContent("adasasdasdasdas");
        boolean success = userSignDubboService.updateUserSignSet(userSignSetDTO);
        System.out.println(success);
    }
}
