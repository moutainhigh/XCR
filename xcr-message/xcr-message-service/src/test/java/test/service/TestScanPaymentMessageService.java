package test.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.utils.DateUtils;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessageQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.biz.message.dto.ScanPaymentMessage;
import com.yatang.xc.xcr.biz.message.dto.ScanPaymentMessageQuery;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessage;
import com.yatang.xc.xcr.biz.message.service.ScanPaymentMessageService;
import org.springframework.util.CollectionUtils;

/**
 * Dao 的 junit test 类
 *
 * @author : zhaokun
 * @version : 2017年3月20日
 * @date : 2017年3月20日 上午9:16:50
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestScanPaymentMessageService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ScanPaymentMessageService service;


    @Test
    public void saveScanPaymentMessage() {
        ScanPaymentMessage scan = new ScanPaymentMessage();
        scan.setOrderId("xxx");
        scan.setMerchantId("ttt");
        scan.setTotalAmount(20.00);
        boolean ok = service.saveScanPaymentMessage(scan);
        log.info("ok:" + JSON.toJSONString(ok));
    }

    @Test
    public void queryScanPay() {
        ScanPaymentMessageQuery query = new ScanPaymentMessageQuery();
        query.setOrderId("xxx");
        query.setMerchantId("ttt");
        query.setTotalAmount(20.00);
        List<ScanPaymentMessage> ok = service.queryScanPay(query);
        log.info("ok:" + JSON.toJSONString(ok));
    }

    @Test
    public void saveSwiftPassMessage() {
        SwiftPassMessage swiftPassMessage = new SwiftPassMessage();
        swiftPassMessage.setMch_id("A90324");
        swiftPassMessage.setTotal_fee(2045);
        swiftPassMessage.setHaveShopCode(1);
        swiftPassMessage.setOpenid("asdasdas2222sd111112");
        swiftPassMessage.setOut_trade_no("1567567567785444970123");
        swiftPassMessage.setTrade_type("asd");
        swiftPassMessage.setTime_end("20170927100110");
        swiftPassMessage.setCreateTime(DateUtils.dateDefaultFormat(new Date()));
        swiftPassMessage.setSendDcMqResult("1");
        boolean success = service.saveSwiftPassMessage(swiftPassMessage);
        System.out.println("success:" + success);
    }


    @Test
    public void queryTotalSize() {
        ScanPaymentMessageQuery query = new ScanPaymentMessageQuery();
        //query.setOrderId("xxx");
        query.setMerchantId("ttt");
        query.setTotalAmount(20.00);
        long ok = service.queryTotalSize(query);
        log.info("ok:" + JSON.toJSONString(ok));
    }

    @Test
    public void saveScanPaymentMessages() {
        List<ScanPaymentMessage> list = new ArrayList<ScanPaymentMessage>();
        ScanPaymentMessage scan = new ScanPaymentMessage();
        scan.setOrderId("xxx1");
        scan.setMerchantId("ttt");
        scan.setTotalAmount(20.00);
        ScanPaymentMessage scan2 = new ScanPaymentMessage();
        scan2.setOrderId("xxx2");
        scan2.setMerchantId("ttt");
        scan2.setTotalAmount(20.00);
        list.add(scan);
        list.add(scan2);
        service.saveScanPaymentMessage(list);
    }

    @Test
    public void getSwiftPassMessageList() {
        SwiftPassMessageQuery swiftPassMessageQuery = new SwiftPassMessageQuery();
        swiftPassMessageQuery.setPageNum(1);
        swiftPassMessageQuery.setPageSize(10);
        swiftPassMessageQuery.setMch_id("A001516");
        List<SwiftPassMessage> swiftPassMessageList = service.getSwiftPassMessageList(swiftPassMessageQuery);
        if (!CollectionUtils.isEmpty(swiftPassMessageList)) {

            System.out.println("query size = " + swiftPassMessageList.size());
            System.out.println("result = ");
            for (SwiftPassMessage swiftPassMessage : swiftPassMessageList) {
                System.out.println(swiftPassMessage);
            }
        }
    }

    @Test
    public void updateScanPaymentMessages() {
        SwiftPassMessage swiftPassMessage = new SwiftPassMessage();
        swiftPassMessage.setOut_trade_no("102513761628201709274228294271");
        swiftPassMessage.setPushSendTime(DateUtils.dateDefaultFormat(new Date()));
        swiftPassMessage.setSendDcMqResult("1");
        boolean success = service.updateSwiftPassMessage(swiftPassMessage);
        System.err.println(success);

    }

    @Test
    public void getListWithFailedToSendMQ() {
        List<SwiftPassMessage> swiftPassMessageList = service.getListWithFailedToSendMQ("", "");
        System.err.println("--------------");
        if (!CollectionUtils.isEmpty(swiftPassMessageList)) {
            System.err.println("query size = " + swiftPassMessageList.size());
            for (SwiftPassMessage swiftPassMessage : swiftPassMessageList) {
                System.err.println(swiftPassMessage);
            }
        }
    }
}
