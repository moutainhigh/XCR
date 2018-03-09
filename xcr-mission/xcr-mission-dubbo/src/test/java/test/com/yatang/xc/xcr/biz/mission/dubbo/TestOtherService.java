package test.com.yatang.xc.xcr.biz.mission.dubbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.utils.HttpClientUtils;
import com.busi.common.utils.JsonPathTools;
import com.yatang.xc.dc.biz.facade.dubboservice.DataCenterDobboService;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgDubboService;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.flow.engine.bo.BuyProductResult;
import com.yatang.xc.xcr.biz.mission.flow.engine.calc.MissionTriggerCaller;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

/**
 * 
 * MissionDubboService 测试类
 * 
 * @author : zhaokun
 * @date : 2017年3月27日 下午7:19:58
 * @version : 2017年3月27日 zhaokun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class TestOtherService extends ApplicationObjectSupport {

    protected final Log log = LogFactory.getLog(this.getClass());

    private DataCenterDobboService dataCenterDobboService;

    @Autowired
    private MissionExecuteFlowService missionExecuteFlowService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionExecuteService missionExecuteService;
    
    @Autowired
    private MsgDubboService msgDubboService;
    
    @Resource(name = "financeCheckTriggerCaller")
    private MissionTriggerCaller financeCheckTriggerCaller;


    public static String unicode2String(String unicode) {
        if (StringUtils.isBlank(unicode))
            return null;
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }

        return sb.toString();
    }


    /**
     * 
     * @Description：查询门店交易数量接口测试（已调通）
     * @return void: 返回值类型
     * @throws
     */
    @Test
    public void querySmallTicketStatisticsInfo() {

        dataCenterDobboService = (DataCenterDobboService) getApplicationContext().getBean("dataCenterDobboService");
        // TO DO 调用 的接口查询门店用户完成了多少笔交易 判断交易数量是否大于1
        JSONObject reqJson = new JSONObject();
        reqJson.put("StoreSerialNo", "88");
        reqJson.put("PageIndex", 1);
        reqJson.put("PageSize", 1);
        log.info("reqJson ==" + reqJson);
        String result = dataCenterDobboService.querySmallTicketStatisticsInfo(reqJson.toString());
        log.info("result ==" + result);
        // List<Object> transactionAllValues =
        // JsonPathTools.getValues("aggregations.TransactionAllValue.value",
        // result);
        // List<Object> dataLists = JsonPathTools.getValues("hits.hits._source", result);
        List<Object> totals = JsonPathTools.getValues("hits.total", result);
        if (totals != null) {
            Double total = (Double)totals.get(0);
            if (total > 0d) {
                log.info("success:" + total);
            }
        }

        log.info("result:" + JSON.toJSONString(totals));
    }


    /**
     * 
     * @Description：组织服务查询所有门店信息接口(已调通)
     * @return void: 返回值类型
     * @throws
     */
    @Test
    public void testOrganizationService() {
        int pageSize = 100;

        Integer count = missionExecuteFlowService.getOrganizationService().queryStoreCount(null).getResultObject();
        log.info("count:" + count);
        //count =10l;
        if(count!=null && count>0){
            int pageCount = (int) (count / pageSize);
            for (int i = 0; i <= pageCount; i++) {
                List<StoreDto> list =
                    missionExecuteFlowService.getOrganizationService().queryPageStores(i+1, pageSize, null).getResultObject();
                log.info("list size:"+list.size()+" list:" + JSON.toJSONString(list));
                if (list != null && !list.isEmpty()) {
                    for (StoreDto dto : list) {
                        //log.info("dto:" + JSON.toJSONString(dto.getId()));
                    }
                }
            }          
        }
    }


    /**
     * 
     * @Description：查询购买指定商品的接口测试 (url数据还没拿到->测试环境url还没拿到，开发联调通过)
     * @return void: 返回值类型
     * @throws
     */
    @Test
    public void testBuyProduct() {
        
        String url = "http://172.30.40.224:7805/rest/model/atg/commerce/order/OrderLookupActor/getRelatedProductCount";
        url="http://172.30.210.19:7003/rest/model/atg/commerce/order/OrderLookupActor/getRelatedProductCount";
        String specifyProductIds = "prod400326,prod400327,prod400360";
        String login = "wangyu111";
        String orderId = "sit2-26300008";
        
        Map<String, String> params = new HashMap<>();
        params.put("productIds", specifyProductIds);
        params.put("login", login);
        params.put("orderId", orderId);
        log.info("url:" + url+" params:"+params);
        String json = HttpClientUtils.doPost(url, params);
        
        BuyProductResult result = (BuyProductResult) JSON.toJavaObject(JSON.parseObject(json), BuyProductResult.class);
        log.info("result:" + JSON.toJSONString(result));
        if (result.isResult()) {
            int count = 0;
            if (result.getProductCount() != null && !result.getProductCount().isEmpty()) {
                for (int icount : result.getProductCount().values()) {
                    count += icount;
                }
                log.info("success count:" + count);
            }
        }
    }

    


    /**
     * 
     * @Description：启动bpm流程接口测试(已调试，未通，bpm服务端出问题梦军在处理中)
     * @return void: 返回值类型
     * @throws
     */
    @Test
    public void startBpmProcess(){
        missionExecuteFlowService.startBpmProcess(16l, "T004", "10000","","");
    }
    
    /**
     * 
     * @Description：发消息接口测试(已调通)
     * @return void: 返回值类型
     * @throws
     */
    @Test
    public void sendMsg(){
        
        boolean ok = missionExecuteFlowService.sendAppMessage("merchantId","xxx");
        log.info("ok:" + JSON.toJSONString(ok));
    }
}
