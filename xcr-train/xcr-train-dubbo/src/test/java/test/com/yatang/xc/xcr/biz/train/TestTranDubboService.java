package test.com.yatang.xc.xcr.biz.train;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainDubboService;

/**
 * @描述: 课堂服务
 * @作者: huangjianjun
 * @创建时间: 2017年3月31日-下午8:36:20 .
 * @版本: 1.0 .
 * @param <T>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TestTranDubboService {
	@Autowired
	private TrainDubboService service;
	
	
	@Test
	public void testPersistence(){
		TrainInfoDTO dto = new TrainInfoDTO();
		dto.setName("扫码调价1");
		dto.setContent("contentcontentcontentcontent扫码调价1contentcontentcontentcontentcontentcontent");
		dto.setTrainLength(10);
		dto.setStatus(0);
		dto.setCreateUid(1);
		dto.setIcon("image/insertGoods.png");
		dto.setFileUrl("172.30.10.30:21/prl/www/html/扫码调价.html");
		dto.setCreateTime(new Date());;
		dto.setModifyTime(new Date());
		dto.setReleasesTime(null);
		dto.setRemark("remark自动调价1contentcontentcontentcontentcontentcontent");
		service.editTrain(dto);
	}
	
	@Test
	public void testSetUpTrainMission(){
		Response res = service.setUpTrainMission(3L);
		System.out.println(res.isSuccess());
	}
	@Test
	public void testDownOrReleases(){
		Response res = service.downShelfOrReleases(30L, "1");
		System.out.println(res.isSuccess());
	}
}
