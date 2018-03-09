package test.com.yatang.xc.xcr.biz;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnDto;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnQuery;
import com.yatang.xc.xcr.biz.message.dubboservice.SwiftPassDubboService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wangyang on 2017/9/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test/applicationContext-test.xml"})
public class TestSwiftPassDubboService {


    @Autowired
    private SwiftPassDubboService swiftPassDubboService;

    @Test
    public void test1() {
        SwiftPassReturnDto swiftPassReturnDto = new SwiftPassReturnDto();
        swiftPassReturnDto.setMch_id("A001516");
        swiftPassReturnDto.setHaveShopCode(1);
        swiftPassReturnDto.setOpenid("oMJGHsxW_hKO8qDy3whZh6R_0mWA");
        swiftPassReturnDto.setOut_trade_no("102513761628201709274228294279");
        swiftPassReturnDto.setTime_end("20170927135144");
        swiftPassReturnDto.setTotal_fee(1);
        swiftPassReturnDto.setTrade_type("pay");
        swiftPassReturnDto.setMerchants_id("123123123");
        Response<String> response = swiftPassDubboService.saveStreamAndSendMessage(swiftPassReturnDto);
        System.out.println(response.isSuccess() + "  " + response.getResultObject());

    }

    @Test
    public void test2() {
        Response<SwiftPassReturnQuery> returnQueryResponse = swiftPassDubboService.getSwiftPassMessageList("A001516", 1, 10);
        System.out.println(JSONObject.toJSONString(returnQueryResponse.getResultObject()));
        SwiftPassReturnQuery swiftPassReturnQuery = returnQueryResponse.getResultObject();
        System.err.println(swiftPassReturnQuery.getTotalCount());
        System.err.println(JSONObject.toJSONString(swiftPassReturnQuery.getSwiftPassReturnDtoList()));

    }

    @Test
    public void test3() {
        SwiftPassReturnDto swiftPassReturnDto = new SwiftPassReturnDto();
        swiftPassReturnDto.setMch_id("A001516");
        swiftPassReturnDto.setOut_trade_no("102513761628201709274228294279");
        swiftPassReturnDto.setCreateTime("2017-11-30 00:00:00");
        swiftPassReturnDto.setPushSendTime("2017-11-30 23:59:59");
        swiftPassDubboService.updateSwiftPassMessage(swiftPassReturnDto);

    }

}
