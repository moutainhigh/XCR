package test.com.yatang.xc.xcr.web;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.yatang.xc.xcr.util.HttpClientUtil;


public class TestStockInterface {
	
	private static String appKey="00000025";
	private static String accessKey="8F3387A7C291D05480BEA33611418A9C";
	private static String getProductCategoryUrl="http://172.30.40.82:8080/ec-erp/ItemTypeAction.do?method=doExecute";
	private static String queryProductsUrl="http://172.30.40.82:8080/ec-erp/ItemTypeAction.do?method=selectItem";
	private static String storeStockUrl="http://172.30.40.82:8080/ec-erp/StockEnterAction.do?method=doExecute";
	private static String queryStockUrl="http://172.30.40.82:8080/ec-erp/StockQueryAction.do?method=doExecute";
	private static String queryStoreStockRecordsUrl="http://172.30.40.82:8080/ec-erp/StockEnterAction.do?method=queryStock";
	
    private static Logger log = LoggerFactory.getLogger(TestStockInterface.class);
    
    private StringBuffer addKey(){
		StringBuffer param=new StringBuffer();
		param.append("appKey=").append(appKey).append("&accessKey=").append(accessKey);
		return param;
    }
    
	/**
	 * 
	 * @Description：M2.5获取商品分类
	 * @return void: 返回值类型
	 * @throws
	 */
	@Test
	public void testGetProductCategroy() {
		log.info("************testGetProductCategroy************");
		
		StringBuffer param=addKey();
		param.append("&alliBusiId=900001").append("&shopCode=A900001");
		
		Object result=HttpClientUtil.okHttpPost(getProductCategoryUrl, param.toString());
		log.info("************result："+JSON.toJSON(result));
	}
	/**
	 * 
	 * @Description：M2.6查询商品列表
	 * @return void: 返回值类型
	 * @throws
	 */
	@Test
	public void testQueryProducts() {
		log.info("************testQueryProducts************");
		String jms = "jms_000265";
		String shopCode="A000265";
		
		StringBuffer param=addKey();
		param.append("&alliBusiId=").append(jms).append("&shopCode=").append(shopCode).
		append("&start=0").append("&end=10").append("&search=口香糖").append("&itemTypeCode=901404005");
		
		Object result=HttpClientUtil.okHttpPost(queryProductsUrl, param.toString());
		log.info("************result："+JSON.toJSON(result));
	}

	/**
	 * 
	 * @Description：M2.7商品入库
	 * @return void: 返回值类型
	 * @throws
	 */
	@Test
	public void testStoreStock() {
		log.info("************testQueryProducts************");
		String jms = "jms_000265";
		String shopCode="A000265";
		
		StringBuffer param=addKey();
		param.append("&alliBusiId=").append(jms).append("&shopCode=").append(shopCode).
		append("&itemNum=4054500627867").append("&costPrice=2.0").append("&costMoney=10.0").append("&inoutPrice=3.0")
		.append("&inoutMny=15.0").append("&inoutQty=5").append("&remarks=");
		
		
		Object result=HttpClientUtil.okHttpPost(this.storeStockUrl, param.toString());
		log.info("************result："+JSON.toJSON(result));
	}	
	
	/**
	 * 
	 * @Description：M2.8库存查询
	 * @return void: 返回值类型
	 * @throws
	 */
	@Test
	public void testQueryStock() {
		log.info("************testQueryProducts************");
		String jms = "jms_002333";
		String shopCode="A002333";
		
		StringBuffer param=addKey();
		param.append("&alliBusiId=").append(jms).append("&shopCode=").append(shopCode).
		append("&search=6901028025812").
		append("&start=0").append("&end=10");
				
		Object result=HttpClientUtil.okHttpPost(this.queryStockUrl, param.toString());
		log.info("************result："+JSON.toJSON(result));
	}	
	
	/**
	 * 
	 * @Description：M2.9入库记录查询
	 * @return void: 返回值类型
	 * @throws
	 */
	@Test
	public void testQueryStoreStockRecords() {
		log.info("************testQueryProducts************");
		String jms = "jms_000265";
		String shopCode="A000265";
		
		StringBuffer param=addKey();
		param.append("&alliBusiId=").append(jms).append("&shopCode=").append(shopCode).
		append("&search=4054500627867").append("&start=0").append("&end=10");
				
		Object result=HttpClientUtil.okHttpPost(this.queryStoreStockRecordsUrl, param.toString());
		log.info("************result："+JSON.toJSON(result));
	}	
	
}
