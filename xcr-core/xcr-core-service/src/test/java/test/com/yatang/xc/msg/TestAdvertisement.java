package test.com.yatang.xc.msg;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yatang.xc.xcr.biz.core.dao.AdvertisementDao;
import com.yatang.xc.xcr.biz.core.domain.StatePO;

public class TestAdvertisement extends BaseJunit{
	
	@Autowired
	AdvertisementDao advertisementDao;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	
	
//	@Test
//	public void testSave() {
//		AdvertisementPO advertisementPO = new AdvertisementPO();
//		advertisementPO.setActivityUrl("SDAD");
//		advertisementPO.setCreateTime(new Date().toString());
//		advertisementPO.setPicName("ceshi");
//		advertisementPO.setPicUrl("www.baidu.com");
//		advertisementPO.setState(0);
//		advertisementPO.setRemaind("none");
//		advertisementDao.insertAdvertisement(advertisementPO);
//	}
	
	
	@Test
	public void testfind(){
		StatePO statePO = new StatePO();
		statePO.setState(1);
		System.out.println(advertisementDao.findAdvertisementByState(statePO));
		System.out.println("*******");
	}
	
	
//	@Test
//	public void testfupdate(){
//		AdvertisementUpdatePO advertisementUpdatePO = new AdvertisementUpdatePO();
//		advertisementUpdatePO.setId(1);
//		advertisementUpdatePO.setState(1);
//		
//		advertisementDao.updateAdvertisement(advertisementUpdatePO);
//	}

}
