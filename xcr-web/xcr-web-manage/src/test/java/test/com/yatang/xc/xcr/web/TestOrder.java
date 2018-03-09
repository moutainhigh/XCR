package test.com.yatang.xc.xcr.web;

import com.yatang.xcsm.common.page.PageQuery;
import com.yatang.xcsm.common.response.Response;
import com.yatang.xcsm.remote.api.dto.PushOrderDTO;
import com.yatang.xcsm.remote.api.dto.PushQueryOrderDTO;
import com.yatang.xcsm.remote.api.dubboxservice.PushOrderDubboxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 运营C端订单管理dubbo接口测试
 * Created by wangyang on 2017/7/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class TestOrder {

    @Autowired
    private PushOrderDubboxService pushOrderDubboxService;

    @Test
    public void queryList() {

        String shopId = "A900005";
        int state = 0;//状态 (可选) 0 全部,1待支付,2待接单,3待发货,31待收货,30已取消,4已完成
//        Date startTime = new Date(); //订单提交开始时间(可选)
//        Date endTime = new Date(); //订单提交结束时间(可选)
        String shippingWay = ""; //配送方式：(可选),空 全部,101 送货上门,102 到店自提
        int payway = 0; //支付方式：(可选),0 全部,10支付宝,11微信
        String userName = ""; //用户名
        String productName = ""; //商品名称信息
        String orderNo = ""; //订单编号
        String companyName = ""; //子公司名称
        int pageNumber = 1;   //页码 (必填)
        int pageSize = 20;    //每页显示数(必填)

        PushQueryOrderDTO queryOrderDTO = new PushQueryOrderDTO();
        queryOrderDTO.setShopId(shopId);
        queryOrderDTO.setState(state);
//        queryOrderDTO.setStartTime(startTime);
//        queryOrderDTO.setEndTime(endTime);
        queryOrderDTO.setShippingWay(shippingWay);
        queryOrderDTO.setPayway(payway);
        queryOrderDTO.setUserName(userName);
        queryOrderDTO.setOrderNo(orderNo);
        queryOrderDTO.setCompanyName(companyName);

        Response<PageQuery<PushOrderDTO>> queryResponse = pushOrderDubboxService.queryListToOC(queryOrderDTO);
        PageQuery<PushOrderDTO> resultObject = queryResponse.getResultObject();
        List<PushOrderDTO> orderList = resultObject.getRows();
        if (CollectionUtils.isEmpty(orderList)) {
            System.out.println("得到列表结果为空!!!!!");
            return;
        }

        for (PushOrderDTO orderDTO : orderList) {
            System.out.println(orderDTO);
        }

    }

    @Test
    public void queryDetailById() {
        String orderNo = "";
        Response<PushOrderDTO> response = pushOrderDubboxService.queryOrderToOC(orderNo);
        PushOrderDTO orderDTO = response.getResultObject();
        System.out.println("orderDto:" + orderDTO);
    }


}
