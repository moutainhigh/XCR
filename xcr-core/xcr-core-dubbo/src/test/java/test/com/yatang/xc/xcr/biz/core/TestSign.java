package test.com.yatang.xc.xcr.biz.core;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.UserSignDTO;
import com.yatang.xc.xcr.biz.core.dto.UserSignInfoDTO;
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
@ContextConfiguration(locations = {"classpath*:test/applicationContext-test.xml"})
public class TestSign {

    @Autowired
    private UserSignDubboService userSignDubboService;

    @Test
    public void test1() {
        UserSignDTO dto = new UserSignDTO();
        dto.setShopCode("A000444");
        dto.setUserId("jms_000444");
        Response<UserSignInfoDTO> userSignInfoDTOResponse = userSignDubboService.getSignArray(dto);
        UserSignInfoDTO userSignInfoDTO = userSignInfoDTOResponse.getResultObject();
        System.err.println(JSONObject.toJSONString(userSignInfoDTO));
    }


    @Test
    public void test2() {
        UserSignDTO dto = new UserSignDTO();
        dto.setUserId("jms_000044");
        dto.setShopCode("A000044");
        Response response = userSignDubboService.sign(dto);
        System.err.println(JSONObject.toJSONString(response));
    }


}
