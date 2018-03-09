package test.service;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.biz.pay.domain.DepositPayRecordPO;
import com.yatang.xc.xcr.biz.pay.service.SwiftPassPayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * 支付
 * Created by wangyang on 2017/11/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestPayService {

    @Autowired
    private SwiftPassPayService swiftPassPayService;

    @Test
    public void testInsert() {

        /**
         * private Long id;              //主键ID
         private String mchId;         //商户号
         private String shopCode;      //小超编号
         private String phone;         //商户电话
         private String outTradeNo;    //预下单订单号（商户订单号），用"_"拼接合同编号及时间戳
         private String contractNo;    //合同编号
         private String body;          //商品描述
         private Integer totalFee;         //总金额，以分为单位，不允许包含任何字、符号
         private String mchCreateIp;   //终端IP,订单生成的机器 IP
         private String notifyUrl;     //通知地址,接收平台通知的URL
         private String tokenId;       //支付授权码
         private Integer state;            //订单状态：0、预下单，1、待支付，2、预下单失败，3支付成功，4、支付失败
         private Date createTime;      //创建时间
         private Date updateTime;      //更新时间
         */
        DepositPayRecordPO po = new DepositPayRecordPO();
        po.setMchId("1231231231_商户号");
        po.setShopCode("12312312_小超编号");
        po.setPhone("138802564646");
        po.setOutTradeNo("asdaw123111510059387013");
        po.setContractNo("asdaw12311");
        po.setBody("加盟定金");
        po.setTotalFee(12345);
        po.setMchCreateIp("192.132.2.2");
        po.setNotifyUrl("www.test.com");
        po.setTokenId("asdasdasd1231231");
        po.setState(1);
        po.setCreateTime(new Date());
        po.setUpdateTime(new Date());

        System.out.println(swiftPassPayService.insert(po));

    }

    @Test
    public void testUpdate() {
        String mchId = "1231231231_商户号";
        String outTradeNo = "asdaw123111510058373209";
        int payResult = 3;
        System.out.println(swiftPassPayService.updatePayState(mchId, outTradeNo, payResult));
    }

    @Test
    public void test3() {
        System.out.println(swiftPassPayService.getStateByOutTradeNo("asdaw123111510059387013"));
    }

    @Test
    public void test4(){
        DepositPayRecordPO recordPO = swiftPassPayService.getPayRecordByOutTradeNo("asdaw123111510058373209");
        System.out.println(JSONObject.toJSONString(recordPO));
    }

}
